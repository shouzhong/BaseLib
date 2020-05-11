package com.shouzhong.base.demo.popup

import android.view.View
import androidx.databinding.ObservableField
import com.shouzhong.base.demo.R
import com.shouzhong.base.popup.BPopup
import com.shouzhong.base.popup.BPopupBean
import com.shouzhong.base.popup.BViewModel

class TestPopup : BPopup<TestViewModel>(R.layout.popup_test)

class TestViewModel : BViewModel<TestPopupBean>() {
    fun onClick(view: View) {
        data?.listener?.get()?.invoke()
    }
}

data class TestPopupBean(
    val title:  ObservableField<CharSequence> = ObservableField(),
    val listener: ObservableField<() -> Unit> = ObservableField()
) : BPopupBean()