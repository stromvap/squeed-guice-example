package com.squeed.guice.example.thread.scope;

import com.google.inject.Scope;

public class CustomScopes {
    /**
     * Thread scope, backed by a {@link java.lang.ThreadLocal}. Example usage:
     * 
     * <pre>
     * Injector i = Guice.createInjector(new Module() {
     *     public void configure(Binder binder) {
     *         binder.bindScope(ThreadScoped.class, CustomScopes.THREAD);
     *         binder.bind(ThreadCache.class).in(Scopes.SINGLETON);
     *         binder.bind(SomeClass.class).in(CustomScopes.THREAD);
     *     }
     * });
     * </pre>
     * 
     * In thread pooling scenario's, never forget to reset the scope at the end of a request:
     * 
     * <pre>
     * i.getInstance(ThreadCache.class).reset();
     * </pre>
     * 
     * Note that if you create new threads within this scope, they will start with a clean slate.
     */
    public static final Scope THREAD = new ThreadScope(new ThreadCache());
}
