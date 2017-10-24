package com.android.szparag.todoist.dagger

import android.content.Context
import android.os.Build
import com.android.szparag.todoist.models.contracts.CalendarModel
import com.android.szparag.todoist.models.contracts.FrontModel
import com.android.szparag.todoist.models.implementations.TodoistFrontModel
import com.android.szparag.todoist.presenters.contracts.FrontPresenter
import com.android.szparag.todoist.presenters.implementations.TodoistFrontPresenter
import dagger.Module
import dagger.Provides
import java.util.Locale
import java.util.Random
import javax.inject.Singleton

@Module
class TodoistMainModule(private val context: Context) {

  @Provides @Singleton fun provideContext() = context
  @Provides fun provideFrontPresenter(frontModel: FrontModel): FrontPresenter = TodoistFrontPresenter(frontModel)
  @Provides @Singleton fun provideFrontModel(currentLocale: Locale, random: Random): FrontModel = TodoistFrontModel(currentLocale, random)
  @Provides @Singleton fun provideRandom() = Random()

  @Suppress("DEPRECATION") @Provides fun provideCurrentLocale() =
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) context.resources.configuration.locales.get(0)
      else context.resources.configuration.locale


}