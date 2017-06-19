package mypackage;

import io.deloop.tools.proxy.CreateReplayProxy;

@CreateReplayProxy
interface Giraffe extends mypackage.BaseAnimal {

    void validMethod();
}