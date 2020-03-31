package com.shouzhong.base.popup

import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat
import androidx.databinding.ObservableInt

open class BPopupBean(
    val tag: ObservableField<String> = ObservableField("${System.currentTimeMillis()}${(0..100000000).random()}"),
    val shadowAlpha: ObservableFloat = ObservableFloat(1f),
    val showStyle: ObservableField<String> = ObservableField(PopupFragment.SHOW_STYLE_DROP_DOWN),
    val relatedView: ObservableField<View> = ObservableField(),
    val gravity: ObservableInt = ObservableInt(0),
    val x: ObservableInt = ObservableInt(0),
    val y: ObservableInt = ObservableInt(0)
)


