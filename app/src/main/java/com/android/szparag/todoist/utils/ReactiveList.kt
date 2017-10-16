package com.android.szparag.todoist.utils

import io.reactivex.Observable

interface ReactiveList<E : Any> {

  enum class ReactiveChangeType {
    INSERTED, DELETED, UPDATED, RESET
  }

  val size: Int

  fun insert(element: E)
  fun insert(index: Int, element: E)
  fun insert(elements: Collection<E>)
  fun insert(index: Int, elements: Collection<E>)

  fun delete(element: E)
  fun delete(index: Int)
  fun delete(elements: Collection<E>)
  fun delete(indexes: Pair<Int, Int>)

  fun update(index: Int, element: E)
  fun update(originalElement: E, updatedElement: E)

  fun clear()

  fun get(index: Int): E

  fun subscribeForListEvents(): Observable<ReactiveListEvent>
//  fun subscribeForDataChanges(): Observable<>

//  fun find(elem)
  fun first(): E
  fun last(): E
  fun boundary(forward: Boolean): E
}