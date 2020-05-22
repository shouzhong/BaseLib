package com.shouzhong.base.demo.dlg

import android.view.View
import com.shouzhong.base.demo.R
import com.shouzhong.base.dlg.BDialog
import com.shouzhong.base.dlg.BViewModel

class TestDialog : BDialog<TestViewModel>(R.layout.dlg_test)

class TestViewModel : BViewModel<TestDialogBean>() {
    fun onClick(view: View) {
        data?.listener?.invoke()
    }
}

class TestDialogBean {
    var title: CharSequence? = null
    var content: CharSequence? = null
    var listener: (() -> Unit)? = null
}