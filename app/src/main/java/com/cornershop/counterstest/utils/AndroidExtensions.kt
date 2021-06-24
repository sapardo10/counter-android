package com.cornershop.counterstest.utils

import android.app.Activity
import android.app.AlertDialog
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.R
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Method that replaces the [View.setOnClickListener] adding a safe check to avoid fast double
 * clicking on the view.
 * This code was extracted from
 * @see <a href="https://medium.com/@masaaki.iwaguchi/android-how-to-prevent-multiple-view-clicks-using-databinding-and-kotlin-extensions-5d5859071b4b">medium link</a>
 */
fun View.setOnSingleClickListener(clickListener: ((View) -> Unit)?) {
    clickListener?.also {
        setOnClickListener(OnSingleClickListener(it))
    } ?: setOnClickListener(null)
}

/**
 * Class created to complete the [View.setOnSingleClickListener] functionality. It will hold
 * the value of the last time a view was clicked and if its less than [intervalMs] it will ignore
 * such action. If it's greater it will execute [clickListener]
 */
class OnSingleClickListener(
    private val clickListener: (View) -> Unit,
    private val intervalMs: Long = 1000
) : View.OnClickListener {

    //Atomic boolean used in case the operations happens on different threads.
    private var canClick = AtomicBoolean(true)

    override fun onClick(v: View?) {
        if (canClick.getAndSet(false)) {
            v?.run {
                postDelayed({
                    canClick.set(true)
                }, intervalMs)
                clickListener.invoke(v)
            }
        }
    }
}

fun <T> RecyclerView.Adapter<*>.autoNotify(
    oldList: List<T>,
    newList: List<T>,
    compare: (T, T) -> Boolean
) {
    val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return compare(oldList[oldItemPosition], newList[newItemPosition])
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size
    })

    diff.dispatchUpdatesTo(this)
}

/**
 * Extension that allows setting links inside a text view even multiple times
 * @param links [Pair] of [String] and [View.OnClickListener] that will set the text of the string
 * as clickable if found and will perform the click listener action when the user clicks on it
 * Code extracted from: https://stackoverflow.com/questions/10696986/how-to-set-the-part-of-the-text-view-is-clickable
 */
fun TextView.insertLinks(vararg links: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    var startIndexOfLink = -1
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {
            override fun updateDrawState(textPaint: TextPaint) {
                textPaint.isUnderlineText = true
            }

            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                link.second.onClick(view)
            }
        }
        startIndexOfLink = this.text.toString().indexOf(link.first, startIndexOfLink + 1)
        if (startIndexOfLink == -1) continue
        spannableString.setSpan(
            clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod = LinkMovementMethod.getInstance()
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

fun Activity.buildDialog(
    message: String = "",
    negativeButtonText: String = "",
    onPositiveClicked: (() -> Unit)? = null,
    positiveButtonText: String = "",
    title: String = ""
): AlertDialog {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.AlertDialogCustom)

    if (message.isNotBlank()) {
        builder.setMessage(message)
    }

    if (title.isNotBlank()) {
        builder.setTitle(title)
    }

    if (positiveButtonText.isNotBlank() && onPositiveClicked != null) {
        builder.setPositiveButton(positiveButtonText) { _, _ ->
            onPositiveClicked()
        }
    }

    if (negativeButtonText.isNotBlank()) {
        builder.setNegativeButton(negativeButtonText) { dialog, _ ->
            dialog.dismiss()
        }
    }
    return builder.create()
}