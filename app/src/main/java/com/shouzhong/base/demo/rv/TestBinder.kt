package com.shouzhong.base.demo.rv

import android.view.View
import androidx.databinding.ObservableBoolean
import com.shouzhong.base.annotation.DialogCancelable
import com.shouzhong.base.annotation.DialogData
import com.shouzhong.base.annotation.DialogSwitch
import com.shouzhong.base.demo.R
import com.shouzhong.base.demo.dlg.TestDialog
import com.shouzhong.base.demo.dlg.TestDialogBean
import com.shouzhong.base.demo.dlg.TestDialogListener
import com.shouzhong.base.rv.BBinder
import com.shouzhong.base.rv.BHolder
import com.shouzhong.base.rv.DataList

class TestBinder1 : BBinder<String, TestHolder1>(R.layout.rv_test_1)

class TestHolder1(itemView: View, dataList: DataList) : BHolder<String>(itemView, dataList) {
    @DialogSwitch(TestDialog::class)
    val testDialogSwitch = ObservableBoolean(false)
    @DialogData(TestDialog::class)
    val testDialogData = TestDialogBean()
    @DialogCancelable(TestDialog::class)
    val testDialogCancelable = ObservableBoolean(false)

    fun onClick(view: View) {
        testDialogData.run {
            title.set("第${data.get()}项")
            content.set("这是第${data.get()}项内容，是大概萨嘎搜噶搜噶顺序执行吃v先选择v字形正序")
            listener.set(object : TestDialogListener {
                override fun onClick() {
                    testDialogSwitch.set(false)
                }
            })
        }
        testDialogCancelable.set(adapterPosition % 2 == 0)
        testDialogSwitch.set(true)
    }
}