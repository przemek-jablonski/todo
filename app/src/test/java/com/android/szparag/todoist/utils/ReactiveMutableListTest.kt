package com.android.szparag.todoist.utils

import com.android.szparag.todoist.utils.ReactiveList.ReactiveChangeType.DELETED
import com.android.szparag.todoist.utils.ReactiveList.ReactiveChangeType.INSERTED
import com.android.szparag.todoist.utils.ReactiveList.ReactiveChangeType.UPDATED
import io.reactivex.Observer
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.never
import org.mockito.Mockito.reset
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.Random
import java.util.UUID

typealias ReactiveListEventString = ReactiveListEvent<String>

class ReactiveMutableListTest {

  @Mock private lateinit var stringListObserverMock: Observer<ReactiveListEvent<String>>
  private lateinit var stringList: ReactiveList<String>
  private val random: Random by lazy { Random() }

  @Before fun setupTests() {
    MockitoAnnotations.initMocks(this)
    stringList = ReactiveMutableList()
    stringList.subscribeForListEvents().subscribe(stringListObserverMock)
  }


  private fun getRandomStrings() = mutableListOf<String>()
      .apply { repeat(getRandomElementsCount(), { this.add(getRandomString()) }) }

  private fun getRandomString() = UUID.randomUUID().toString()

  private fun getRandomElementsCount(maxAddedRandomElements: Int = 25) = random.nextIntPositive(maxAddedRandomElements) + 25

  private fun setupMockForAcceptingOneEventType(acceptedEventType: ReactiveList.ReactiveChangeType) {
//    `when`(stringListObserverMock.onNext(any(ReactiveListEvent<String>::class.java))).thenAnswer {
//      it.getArgument<ReactiveListEvent<String>>(0).let { event ->
//        if (event.eventType != acceptedEventType)
//          throw RuntimeException("Unexpected eventType. Expected $acceptedEventType, got actual: ${event.eventType}")
//      }
//    }
  }


  @Test fun `List should have 0 size upon construction`() {
    assertTrue(stringList.size == 0)
  }

  @Test(expected = IndexOutOfBoundsException::class) fun `List should throw exception if any get operation called upon construction`() {
    stringList.get(0)
    stringList.get(getRandomElementsCount())
  }

  @Test(expected = IndexOutOfBoundsException::class) fun `List should always throw exception if get with any negative index called`() {
    repeat(getRandomElementsCount(), { stringList.insert(getRandomString()) })
    stringList.get(random.nextInt(10) - 10)
  }

  @Test fun `Single item insertion should grow list size and return last valid element`() {
    val singleRandomItem = getRandomString()
    stringList.insert(singleRandomItem)
    assertTrue(stringList.size == 1 && stringList.get(stringList.size - 1) == singleRandomItem)
  }

  @Test fun `Group item insertion should grow list size and return last valid element`() {
    repeat(getRandomElementsCount(), { stringList.insert(getRandomString()) })
    var randomInsertedCount = stringList.size
    val singleRandomItem = getRandomString()
    stringList.insert(singleRandomItem)
    ++randomInsertedCount

    assertTrue(stringList.size == randomInsertedCount)
    assertTrue(stringList.get(stringList.size - 1) == singleRandomItem)
  }

  @Test fun `Single insert at the front should push list elements forward`() {
    repeat(getRandomElementsCount(), { stringList.insert(getRandomString()) })
    val cachedSize = stringList.size
    val cachedElement = stringList.get(0)
    val singleRandomItem = getRandomString()

    stringList.insert(0, singleRandomItem)
    assertTrue(stringList.size == cachedSize + 1)
    assertTrue(stringList.get(0) == singleRandomItem)
    assertTrue(stringList.get(1) == cachedElement)
  }

  @Test fun `Single insert at the middle should push list elements forward`() {
    repeat(getRandomElementsCount(), { stringList.insert(getRandomString()) })
    val cachedSize = stringList.size
    val cachedSizeMiddle = cachedSize / 2
    val cachedElement = stringList.get(cachedSizeMiddle - 1)
    val singleRandomItem = getRandomString()

    stringList.insert(cachedSizeMiddle, singleRandomItem)
    assertTrue(stringList.size == cachedSize + 1)
    assertTrue(stringList.get(cachedSizeMiddle - 0) == singleRandomItem)
    assertTrue(stringList.get(cachedSizeMiddle - 1) == cachedElement)
  }

  @Test fun `Single insert at the end should append to the list`() {
    repeat(getRandomElementsCount(), { stringList.insert(getRandomString()) })
    val cachedSize = stringList.size
    val cachedElement = stringList.get(cachedSize - 1)
    val singleRandomItem = getRandomString()

    stringList.insert(cachedSize, singleRandomItem)
    assertTrue(stringList.size == cachedSize + 1)
    assertTrue(stringList.get(cachedSize - 0) == singleRandomItem)
    assertTrue(stringList.get(cachedSize - 1) == cachedElement)
  }

  @Test fun `Group insert with index should expand the list from passed index`() {
    stringList.insert(getRandomStrings())
    val cachedListSize = stringList.size
    val cached2ndItem = stringList.get(1)
    val cached3rdItem = stringList.get(2)

    val groupCount = 10
    val groupInsertList = mutableListOf<String>().apply { repeat(groupCount, { this.add(getRandomString()) }) }
    stringList.insert(2, groupInsertList)

    assertTrue(stringList.size == cachedListSize + groupCount)
    assertTrue(stringList.get(1) == cached2ndItem)
    assertTrue(stringList.get(2) == groupInsertList[0])
    assertTrue(stringList.get(2 + groupCount - 1) == groupInsertList.last())
    assertTrue(stringList.get(2 + groupCount) == cached3rdItem)
  }

