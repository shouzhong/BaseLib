package com.shouzhong.base.demo.reflect

import com.shouzhong.base.reflect.*

class TestReflect {
    companion object {
        val TYPE: Class<*> = RefClass.load(TestReflect::class.java, realClass = Test::class.java)

        lateinit var A: RefStaticObject<String>

        lateinit var a: RefCompanionMethod<Void>

        @MethodReflectParams(value = ["int", "java.lang.String"])
        lateinit var b: RefStaticMethod<String>

        lateinit var i: RefInt

        lateinit var aa: RefMethod<Void>

        @MethodParams(value = [Int::class, String::class])
        lateinit var bb: RefMethod<String>

        @MethodReflectParams(value = ["java.lang.String"])
        lateinit var newInstance: RefConstructor<Test>
    }
}