package com.android.szparag.todoist.utils

import com.android.szparag.todoist.utils.ReactiveList.ReactiveChangeType

data class ReactiveListEvent(
    val eventType: ReactiveChangeType,
    val affectedIndexes: List<Int>
) {
  constructor(eventType: ReactiveChangeType, affectedIndex: Int) : this(eventType, listOf(affectedIndex))
}