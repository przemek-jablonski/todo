package com.android.szparag.todoist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.szparag.todoist.FrontTestAdapter.DayViewHolder
import com.android.szparag.todoist.utils.bindView
import com.android.szparag.todoist.utils.setViewDimensions

class FrontTestAdapter(private val itemWidth: Int? = null, private val itemHeight: Int? = null) :
    RecyclerView.Adapter<DayViewHolder>() {

  private val daysList = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
  private var layoutInflater: LayoutInflater? = null
    private set(value) {
      if (value == null) return else field = value
    }

  private fun getLayoutInflater(context: Context): LayoutInflater {
    if (layoutInflater == null) {
      layoutInflater = LayoutInflater.from(context)
    }
    return layoutInflater!!
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DayViewHolder(
      getLayoutInflater(parent.context)
          .inflate(R.layout.item_recycler_calendar_day, parent, false)
          .apply { setViewDimensions(itemWidth, itemHeight) }
  )


  override fun getItemCount() = daysList.size


  override fun onBindViewHolder(holder: DayViewHolder?, position: Int) {
    holder?.testText?.text = daysList[position].toString()
  }


  class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val testText: TextView by bindView(R.id.textView7)
  }

}