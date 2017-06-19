package io.deloop.tools.proxy.helpers

import java.util.UUID.randomUUID

interface MethodIdGenerator {

    fun uniqueId(methodName: String): String

    companion object {
        val real: MethodIdGenerator = object : MethodIdGenerator {
            override fun uniqueId(methodName: String) = methodName + "-" + randomUUID().toString()
        }
    }
}
