package com.shouzhong.base.demo.frgm

import com.shouzhong.base.demo.R
import com.shouzhong.base.demo.rv.TestBinder1
import com.shouzhong.base.frgm.BFragment
import com.shouzhong.base.frgm.BViewModel
import com.shouzhong.base.rv.DataList

class TestFragment : BFragment<TestViewModel>(R.layout.frgm_test)

class TestViewModel : BViewModel() {
    val dataList = DataList().apply {
        adapter.run {
            register(String::class, TestBinder1())
        }
    }

    override fun onFragmentFirstVisible() {
        for (i in 0..30)  {
            dataList.add("${dataList.size}")
        }
    }
}