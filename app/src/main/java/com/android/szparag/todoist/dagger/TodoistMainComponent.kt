@file:Suppress("KDocMissingDocumentation")

package com.android.szparag.todoist.dagger

import com.android.szparag.todoist.presenters.contracts.Presenter
import com.android.szparag.todoist.views.implementations.TodoistBaseActivity
import com.android.szparag.todoist.views.implementations.TodoistDayActivity
import com.android.szparag.todoist.views.implementations.TodoistFrontActivity
import com.android.szparag.todoist.views.implementations.TodoistWeekActivity
import dagger.Component
import javax.inject.Singleton

@Singleton @Component(modules = arrayOf(TodoistMainModule::class)) interface TodoistMainComponent {

  fun inject(target: TodoistWeekActivity)
  fun inject(target: TodoistDayActivity)
  fun inject(target: TodoistFrontActivity)

}