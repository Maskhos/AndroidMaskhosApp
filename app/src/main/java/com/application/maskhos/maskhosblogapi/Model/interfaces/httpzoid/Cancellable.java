package com.application.maskhos.maskhosblogapi.Model.interfaces.httpzoid;

/**
 * (c) Artur Sharipov
 */
public interface Cancellable {
    public static final Cancellable Empty = new Cancellable() {
        @Override
        public void cancel() {
        }
    };
    public void cancel();
}

