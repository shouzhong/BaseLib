package com.shouzhong.base.demo.rv

import android.view.View
import com.shouzhong.base.demo.R
import com.shouzhong.base.rv.BBinder
import com.shouzhong.base.rv.BHolder
import com.shouzhong.base.rv.DataList

class TestBinder1 : BBinder<String, TestHolder1>(R.layout.rv_test_1)

class TestHolder1(itemView: View, dataList: DataList) : BHolder<String>(itemView, dataList)