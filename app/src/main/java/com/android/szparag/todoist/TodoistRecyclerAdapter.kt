package com.android.szparag.todoist

interface TodoistRecyclerAdapter<E : Any> {

  fun updateData(updatedList: List<E>)

  fun updateData(updatedListRanges: List<E>, fromIndex: Int, changedElementsCount: Int)

  fun get(index: Int): E

  fun get(): List<E>

}