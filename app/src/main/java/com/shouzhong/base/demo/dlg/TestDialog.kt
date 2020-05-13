package com.shouzhong.base.demo.dlg

import android.view.View
import androidx.databinding.ObservableField
import com.shouzhong.base.demo.R
import com.shouzhong.base.dlg.BDialog
import com.shouzhong.base.dlg.BViewModel

class TestDialog : BDialog<TestViewModel>(R.layout.dlg_test)

class TestViewModel : BViewModel<TestDialogBean>() {
    fun onClick(view: View) {
        data?.listener?.get()?.invoke()
    }
}

class TestDialogBean {
    val title:  ObservableField<CharSequence> = ObservableField()
    val content: ObservableField<CharSequence> = ObservableField()
    val listener: ObservableField<() -> Unit> = ObservableField()
}