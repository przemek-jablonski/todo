package com.android.szparag.todoist.presenters.contracts

import com.android.szparag.todoist.views.contracts.DayView

typealias UnixTimestamp = Long

interface DayPresenter : Presenter<DayView> {

  fun attach(view: DayView, dayUnixTimestamp: UnixTimestamp)

}