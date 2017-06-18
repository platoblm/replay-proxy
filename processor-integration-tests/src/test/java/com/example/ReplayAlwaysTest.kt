package com.example

import io.deloop.tools.references.replay.ReplayReferenceFactory
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ReplayAlwaysTest {

    @Mock lateinit var first: Example
    @Mock lateinit var second: Example

    val reference = ReplayReferenceFactory.createFor(Example::class.java)

    @Test fun shouldAlwaysReplayMethod() {
        val arg = Any()
        reference.setTarget(first)
        with(reference.get()) {
            doOnceA()
            doAlways(arg)
        }

        reference.setTarget(second)

        with(inOrder(first)) {
            verify(first).doOnceA()
            verify(first).doAlways(arg)
        }
        verify(second).doAlways(arg)
        verify(second, never()).doOnceA()
    }

    @Test fun shouldReplayAlwaysMethodFirst() {
        val arg = Any()
        with(reference.get()) {
            doOnceA()
            doAlways(arg)
        }

        reference.setTarget(first)
        reference.setTarget(second)

        with(inOrder(first)){
            verify(first).doAlways(arg)
            verify(first).doOnceA()
        }
        verify(second).doAlways(arg)
        verify(second, never()).doOnceA()
    }

}