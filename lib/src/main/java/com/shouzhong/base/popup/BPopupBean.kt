package com.shouzhong.base.popup

import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat
import androidx.databinding.ObservableInt
import com.shouzhong.base.util.hashCode

open class BPopupBean {
    val tag: ObservableField<String> = ObservableField("${hashCode(this)}")
    val shadowAlpha: ObservableFloat = ObservableFloat(1f)
    val showStyle: ObservableField<String> = ObservableField(PopupFragment.SHOW_STYLE_DROP_DOWN)
    val relatedView: ObservableField<View> = ObservableField()
    val gravity: ObservableInt = ObservableInt(0)
    val x: ObservableInt = ObservableInt(0)
    val y: ObservableInt = ObservableInt(0)
}


