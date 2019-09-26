package br.pprojects.questioncollectionapp.ui.questionDetails

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.pprojects.questioncollectionapp.R
import br.pprojects.questioncollectionapp.data.model.Question
import br.pprojects.questioncollectionapp.util.createDialog
import br.pprojects.questioncollectionapp.util.formatString
import br.pprojects.questioncollectionapp.util.gone
import br.pprojects.questioncollectionapp.util.visible
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_question_details.*
import kotlinx.android.synthetic.main.item_choice.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class QuestionDetailsFragment : Fragment() {
    private val questionDetailsViewModel: QuestionDetailsViewModel by viewModel()
    private var question: Question? = null
    private lateinit var adapter: ChoiceAdapter
    private var linearLayoutManager = LinearLayoutManager(activity)

    companion object {
        const val TAG = "QUESTION_DETAILS_FRAGMENT"
        fun newInstance(question: Question): QuestionDetailsFragment {
            return QuestionDetailsFragment().apply {
                this.question = question
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_question_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_id.text = question?.id.toString()
        tv_question.text = question?.question
        tv_date.text = question?.publishedAt?.formatString("yyyy-MM-dd", "dd-MM-yyyy")
        context?.let {Glide.with(it).load(question?.imageUrl).into(iv_choice)}

        iv_share.setOnClickListener {
            shareQuestion()
        }

        bt_vote.setOnClickListener{
            voteClick()
        }

        question?.choices?.let {
            context?.let { context ->
                adapter =
                    ChoiceAdapter(context, it)
                adapter.setRadioButtonClick(radioButtonClick)
                rv_answers.layoutManager = linearLayoutManager
                rv_answers.adapter = adapter
            }
        }

        questionDetailsViewModel.loading.observe(this, Observer {
            if (it)
                pb_layout.visible()
            else
                pb_layout.gone()
        })

        questionDetailsViewModel.updated.observe(this, Observer {
            if (it)
                activity?.supportFragmentManager?.popBackStack()
        })

        questionDetailsViewModel.error.observe(this, Observer {error ->
            if (!error.isNullOrEmpty())
                context?.let {
                    createDialog(it, "ERROR", error)
                }
        })
    }

    private val radioButtonClick: (position: Int) -> Unit = { position ->
        var i = 0
        while(i < adapter.itemCount) {
            if(i != position)
                rv_answers[i].rb_choice.isChecked = false
            else
                rv_answers[position].rb_choice.isChecked = true
            i++
        }
    }

    fun voteClick() {
        var i = 0
        while(i < adapter.itemCount) {
            if (rv_answers[i].rb_choice.isChecked) {
                question?.choices?.get(i)?.votes?.let { votes ->
                    question?.choices?.get(i)?.votes = votes + 1
                    question?.let {
                        questionDetailsViewModel.updateQuestion(it)
                    }
                }
            }
            i++
        }
    }

    fun shareQuestion() {
        val intent = Intent(Intent.ACTION_SEND)

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.question_subject))

        val text = String.format(getString(R.string.question_url), question?.id.toString())
        intent.putExtra(Intent.EXTRA_TEXT, text)

        context?.packageManager?.let {
            startActivity(intent)
        }

    }
}
