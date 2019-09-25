package br.pprojects.questioncollectionapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.pprojects.questioncollectionapp.R
import br.pprojects.questioncollectionapp.data.model.NetworkState
import br.pprojects.questioncollectionapp.data.model.Question
import br.pprojects.questioncollectionapp.util.*
import kotlinx.android.synthetic.main.activity_questions.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class QuestionsActivity : AppCompatActivity() {
    private val questionsViewModel: QuestionsViewModel by viewModel()
    private val linearLayoutManager = LinearLayoutManager(this)
    private lateinit var adapter: QuestionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

        setupRecycler()

        et_search.addTextChangedListener(setupTextWatcher())

        iv_dismiss.setOnClickListener {
            et_search.setText("")
            replaceSubscription()
        }

        questionsViewModel.loading.observe(this, Observer {
            when (it) {
                NetworkState.LOADING -> {
                    rv_questions.gone()
                    pb_questions.visible()
                }
                NetworkState.DONE -> {
                    rv_questions.visible()
                    pb_questions.gone()
                }
                NetworkState.ERROR -> {
                    pb_questions.gone()
                    rv_questions.visible()
                    createDialog(this, "ERROR", "An error occurred.")
                }
                else -> {}
            }
        })

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount < 1) {
                cv_search.visible()
                adapter.setItemClick(itemClick)
            }
        }
    }

    private fun setupTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                p0?.let {
                    if (it.length > 2) {
                        replaceSubscription(it.toString())
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        }
    }

    private fun setupRecycler() {
        adapter = QuestionsAdapter(this)
        adapter.setItemClick(itemClick)
        rv_questions.layoutManager = linearLayoutManager
        startListening()
        rv_questions.adapter = adapter
    }

    private val itemClick: (question: Question) -> Unit = {
        adapter.removeItemClick()
        val fragment = QuestionDetailsFragment.newInstance(it)
        addFragment(fragment, R.id.questions_list_layout, QuestionDetailsFragment.TAG, true)
        cv_search.gone()
    }

    private fun replaceSubscription(filter: String? = null) {
        questionsViewModel.replaceSubscription(this, filter)
        startListening()
    }

    private fun startListening() {
        questionsViewModel.questions.observe(this, Observer {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })
    }
}