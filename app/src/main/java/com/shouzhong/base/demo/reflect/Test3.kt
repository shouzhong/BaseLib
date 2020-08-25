package com.shouzhong.base.demo.reflect

fun main() {
    a {
        s1 = "666"
        s2 = "777"
        success {

        }
        error {

        }
    }
}

class A {
    var s1: String? = null
    var s2: String? = null
    internal var success: ((s: String) -> Unit)? = null
    internal var error: ((s: String?) -> Unit)? = null

    fun success(block: (s: String) -> Unit) {
        this.success = block
    }

    fun error(block: (s: String?) -> Unit) {
        this.error = block
    }
}

fun a(init: A.() -> Unit) {
    val a = A()
    a.apply(init)
    if (a.s1 == null || a.s2 == null) a.error?.invoke("null")
    else a.success?.invoke("${a.s1}${a.s2}")
}