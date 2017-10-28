package com.android.szparag.todoist.models.implementations

import com.android.szparag.todoist.events.DayCalendarEvent
import com.android.szparag.todoist.events.DayTasksEvent
import com.android.szparag.todoist.models.contracts.DayModel
import com.android.szparag.todoist.models.contracts.UnixTimestamp
import com.android.szparag.todoist.models.entities.TodoistDay
import com.android.szparag.todoist.utils.Logger
import com.android.szparag.todoist.utils.debugAllObjects
import com.android.szparag.todoist.utils.safeFirst
import com.android.szparag.todoist.utils.toDayCalendarEvent
import com.android.szparag.todoist.utils.toDayTasksEvent
import com.android.szparag.todoist.utils.toObservable
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.realm.Realm
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.util.Locale

class TodoistDayModel(private val locale: Locale, private val realm: Realm) : DayModel {

  override val logger = Logger.create(this)
  private lateinit var currentDay: LocalDate

  override fun getDayData(unixTimestamp: UnixTimestamp): Single<DayCalendarEvent> {
    logger.debug("getDayData, unixTimestamp: $unixTimestamp")
    check(unixTimestamp >= 0)
    return Single.fromCallable { DateTime(unixTimestamp).toDayCalendarEvent(locale) }
  }

  override fun subscribeForTasksData(unixTimestamp: UnixTimestamp): Observable<DayTasksEvent> {
    logger.debug("subscribeForTasksData, unixTimestamp: $unixTimestamp")
    check(unixTimestamp >= 0)
    realm.debugAllObjects(TodoistDay::class.java, logger)
    return realm.where(TodoistDay::class.java)
        .equalTo("unixTimestamp", unixTimestamp)
        .findAll()
        .toObservable()
        .map { results -> results.safeFirst()?.toDayTasksEvent() ?: DayTasksEvent.empty(unixTimestamp) }
  }


  override fun attach(): Completable {
    logger.debug("attach")
    return Completable.fromAction {
      currentDay = LocalDate()
    }
  }

  override fun detach(): Completable {
    logger.debug("detach")
    return Completable.fromAction {
      realm.close()
    }
  }


}