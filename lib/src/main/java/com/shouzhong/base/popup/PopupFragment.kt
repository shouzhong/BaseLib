package com.shouzhong.base.popup

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

open class PopupFragment : Fragment(), PopupWindow.OnDismissListener {
    var isCancelable = true
        set(value) {
            field = value
            popup?.isOutsideTouchable = value
            popup?.isFocusable = value
            if (popup?.isShowing == true) popup?.update()
        }
    var shadowAlpha = 1f
        set(value) {
            field = value
            if (popup?.isShowing == true) {
                activity?.window?.attributes = activity?.window?.attributes?.apply {
                    alpha = field
                }
            }
        }
    var popup: PopupWindow? = null
        internal set

    internal var mViewDestroyed: Boolean = false
    internal var mDismissed: Boolean = false
    internal var mShownByMe: Boolean = false
    internal var mLocation: Boolean = false
    internal var mView: View? = null
    internal var mGravity: Int = 0
    internal var x: Int = 0
    internal var y: Int = 0

    open fun showAsDropDown(manager: FragmentManager, tag: String, anchor: View, gravity: Int, xoff: Int, yoff: Int) {
        this.mDismissed = false
        this.mShownByMe = true
        this.mLocation = false
        this.mView = anchor
        this.mGravity = gravity
        this.x = xoff
        this.y = yoff
        manager.beginTransaction().run {
            add(this@PopupFragment, tag)
            commit()
        }
    }

    open fun showAtLocation(manager: FragmentManager, tag: String, parent: View, gravity: Int, x: Int, y: Int) {
        this.mDismissed = false
        this.mShownByMe = true
        this.mLocation = true
        this.mView = parent
        this.mGravity = gravity
        this.x = x
        this.y = y
        manager.beginTransaction().run {
            add(this@PopupFragment, tag)
            commit()
        }
    }

    open fun dismiss() {
        dismissInternal(allowStateLoss = false, fromOnDismiss = false)
    }

    open fun dismissAllowingStateLoss() {
        dismissInternal(allowStateLoss = true, fromOnDismiss = false)
    }

    internal fun dismissInternal(allowStateLoss: Boolean, fromOnDismiss: Boolean) {
        if (mDismissed) return
        mDismissed = true
        mShownByMe = false
        if (popup != null) {
            popup?.setOnDismissListener(null)
            popup?.dismiss()
            if (!fromOnDismiss) onDismiss()
        }
        mViewDestroyed = true
        requireFragmentManager().beginTransaction().run {
            remove(this@PopupFragment)
            if (allowStateLoss) commitAllowingStateLoss()
            else commit()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (!mShownByMe) mDismissed = false
    }

    override fun onDetach() {
        super.onDetach()
        if (!mShownByMe && !mDismissed) {
            mDismissed = true
        }
    }

    override fun onDismiss() {
        if (!mViewDestroyed) {
            dismissInternal(allowStateLoss = true, fromOnDismiss = true)
        }
        activity?.window?.attributes = activity?.window?.attributes?.apply {
            alpha = 1f
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        check(view != null && view?.parent == null) { "PopupFragment can not be attached to a empty or container view" }
        popup = PopupWindow().apply {
            contentView = view
            isFocusable = isCancelable
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        initPopup(popup)
        popup?.setOnDismissListener(this)
    }

    override fun onStart() {
        super.onStart()
        if (popup != null) {
            mViewDestroyed = false
            when {
                mLocation -> popup?.showAtLocation(mView, mGravity, x, y)
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> popup?.showAsDropDown(mView, x, y, mGravity)
                else -> popup?.showAsDropDown(mView, x, y)
            }
            activity?.window?.attributes = activity?.window?.attributes?.apply {
                alpha = shadowAlpha
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (popup != null) {
            mViewDestroyed = true
            popup?.setOnDismissListener(null)
            popup?.dismiss()
            if (!mDismissed) {
                onDismiss()
            }
            popup = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mView = null
    }

    open fun initPopup(popup: PopupWindow?) = Unit
}