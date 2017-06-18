package io.deloop.tools.replayreference;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AnimalTest {

    @Test public void shouldMakeNoise() {
        assertThat(new Animal().makeNoise()).isEqualTo("Woof");
    }
}