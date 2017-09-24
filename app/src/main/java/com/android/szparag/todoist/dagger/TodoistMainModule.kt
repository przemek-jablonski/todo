package com.android.szparag.todoist.dagger

import android.content.Context
import com.android.szparag.todoist.presenters.TodoistWeekPresenter
import com.android.szparag.todoist.presenters.contracts.WeekPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TodoistMainModule(private val context: Context) {

  @Provides @Singleton fun provideContext() = context

  @Provides fun provideWeekPresenter(): WeekPresenter = TodoistWeekPresenter()

}