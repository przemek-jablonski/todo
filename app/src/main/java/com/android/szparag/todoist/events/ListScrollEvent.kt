package com.android.szparag.todoist.events

import android.support.v7.widget.RecyclerView

data class ListScrollEvent(
    val dx: Int,
    val dy: Int,
    val state: ScrollEventState,
    val firstVisibleItemPos: Int,
    val lastVisibleItemPos: Int,
    val lastItemOnListPos: Int
) {

  constructor(dx: Int, dy: Int, stateInt: Int, firstVisibleItemPos: Int, lastItemOnListPos: Int, lastVisibleItemPos: Int)
      : this(dx, dy, ScrollEventState.convertFrom(stateInt), firstVisibleItemPos, lastItemOnListPos, lastVisibleItemPos)


  enum class ScrollEventState {
    IDLE, DRAGGING, SETTLING;

    companion object {
      fun convertFrom(stateInt: Int) = when (stateInt) {
        RecyclerView.SCROLL_STATE_DRAGGING -> {
          ScrollEventState.DRAGGING
        }
        RecyclerView.SCROLL_STATE_SETTLING -> {
          ScrollEventState.SETTLING
        }
        else -> {
          ScrollEventState.IDLE
        }
      }
    }
  }

}