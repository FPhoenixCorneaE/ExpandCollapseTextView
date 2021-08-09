package com.fphoenixcorneae.widget

import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.view.MotionEvent
import android.widget.TextView

/**
 * @desc：解决 ClickableSpan 空白区域也能点击的问题
 * @date：2021/08/09 15:22
 */
class ClickMovementMethod private constructor() : LinkMovementMethod() {
    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        val action = event.action
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            var x = event.x.toInt()
            var y = event.y.toInt()
            x -= widget.totalPaddingLeft
            y -= widget.totalPaddingTop
            x += widget.scrollX
            y += widget.scrollY
            val layout = widget.layout
            val line = layout.getLineForVertical(y)
            val off = layout.getOffsetForHorizontal(line, x.toFloat())
            if (off >= widget.text.length) {
                // Return true so click won't be triggered in the leftover empty space
                return true
            }
        }
        return super.onTouchEvent(widget, buffer, event)
    }

    companion object {
        fun getInstance(): ClickMovementMethod {
            if (sInstance == null) {
                sInstance = ClickMovementMethod()
            }
            return sInstance!!
        }

        private var sInstance: ClickMovementMethod? = null
    }
}