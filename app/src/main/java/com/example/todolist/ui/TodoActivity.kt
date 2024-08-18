package com.example.todolist.ui

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.ui.TaskCategory.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TodoActivity : AppCompatActivity() {

    private val categories = listOf(
        Importante,
        Compra,
        Other
    )

    //Lista tareas
    private val tasks = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)
        initComponent()         //Inicializa los componentes de la UI
        initUI()                //Configura la interfaz de usuario
        initListeners()         //Configura los listeners de eventos
        loadTasksFromDatabase() //Carga las tareas desde la base de datos
    }

    private fun initListeners() {
        fabAddTask.setOnClickListener { showDialog() } //Dialog para crear una nueva tarea
    }

    private fun loadTasksFromDatabase() {
        val db = DatabaseHelper(this)
        tasks.clear()                           //Limpia la lista actual
        tasks.addAll(db.getAllTasks())          //Añade las tareas obtenidas de la base de datos
        tasksAdapter.notifyDataSetChanged()     //Actualiza la lista
        db.close()
    }

    //Método para obtener la categoría de tarea mediante el texto del botón seleccionado
    fun getTaskCategoryFromText(text: String): TaskCategory {
        return when (text) {
            getString(R.string.todo_dialog_category_buying) -> Importante
            getString(R.string.todo_dialog_category_important) -> Compra
            getString(R.string.todo_dialog_category_other) -> Other
            else -> throw IllegalArgumentException("Unknown category: $text")
        }
    }

    private fun showDialog(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_task)

        val btnAddTask: Button = dialog.findViewById(R.id.btnAddTask)
        val etTask: EditText = dialog.findViewById(R.id.etTask)
        val rgCategories: RadioGroup = dialog.findViewById(R.id.rgCategories)

        //Boton añadir tarea
        btnAddTask.setOnClickListener {
            val currentTask = etTask.text.toString()
            if(currentTask.isNotEmpty()){
                val selectedId = rgCategories.checkedRadioButtonId
                val selectedRadioButton: RadioButton = rgCategories.findViewById(selectedId)
                val selectedCategoryText = selectedRadioButton.text.toString()

                //Obtiene la categoría actual mediante el texto del RadioButton
                val currentCategory: TaskCategory = getTaskCategoryFromText(selectedCategoryText)

                //Crea una nueva tarea y la inserta en la base de datos
                val task = Task(name = currentTask, category = currentCategory.value)
                val db=DatabaseHelper(this)

                db.insertTask(task)
                updateTasks()
                loadTasksFromDatabase()

                dialog.hide()
            }
        }
        dialog.show()
    }

    private fun initComponent() {
        rvCategories = findViewById(R.id.rvCategories)
        rvTasks = findViewById(R.id.rvTasks)
        fabAddTask = findViewById(R.id.fabAddTask)
    }

    //Configuramos la UI y asignamos los adaptadores a los RecyclerViews
    private fun initUI() {
        categoriesAdapter = CategoriesAdapter(categories) { position -> updateCategories(position) }
        rvCategories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvCategories.adapter = categoriesAdapter

        tasksAdapter = TasksAdapter(tasks) {position -> onItemSelected(position)}
        rvTasks.layoutManager = LinearLayoutManager(this)
        rvTasks.adapter = tasksAdapter
    }

    private fun onItemSelected(position:Int){
        val task = tasks[position]
        task.isSelected = !task.isSelected
        //Cambia el estado de selección de la tarea
        val db = DatabaseHelper(this)
        db.updateTask(task)  // Actualiza la tarea en la base de datos
        updateTasks()
    }

    //Filtra las tareas por las categorías seleccionadas y actualiza la lista mostrada
    private fun updateCategories(position: Int){
        categories[position].isSelected = !categories[position].isSelected
        categoriesAdapter.notifyItemChanged(position)
        updateTasks()
    }

    private fun updateTasks(){
        //Limpia la lista de tareas
        tasks.clear()

        //Carga todas las tareas desde la base de datos y las añade a la lista de tareas
        tasks.addAll(DatabaseHelper(this).getAllTasks())

        //Filtra las categorías seleccionadas (aquellas cuyo atributo isSelected es true)
        val selectedCategoryValues = categories.filter { it.isSelected }
            .map { it.value }

        // Verifica si hay categorías seleccionadas
        if (selectedCategoryValues.isEmpty()) {
            // Si no hay categorías seleccionadas, no muestra ninguna tarea
            tasksAdapter.tasks = emptyList()
        } else {
            // Si hay categorías seleccionadas, filtra las tareas por estas categorías
            val newTasks = tasks.filter { task -> selectedCategoryValues.contains(task.category) }
            tasksAdapter.tasks = newTasks
        }
        tasksAdapter.notifyDataSetChanged()
    }



    private lateinit var rvCategories: RecyclerView
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var rvTasks:RecyclerView
    private lateinit var tasksAdapter: TasksAdapter
    private lateinit var fabAddTask:FloatingActionButton
}