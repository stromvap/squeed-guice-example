package com.squeed.guice.example.thread.scope;

import com.google.inject.Key;

import java.util.HashMap;
import java.util.Map;

public class ThreadCache {
    // use lazy init to avoid memory overhead when not using the scope?
    private static final ThreadLocal<Cache> THREAD_LOCAL = new ThreadLocal<Cache>() {
        @Override
        protected Cache initialValue() {
            return new Cache();
        }
    };

    public ThreadCache() {
    }

    public Cache getCache() {
        return THREAD_LOCAL.get();
    }

    /**
     * Execute this if you plan to reuse the same thread, e.g. in a servlet environment threads might get reused.
     * Preferably, call this method in a finally block to make sure that it executes, so that you avoid possible memory
     * leaks.
     */
    public void reset() {
        THREAD_LOCAL.remove();
    }

    /**
     * Cache class for type capture and minimizing ThreadLocal lookups.
     */
    public static class Cache {
        private Map<Key<?>, Object> map = new HashMap<Key<?>, Object>();

        public Cache() {
        }

        // suppress warnings because the add method
        // captures the type
        @SuppressWarnings("unchecked")
        public <T> T get(Key<T> key) {
            return (T) map.get(key);
        }

        public <T> void add(Key<T> key, T value) {
            map.put(key, value);
        }
    }
}