  @Test fun `Single delete of existing element should delete item and shrink list`() {
    stringList.insert(getRandomStrings())
    val cachedListSize = stringList.size
    val itemToDeleteBefore = stringList.get(4)
    val itemToDelete = stringList.get(5)
    val itemToDeleteAfter = stringList.get(6)

    stringList.delete(itemToDelete)

    assertTrue(stringList.size == cachedListSize - 1)
    assertTrue(stringList.get(4) == itemToDeleteBefore)
    assertTrue(stringList.get(5) == itemToDeleteAfter)
  }

  @Test fun `Multiple delete of existing elements should delete items and shrink list`() {
    stringList.insert(getRandomStrings())
    val cachedListSize = stringList.size
    val itemToDeleteBefore = stringList.get(4)
    val itemToDelete1 = stringList.get(5)
    val itemToDelete2 = stringList.get(6)
    val itemToDelete3 = stringList.get(7)
    val itemToDeleteAfter = stringList.get(8)

    stringList.delete(itemToDelete1)
    stringList.delete(itemToDelete2)
    stringList.delete(itemToDelete3)

    assertTrue(stringList.size == cachedListSize - 3)
    assertTrue(stringList.get(4) == itemToDeleteBefore)
    assertTrue(stringList.get(5) == itemToDeleteAfter)
  }

  @Test fun `Single insert operation should result in single onNext Observer notification`() {
    setupMockForAcceptingOneEventType(INSERTED)

    stringList.insert(getRandomString())

//    verify(stringListObserverMock, times(1)).onNext(ReactiveListEvent<String>(INSERTED, stringList.size))
    verify(stringListObserverMock, never()).onError(any(Throwable::class.java))
    verify(stringListObserverMock, never()).onComplete()
  }

  @Test fun `Multiple insert operation should result in multiple onNext Observer notification`() {
    setupMockForAcceptingOneEventType(INSERTED)

    stringList.insert(getRandomString())
    stringList.insert(getRandomStrings())
    stringList.insert(getRandomString())

//    verify(stringListObserverMock, times(3)).onNext(any(ReactiveListEvent::class.java))
    verify(stringListObserverMock, never()).onError(any(Throwable::class.java))
    verify(stringListObserverMock, never()).onComplete()
  }

  @Test fun `Single update operation should result in single onNext Observer notification`() {
    stringList.insert(getRandomStrings())
    setupMockForAcceptingOneEventType(UPDATED)
    reset(stringListObserverMock)

    stringList.update(0, getRandomString())

//    verify(stringListObserverMock, times(1)).onNext(ReactiveListEvent(UPDATED, 0))
    verify(stringListObserverMock, never()).onError(any(Throwable::class.java))
    verify(stringListObserverMock, never()).onComplete()
  }

  @Test fun `Multiple update operation should result in multiple onNext Observer notification`() {
    stringList.insert(getRandomStrings())
    setupMockForAcceptingOneEventType(UPDATED)
    reset(stringListObserverMock)

    stringList.update(0, getRandomString())
    stringList.update(1, getRandomString())
    stringList.update(stringList.get(stringList.size - 1), getRandomString())

//    verify(stringListObserverMock, times(3)).onNext(any(ReactiveListEvent::class.java))
    verify(stringListObserverMock, never()).onError(any(Throwable::class.java))
    verify(stringListObserverMock, never()).onComplete()
  }

  @Test fun `Single delete operation should result in single onNext Observer notification`() {
    stringList.insert(getRandomStrings())
    setupMockForAcceptingOneEventType(DELETED)
    reset(stringListObserverMock)

    stringList.delete(0)

//    verify(stringListObserverMock, times(1)).onNext(ReactiveListEvent(DELETED, 0))
    verify(stringListObserverMock, never()).onError(any(Throwable::class.java))
    verify(stringListObserverMock, never()).onComplete()
  }

  @Test fun `Multiple delete operation should result in multiple onNext Observer notification`() {
    stringList.insert(getRandomStrings())
    setupMockForAcceptingOneEventType(DELETED)
    reset(stringListObserverMock)

    stringList.delete(0)
//    stringList.delete(Pair(1, 5))
    stringList.delete(mutableListOf(stringList.get(0), stringList.get(2), stringList.get(4), stringList.get(6)))

//    verify(stringListObserverMock, times(3)).onNext(any(ReactiveListEvent::class.java))
    verify(stringListObserverMock, never()).onError(any(Throwable::class.java))
    verify(stringListObserverMock, never()).onComplete()
  }

  @Test fun `Calling auxilliary functions (get or size) should not send events to Observer`() {
    try {
      stringList.get(0)
    } catch (exc: IndexOutOfBoundsException) {
    }
    stringList.size

//    verify(stringListObserverMock, never()).onNext(any(ReactiveListEvent::class.java))
    verify(stringListObserverMock, never()).onError(any(Throwable::class.java))
    verify(stringListObserverMock, never()).onComplete()
  }

  @Test fun `Clearing list should delete all items and issue onNext Observer notification`() {
    stringList.insert(getRandomStrings())
    setupMockForAcceptingOneEventType(DELETED)
    reset(stringListObserverMock)

    val cachedListSize = stringList.size
    stringList.clear()

//    verify(stringListObserverMock, times(1)).onNext(ReactiveListEvent(DELETED, rangeListOf(0..cachedListSize)))
    verify(stringListObserverMock, never()).onError(any(Throwable::class.java))
    verify(stringListObserverMock, never()).onComplete()
  }

}