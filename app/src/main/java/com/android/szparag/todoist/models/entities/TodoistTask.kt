package com.android.szparag.todoist.models.entities

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.RealmClass

@RealmClass open class TodoistTask : RealmObject() {

  @Index var timeCreatedUnix: Long = -1
  lateinit var name: String
  var points: Int = -1
  var transitional: Boolean = false
  var done: Boolean = false

}