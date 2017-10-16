package com.android.szparag.todoist.dagger

import android.content.Context
import android.os.Build
import com.android.szparag.todoist.models.contracts.CalendarModel
import com.android.szparag.todoist.models.implementations.TodoistCalendarModel
import com.android.szparag.todoist.presenters.contracts.FrontPresenter
import com.android.szparag.todoist.presenters.implementations.TodoistFrontPresenter
import dagger.Module
import dagger.Provides
import java.util.Locale
import javax.inject.Singleton

@Module
class TodoistMainModule(private val context: Context) {

  @Provides @Singleton fun provideContext() = context
  @Provides fun provideFrontPresenter(calendarModel: CalendarModel): FrontPresenter = TodoistFrontPresenter(calendarModel)
  @Provides @Singleton fun provideCalendarModel(currentLocale: Locale): CalendarModel = TodoistCalendarModel(currentLocale)

  @Suppress("DEPRECATION") @Provides fun provideCurrentLocale() =
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) context.resources.configuration.locales.get(0)
      else context.resources.configuration.locale


}