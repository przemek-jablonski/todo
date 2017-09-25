package com.android.szparag.todoist.models.implementations

import com.android.szparag.todoist.models.contracts.CalendarModel
import com.android.szparag.todoist.utils.Logger
import com.android.szparag.todoist.utils.unixTime
import io.reactivex.Completable
import java.util.Calendar
import java.util.Locale
import org.joda.time.DateTimeZone
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.DateTimeFieldType.dayOfWeek
import org.joda.time.Period
import org.joda.time.Weeks


//todo: locale is useless here
class TodoistCalendarModel(private val locale: Locale): CalendarModel {

  override lateinit var calendar: Calendar
  override lateinit var logger: Logger

  override fun attach(): Completable {
    return Completable.fromAction {
      logger = Logger.create(this::class)
      logger.debug("attach")
      setupCalendarInstance()
    }
  }

  override fun setupCalendarInstance() {
    logger.debug("setupCalendarInstance")
    calendar = Calendar.getInstance(locale)
    val localTime = LocalTime()
    val localDate = LocalDate()
    val dateTime = DateTime()
    val localDateTime = LocalDateTime()
    val dateTimeZone = DateTimeZone.getDefault()
    val startOfWeek = dateTime.minusDays(dateTime.dayOfWeek().get() - 1)
    val endOfWeek = dateTime.plusDays(7 - dateTime.dayOfWeek().get())
    val currentWeekPeriod = Period(startOfWeek.unixTime(), endOfWeek.unixTime())
    val currentWeekWeeks = Weeks.standardWeeksIn(currentWeekPeriod)
    logger.debug("setupCalendarInstance")
  }

  override fun detach() = Completable.fromAction { logger.debug("detach") }

  override fun getCurrentDay() {
    logger.debug("getCurrentDay")

  }

  override fun getCurrentWeek() {
    logger.debug("getCurrentWeek")

  }


}