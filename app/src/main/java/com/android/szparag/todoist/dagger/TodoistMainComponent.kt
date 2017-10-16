@file:Suppress("KDocMissingDocumentation")

package com.android.szparag.todoist.dagger

import com.android.szparag.todoist.views.implementations.TodoistFrontActivity
import dagger.Component
import javax.inject.Singleton

@Singleton @Component(modules = arrayOf(TodoistMainModule::class)) interface TodoistMainComponent {

  fun inject(target: TodoistFrontActivity)

}