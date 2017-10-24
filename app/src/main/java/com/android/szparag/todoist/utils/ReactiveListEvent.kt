package com.android.szparag.todoist.utils

import com.android.szparag.todoist.utils.ReactiveList.ReactiveChangeType

data class ReactiveListEvent<E>(
    val eventType: ReactiveChangeType,
    val affectedItems: Collection<E>,
    val fromIndexInclusive: Int
) {
  constructor(eventType: ReactiveChangeType, affectedItem: E, fromIndexInclusive: Int): this(eventType, listOf(affectedItem), fromIndexInclusive)
}