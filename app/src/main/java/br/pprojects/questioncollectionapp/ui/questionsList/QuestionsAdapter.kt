package br.pprojects.questioncollectionapp.ui.questionsList

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
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_question.view.*

class QuestionsAdapter(private val context: Context) : PagedListAdapter<Question, QuestionsAdapter.ViewHolder>(
    QuestionsDiffCalback
) {
    private var itemClick: (question: Question) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question = getItem(position)
        question?.let {question ->
            holder.itemView.setOnClickListener { itemClick(question) }
            holder.bind(context, question)
        }
    }

    fun setItemClick(itemClick: (question: Question) -> Unit) {
        this.itemClick = itemClick
    }

    fun removeItemClick() {
        this.itemClick = {}
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val questionTextView: TextView = item.tv_question
        private val datePublishedTextView: TextView = item.tv_date_published
        private val questionImageView: ImageView = item.iv_question
        private val idTextView: TextView = item.tv_id

        fun bind(context: Context, question: Question) {

            Glide.with(context).load(question.thumbUrl).into(questionImageView)

            questionTextView.text = question.question
            idTextView.text = question.id.toString()

            datePublishedTextView.text = question.publishedAt.formatString("yyyy-MM-dd", "dd-MM-yyyy")
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
