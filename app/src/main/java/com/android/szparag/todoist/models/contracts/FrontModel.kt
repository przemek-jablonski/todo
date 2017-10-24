package com.android.szparag.todoist.models.contracts

import com.android.szparag.todoist.models.entities.RenderDay
import com.android.szparag.todoist.utils.ReactiveList
import com.android.szparag.todoist.utils.ReactiveListEvent
import io.reactivex.Flowable
import io.reactivex.Observable
import org.joda.time.LocalDate

interface FrontModel: CalendarModel, TasksModel {

  fun subscribeForDaysList(): Flowable<ReactiveListEvent<RenderDay>>

}