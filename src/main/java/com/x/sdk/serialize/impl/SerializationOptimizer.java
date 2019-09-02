package com.x.sdk.serialize.impl;

import java.util.Collection;

public interface SerializationOptimizer {

    @SuppressWarnings("rawtypes")
    Collection<Class> getSerializableClasses();
}
