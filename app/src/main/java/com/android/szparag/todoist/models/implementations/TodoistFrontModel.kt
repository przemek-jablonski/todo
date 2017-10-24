package com.android.szparag.todoist.models.implementations

import com.android.szparag.todoist.models.contracts.CalendarModel
import com.android.szparag.todoist.models.contracts.FrontModel
import com.android.szparag.todoist.models.entities.RenderDay
import com.android.szparag.todoist.utils.Logger
import com.android.szparag.todoist.utils.Logger.Companion
import com.android.szparag.todoist.utils.ReactiveList
import com.android.szparag.todoist.utils.ReactiveListEvent
import com.android.szparag.todoist.utils.ReactiveMutableList
import com.android.szparag.todoist.utils.dayUnixTimestamp
import com.android.szparag.todoist.utils.range
import com.android.szparag.todoist.utils.weekAsDays
import io.reactivex.Completable
import io.reactivex.Observable
import org.joda.time.LocalDate
import java.util.Locale
import java.util.Random
import javax.inject.Inject


private const val INITIAL_DAYS_CAPACITY = 7 * 8

//todo: locale is useless here
//todo: or is it not?
//todo: make constructor injection
class TodoistFrontModel @Inject constructor(private var locale: Locale, private val random: Random): FrontModel {
  override val logger by lazy { Logger.create(this::class.java, this.hashCode()) }
  private lateinit var currentDay: LocalDate
  private val datesList: ReactiveList<LocalDate> = ReactiveMutableList(INITIAL_DAYS_CAPACITY, true)

  override fun attach(): Completable {
    logger.debug("attach")
    return Completable.fromAction {
      setupCalendarInstance()
      logger.debug("attach.action.finished")
    }
  }

  override fun detach(): Completable = Completable.fromAction { logger.debug("detach") }

  override fun subscribeForDaysListEvents(): Observable<ReactiveListEvent> {
    logger.debug("subscribeForDaysListEvents")
    return datesList.subscribeForListEvents()
  }

  override fun subscribeForDaysListData(): Observable<ReactiveList<LocalDate>> {
    logger.debug("subscribeForDaysListData")
    return datesList.subscribeForListData()
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

  override fun setupCalendarInstance() {
    logger.debug("setupCalendarInstance")
    currentDay = LocalDate()
  }

  //todo: this should not be here (or its badly used)
  override fun mapToRenderDay(date: LocalDate) = RenderDay(
      unixTimestamp = date.dayUnixTimestamp(),
      dayName = date.dayOfWeek().getAsText(locale),
      dayNumber = date.dayOfMonth,
      monthNumber = date.monthOfYear,
      monthName = date.monthOfYear().getAsText(locale),
      yearNumber = date.year,
      tasksList = emptyList(),
      tasksCompletedCount = random.nextInt(20),
      tasksRemainingCount = random.nextInt(20)
  )

  override fun fillDaysListInitial() {
    logger.debug("fillDaysListInitial")
    requestRelativeWeekAsDays(true, 2)
    requestRelativeWeekAsDays(false, 2)
  }


}