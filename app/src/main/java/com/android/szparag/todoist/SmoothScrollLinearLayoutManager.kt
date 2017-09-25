package com.android.szparag.todoist

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.State
import android.util.Log
import io.reactivex.Observable


class SmoothScrollLinearLayoutManager(context: Context) : LinearLayoutManager(context) {

  private val customLinearScroller by lazy { CustomLinearSmoothScroller(context) }

  override fun smoothScrollToPosition(recyclerView: RecyclerView, state: State, position: Int) {
    Log.d("RV", "smoothScrollToPosition, state: $state, pos: $position, ")
    startSmoothScroll(customLinearScroller.apply { this.targetPosition = position })
    Log.d("RV", "smoothScrollToPosition, state: $state, pos: $position, ")
  }
}