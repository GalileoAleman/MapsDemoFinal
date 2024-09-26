package com.technicaltest.mapsdemo.common.dialog

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.technicaltest.mapsdemo.common.delegate.weak
import javax.inject.Inject

class DialogFragmentLauncher @Inject constructor() : DefaultLifecycleObserver {

    private var activity: FragmentActivity? by weak()
    private var dialogFragment: DialogFragment? by weak()

    fun show(dialogFragment: DialogFragment, activity: FragmentActivity) {
        if (activity.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            dialogFragment.show(activity.supportFragmentManager, null)
        } else {
            this.activity = activity
            this.dialogFragment = dialogFragment
            activity.lifecycle.addObserver(this)
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        activity?.let { act ->
            dialogFragment?.let { dialog ->
                dialog.show(act.supportFragmentManager, null)
                cleanUp()
            }
        }
    }

    private fun cleanUp() {
        activity?.lifecycle?.removeObserver(this)
        activity = null
        dialogFragment = null
    }
}
