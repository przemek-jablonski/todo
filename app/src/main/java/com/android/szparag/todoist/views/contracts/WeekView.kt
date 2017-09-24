package com.android.szparag.todoist.views.contracts

import io.reactivex.Observable


interface WeekView : View {

  fun onUserDayPicked(): Observable<Any>

}