package com.example.todolist.ui

import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R

class TasksViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvTask: TextView = view.findViewById(R.id.tvTask)
    private val cbTask: CheckBox = view.findViewById(R.id.cbTask)
    private val db = DatabaseHelper(view.context)

    fun render(task: Task) {

        if (task.isSelected) {
            //Si esta selecionada la tarea,tacha el texto
            tvTask.paintFlags = tvTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            //Si no, destacha el texto
            tvTask.paintFlags = tvTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        tvTask.text = task.name
        cbTask.isChecked = task.isSelected



        val color = when (task.category) {
            0 -> R.color.todo_important_category
            1 -> R.color.todo_buying_category
            2 -> R.color.todo_other_category
            else -> {0}
        }

        cbTask.buttonTintList = ColorStateList.valueOf(
            ContextCompat.getColor(cbTask.context, color)
        )

        //Maneja la interacción del usuario al marcar/desmarcar la tarea
        cbTask.setOnCheckedChangeListener { _, isChecked ->
            task.isSelected = isChecked
            db.updateTask(task) //Actualiza la base de datos solo en respuesta al usuario
            render(task) //Vuelve a renderizar la tarea para reflejar el cambio
        }

    }
}