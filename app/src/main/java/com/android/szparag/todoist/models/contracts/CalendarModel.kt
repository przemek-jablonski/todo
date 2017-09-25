package com.android.szparag.todoist.models.contracts

import java.util.Calendar

interface CalendarModel: Model {

  val calendar: Calendar

  fun setupCalendarInstance()

  fun getCurrentDay()

  fun getCurrentWeek()

}