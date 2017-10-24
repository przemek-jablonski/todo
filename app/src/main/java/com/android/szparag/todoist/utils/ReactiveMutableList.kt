package com.android.szparag.todoist.utils

import com.android.szparag.todoist.utils.ReactiveList.ReactiveChangeType.DELETED
import com.android.szparag.todoist.utils.ReactiveList.ReactiveChangeType.INSERTED
import com.android.szparag.todoist.utils.ReactiveList.ReactiveChangeType.UPDATED
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ReactiveMutableList<E : Any>(private val initialCapacity: Int = 64, private val debuggable: Boolean = false) :
    ArrayList<E>(initialCapacity), MutableList<E>, ReactiveList<E> {

  private val logger: Logger = if (debuggable) Logger.create(this::class.java, hashCode()) else Logger.createInfunctionalStub()
  private var listEventsSubject = PublishSubject.create<ReactiveListEvent<E>>()
  private var listDataSubject = PublishSubject.create<ReactiveList<E>>()

  override val size: Int
    get() = super.size

  override fun subscribeForListEvents(): Observable<ReactiveListEvent<E>> {
    logger.debug("subscribeForListEvents")
    return listEventsSubject
  }

  override fun subscribeForListData(): Observable<ReactiveList<E>> {
    logger.debug("subscribeForListData")
    return listDataSubject
  }


  //todo: fix -1's where they occur
  override fun insert(element: E) {
    logger.debug("insert, element: $element")
    add(element)
        .also { listEventsSubject.onNext(ReactiveListEvent(INSERTED, element, lastIndex())) }
        .also { listDataSubject.onNext(this) }
  }

  override fun insert(index: Int, element: E) {
    logger.debug("insert, index: $index, element: $element")
    add(index, element)
        .also { listEventsSubject.onNext(ReactiveListEvent(INSERTED, element, index)) }
        .also { listDataSubject.onNext(this) }
  }

  override fun insert(elements: Collection<E>) {
    logger.debug("insert, elements: $elements")
    addAll(elements)
        .also { listEventsSubject.onNext(ReactiveListEvent(INSERTED, elements, lastIndex() - (elements.size-1))) }
        .also { listDataSubject.onNext(this) }
  }

  override fun insert(index: Int, elements: Collection<E>) {
    logger.debug("insert, index: $index, elements: $elements")
    addAll(index, elements)
        .also { listEventsSubject.onNext(ReactiveListEvent(INSERTED, elements, index)) }
        .also { listDataSubject.onNext(this) }
  }

  override fun delete(element: E) {
    logger.debug("delete, element: $element")
    super.indexOf(element).let { index ->
      remove(element)
          .also { listEventsSubject.onNext(ReactiveListEvent(DELETED, element, index)) }
          .also { listDataSubject.onNext(this) }
    }
  }

  override fun delete(index: Int) {
    logger.debug("delete, index: $index")
    removeAt(index)
        .also { listEventsSubject.onNext(ReactiveListEvent(DELETED, it, index)) }
        .also { listDataSubject.onNext(this) }
  }

  override fun delete(elements: Collection<E>) {
    logger.debug("remove, elements: $elements")
    removeAll(elements)
        .also { listEventsSubject.onNext(ReactiveListEvent(DELETED, elements, -1)) } //todo -1
        .also { listDataSubject.onNext(this) }
  }

//  override fun delete(indexes: Pair<Int, Int>) {
//    logger.debug("remove, indexes: $indexes")
//    removeRange(indexes.first, indexes.second)
//        .also { listEventsSubject.onNext(ReactiveListEvent(DELETED, )) }
//        .also { listDataSubject.onNext(this) }
//  }

  override fun update(index: Int, element: E) {
    logger.debug("update, index: $index, element: $element")
    set(index, element)
        .also { listEventsSubject.onNext(ReactiveListEvent(UPDATED, element, index)) }
        .also { listDataSubject.onNext(this) }
  }

  override fun update(originalElement: E, updatedElement: E) {
    logger.debug("update, originalElement: $originalElement. updatedElement: $")
    indexOf(originalElement).let { index ->
      set(index, updatedElement)
          .also { listEventsSubject.onNext(ReactiveListEvent(DELETED, updatedElement, index)) }
          .also { listDataSubject.onNext(this) }
    }
  }

  //todo: figure out rangeListOf indexing - if those should be indexes indeed, then 0..size-1 below:
  override fun clear() {
    logger.debug("clear")
    size.let { size ->
      super.clear()
          .also { listEventsSubject.onNext(ReactiveListEvent(DELETED, this, 0)) } //todo: event CLEARED
          .also { listDataSubject.onNext(this) }
    }
  }

  override fun get(index: Int): E {
    logger.debug("get, index: $index")
    return super.get(index)
  }

  //todo: test - check if when 0-indexed element is deleted, list shrinks and fills the space of this element
  override fun first() = get(0)

  override fun last() = get(size - 1)

  //todo: this produces ArrayIndexOutOfBoundsException if list is empty
  override fun boundary(forward: Boolean) = if (forward) last() else first()

  override fun toString() = asString()

  fun lastIndex() = Math.max(0, size-1)

}