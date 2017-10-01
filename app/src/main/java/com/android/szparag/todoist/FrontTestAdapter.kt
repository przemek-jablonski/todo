package com.android.szparag.todoist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.szparag.todoist.FrontTestAdapter.DayViewHolder
import com.android.szparag.todoist.WeekRvAdapter.WeekViewHolder

class FrontTestAdapter(private val itemWidth: Int) : RecyclerView.Adapter<DayViewHolder>() {

  private val daysList = listOf(1, 2, 3, 4, 5, 6, 7)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
    val inflatedItem = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_calendar_day, parent, false)
    val params = inflatedItem.layoutParams
    params.width = itemWidth
    inflatedItem.layoutParams = params
    return DayViewHolder(inflatedItem)
  }


  override fun getItemCount() = daysList.size


  override fun onBindViewHolder(holder: DayViewHolder?, position: Int) {

  }


  class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  }

}