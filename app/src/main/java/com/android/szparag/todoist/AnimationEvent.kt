package com.android.szparag.todoist

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 17/09/2017.
 */
data class AnimationEvent(val eventType: AnimationEventType) {

  enum class AnimationEventType {
    START,
    END,
    REPEAT
  }



}