package org.example.factory;

public interface ObjectFactory {
    <T> T createObject(Class<T> type);
}
