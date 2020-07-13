package com.shouzhong.base.demo.reflect

import com.blankj.utilcode.util.LogUtils
import kotlin.jvm.internal.Reflection

class Test private constructor(val s: String) {
    companion object {
        private var A: String = "123"

        private fun a() {
            LogUtils.e("a->")
        }

        private fun b(a: Int, b: String): String {
            return "b->$a$b"
        }
    }

    private var i: Int = 666

    private fun aa() {
        LogUtils.e("a->$s")
    }

    private fun bb(a: Int, b: String): String {
        return "bb->$a$b"
    }
}