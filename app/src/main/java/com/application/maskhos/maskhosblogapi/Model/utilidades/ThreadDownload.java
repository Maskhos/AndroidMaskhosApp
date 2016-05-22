package com.application.maskhos.maskhosblogapi.Model.utilidades;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.application.maskhos.maskhosblogapi.Controller.Adapter.AdapterRecyclerViewPost;
import com.application.maskhos.maskhosblogapi.Model.DAO;
import com.application.maskhos.maskhosblogapi.Model.Post;
import com.application.maskhos.maskhosblogapi.Model.TokenResponse;
import com.application.maskhos.maskhosblogapi.Model.UserComment;
import com.application.maskhos.maskhosblogapi.Model.interfaces.httpzoid.Http;
import com.application.maskhos.maskhosblogapi.Model.interfaces.httpzoid.HttpFactory;
import com.application.maskhos.maskhosblogapi.Model.interfaces.httpzoid.HttpResponse;
import com.application.maskhos.maskhosblogapi.Model.interfaces.httpzoid.ResponseHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Matthew on 17/05/2016.
 */
public class ThreadDownload extends AsyncTask<Object, Integer, String> {
    private ProgressDialog progress;
    private TokenResponse usersComments;
    private TokenResponse posts;
    private RecyclerView adapter;
    private Context context;

    private DAO dao;

    public ThreadDownload(Context context) {
        this.context = context;
        if (usersComments == null) {
            usersComments = new TokenResponse();
        }
        if (posts == null) {
            posts = new TokenResponse();
        }
        //progress = new ProgressDialog(context).show(context, "Please wait ...", "Downloading Image ...", true);

        //progress.setCancelable(true);
    }

    private final Class[] classes = new Class[]{
            UserComment.class, Post.class
    };

    private String getUrl(String resource) {
        return "http://10.10.43.149/apiRest/public/" + resource;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // progress.show();
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //   progress.dismiss();
    }

    public TokenResponse getPosts() {
        return posts;
    }

    public TokenResponse getComments() {
        return usersComments;
    }

    @Override
    protected String doInBackground(Object... params) {
        switch ((String) params[0]) {
            case "GET":
                String url = getUrl((String) params[1]);
                doGet(url, (String) params[2], null);
                break;
            case "POST":

                break;
            case "PUT":

                break;
            case "SOS":

                doGet(getUrl("post"), "POST", params[1]);
                doGet(getUrl("comment"), "USERCOMMENT", null);
                break;
        }
        return null;
    }

    private boolean doGet(String url, String type, final Object x) {
        Http http = null;
        this.adapter = (RecyclerView) x;

        final boolean[] complete = {false};
        switch (type) {
            case "USERCOMMENT":
                http = HttpFactory.create(context);
                http.get(url)
                        .handler(new ResponseHandler<TokenResponse>() {
                            @Override
                            public void success(TokenResponse user, HttpResponse response) {
                                usersComments = user;
                                ((RecyclerView) adapter).setAdapter(new AdapterRecyclerViewPost(user.getData()));
                                complete[0] = true;
                            }
                        }).send();
                break;
            case "POST":
                http = HttpFactory.create(context);
                http.get(url)
                        .handler(new ResponseHandler<TokenResponse>() {
                            @Override
                            public void success(TokenResponse post, HttpResponse response) {
                                posts = post;
                                complete[0] = true;
                            }
                        }).send();
                break;


        }
        return complete[0];
    }

}
