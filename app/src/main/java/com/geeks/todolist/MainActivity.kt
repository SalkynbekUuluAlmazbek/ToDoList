package com.geeks.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: TaskViewModel
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val addButton = findViewById<Button>(R.id.addButton)
        val taskRecyclerView = findViewById<RecyclerView>(R.id.taskRecyclerView)
        val taskEditText = findViewById<EditText>(R.id.taskEditText)

        viewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        taskAdapter = TaskAdapter(viewModel.getAllTasks(),
            onItemClick = { task -> viewModel.markTaskCompleted(task) },
            onItemLongClick = { task -> showDeleteDialog(task) })

        taskRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = taskAdapter
        }

        addButton.setOnClickListener {
            val taskTitle = taskEditText.text.toString()
            if (taskTitle.isNotBlank()) {
                viewModel.addTask(taskTitle)
                taskAdapter.notifyDataSetChanged()
                taskEditText.text.clear()
            }
        }
    }
    private fun showDeleteDialog(task: Task) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Delete Task")
            .setMessage("Are you sure you want to delete this task?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.removeTask(task)
                taskAdapter.notifyDataSetChanged()
            }
            .setNegativeButton("Cancel", null)
            .create()

        alertDialog.show()
    }
}
