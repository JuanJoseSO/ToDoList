package com.example.todolist.ui


data class Task(
    val id: Int? = null,
    val name: String,
    val category: Int,
    var isSelected: Boolean = false
)

