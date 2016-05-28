package com.application.maskhos.maskhosblogapi.View;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.application.maskhos.maskhosblogapi.Model.interfaces.ActivityMain;
import com.application.maskhos.maskhosblogapi.R;
import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

/**
 * Created by Matthew on 28/05/2016.
 */
public class SaveConfigurationActivity extends ActivityMain implements View.OnClickListener {
    private BootstrapEditText et_api;
    private BootstrapEditText et_web;
    private BootstrapButton btn_saveAPI;
    private BootstrapButton btn_saveWeb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setResources();
        setResourcesFormat();
        setEvents();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setResources() {
        et_api = (BootstrapEditText) findViewById(R.id.ac_et_api);
        et_web = (BootstrapEditText) findViewById(R.id.ac_et_web);
        btn_saveAPI = (BootstrapButton) findViewById(R.id.ac_btn_api);
        btn_saveWeb = (BootstrapButton) findViewById(R.id.ac_btn_web);
    }

    @Override
    public void setResourcesFormat() {
        SharedPreferences prefs =
                getSharedPreferences("prefs", getApplicationContext().MODE_PRIVATE);

        String api = prefs.getString("api", getString(R.string.api));
        et_api.setText(api);

        String web = prefs.getString("web", getString(R.string.web));
        et_web.setText(web);
    }

    @Override
    public void setEvents() {
        btn_saveWeb.setOnClickListener(this);
        btn_saveAPI.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ac_btn_api:
                saveApi();
                break;
            case R.id.ac_btn_web:
                saveWeb();
                break;
        }
    }

    private void saveWeb() {
        SharedPreferences prefs =
                getSharedPreferences("prefs", getApplicationContext().MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("web", et_web.getText().toString());
        editor.commit();
    }

    private void saveApi() {
        SharedPreferences prefs =
                getSharedPreferences("prefs", getApplicationContext().MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("api", et_api.getText().toString());
        editor.commit();


    }
}
