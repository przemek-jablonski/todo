package com.android.szparag.todoist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.szparag.todoist.WeekRvAdapter.WeekViewHolder
import com.android.szparag.todoist.models.entities.RenderDay
import com.android.szparag.todoist.utils.bindView

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 9/16/2017.
 */
class WeekRvAdapter(private val list: List<RenderDay>) : RecyclerView.Adapter<WeekViewHolder>() {


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
      WeekViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_week_day, parent, false))

  override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
    val weekDay = list[position]
    holder.calendarWeekDay.text = weekDay.dayName
    holder.calendarWeekDate.text = "${weekDay.dayNumber} ${weekDay.monthName} ${weekDay.yearNumber}"
    holder.calendarWeekTasksDone.text = "${weekDay.tasksDoneCount} tasks done"
    holder.calendarWeekTasksRemaining.text = "${weekDay.tasksRemainingCount} tasks remaining"
  }

  override fun getItemCount() = list.size


  class WeekViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    internal val calendarWeekGraph: ImageView by bindView(R.id.calendarWeekGraph)
    internal val calendarWeekDay: TextView by bindView(R.id.calendarWeekDay)
    internal val calendarWeekDate: TextView by bindView(R.id.calendarWeekDate)
    internal val calendarWeekTasksDone: TextView by bindView(R.id.calendarWeekTasksDone)
    internal val calendarWeekTasksRemaining: TextView by bindView(R.id.calendarWeekTasksRemaining)
    internal val calendarWeekAlarm: ImageView by bindView(R.id.calendarWeekAlarm)
    internal val calendarWeekAlarmHour: TextView by bindView(R.id.calendarWeekAlarmHour)
  }

}