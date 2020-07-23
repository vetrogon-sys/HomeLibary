package org.example.factory.Impl;

import lombok.SneakyThrows;
import org.example.factory.ObjectFactory;

public class ObjectFactoryImpl implements ObjectFactory {

    @SneakyThrows
    @Override
    public <T> T createObject(Class<T> type) {
        return type.getDeclaredConstructor().newInstance();
    }
}
