package org.example.context;

import lombok.NoArgsConstructor;
import org.example.factory.Impl.ObjectFactoryImpl;
import org.example.factory.ObjectFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor
public class ApplicationContext {
    private static volatile ApplicationContext context;
    private ObjectFactory factory = new ObjectFactoryImpl();
    private Map<Class, Object> cache = new ConcurrentHashMap<>();

    public <T> T getObject(Class<T> type) {
        if (cache.containsKey(type)) {
            return (T) cache.get(type);
        }
        T t = factory.createObject(type);
        cache.put(type, t);
        return t;
    }

    public static ApplicationContext getInstance() {
        ApplicationContext result = context;
        if (result == null) {
            context = result = new ApplicationContext();
        }
        return result;
    }
}
