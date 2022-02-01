package com.example.todolist.ui

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.databinding.ActivityAddTaskBinding
import com.example.todolist.datasource.TaskDataSource
import com.example.todolist.extensions.format
import com.example.todolist.extensions.text
import com.example.todolist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTaskActivity: AppCompatActivity() {

    private lateinit var bind: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(bind.root)

        if(intent.hasExtra(TASK_ID)){

            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                bind.tilTitle.text = it.title
                bind.tilDate.text = it.title
                bind.tilHour.text = it.title

            }
        }

        inserirListas()

    }

private fun inserirListas(){

    bind.tilDate.editText?.setOnClickListener{
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        datePicker.addOnPositiveButtonClickListener {
            val timezone = TimeZone.getDefault()
            val offset = timezone.getOffset(Date().time) * -1
            bind.tilDate.text = Date(it+offset).format()
        }
        datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")

    }
    bind.tilHour.editText?.setOnClickListener {
         val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
         timePicker.addOnPositiveButtonClickListener{
             val minute = if(timePicker.hour in 0..9) "0${timePicker.minute}" else timePicker.minute

             val hour =  if(timePicker.minute in 0..9) "0${timePicker.hour}" else timePicker.hour

            bind.tilHour.text = "${hour}:${minute}"
         }
    timePicker.show(supportFragmentManager, null)

     }

    bind.btnCancel.setOnClickListener{
            finish()
        }

    bind.btnNewTask.setOnClickListener{

        val task = Task(
            title = bind.tilTitle.text,
            date = bind.tilDate.text,
            hour= bind.tilHour.text,
            id = intent.getIntExtra(TASK_ID, 0)

        )
        TaskDataSource.insertTask(task)
setResult(Activity.RESULT_OK)
        finish()
    }
}

    companion object{
        const val TASK_ID = "task_id"
    }

}