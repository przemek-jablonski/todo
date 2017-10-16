package com.android.szparag.todoist.models.implementations

import com.android.szparag.todoist.models.contracts.CalendarModel
import com.android.szparag.todoist.models.entities.RenderDay
import com.android.szparag.todoist.models.entities.RenderWeekDays
import com.android.szparag.todoist.utils.Logger
import com.android.szparag.todoist.utils.ReactiveList
import com.android.szparag.todoist.utils.ReactiveMutableList
import com.android.szparag.todoist.utils.add
import com.android.szparag.todoist.utils.boundary
import com.android.szparag.todoist.utils.emptyMutableList
import com.android.szparag.todoist.utils.range
import com.android.szparag.todoist.utils.unixTime
import com.android.szparag.todoist.utils.weekAsDays
import io.reactivex.Completable
import io.reactivex.Observable
import org.joda.time.DateTime
import org.joda.time.DateTimeConstants
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.Period
import org.joda.time.Weeks
import java.util.Calendar
import java.util.Locale
import java.util.Random


//todo: locale is useless here
//todo: or is it not?
class TodoistCalendarModel(private var locale: Locale) : CalendarModel {

  private val random by lazy { Random() }

  private val testList: ReactiveList<LocalDate> = ReactiveMutableList()

  private fun mapToRenderDay(date: LocalDate) = RenderDay(
      dayName = date.dayOfWeek().getAsText(locale),
      dayNumber = date.dayOfMonth,
      monthNumber = date.monthOfYear,
      monthName = date.monthOfYear().getAsText(locale),
      yearNumber = date.year,
      tasksCompletedCount = random.nextInt(20),
      tasksRemainingCount = random.nextInt(20)
  )




  override fun requestRelativeWeekAsDays(weekForward: Boolean, fetchMultiplier: Int) {
    logger.debug("requestRelativeWeekAsDays, weekForward: $weekForward, fetchMultiplier: $fetchMultiplier")
    val boundaryLocalDate = relativeLocalDates.boundary(weekForward)
    range(1, fetchMultiplier - 1).forEach { weekIndex ->
      val appendingLocalDates = boundaryLocalDate.plusWeeks(weekIndex).weekAsDays()
      relativeLocalDates.add(appendingLocalDates)
      logger.debug("requestRelativeWeekAsDays, index: $weekIndex, appending: $appendingLocalDates")
    }.also {
      logger.debug("requestRelativeWeekAsDays, modifiedLocalDates: $relativeLocalDates")
    }
  }

  override fun fetchRelativeWeekAsDays(): Observable<RenderDay> {
    logger.debug("fetchRelativeWeekAsDays, relativeLocalDates: $relativeLocalDates")
    return Observable.fromIterable(relativeLocalDates)
        .doOnSubscribe { resetRelativeWeekAsDays() }
        .map { localDate -> mapToRenderDay(localDate) }
        .doOnEach {
          logger.debug("fetchRelativeWeekAsDays, Observable.fromIterable(relativeLocalDates), notification: $it")
        }
  }


  override fun resetRelativeWeekAsDays() {
    logger.debug("resetRelativeWeekAsDays")
    relativeLocalDates.add(currentDay.weekAsDays())
  }

  override lateinit var logger: Logger
  private val calendar by lazy { Calendar.getInstance(locale) }
  private val currentDay by lazy { LocalDate() }
  private val currentDayStartOfTheWeek by lazy { currentDay.withDayOfWeek(DateTimeConstants.MONDAY) }
  private var selectedDay: LocalDate? = null

  private var relativeLocalDates = emptyMutableList<LocalDate>()


  //_____________
//  private var relative

  override fun attach(): Completable {
    logger = Logger.create(this::class.java, this.hashCode())
    return Completable.fromAction {
      logger.debug("attach")
      setupCalendarInstance()
    }
  }

  override fun setupCalendarInstance() {
    logger.debug("setupCalendarInstance")
    val localTime = LocalTime()
    val localDate = LocalDate()
    val dateTime = DateTime()
    val localDateTime = LocalDateTime()
    val dateTimeZone = DateTimeZone.getDefault()
    val startOfWeek = dateTime.minusDays(dateTime.dayOfWeek().get() - 1)
    val endOfWeek = dateTime.plusDays(7 - dateTime.dayOfWeek().get())
    val currentWeekPeriod = Period(startOfWeek.unixTime(), endOfWeek.unixTime())
    val currentWeekWeeks = Weeks.standardWeeksIn(currentWeekPeriod)

    resetRelativeWeekAsDays()
  }

  override fun detach() = Completable.fromAction { logger.debug("detach") }

  override fun setSelectedDay(dayNumberInTheWeek: Int): Observable<Int> {
    return Observable.create { emitter ->
      selectedDay = currentDayStartOfTheWeek.plusDays(dayNumberInTheWeek)
      logger.debug("setSelectedDay, dayNumberInTheWeek: $dayNumberInTheWeek, selectedDay: $selectedDay")
      emitter.onNext(dayNumberInTheWeek)
    }
  }

  override fun setSelectedDaySync(dayNumberInTheWeek: Int) {
    selectedDay = currentDayStartOfTheWeek.plusDays(dayNumberInTheWeek)
    logger.debug("setSelectedDaySync, dayNumberInTheWeek: $dayNumberInTheWeek, selectedDay: $selectedDay, currentDay: $currentDay")
  }

  override fun getSelectedDay(): Observable<com.android.szparag.todoist.models.entities.RenderDay> {
    logger.debug("getSelectedDay, selectedDay: $selectedDay, currentDay: $currentDay")
    return Observable.create { emitter ->
      selectedDay?.let {
        emitter.onNext(
            RenderDay(
                dayName = it.dayOfWeek().getAsText(locale),
                dayNumber = it.dayOfMonth,
                monthNumber = it.monthOfYear,
                monthName = it.monthOfYear().getAsText(locale),
                yearNumber = it.year,
                tasksCompletedCount = 0,
                tasksRemainingCount = 0
            )
        )
      } ?: emitter.onError(Throwable("selectedday is null")) //todo: empty throwable

    }
  }

  override fun getCurrentWeek(): Observable<RenderWeekDays> {
    logger.debug("getCurrentWeek")
    return Observable.create { emitter ->
      val weekDays = mutableListOf<RenderDay>()
      val now = LocalDate()
      val monday = now.withDayOfWeek(DateTimeConstants.MONDAY)
      (0..6)
          .map { monday.plusDays(it) }
          .mapTo(weekDays) {
            RenderDay(
                dayName = it.dayOfWeek().getAsText(locale),
                dayNumber = it.dayOfMonth,
                monthNumber = it.monthOfYear,
                monthName = it.monthOfYear().getAsText(locale),
                yearNumber = it.year,
                tasksCompletedCount = 0,
                tasksRemainingCount = 0
            )
          }
      emitter.onNext(RenderWeekDays(weekDays))
    }
  }

  override fun updateLocale(locale: Locale) {
    logger.debug("updateLocale, locale: $locale")
    this.locale = locale
  }

}