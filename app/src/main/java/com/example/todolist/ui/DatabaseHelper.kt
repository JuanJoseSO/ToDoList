package com.example.todolist.ui

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper (context: Context) :

    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TASKS"

        //Tabla TAREAS
        const val TABLE_TASK = "taks"
        const val ID_TASK = "id_tarea"
        const val TYPE_TASK = "tipoTarea"
        const val NAME_TASK = "nombreTarea"
        const val IS_CROSSED = "tachado"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_TASK (
            $ID_TASK INTEGER PRIMARY KEY AUTOINCREMENT,
            $TYPE_TASK INT,
            $NAME_TASK STRING,
            $IS_CROSSED INTEGER DEFAULT 0
            )
        """.trimIndent()

        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
    }

    fun insertTask(task: Task){
        val db= writableDatabase

       val insert = ContentValues().apply {
            put(NAME_TASK,task.name)
            put(TYPE_TASK,task.category)
            put(IS_CROSSED,task.isSelected)
       }
        try {
           db.insert(TABLE_TASK, null, insert)
        } catch (e: SQLiteException) {
            Log.e("SQLite", "Error al añadir tareas", e)
        }
    }

    fun getAllTasks(): MutableList<Task> {
        val tasks = mutableListOf<Task>()
        val db = this.readableDatabase
        val cursor = db.query(TABLE_TASK, null, null, null, null, null, null)
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(ID_TASK))
                val name = getString(getColumnIndexOrThrow(NAME_TASK))
                val category = getInt(getColumnIndexOrThrow(TYPE_TASK))
                val isSelected = getInt(getColumnIndexOrThrow(IS_CROSSED)) > 0
                tasks.add(Task(id,name, category, isSelected))
            }
        }
        cursor.close()
        return tasks
    }

    fun updateTask(task: Task) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(NAME_TASK, task.name)
            put(TYPE_TASK, task.category)
            put(IS_CROSSED, if (task.isSelected) 1 else 0)
        }
        db.update(TABLE_TASK, values, "$ID_TASK = ?", arrayOf(task.id.toString()))
    }

    fun deleteTasks() {
        val db= writableDatabase
        db.delete(TABLE_TASK,null,null)
        db.close()
    }

    fun deleteTask(id: Int){
        val db= writableDatabase
        db.delete(TABLE_TASK,"$ID_TASK=?", arrayOf(id.toString()))
        db.close()
    }

}