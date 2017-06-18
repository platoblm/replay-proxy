package io.deloop.tools.reference.replay.helpers;

import static java.util.UUID.randomUUID;

public interface MethodIdGenerator {

    String uniqueId(String methodName);

    MethodIdGenerator REAL = new MethodIdGenerator() {
        @Override
        public String uniqueId(String methodName) {
            return methodName + "-" + randomUUID().toString();
        }
    };
}
