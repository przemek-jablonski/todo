package com.android.szparag.todoist.models.implementations

import com.android.szparag.todoist.models.contracts.CalendarModel
import com.android.szparag.todoist.models.entities.RenderDay
import com.android.szparag.todoist.utils.Logger
import com.android.szparag.todoist.utils.ReactiveList
import com.android.szparag.todoist.utils.ReactiveListEvent
import com.android.szparag.todoist.utils.ReactiveMutableList
import com.android.szparag.todoist.utils.range
import com.android.szparag.todoist.utils.weekAsDays
import io.reactivex.Completable
import io.reactivex.Observable
import org.joda.time.LocalDate
import java.util.Locale
import java.util.Random


//todo: locale is useless here
//todo: or is it not?

private const val INITIAL_DAYS_CAPACITY = 7 * 8

class TodoistCalendarModel(private var locale: Locale) : CalendarModel {

  override val logger by lazy { Logger.create(this::class.java, this.hashCode()) }
  private lateinit var currentDay: LocalDate
  private val random by lazy { Random() }
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
    val boundaryLocalDate = datesList.boundary(weekForward)
    range(1, fetchMultiplier - 1).forEach { weekIndex ->
      val appendingLocalDates = boundaryLocalDate.plusWeeks(weekIndex).weekAsDays()
      datesList.insert(appendingLocalDates)
      logger.debug("requestRelativeWeekAsDays, index: $weekIndex, appending: $appendingLocalDates")
    }.also {
      logger.debug("requestRelativeWeekAsDays, datesList: $datesList")
    }
  }

  override fun setupCalendarInstance() {
    logger.debug("setupCalendarInstance")
    currentDay = LocalDate()
  }

  //todo: this should not be here (or its badly used)
   override fun mapToRenderDay(date: LocalDate) = RenderDay(
      dayName = date.dayOfWeek().getAsText(locale),
      dayNumber = date.dayOfMonth,
      monthNumber = date.monthOfYear,
      monthName = date.monthOfYear().getAsText(locale),
      yearNumber = date.year,
      tasksCompletedCount = random.nextInt(20),
      tasksRemainingCount = random.nextInt(20)
  )

  override fun fillDaysListInitial() {
    logger.debug("fillDaysListInitial")
    datesList.insert(currentDay.minusWeeks(1).weekAsDays())
    datesList.insert(currentDay.weekAsDays())
  }

}