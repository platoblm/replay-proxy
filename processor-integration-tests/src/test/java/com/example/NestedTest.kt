package com.example

import com.example.NestedExample.NestedInterface
import io.deloop.tools.references.replay.Reference
import io.deloop.tools.references.replay.ReplayReferenceFactory
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

    lateinit var reference: Reference<NestedInterface>

    @Before fun setup() {
        reference = ReplayReferenceFactory.createFor(NestedInterface::class.java)
    }

    @Test fun shouldSupportNestedInterfaces() {
        with(reference) {
            get().doOnce()

            setTarget(first)
            setTarget(second)
        }

        verify(first).doOnce()
        verify(second, never()).doOnce()
    }
}
