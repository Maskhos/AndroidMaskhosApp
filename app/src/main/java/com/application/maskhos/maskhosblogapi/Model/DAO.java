package com.application.maskhos.maskhosblogapi.Model;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.application.maskhos.maskhosblogapi.Model.utilidades.ThreadDownload;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by Matthew on 17/05/2016.
 */
public class DAO implements Serializable {

    private static ThreadDownload thread;
    private Activity ins;
    private RecyclerView post;

    public DAO(Activity ins) {
        if (thread == null) {
            thread = new ThreadDownload(ins.getApplicationContext());
            //Object[] object = new Object[]{"SOS"};
              //  thread.execute(object);

        }

    }

    public TokenResponse getPosts() {
        return thread.getPosts();
    }

    public TokenResponse users() {

        return thread.getComments();
    }

    public void setPost(RecyclerView post) {
        Object[] object = new Object[]{"SOS", post};

        thread.execute(object);
    }
}
