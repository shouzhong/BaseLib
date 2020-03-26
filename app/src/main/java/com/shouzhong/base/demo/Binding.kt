package com.shouzhong.base.demo

import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import com.shouzhong.tablayout.TabLayout

@BindingAdapter("tlSetupWithViewPager")
fun TabLayout.setViewPager(vp: ViewPager) {
    setupWithViewPager(vp)
}