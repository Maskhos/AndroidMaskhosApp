package com.application.maskhos.maskhosblogapi.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.application.maskhos.maskhosblogapi.R;
import com.application.maskhos.maskhosblogapi.Model.interfaces.FragmentsMain;

/**
 * Created by Matthew on 17/05/2016.
 */
public class UserCommentActivity extends FragmentsMain {
    private View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_list_main, container, false);
        return v;
    }

    @Override
    public void setResources() {
        lvMain = (ListView) v.findViewById(R.id.am_lv_main);
    }

    @Override
    public void setResourcesFormat() {

    }

    @Override
    public void setEvents() {

    }
    private ListView lvMain;
}
