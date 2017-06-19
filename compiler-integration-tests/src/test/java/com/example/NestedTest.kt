package com.example

import com.example.NestedExample.NestedInterface
import io.deloop.tools.proxy.ReplayProxy
import io.deloop.tools.proxy.ReplayProxyFactory
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NestedTest {

    @Mock lateinit var first: NestedInterface
    @Mock lateinit var second: NestedInterface

    lateinit var proxy: ReplayProxy<NestedInterface>

    @Before fun setup() {
        proxy = ReplayProxyFactory.createFor(NestedInterface::class.java)
    }

    @Test fun shouldSupportNestedInterfaces() {
        with(proxy) {
            get().doOnce()

            setTarget(first)
            setTarget(second)
        }

        verify(first).doOnce()
        verify(second, never()).doOnce()
    }
}
