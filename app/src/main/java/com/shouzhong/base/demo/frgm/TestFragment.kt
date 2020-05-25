package com.shouzhong.base.demo.frgm

import com.shouzhong.base.demo.R
import com.shouzhong.base.demo.rv.TestBean
import com.shouzhong.base.demo.rv.TestBinder1
import com.shouzhong.base.frgm.BFragment
import com.shouzhong.base.frgm.BViewModel
import com.shouzhong.base.rv.DataList

class TestFragment : BFragment<TestViewModel>(R.layout.frgm_test)

class TestViewModel : BViewModel() {
    val dataList = DataList()

    override fun init() {
        dataList.adapter.run {
            register(TestBean::class, TestBinder1(getLifecycleOwner()))
        }
    }

    override fun onFragmentFirstVisible() {
        for (i in 0..30)  {
            dataList.add(TestBean(dataList.size))
        }
    }
}