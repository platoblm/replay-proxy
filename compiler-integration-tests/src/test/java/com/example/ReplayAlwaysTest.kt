package com.example

import io.deloop.tools.proxy.ReplayProxyFactory
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ReplayAlwaysTest {

    @Mock lateinit var first: Example
    @Mock lateinit var second: Example

    val proxy = ReplayProxyFactory.createFor(Example::class.java)

    @Test fun shouldAlwaysReplayMethod() {
        val arg = Any()
        proxy.setTarget(first)
        with(proxy.get()) {
            doOnceA()
            doAlways(arg)
        }

        proxy.setTarget(second)

        with(inOrder(first)) {
            verify(first).doOnceA()
            verify(first).doAlways(arg)
        }
        verify(second).doAlways(arg)
        verify(second, never()).doOnceA()
    }

    @Test fun shouldReplayAlwaysMethodFirst() {
        val arg = Any()
        with(proxy.get()) {
            doOnceA()
            doAlways(arg)
        }

        proxy.setTarget(first)
        proxy.setTarget(second)

        with(inOrder(first)){
            verify(first).doAlways(arg)
            verify(first).doOnceA()
        }
        verify(second).doAlways(arg)
        verify(second, never()).doOnceA()
    }
}