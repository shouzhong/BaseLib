package com.shouzhong.base.frgm

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.shouzhong.base.util.*
import com.shouzhong.bridge.FragmentStack

abstract class BFragment<T : BViewModel>(val layoutId: Int) : Fragment() {
    var binding: ViewDataBinding? = null
        private set
    private var vm: T? = null
    private var isFirst = false
    private var isFirstVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(getVm())
        vm?.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding != null) return binding!!.root
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding?.javaClass?.getDeclaredMethod("setVm", getGenericClass<T>(0))?.apply {
            isAccessible = true
            invoke(binding, vm)
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (!isFirst) {
            isFirst = true
            if (userVisibleHint && !isHidden) {
                if (!isFirstVisible) {
                    isFirstVisible = true
                    vm?.onFragmentFirstVisible()
                }
                vm?.onFragmentVisibleChange(true)
            }
        }
        super.onViewCreated(view, savedInstanceState)
        vm?.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm?.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        vm?.onStart()
    }

    override fun onResume() {
        super.onResume()
        vm?.onResume()
    }

    override fun onPause() {
        super.onPause()
        vm?.onPause()
    }

    override fun onStop() {
        super.onStop()
        vm?.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vm?.onDestroyView()
        isFirst = false
        isFirstVisible = false
        binding?.unbind()
        binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        vm?.onDestroy()
        vm = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        vm?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        vm?.onConfigurationChanged(newConfig)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        vm?.onSaveInstanceState(outState)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (!isFirst) return
        if (!isFirstVisible && isVisibleToUser) {
            isFirstVisible = true
            vm?.onFragmentFirstVisible()
        }
        vm?.onFragmentVisibleChange(isVisibleToUser)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!isFirst) return
        if (!isFirstVisible && !hidden) {
            isFirstVisible = true
            vm?.onFragmentFirstVisible()
        }
        vm?.onFragmentVisibleChange(!hidden)
    }

    fun getVm(): T {
        if (vm != null) return vm!!
        vm = ViewModelProvider(this).get(getGenericClass<T>(0)!!)
        vm?.uniqueId = FragmentStack.getUniqueId(this)
        if (activity is AppCompatActivity) {
            vm?.initDialog(activity as AppCompatActivity)
            vm?.initPopup(activity as AppCompatActivity)
        }
        vm?.init()
        return vm!!
    }
}