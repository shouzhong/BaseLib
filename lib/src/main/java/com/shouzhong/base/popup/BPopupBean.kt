package com.shouzhong.base.popup

import android.view.View

open class BPopupBean {
    var shadowAlpha: Float = 1f
    var showStyle: String = PopupFragment.SHOW_STYLE_DROP_DOWN
    var relatedView: View? = null
    var gravity: Int = 0
    var x: Int = 0
    var y: Int = 0
}


