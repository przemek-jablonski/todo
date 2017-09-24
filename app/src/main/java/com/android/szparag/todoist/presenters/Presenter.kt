package com.android.szparag.todoist.presenters

import com.android.szparag.todoist.views.contracts.View

interface Presenter<V : View> {


  /**
   *  Attaches given View to this presenter, allowing two-way communication.
   *  Should be implemented in some abstract base Presenter as a final method.
   *
   *  Calls Presenter#onAttached() method when succeeded.
   */
  fun attach(view: V)

  /**
   *  Called when View attachment (#attach() method) was successful.
   *
   *  Should be left blank in abstract base Presenter, so that non-abstract
   *  classes extending the Presenter interface can perform additional mandatory setup operations
   *  like permission/network checks here.
   */
  fun onAttached()

  /**
   *  Detaching View from this Presenter.
   *  Should be called upon View decomposition (onStop / onDestroy methods etc.).
   *  Should be implemented in some abstract base Presenter as a final method.
   *
   *  Calls Presenter#onBeforeDetached() just before actually decomposing
   *  aggregated references and dependencies.
   */
  fun detach()

  /**
   *  Called just before View detachment operation (#detach() method) is completed.
   *
   *  Should be left blank in abstract base Presenter, so that non-abstract
   *  classes extending the Presenter interface can perform mandatory cleaning before
   *  View is detached and Presenter decomposed.
   */
  fun onBeforeDetached()

  /**
   *  Called when given View is fully instantiated and ready to perform actions
   *  ordered by the Presenter.
   */
  fun onViewReady()


}