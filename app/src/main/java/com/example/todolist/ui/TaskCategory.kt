package com.example.todolist.ui

sealed class TaskCategory(val value: Int,var isSelected:Boolean = true) {
    object Importante : TaskCategory(0)
    object Compra : TaskCategory(1)
    object Other : TaskCategory(2)
}
