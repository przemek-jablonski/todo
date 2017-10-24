package com.android.szparag.todoist.models.implementations

import com.android.szparag.todoist.models.contracts.CalendarModel
import com.android.szparag.todoist.models.contracts.FrontModel
import com.android.szparag.todoist.models.entities.RenderDay
import com.android.szparag.todoist.models.entities.TodoistDay
import com.android.szparag.todoist.utils.Logger
import com.android.szparag.todoist.utils.Logger.Companion
import com.android.szparag.todoist.utils.ReactiveList
import com.android.szparag.todoist.utils.ReactiveListEvent
import com.android.szparag.todoist.utils.ReactiveMutableList
import com.android.szparag.todoist.utils.dayUnixTimestamp
import com.android.szparag.todoist.utils.range
import com.android.szparag.todoist.utils.toRenderDay
import com.android.szparag.todoist.utils.weekAsDays
import io.reactivex.BackpressureStrategy.ERROR
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.toSingle
import io.realm.Realm
import org.joda.time.LocalDate
import java.util.Locale
import java.util.Random
import javax.inject.Inject


private const val WEEKDAYS_COUNT = 7
private const val INITIAL_DAYS_CAPACITY = WEEKDAYS_COUNT * 8

//todo: locale is useless here
//todo: or is it not?
//todo: make constructor injection
class TodoistFrontModel @Inject constructor(private var locale: Locale, private val random: Random): FrontModel {

  override val logger by lazy { Logger.create(this::class.java, this.hashCode()) }
  private lateinit var realm: Realm
  private lateinit var currentDay: LocalDate
  private val datesList: ReactiveList<LocalDate> = ReactiveMutableList(INITIAL_DAYS_CAPACITY, true)

  override fun attach(): Completable {
    logger.debug("attach")
    return Completable.fromAction {
      setupCalendarInstance()
      realm = Realm.getDefaultInstance()
      logger.debug("attach.action.finished") //todo refactor
    }
  }

  override fun detach(): Completable = Completable.fromAction {
    logger.debug("detach")
    realm.close()
  }

  private fun setupCalendarInstance() {
    logger.debug("setupCalendarInstance")
    currentDay = LocalDate()
  }

  override fun requestRelativeWeekAsDays(weekForward: Boolean, fetchMultiplier: Int) {
    logger.debug("requestRelativeWeekAsDays, weekForward: $weekForward, fetchMultiplier: $fetchMultiplier")
    val boundaryLocalDate = if (datesList.size == 0) {
      currentDay
    } else {
      datesList.boundary(weekForward)
    }
    val appendingLocalDates = mutableListOf<LocalDate>()
    range(0, fetchMultiplier - 2).forEach { weekIndex ->
      //todo range 0 fetch-2 is little ambiguous, make it more readable
      if (weekForward)
        appendingLocalDates.addAll(boundaryLocalDate.plusWeeks(weekIndex).plusDays(1).weekAsDays())
      else
        appendingLocalDates.addAll(boundaryLocalDate.minusWeeks(weekIndex + 1).weekAsDays())
    }.also {
      if (weekForward) {
        datesList.insert(appendingLocalDates)
      } else {
        datesList.insert(0, appendingLocalDates)
      }
      logger.debug("requestRelativeWeekAsDays, appendingLocalDates: $appendingLocalDates, datesList: $datesList")
    }
  }

  override fun getDay(unixTimestamp: Long): TodoistDay? {
    logger.debug("getDay, unixTimestamp: $unixTimestamp")
    return realm.where(TodoistDay::class.java).equalTo("unixTimestamp", unixTimestamp).findFirst()
  }

  override fun subscribeForDaysList(): Flowable<ReactiveListEvent<RenderDay>> {
    logger.debug("subscribeForDaysList")
    return subscribeForDaysListEvents()
        .map { it -> ReactiveListEvent(
            it.eventType,
            it.affectedItems.map { it.toRenderDay(locale, getDay(it.dayUnixTimestamp())?.tasks?.map { it.name } ?: emptyList(), -1, -1) },
            it.fromIndexInclusive
        ) }
        .toFlowable(ERROR)
  }

  override fun subscribeForDaysListEvents(): Observable<ReactiveListEvent<LocalDate>> {
    logger.debug("subscribeForDaysListEvents")
    return datesList.subscribeForListEvents()
  }

  override fun subscribeForDaysListData(): Observable<ReactiveList<LocalDate>> {
    logger.debug("subscribeForDaysListData")
    return datesList.subscribeForListData()
  }

}