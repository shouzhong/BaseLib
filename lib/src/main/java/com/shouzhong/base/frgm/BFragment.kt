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
import com.shouzhong.base.util.*

abstract class BFragment<T : BViewModel>(val layoutId: Int) : Fragment() {
    var binding: ViewDataBinding? = null
        private set
    private var vm: T? = null
    private var isFirst = false
    private var isFirstVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getVm().onCreated(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding != null) return binding!!.root
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        val cls: Class<T> = getGenericClass(0)
        binding?.javaClass?.getDeclaredMethod("setVm", cls)?.apply {
            isAccessible = true
            invoke(binding, getVm())
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (!isFirst) {
            isFirst = true
            if (userVisibleHint) {
                if (!isFirstVisible) {
                    isFirstVisible = true
                    getVm().onFragmentFirstVisible()
                }
                getVm().onFragmentVisibleChange(true)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getVm().onActivityCreated(savedInstanceState)
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
            getVm().onFragmentFirstVisible()
        }
        vm?.onFragmentVisibleChange(isVisibleToUser)
    }

    fun getVm(): T {
        if (vm != null) return vm!!
        val cls: Class<T> = getGenericClass(0)
        vm = cls.newInstance().apply {
            frgm = this@BFragment
        }
        if (activity is AppCompatActivity) {
            vm?.initDialog(activity as AppCompatActivity)
            vm?.initPopup(activity as AppCompatActivity)
        }
        vm?.init()
        return vm!!
    }
}