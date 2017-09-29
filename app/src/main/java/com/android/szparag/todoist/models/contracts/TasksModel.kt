package com.android.szparag.todoist.models.contracts

import com.android.szparag.todoist.models.entities.TodoistDay
import com.android.szparag.todoist.models.entities.TodoistTask
import io.reactivex.Flowable
import io.reactivex.Observable

interface TasksModel: Model {

  fun getDay(dayMillis: Long): Observable<TodoistDay>
  fun getTasksForDay(dayMillis: Long): Observable<List<TodoistTask>>

}