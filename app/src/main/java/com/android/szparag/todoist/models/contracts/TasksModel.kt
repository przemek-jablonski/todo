package com.android.szparag.todoist.models.contracts

import com.android.szparag.todoist.models.entities.TodoistDay
import io.reactivex.Single

interface TasksModel : Model {

//  fun getDay(dayMillis: Long): Observable<TodoistDay>
//  fun getTasksForDay(dayMillis: Long): Observable<List<TodoistTask>>

  fun getDay(unixTimestamp: Long): TodoistDay?

}