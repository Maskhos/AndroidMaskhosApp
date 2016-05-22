package com.application.maskhos.maskhosblogapi.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.application.maskhos.maskhosblogapi.Controller.Adapter.AdapterRecyclerViewPost;
import com.application.maskhos.maskhosblogapi.Controller.Adapter.RecyclerViewItemClick;
import com.application.maskhos.maskhosblogapi.Controller.ListMainController;
import com.application.maskhos.maskhosblogapi.Model.TokenResponse;
import com.application.maskhos.maskhosblogapi.Model.interfaces.ActivityMain;
import com.application.maskhos.maskhosblogapi.Model.interfaces.httpzoid.Http;
import com.application.maskhos.maskhosblogapi.Model.interfaces.httpzoid.HttpFactory;
import com.application.maskhos.maskhosblogapi.Model.interfaces.httpzoid.HttpResponse;
import com.application.maskhos.maskhosblogapi.Model.interfaces.httpzoid.NetworkError;
import com.application.maskhos.maskhosblogapi.Model.interfaces.httpzoid.ResponseHandler;
import com.application.maskhos.maskhosblogapi.R;

import java.util.HashMap;

/**
 * Created by Matthew on 17/05/2016.
 */
public class ListMainActivity extends ActivityMain implements RecyclerViewItemClick.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private View v;
    private AdapterRecyclerViewPost adapter;
    private ListMainController controller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        //getSupportActionBar().invalidateOptionsMenu();

        setResources();
        setResourcesFormat();
        setEvents();
    }

    private ProgressBar loading;
    private SwipeRefreshLayout swipe;
    private Http http;

    @Override
    public void setResources() {

        rv = (RecyclerView) findViewById(R.id.my_recycler_view);
        controller = new ListMainController(this);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipe.setOnRefreshListener(this);
        loading = (ProgressBar) findViewById(R.id.loading);
        swipe.setColorSchemeColors(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        http = HttpFactory.create(this);
        progress = ProgressDialog.show(this, "Requesting Posts",
                "Wait a sec", true);
        getPost();

        //controller.setAdapter(rv);
    }

    private Menu optionsMenu;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_refresh, menu);
        this.optionsMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.airport_menuRefresh:

                setRefreshActionButtonState(true);
                http.get(controller.getUrl("post"))
                        .handler(new ResponseHandler<TokenResponse>() {
                            @Override
                            public void failure(NetworkError error) {
                                super.failure(error);
                                error.name();
                            }

                            @Override
                            public void success(TokenResponse user, HttpResponse response) {
                                rv.setHasFixedSize(true);
                                LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                                rv.setLayoutManager(llm);
                                adapter = new AdapterRecyclerViewPost(user.getData());
                                rv.setAdapter(adapter);
                                rv.addOnItemTouchListener(new RecyclerViewItemClick(ListMainActivity.this, ListMainActivity.this));
                                setRefreshActionButtonState(false);
                            }
                        }).send();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (optionsMenu != null) {
            final MenuItem refreshItem = optionsMenu
                    .findItem(R.id.airport_menuRefresh);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }

    public void getPost() {
        swipe.setRefreshing(true);
        http.get(controller.getUrl("post"))
                .handler(new ResponseHandler<TokenResponse>() {
                    @Override
                    public void failure(NetworkError error) {
                        super.failure(error);
                        error.name();
                    }

                    @Override
                    public void success(TokenResponse user, HttpResponse response) {
                        rv.setHasFixedSize(true);
                        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                        rv.setLayoutManager(llm);
                        adapter = new AdapterRecyclerViewPost(user.getData());
                        rv.setAdapter(adapter);
                        rv.addOnItemTouchListener(new RecyclerViewItemClick(ListMainActivity.this, ListMainActivity.this));
                        if (progress != null) {
                            progress.dismiss();
                            progress = null;
                        }
                        swipe.setRefreshing(false);
                    }
                }).send();

    }

    @Override
    public void setResourcesFormat() {

    }

    @Override
    public void setEvents() {

    }

    private RecyclerView rv;
    private ProgressDialog progress;

    @Override
    public void onItemClick(View childView, int position) {

        final ProgressDialog progress = ProgressDialog.show(this, "Opening Posts",
                "Wait a sec", true);

        final HashMap<String, String> map = adapter.getComponentAt(position);
        Http http = HttpFactory.create(this);
        http.get(controller.getUrl("user/" + map.get("user_id")))
                .handler(new ResponseHandler<TokenResponse>() {
                    @Override
                    public void failure(NetworkError error) {
                        super.failure(error);

                        progress.dismiss();
                        Toast.makeText(ListMainActivity.this, error.name(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void success(TokenResponse user, HttpResponse response) {

                        Bundle bun = new Bundle();
                        bun.putSerializable("post", map);
                        bun.putSerializable("user", user);
                        Intent intent = new Intent(ListMainActivity.this, PostDetailActivity.class);
                        intent.putExtra("bun", bun);
                        adapter.clearCache();
                        progress.dismiss();
                        startActivity(intent);
                    }
                }).send();


    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    @Override
    public void onRefresh() {
        swipe.setRefreshing(true);
        getPost();
    }
}
