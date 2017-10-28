package com.android.szparag.todoist.widgets.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.szparag.todoist.R.id
import com.android.szparag.todoist.R.layout
import com.android.szparag.todoist.utils.Logger
import com.android.szparag.todoist.utils.bindView
import com.android.szparag.todoist.widgets.adapters.FrontTasksAdapter.TaskViewHolder

class FrontTasksAdapter : RecyclerView.Adapter<TaskViewHolder>() {

  private var tasksList = emptyList<String>()
  private var layoutInflater: LayoutInflater? = null
    private set(value) {
      if (value == null) return else field = value
    }


  private fun getLayoutInflater(context: Context) = layoutInflater?.let { it } ?: LayoutInflater.from(context)

  //todo: delete that
  fun updateData(tasksList: List<String>) {
    this.tasksList = tasksList
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TaskViewHolder(
      getLayoutInflater(parent.context)
          .inflate(layout.item_recycler_front_task, parent, false)
  )

  override fun onBindViewHolder(holder: TaskViewHolder?, position: Int) {
    val item = tasksList[position]
    holder?.let {
      it.taskText.text = item
    }
  }

  override fun getItemCount() = tasksList.size

  class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val taskText: TextView by bindView(id.taskText)
  }
}