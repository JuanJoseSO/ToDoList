package com.example.todolist.ui

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R


class CategoriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvCategoryName: TextView = view.findViewById(R.id.tvCategoryName)
    private val divider: View = view.findViewById(R.id.divider)
    private val viewContainer: CardView = view.findViewById(R.id.viewContainer)
    private val context = view.context

    fun render(taskCategory: TaskCategory, onItemSelected: (Int) -> Unit) {

        val color = if (taskCategory.isSelected) {
            R.color.todo_background_card
        } else {
            R.color.todo_background_disabled
        }

        viewContainer.setCardBackgroundColor(ContextCompat.getColor(context, color))

        itemView.setOnClickListener { onItemSelected(layoutPosition) }

        when (taskCategory) {
            TaskCategory.Compra -> {
                tvCategoryName.text = ContextCompat.getString(context, R.string.todo_dialog_category_buying)
                divider.setBackgroundColor(ContextCompat.getColor(context, R.color.todo_buying_category))
            }
            TaskCategory.Other -> {
                tvCategoryName.text = ContextCompat.getString(context, R.string.todo_dialog_category_other)
                divider.setBackgroundColor(ContextCompat.getColor(context, R.color.todo_other_category))
            }
            TaskCategory.Importante -> {
                tvCategoryName.text = ContextCompat.getString(context, R.string.todo_dialog_category_important)
                divider.setBackgroundColor(ContextCompat.getColor(context, R.color.todo_important_category))
            }
        }
    }
}
