package com.application.maskhos.maskhosblogapi.Model.interfaces.httpzoid.serializers;

/**
 * (c) Artur Sharipov
 */
public interface HttpSerializer {
    public String getContentType();
    public String serialize(Object object);
    public Object deserialize(String value, Class type);
}
