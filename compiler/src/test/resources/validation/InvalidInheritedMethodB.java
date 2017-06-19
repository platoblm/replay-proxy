package mypackage;

import io.deloop.tools.proxy.HasReplayProxy;

@HasReplayProxy
interface Giraffe extends mypackage.BaseAnimal {

    void validMethod();
}