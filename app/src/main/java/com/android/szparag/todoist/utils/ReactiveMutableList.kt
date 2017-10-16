package com.android.szparag.todoist.utils

import com.android.szparag.todoist.utils.ReactiveList.ReactiveChangeType.DELETED
import com.android.szparag.todoist.utils.ReactiveList.ReactiveChangeType.INSERTED
import com.android.szparag.todoist.utils.ReactiveList.ReactiveChangeType.UPDATED
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ReactiveMutableList<E : Any>(private val initialCapacity: Int = 64, private val debuggable: Boolean = false) :
    ArrayList<E>(initialCapacity), MutableList<E>, ReactiveList<E> {

  private val logger: Logger = if (debuggable) Logger.create(this::class.java, hashCode()) else Logger.createInfunctionalStub()
  private val listEventsSubject = PublishSubject.create<ReactiveListEvent>()

  override val size: Int
    get() = super.size

  override fun subscribeForListEvents(): Observable<ReactiveListEvent> {
    logger.debug("subscribeForListEvents")
    return listEventsSubject
  }

  //todo: fix -1's where they occur
  override fun insert(element: E) {
    logger.debug("insert")
    add(element)
        .also { listEventsSubject.onNext(ReactiveListEvent(INSERTED, size)) }
  }

  override fun insert(index: Int, element: E) {
    logger.debug("add, index: $index, element: $element")
    add(index, element)
        .also { listEventsSubject.onNext(ReactiveListEvent(INSERTED, index)) }
  }

  override fun insert(elements: Collection<E>) {
    logger.debug("add, elements: $elements")
    addAll(elements)
        .also { listEventsSubject.onNext(ReactiveListEvent(INSERTED, rangeListOf(size - elements.size..size))) }
  }

  override fun insert(index: Int, elements: Collection<E>) {
    logger.debug("add, index: $index, elements: $elements")
    addAll(index, elements)
        .also { listEventsSubject.onNext(ReactiveListEvent(INSERTED, rangeListOf(index..index + elements.size))) }
  }

  override fun delete(element: E) {
    logger.debug("delete")
    super.indexOf(element).let { index ->
      remove(element).also { listEventsSubject.onNext(ReactiveListEvent(DELETED, index)) }
    }
  }

  override fun delete(index: Int) {
    logger.debug("delete")
    removeAt(index)
        .also { listEventsSubject.onNext(ReactiveListEvent(DELETED, index)) }
  }

  override fun delete(elements: Collection<E>) {
    logger.debug("remove, elements: $elements")
    removeAll(elements)
        .also { listEventsSubject.onNext(ReactiveListEvent(DELETED, -1)) }
  }

  override fun delete(indexes: Pair<Int, Int>) {
    logger.debug("remove, indexes: $indexes")
    removeRange(indexes.first, indexes.second)
        .also { listEventsSubject.onNext(ReactiveListEvent(DELETED, rangeListOf(indexes.first..indexes.second))) }
  }

  override fun update(index: Int, element: E) {
    logger.debug("update, index: $index, element: $element")
    set(index, element)
        .also { listEventsSubject.onNext(ReactiveListEvent(UPDATED, index)) }
  }

  override fun update(originalElement: E, updatedElement: E) {
    logger.debug("update, originalElement: $originalElement. updatedElement: $")
    indexOf(originalElement).let { index ->
      set(index, updatedElement).also { listEventsSubject.onNext(ReactiveListEvent(DELETED, index)) }
    }
  }

  //todo: figure out rangeListOf indexing - if those should be indexes indeed, then 0..size-1 below:
  override fun clear() {
    logger.debug("clear")
    size.let { size ->
      super.clear().also { listEventsSubject.onNext(ReactiveListEvent(DELETED, rangeListOf(0..size))) }
    }
  }

  override fun get(index: Int): E {
    logger.debug("get, index: $index")
    return super.get(index)
  }

}