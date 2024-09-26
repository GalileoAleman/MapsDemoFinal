package com.technicaltest.mapsdemo.common.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.DialogFragment
import com.technicaltest.mapsdemo.R
import com.technicaltest.mapsdemo.databinding.DialogInfoBinding

class InfoDialog : DialogFragment() {

    private var title: String = ""
    private var description: String = ""
    private var isDialogCancelable: Boolean = true
    private var positiveAction: Action = Action.Empty
    private var negativeAction: Action = Action.Empty

    companion object {
        fun create(
            title: String = "",
            description: String = "",
            isDialogCancelable: Boolean = true,
            positiveAction: Action = Action.Empty,
            negativeAction: Action = Action.Empty,
        ): InfoDialog = InfoDialog().apply {
            this.title = title
            this.description = description
            this.isDialogCancelable = isDialogCancelable
            this.positiveAction = positiveAction
            this.negativeAction = negativeAction
        }
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window ?: return
        window.setLayout(
            resources.getDimensionPixelOffset(R.dimen.dialog_width),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogInfoBinding.inflate(requireActivity().layoutInflater)

        binding.tvTitle.text = title
        binding.tvDescription.text = description

        if (negativeAction == Action.Empty) {
            binding.btnNegative.isGone = true
        } else {
            binding.btnNegative.text = negativeAction.text
            binding.btnNegative.setOnClickListener { negativeAction.onClickListener(this) }
        }

        binding.btnPositive.text = positiveAction.text
        binding.btnPositive.setOnClickListener { positiveAction.onClickListener(this) }

        isCancelable = isDialogCancelable

        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .setCancelable(isDialogCancelable)
            .create()
    }

    data class Action(
        val text: String,
        val onClickListener: (InfoDialog) -> Unit
    ) {
        companion object {
            val Empty = Action("") {}
        }
    }
}
