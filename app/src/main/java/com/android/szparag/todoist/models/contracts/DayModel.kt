package com.android.szparag.todoist.models.contracts

import com.android.szparag.todoist.models.entities.RenderDay
import com.android.szparag.todoist.models.entities.TodoistDay
import io.reactivex.Observable
import io.reactivex.Single
import org.joda.time.LocalDate

typealias UnixTimestamp = Long

interface DayModel : Model {

  fun getDayData(unixTimestamp: UnixTimestamp): Single<RenderDay>

  fun subscribeForTasksData(unixTimestamp: UnixTimestamp): Observable<TodoistDay>

  fun subscribeForDayData(unixTimestamp: UnixTimestamp): Observable<RenderDay>

}