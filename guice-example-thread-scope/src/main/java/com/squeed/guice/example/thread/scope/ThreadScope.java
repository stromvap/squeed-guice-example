package com.squeed.guice.example.thread.scope;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

public class ThreadScope implements Scope {
    private static ThreadCache thread;

    ThreadScope(ThreadCache thread) {
        ThreadScope.thread = thread;
    }

    /**
     * @see com.google.inject.Scope#scope(com.google.inject.Key, com.google.inject.Provider)
     */
    public <T> Provider<T> scope(final Key<T> key, final Provider<T> creator) {
        return new Provider<T>() {
            public T get() {
                ThreadCache.Cache cache = ThreadScope.thread.getCache();
                T value = cache.get(key);
                if (value == null) {
                    value = creator.get();
                    cache.add(key, value);
                }
                return value;
            }
        };
    }

    public static ThreadCache getThread() {
        return thread;
    }
}
