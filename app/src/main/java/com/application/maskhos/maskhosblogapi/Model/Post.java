package com.application.maskhos.maskhosblogapi.Model;

import android.graphics.Bitmap;

/**
 * Created by Matthew on 17/05/2016.
 */
public class Post {
    private String titulo;
    private String descripcion;
    private String comments;
    private Bitmap image;

    public Post(String titulo, String descripcion, String comments, Bitmap image) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.comments = comments;
        this.image = image;
    }

    public Post(String titulo, String descripcion, String comments) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.comments = comments;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
