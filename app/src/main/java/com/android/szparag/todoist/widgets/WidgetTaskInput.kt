package com.android.szparag.todoist.widgets

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.widget.Button
import android.widget.EditText
import com.android.szparag.todoist.R
import com.android.szparag.todoist.utils.bindView
import com.android.szparag.todoist.utils.ui
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject

class WidgetTaskInput @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

  private val inputEditText: EditText by bindView(R.id.inputEditText)
  private val inputButton: Button by bindView(R.id.inputButton)
  private val textAcceptedSubject by lazy { PublishSubject.create<CharSequence>() }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    RxTextView.textChanges(inputEditText).ui().subscribeBy(onNext = { text -> onInputEditTextTextChanged(text) })
    RxView.focusChanges(inputEditText).ui().subscribeBy(onNext = { focused ->
      if (focused) onInputEditTextFocusGained() else onInputEditTextFocusLost()
    })
    RxView.clicks(inputButton).ui().subscribeBy(onNext = { onInputButtonClicked() })
  }

  init {
  }

  fun subscribeTextAccepted(): Observable<CharSequence> = textAcceptedSubject

  private fun onInputButtonClicked() {
    textAcceptedSubject.onNext(inputEditText.text.toString())
    inputEditText.text = SpannableStringBuilder("") //wtf
  }

  private fun onInputEditTextTextChanged(string: CharSequence) {

  }


  private fun onInputEditTextFocusGained() {

  }

  private fun onInputEditTextFocusLost() {

  }

}