package com.shouzhong.base.demo.act

import androidx.fragment.app.Fragment
import com.shouzhong.base.act.BActivity
import com.shouzhong.base.act.BViewModel
import com.shouzhong.base.demo.R
import com.shouzhong.base.demo.frgm.TestFragment
import com.shouzhong.base.vp.VpAdapter

class MainActivity : BActivity<MainViewModel>(R.layout.act_main)

class MainViewModel : BViewModel() {
    lateinit var vpAdapter: VpAdapter

    override fun init() {
        val fragments = ArrayList<Fragment>()
        val titles = ArrayList<CharSequence>()
        (0 until 4).forEach {
            titles.add("标题$it")
            fragments.add(TestFragment())
        }
        getActivity<MainActivity>()?.let {
            vpAdapter = VpAdapter(it.supportFragmentManager, fragments, titles)
        }
    }
}
