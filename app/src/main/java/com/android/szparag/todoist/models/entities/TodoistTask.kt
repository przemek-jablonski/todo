package com.android.szparag.todoist.models.entities

import com.android.szparag.todoist.utils.emptyString
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.RealmClass

@RealmClass open class TodoistTask(@Index var unixCreationTimestamp: Long = -1, var name: String = emptyString()) : RealmObject() {

//  var points: Int = -1
//  var transitional: Boolean = false //todo think about this feature
//  var done: Boolean = false

}