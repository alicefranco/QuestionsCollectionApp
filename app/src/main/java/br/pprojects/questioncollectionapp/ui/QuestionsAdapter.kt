package br.pprojects.questioncollectionapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.pprojects.questioncollectionapp.R
import br.pprojects.questioncollectionapp.data.model.Question
import br.pprojects.questioncollectionapp.util.formatString
import kotlinx.android.synthetic.main.item_question.view.*


class QuestionsAdapter : PagedListAdapter<Question, QuestionsAdapter.ViewHolder>(QuestionsDiffCalback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question = getItem(position)
        question?.let {
            holder.bind(it)
        }
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item){
        private var questionTextView: TextView = item.tv_question
        private var datePublishedTextView: TextView = item.tv_date_published
        private var questionImageView: ImageView = item.iv_question
        private var idTextView: TextView = item.tv_id

        fun bind(question: Question) {
            questionTextView.text = question.question
            idTextView.text = question.id.toString()

            datePublishedTextView.text = question.publishedAt.formatString("yyyy-MM-dd","dd-MM-yyyy")
        }
    }
    companion object {
        val QuestionsDiffCalback = object : DiffUtil.ItemCallback<Question>() {
            override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
                return oldItem == newItem
            }
        }
    }

}
