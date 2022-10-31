package com.d201.depth.depth.common

import android.R
import android.app.Activity
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class SnackbarHelper {
    private val BACKGROUND_COLOR = -0x40cdcdce
    private var messageSnackbar: Snackbar? = null

    private enum class DismissBehavior {
        HIDE, SHOW, FINISH
    }

    private var maxLines = 2
    private var lastMessage = ""

    fun isShowing(): Boolean {
        return messageSnackbar != null
    }

    /** Shows a snackbar with a given message.  */
    fun showMessage(activity: Activity, message: String) {
        if (!message.isEmpty() && (!isShowing() || lastMessage != message)) {
            lastMessage = message
            show(activity, message, DismissBehavior.HIDE)
        }
    }

    /** Shows a snackbar with a given message, and a dismiss button.  */
    fun showMessageWithDismiss(activity: Activity, message: String) {
        show(activity, message, DismissBehavior.SHOW)
    }

    /**
     * Shows a snackbar with a given error message. When dismissed, will finish the activity. Useful
     * for notifying errors, where no further interaction with the activity is possible.
     */
    fun showError(activity: Activity, errorMessage: String) {
        show(activity, errorMessage, DismissBehavior.FINISH)
    }

    /**
     * Hides the currently showing snackbar, if there is one. Safe to call from any thread. Safe to
     * call even if snackbar is not shown.
     */
    fun hide(activity: Activity) {
        if (!isShowing()) {
            return
        }
        lastMessage = ""
        messageSnackbar = null
        activity.runOnUiThread {
            messageSnackbar?.dismiss()
        }
    }

    fun setMaxLines(lines: Int) {
        maxLines = lines
    }

    private fun show(
        activity: Activity, message: String, dismissBehavior: DismissBehavior
    ) {
        activity.runOnUiThread {
            messageSnackbar = Snackbar.make(
                activity.findViewById(R.id.content),
                message,
                Snackbar.LENGTH_INDEFINITE
            )
            messageSnackbar!!.getView().setBackgroundColor(BACKGROUND_COLOR)
            if (dismissBehavior != DismissBehavior.HIDE) {
                messageSnackbar!!.setAction(
                    "Dismiss",
                    View.OnClickListener { messageSnackbar!!.dismiss() })
                if (dismissBehavior == DismissBehavior.FINISH) {
                    messageSnackbar!!.addCallback(
                        object : BaseTransientBottomBar.BaseCallback<Snackbar?>() {
                            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                super.onDismissed(transientBottomBar, event)
                                activity.finish()
                            }
                        })
                }
            }
            (messageSnackbar!!
                .view
                .findViewById(com.google.android.material.R.id.snackbar_text) as TextView).maxLines =
                maxLines
            messageSnackbar!!.show()
        }
    }
}