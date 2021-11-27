package com.kuzmin.shoppinglist.utils

import android.view.MotionEvent
import android.view.View

class TouchListener: View.OnTouchListener {
    var xDelta = 0.0f
    var yDelta = 0.0f

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action){
            MotionEvent.ACTION_DOWN->{
                if (v != null) {
                    xDelta = v.x - event.rawX
                    yDelta = v.y - event.rawY
                }
            }
            MotionEvent.ACTION_MOVE->{
                if (v != null) {
                    v.x = xDelta + event.rawX
                    v.y = yDelta + event.rawY
                }
            }
        }
        return true
    }
}