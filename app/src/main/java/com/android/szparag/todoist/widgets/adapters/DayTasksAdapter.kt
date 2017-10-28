package com.android.szparag.todoist.widgets.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.android.szparag.todoist.R
import com.android.szparag.todoist.utils.bindView
import com.android.szparag.todoist.utils.emptyMutableList
import com.android.szparag.todoist.widgets.adapters.DayTasksAdapter.DayTaskViewHolder

class DayTasksAdapter : RecyclerView.Adapter<DayTaskViewHolder>() {

  private var tasksList = emptyMutableList<CharSequence>()

  private var layoutInflater: LayoutInflater? = null
    private set(value) {
      if (value == null) return else field = value
    }

  fun updateData(tasksList: MutableList<CharSequence>) {
    this.tasksList = tasksList
  }

  private fun getLayoutInflater(context: Context) = layoutInflater?.let { it } ?: LayoutInflater.from(context)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DayTaskViewHolder(
      getLayoutInflater(parent.context).inflate(R.layout.item_recycler_day_task, parent, false)
  )

  override fun onBindViewHolder(holder: DayTaskViewHolder?, position: Int) {
    val item = tasksList[position]
    holder?.let {
      it.taskNameText.text = item
    }
  }

  override fun getItemCount() = tasksList.size


  class DayTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val taskSeverityImage: ImageView by bindView(R.id.taskSeverityImage)
    val taskNameText: TextView by bindView(R.id.taskNameText)
    val taskDoneCheckbox: CheckBox by bindView(R.id.taskDoneCheckbox)
  }

}