package com.android.szparag.todoist.models.entities

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.RealmClass

@RealmClass open class TodoistDay : RealmObject() {

  @Index var timestampDayStartUnix: Long = -1
  lateinit var tasks: RealmList<TodoistTask>
  lateinit var dayName: String
  var dayNumber: Int = -1
  var weekNumber: Int = -1
  lateinit var monthName: String
  var monthNumber: Int = -1
  var yearNumber: Int = -1

  fun getRemainingTasksCount(): Int = -1
  fun getDoneTasksCount(): Int = -1

}