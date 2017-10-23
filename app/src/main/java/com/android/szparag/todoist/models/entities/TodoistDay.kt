package com.android.szparag.todoist.models.entities

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.RealmClass

//todo unixTimestamp documentation: this is milliseconds of start of the day
@RealmClass open class TodoistDay(@Index var unixTimestamp: Long = -1, var tasks: RealmList<TodoistTask> = RealmList()) : RealmObject() {

  fun getRemainingTasksCount(): Int = -1
  fun getDoneTasksCount(): Int = -1

}