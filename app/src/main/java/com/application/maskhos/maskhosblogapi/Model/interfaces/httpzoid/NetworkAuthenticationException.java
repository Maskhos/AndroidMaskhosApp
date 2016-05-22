package com.application.maskhos.maskhosblogapi.Model.interfaces.httpzoid;

/**
 * (c) Artur Sharipov
 */
public class NetworkAuthenticationException extends HttpzoidException {
    public NetworkAuthenticationException() {
        super("Network authentication required", NetworkError.AuthenticationRequired);
    }
}
