package com.shouzhong.base.demo.reflect

import com.shouzhong.base.reflect.MethodReflectParams
import com.shouzhong.base.reflect.RefClass
import com.shouzhong.base.reflect.RefStaticMethod

class Test2Reflect {
    companion object {
        val TYPE: Class<*> = RefClass.load(Test2Reflect::class.java, realClass = Test2::class.java)

        lateinit var a: RefStaticMethod<Void>

        @MethodReflectParams(value = ["int", "java.lang.String"])
        lateinit var b: RefStaticMethod<String>
    }
}