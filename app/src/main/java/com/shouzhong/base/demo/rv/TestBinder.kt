package com.shouzhong.base.demo.rv

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.shouzhong.base.demo.R
import com.shouzhong.base.rv.BBinder
import com.shouzhong.base.rv.BHolder
import com.shouzhong.base.rv.DataList

class TestBinder1(lifecycleOwner: LifecycleOwner) : BBinder<TestBean, TestHolder1>(lifecycleOwner, R.layout.rv_test_1)

class TestHolder1(itemView: View) : BHolder<TestBean>(itemView)