package com.android.szparag.todoist.dagger

import android.content.Context
import android.os.Build
import com.android.szparag.todoist.models.contracts.DayModel
import com.android.szparag.todoist.models.contracts.FrontModel
import com.android.szparag.todoist.models.implementations.TodoistDayModel
import com.android.szparag.todoist.models.implementations.TodoistFrontModel
import com.android.szparag.todoist.presenters.contracts.DayPresenter
import com.android.szparag.todoist.presenters.contracts.FrontPresenter
import com.android.szparag.todoist.presenters.implementations.TodoistDayPresenter
import com.android.szparag.todoist.presenters.implementations.TodoistFrontPresenter
import dagger.Module
import dagger.Provides
import io.realm.Realm
import java.util.Locale
import java.util.Random
import javax.inject.Singleton

@Module
class TodoistMainModule(private val context: Context) {

  @Provides @Singleton fun provideContext() = context
  @Provides @Singleton fun provideRandom() = Random()
  @Provides @Singleton fun provideRealm() = Realm.getDefaultInstance()

  @Provides fun provideFrontPresenter(frontModel: FrontModel): FrontPresenter = TodoistFrontPresenter(frontModel)
  @Provides fun provideDayPresenter(dayModel: DayModel): DayPresenter = TodoistDayPresenter(dayModel)

  @Provides @Singleton fun provideFrontModel(currentLocale: Locale, random: Random, realm: Realm): FrontModel =
      TodoistFrontModel(currentLocale, random, realm)

  @Provides @Singleton fun provideDayModel(currentLocale: Locale, realm: Realm): DayModel =
      TodoistDayModel(currentLocale, realm)


  @Suppress("DEPRECATION") @Provides fun provideCurrentLocale() =
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) context.resources.configuration.locales.get(0)
      else context.resources.configuration.locale


}