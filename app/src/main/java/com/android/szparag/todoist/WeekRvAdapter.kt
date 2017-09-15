package com.android.szparag.todoist

import android.icu.util.Calendar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.szparag.todoist.WeekRvAdapter.WeekViewHolder

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 9/16/2017.
 */
class WeekRvAdapter : RecyclerView.Adapter<WeekViewHolder>() {

  val daysOfTheWeekString = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
    return WeekViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview_calendar_week, parent, false))
  }

  override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
    val dayOfTheWeekString = daysOfTheWeekString[position]
    holder.itemTextView.text = dayOfTheWeekString
  }

  override fun getItemCount(): Int {
    return daysOfTheWeekString.size
  }


  class WeekViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    internal var itemTextView : TextView = itemView.findViewById(R.id.calendarWeekText) as TextView

  }

}