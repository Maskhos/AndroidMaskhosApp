package com.application.maskhos.maskhosblogapi.Model.interfaces;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

import com.application.maskhos.maskhosblogapi.Model.DAO;
import com.application.maskhos.maskhosblogapi.R;

/**
 * Created by Matthew on 17/05/2016.
 */
public  abstract class ControllerMain   implements AdapterView.OnItemClickListener,View.OnClickListener{
    private Activity ins;
    private DAO dao;
    public ControllerMain(Activity ins) {
        this.ins = ins;
        if(dao == null){
            dao = new DAO(ins);
        }

    }
    public String getUrl(String resource) {
        return ins.getString(R.string.api) + resource;
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
}
