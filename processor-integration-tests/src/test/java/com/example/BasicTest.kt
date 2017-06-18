package com.example

import io.deloop.tools.references.replay.ReplayReferenceFactory
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BasicTest {

    @Mock lateinit var target: Example
    @Mock lateinit var first: Example
    @Mock lateinit var second: Example

    val reference = ReplayReferenceFactory.createFor(Example::class.java)

    @Test fun shouldForwardCallsWhenTargetPresent() {
        val argOne = Any()
        val argTwo = Any()
        reference.setTarget(target) // target set first

        with(reference.get()) {
            doOnceA()
            doOnceB(argOne)
            doOnceB(argTwo)
        }

        with(inOrder(target)) {
            verify(target).doOnceA()
            verify(target).doOnceB(argOne)
            verify(target).doOnceB(argTwo)
        }
    }

    @Test fun shouldReplayCallsWhenTargetSet() {
        val argOne = Any()
        val argTwo = Any()
        with(reference.get()) {
            doOnceA()
            doOnceB(argOne)
            doOnceB(argTwo)
        }

        reference.setTarget(target) // target set afterwards

        with(inOrder(target)) {
            verify(target).doOnceA()
            verify(target).doOnceB(argTwo)
        }
        verify(target, never()).doOnceB(argOne)
    }


    @Test fun shouldReplaySimpleMethodsOnce() {
        with(reference.get()) {
            doOnceA()
            doOnceB(Any())
        }

        with(reference) {
            setTarget(first)
            setTarget(second)
        }

        verify(second, never()).doOnceA()
        verify(second, never()).doOnceB(any())
    }

    @Test fun shouldUpdateInvocationOrderWhenCalledMultipleTimes() {
        val arg = Any()
        with(reference.get()) {
            doOnceA() // first call
            doOnceB(arg)
            doOnceA() // second call
        }

        reference.setTarget(target)

        with(inOrder(target)) {
            verify(target).doOnceB(arg)
            verify(target).doOnceA()
        }
    }

    @Test fun shouldTreadOverloadedMethodsAsSeparate() {
        val argOne = Any()
        val argTwo = Any()
        val argThree = 0
        with(reference.get()) {
            doOnceB(argOne)
            doOnceB(argTwo, argThree)
        }

        reference.setTarget(target)

        with(inOrder(target)) {
            verify(target).doOnceB(argOne)
            verify(target).doOnceB(argTwo, argThree)
        }
    }
}