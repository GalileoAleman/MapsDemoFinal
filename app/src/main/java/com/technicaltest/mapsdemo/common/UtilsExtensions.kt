package com.technicaltest.mapsdemo.common

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast

const val MIN_PASSWORD_LENGTH = 5
const val MIN_USER_LENGTH = 4

fun EditText.onTextChanged(listener: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            listener(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun EditText.loseFocusAfterAction(action: Int) {
    this.setOnEditorActionListener { v, actionId, _ ->
        if (actionId == action) {
            this.dismissKeyboard()
            v.clearFocus()
        }
        return@setOnEditorActionListener false
    }
}

fun View.dismissKeyboard(completed: () -> Unit = {}) {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val wasOpened = inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    if (!wasOpened) completed()
}
var View.visible : Boolean
    get() = visibility == View.VISIBLE
    set(value){
        visibility = if(value) View.VISIBLE else View.GONE
    }

fun View.showKeyboard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun String.isValidUser(): Boolean =
    this.length >= MIN_USER_LENGTH && !this.contains(" ")


fun String.isValidPassword(): Boolean =
    this.length >= MIN_PASSWORD_LENGTH || this.isNotEmpty()

fun Context.toast(message : String?, duration : Int = Toast.LENGTH_SHORT){
    if(message == null)
        return
    Toast.makeText(this, message, duration).show()
}
