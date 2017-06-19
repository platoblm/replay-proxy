package mypackage;

import io.deloop.tools.references.replay.ReplayReference;

@ReplayReference
interface Giraffe extends mypackage.BaseAnimal {

    void validMethod();
}