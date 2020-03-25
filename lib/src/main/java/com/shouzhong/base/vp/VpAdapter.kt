package com.shouzhong.base.vp

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class VpAdapter(fm: FragmentManager, var fragments: List<Fragment>, var titles: List<CharSequence?>? = null, var isDestroy: Boolean = false)
    : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence? = if (titles?.size ?: 0 < position + 1) null else titles!![position]

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        if (isDestroy) super.destroyItem(container, position, obj)
    }
}