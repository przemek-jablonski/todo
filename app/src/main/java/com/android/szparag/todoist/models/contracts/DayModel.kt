package com.android.szparag.todoist.models.contracts

import com.android.szparag.todoist.events.DayCalendarEvent
import com.android.szparag.todoist.events.DayTasksEvent
import io.reactivex.Observable
import io.reactivex.Single

typealias UnixTimestamp = Long

interface DayModel : Model {

  fun getDayData(unixTimestamp: UnixTimestamp): Single<DayCalendarEvent>

  fun subscribeForTasksData(unixTimestamp: UnixTimestamp): Observable<DayTasksEvent>

}