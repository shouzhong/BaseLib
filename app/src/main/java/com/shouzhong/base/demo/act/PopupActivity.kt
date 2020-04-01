package com.shouzhong.base.demo.act

import android.view.Gravity
import android.view.View
import androidx.databinding.ObservableBoolean
import com.shouzhong.base.act.BActivity
import com.shouzhong.base.act.BViewModel
import com.shouzhong.base.annotation.PopupCancelable
import com.shouzhong.base.annotation.PopupData
import com.shouzhong.base.annotation.PopupSwitch
import com.shouzhong.base.demo.R
import com.shouzhong.base.demo.popup.TestPopup
import com.shouzhong.base.demo.popup.TestPopupBean
import com.shouzhong.base.demo.popup.TestPopupListener
import com.shouzhong.base.popup.PopupFragment

class PopupActivity : BActivity<PopupViewModel>(R.layout.act_popup)

class PopupViewModel : BViewModel() {
    @PopupSwitch(TestPopup::class)
    val testPopupSwitch = ObservableBoolean(false)
    @PopupData(TestPopup::class)
    val testPopupData = TestPopupBean()
    @PopupCancelable(TestPopup::class)
    val testPopupCancelable = ObservableBoolean(false)

    fun onClickLeft(view: View) {
        show(view, PopupFragment.SHOW_STYLE_LEFT)
    }

    fun onClickRight(view: View) {
        show(view, PopupFragment.SHOW_STYLE_RIGHT)
    }

    fun onClickTop(view: View) {
        show(view, PopupFragment.SHOW_STYLE_TOP)
    }

    fun onClickBottom(view: View) {
        show(view, PopupFragment.SHOW_STYLE_BOTTOM)
    }

    fun onClickDropDown(view: View) {
        show(view, PopupFragment.SHOW_STYLE_DROP_DOWN)
    }

    fun onClickLocation(view: View) {
        show(view, PopupFragment.SHOW_STYLE_LOCATION)
    }

    private fun show(view: View, style: String) {
        testPopupData.run {
            showStyle.set(style)
            shadowAlpha.set(if ((0..1).random() == 0) 0.5f else 1f)
            relatedView.set(view)
            gravity.set(when(style) {
                in arrayOf(PopupFragment.SHOW_STYLE_LEFT, PopupFragment.SHOW_STYLE_RIGHT) -> when((0..4).random()){
                    0 -> Gravity.START
                    1 -> Gravity.TOP
                    2 -> Gravity.CENTER
                    3 -> Gravity.BOTTOM
                    else -> Gravity.END
                }
                in arrayOf(PopupFragment.SHOW_STYLE_TOP, PopupFragment.SHOW_STYLE_BOTTOM) -> when((0..4).random()) {
                    0 -> Gravity.START
                    1 -> Gravity.LEFT
                    2 -> Gravity.CENTER
                    3 -> Gravity.RIGHT
                    else -> Gravity.END
                }
                PopupFragment.SHOW_STYLE_LOCATION -> Gravity.CENTER
                else -> Gravity.NO_GRAVITY
            })
            title.set("Popup")
            listener.set(object : TestPopupListener {
                override fun onClick() {
                    testPopupSwitch.set(false)
                }
            })
        }
        testPopupCancelable.set(true)
        testPopupSwitch.set(true)
    }
}