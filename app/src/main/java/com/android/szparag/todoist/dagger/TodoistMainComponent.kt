@file:Suppress("KDocMissingDocumentation")

package com.android.szparag.todoist.dagger

import com.android.szparag.todoist.presenters.implementations.TodoistDayPresenter
import com.android.szparag.todoist.presenters.implementations.TodoistFrontPresenter
import com.android.szparag.todoist.views.implementations.TodoistDayActivity
import com.android.szparag.todoist.views.implementations.TodoistFrontActivity
import dagger.Component
import javax.inject.Singleton

@Singleton @Component(modules = arrayOf(TodoistMainModule::class)) interface TodoistMainComponent {

  fun inject(target: TodoistFrontActivity)
  fun inject(target: TodoistDayActivity)

  fun inject(target: TodoistFrontPresenter)
  fun inject(target: TodoistDayPresenter)

}