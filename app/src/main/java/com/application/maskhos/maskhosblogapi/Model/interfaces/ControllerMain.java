package com.application.maskhos.maskhosblogapi.Model.interfaces;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;

import com.application.maskhos.maskhosblogapi.Model.DAO;
import com.application.maskhos.maskhosblogapi.R;

/**
 * Created by Matthew on 17/05/2016.
 */
public abstract class ControllerMain implements AdapterView.OnItemClickListener, View.OnClickListener {
    private Activity ins;
    private DAO dao;

    public ControllerMain(Activity ins) {
        this.ins = ins;
        if (dao == null) {
            dao = new DAO(ins);
        }

    }

    public String getUrl(String resource) {

        SharedPreferences prefs =
                ins.getSharedPreferences("prefs", ins.getApplicationContext().MODE_PRIVATE);

        String api = prefs.getString("api", ins.getString(R.string.api));
        return api + resource;
    }

    public DAO getDao() {
        return dao;
    }

    public ControllerMain() {
    }

    public Activity getIns() {
        return ins;
    }

    public void setIns(Activity ins) {
        this.ins = ins;
    }

    public String getWeb(String post) {
        SharedPreferences prefs =
                ins.getSharedPreferences("prefs", ins.getApplicationContext().MODE_PRIVATE);

        String web = prefs.getString("web", ins.getString(R.string.api) );
        return web+ "blog/" + post;
    }
}
