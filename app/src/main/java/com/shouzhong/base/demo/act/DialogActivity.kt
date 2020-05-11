package com.shouzhong.base.demo.act

import android.view.View
import androidx.databinding.ObservableBoolean
import com.shouzhong.base.act.BActivity
import com.shouzhong.base.act.BViewModel
import com.shouzhong.base.annotation.DialogCancelable
import com.shouzhong.base.annotation.DialogData
import com.shouzhong.base.annotation.DialogSwitch
import com.shouzhong.base.demo.R
import com.shouzhong.base.demo.dlg.TestDialog
import com.shouzhong.base.demo.dlg.TestDialogBean

class DialogActivity : BActivity<DialogViewModel>(R.layout.act_dialog)

class DialogViewModel : BViewModel() {
    @DialogSwitch(TestDialog::class)
    val testDialogSwitch = ObservableBoolean(false)
    @DialogData(TestDialog::class)
    val testDialogData = TestDialogBean()
    @DialogCancelable(TestDialog::class)
    val testDialogCancelable = ObservableBoolean(false)

    fun onClickShow(view: View) {
        testDialogData.run {
            title.set("标题")
            content.set("这是内容，是大概萨嘎搜噶搜噶顺序执行吃v先选择v字形正序")
            listener.set {
                testDialogSwitch.set(false)
            }
        }
        testDialogCancelable.set((0..1).random() == 0)
        testDialogSwitch.set(true)
    }
}