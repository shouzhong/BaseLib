package com.shouzhong.base.demo.act

import com.shouzhong.base.act.BActivity
import com.shouzhong.base.act.BViewModel
import com.shouzhong.base.demo.R

class FragmentActivity  : BActivity<FragmentViewModel>(R.layout.act_fragment)

class FragmentViewModel : BViewModel()