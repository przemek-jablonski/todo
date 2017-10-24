package com.android.szparag.todoist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.szparag.todoist.FrontTasksAdapter.TaskViewHolder
import com.android.szparag.todoist.utils.Logger
import com.android.szparag.todoist.utils.bindView

class FrontTasksAdapter : RecyclerView.Adapter<TaskViewHolder>() {

  private val logger by lazy { Logger.create(this::class.java, hashCode()) }
  private var tasksList = emptyList<String>()
  private var layoutInflater: LayoutInflater? = null
    private set(value) {
      if (value == null) return else field = value
    }

  private fun getLayoutInflater(context: Context): LayoutInflater {
    logger.debug("getLayoutInflater, inflater: $layoutInflater")
    if (layoutInflater == null) {
      layoutInflater = LayoutInflater.from(context)
    }
    return layoutInflater!!
  }

  fun updateData(tasksList: List<String>) {
    this.tasksList = tasksList
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TaskViewHolder(
      getLayoutInflater(parent.context)
          .inflate(R.layout.item_recycler_front_task, parent, false)
  )

  override fun onBindViewHolder(holder: TaskViewHolder?, position: Int) {
    val item = tasksList[position]
    holder?.let {
      it.taskText.text = item
    }
  }

  override fun getItemCount() = tasksList.size

  class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val taskText: TextView by bindView(R.id.taskText)
  }
}