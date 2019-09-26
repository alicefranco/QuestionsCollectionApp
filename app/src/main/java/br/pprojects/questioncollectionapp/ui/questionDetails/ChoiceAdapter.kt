package br.pprojects.questioncollectionapp.ui.questionDetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import br.pprojects.questioncollectionapp.R
import br.pprojects.questioncollectionapp.data.model.Choice
import kotlinx.android.synthetic.main.item_choice.view.*

class ChoiceAdapter(private val context: Context, private val choices: List<Choice>) : RecyclerView.Adapter<ChoiceAdapter.ViewHolder>() {
    private var itemClick: (choice: Choice) -> Unit = {}
    private var radioButtonClick: (position: Int) -> Unit = {}
    var currentItemPosition: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_choice, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, choices[position], itemClick, radioButtonClick)
        currentItemPosition = position
    }

    override fun getItemCount(): Int {
        return choices.size
    }

    fun setRadioButtonClick(radioButtonClick: (position: Int) -> Unit) {
        this.radioButtonClick = radioButtonClick
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val choiceLayout: ConstraintLayout = item.choice_layout
        val choiceTextView: TextView = item.tv_choice
        val choiceRadioButton: RadioButton = item.rb_choice
        val votesTextView: TextView = item.tv_votes

        fun bind(context: Context, choice: Choice, itemClick: (choice: Choice) -> Unit, radioButtonClick: (position: Int) -> Unit) {
            choiceLayout.setOnClickListener {
                itemClick(choice)
            }
            choiceTextView.text = choice.choice
            votesTextView.text = String.format(context.getString(R.string.votes), choice.votes.toString())

            choiceRadioButton.setOnClickListener {
                choiceRadioButton.isChecked = !choiceRadioButton.isChecked
                radioButtonClick(adapterPosition)
            }
        }
    }
}
