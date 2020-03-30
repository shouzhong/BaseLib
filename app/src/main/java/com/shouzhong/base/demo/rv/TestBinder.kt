package com.shouzhong.base.demo.rv

import android.view.Gravity
import android.view.View
import androidx.databinding.ObservableBoolean
import com.shouzhong.base.annotation.*
import com.shouzhong.base.demo.R
import com.shouzhong.base.demo.dlg.TestDialog
import com.shouzhong.base.demo.dlg.TestDialogBean
import com.shouzhong.base.demo.dlg.TestDialogListener
import com.shouzhong.base.demo.popup.TestPopup
import com.shouzhong.base.demo.popup.TestPopupBean
import com.shouzhong.base.demo.popup.TestPopupListener
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
    @PopupSwitch(TestPopup::class)
    val testPopupSwitch = ObservableBoolean(false)
    @PopupData(TestPopup::class)
    val testPopupData = TestPopupBean()
    @PopupCancelable(TestPopup::class)
    val testPopupCancelable = ObservableBoolean(false)

    fun onClickDialog(v: View) {
        testDialogData.run {
            title.set("第${data.get()}项Dialog")
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

    fun onClickPopup(v: View) {
        testPopupData.run {
            showAsDropDown.set(adapterPosition % 2 == 0)
            shadowAlpha.set(if (adapterPosition % 2 == 0) 0.5f else 1f)
            relatedView.set(itemView.findViewById(R.id.tv))
            gravity.set(Gravity.CENTER)
            title.set("第${data.get()}项Popup")
            content.set("这是第${data.get()}项内容，是大概萨嘎搜噶搜噶顺序执行吃v先选择v字形正序")
            listener.set(object : TestPopupListener {
                override fun onClick() {
                    testPopupSwitch.set(false)
                }
            })
        }
        testPopupCancelable.set(adapterPosition % 2 == 0)
        testPopupSwitch.set(true)
    }
}