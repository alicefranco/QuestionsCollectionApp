package br.pprojects.questioncollectionapp.ui

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.pprojects.questioncollectionapp.R
import br.pprojects.questioncollectionapp.data.model.NetworkState
import br.pprojects.questioncollectionapp.util.createDialog
import br.pprojects.questioncollectionapp.util.gone
import br.pprojects.questioncollectionapp.util.visible
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
                    createDialog(this, "ERROR", "ERROR")
                }
                else -> {}
            }
        })
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

    private fun setupRecycler(){
        adapter = QuestionsAdapter()
        rv_questions.layoutManager = linearLayoutManager
        startListening()
        rv_questions.adapter = adapter
    }

    private fun replaceSubscription(filter: String) {
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