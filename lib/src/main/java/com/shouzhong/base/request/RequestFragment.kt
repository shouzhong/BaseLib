package com.shouzhong.base.request

import android.content.Intent
import android.util.SparseArray
import androidx.fragment.app.Fragment

class RequestFragment : Fragment() {
    companion object {
        val TAG = RequestFragment::class.java.name
    }

    private val cache = SparseArray<(resultCode: Int, data: Intent?) -> Unit>()

    internal fun put(uniqueId: Int, intent: Intent, callback: (resultCode: Int, data: Intent?) -> Unit) {
        val requestCode = uniqueId and 0xffff
        cache.put(requestCode, callback)
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        cache[requestCode]?.invoke(resultCode, data)
        cache.remove(requestCode)
    }
}