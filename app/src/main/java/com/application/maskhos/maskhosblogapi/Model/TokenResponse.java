package com.application.maskhos.maskhosblogapi.Model;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Matthew on 18/05/2016.
 */
public class TokenResponse implements Serializable {
    private String status;
    private HashMap<String, String>[] data;

    public TokenResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HashMap<String, String>[] getData() {
        return data;
    }

    public void setData(HashMap<String, String>[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TokenResponse{" +
                "status='" + status + '\'' +
                ", json=" + data +
                '}';
    }
}
