package com.android.szparag.todoist.models.contracts

import com.android.szparag.todoist.utils.Logger
import io.reactivex.Completable

interface Model {

  val logger: Logger

  fun attach(): Completable
  fun detach(): Completable
}