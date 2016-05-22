package com.application.maskhos.maskhosblogapi.Controller;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.application.maskhos.maskhosblogapi.Model.interfaces.ControllerMain;

/**
 * Created by Matthew on 17/05/2016.
 */
public class ListMainController  extends ControllerMain{
    private RecyclerView adapter;

    public ListMainController(Activity ins) {
        super(ins);
    }



    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void setAdapter(RecyclerView adapter) {
        getDao().setPost(adapter);
        //this.adapter = adapter;
    }
}
