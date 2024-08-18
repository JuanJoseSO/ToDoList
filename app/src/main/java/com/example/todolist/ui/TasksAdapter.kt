package com.example.todolist.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R


class TasksAdapter(private val context:Context, var tasks: MutableList<Task>, private val onTaskSelected: (Int) -> Unit) :
    RecyclerView.Adapter<TasksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_todo_task, parent, false)
        return TasksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.render(tasks[position])
        holder.itemView.setOnClickListener { onTaskSelected(position) }
    }
    override fun getItemCount() = tasks.size

    fun deleteTasks(position: Int) {
        //Elimina el item de la posicion que le pasemos
        val idTask = tasks[position].id
        tasks.removeAt(position)
        //Notifica el cambio
        notifyItemRemoved(position)
        //Necesario para que no haya una excepcion de fuera de rango al eliminar el ultimo item
        notifyItemRangeChanged(position, tasks.size - position)

        //Lo guardamos en la base de datos
        val db =DatabaseHelper(context)
        if (idTask != null) {
            db.deleteTask(idTask)
        }
    }
}