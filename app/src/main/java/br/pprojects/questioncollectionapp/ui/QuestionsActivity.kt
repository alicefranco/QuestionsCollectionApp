package br.pprojects.questioncollectionapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.pprojects.questioncollectionapp.R
import kotlinx.android.synthetic.main.activity_questions.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class QuestionsActivity : AppCompatActivity() {
    private val questionsViewModel: QuestionsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

        val adapter = QuestionsAdapter()
        rv_questions.layoutManager = LinearLayoutManager(this)
        questionsViewModel.questions.observe(this, Observer {
            adapter.submitList(it)
        })
        rv_questions.adapter = adapter



    }
}