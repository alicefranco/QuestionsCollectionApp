package br.pprojects.questioncollectionapp.ui

import android.content.Intent
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
import java.net.URL


class QuestionsActivity : AppCompatActivity() {
    private val questionsViewModel: QuestionsViewModel by viewModel()
    private val linearLayoutManager = LinearLayoutManager(this)
    private lateinit var adapter: QuestionsAdapter
    private var questionId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)


        if (intent?.action == Intent.ACTION_VIEW) {
            val uri = intent.data
            uri?.let {
                val urlFromIntent = URL(it.getScheme(), it.getHost(), it.getPath()).toString()
                questionId = urlFromIntent.get(urlFromIntent.length - 1).toString()
            }
        }

        setupRecycler()
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount < 1) {
                cv_search.visible()
                adapter.setItemClick(itemClick)
            }
        }

        if (questionId != null) {
            questionsViewModel.searchQuestion(questionId?.toInt() ?: 0)
            questionsViewModel.questionFound.observe(this, Observer {
                openQuestionDetailsScreen(it)
            })
            questionsViewModel.loadingMutable.observe(this, Observer {
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
                    NetworkState.NO_CONNECTION -> {
                        createDialog(this, "ERROR", "Please, check your internet connection.")
                    }
                    else -> {
                    }
                }
            })


        } else {

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
                    NetworkState.NO_CONNECTION -> {
                        createDialog(this, "ERROR", "Please, check your internet connection.")
                        createTimer(
                            {
                                questionsViewModel.listQuestions()
                            },
                            2000,
                            5000
                        )
                    }
                    else -> {
                    }
                }
            })
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

    private val itemClick: (question: Question) -> Unit = { question ->
        adapter.removeItemClick()
        openQuestionDetailsScreen(question)
    }

    fun openQuestionDetailsScreen(question: Question) {
        val fragment = QuestionDetailsFragment.newInstance(question)
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