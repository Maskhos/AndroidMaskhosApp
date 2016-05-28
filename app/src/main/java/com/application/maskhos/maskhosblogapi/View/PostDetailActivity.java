package com.application.maskhos.maskhosblogapi.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.application.maskhos.maskhosblogapi.Controller.Adapter.AdapterListMain;
import com.application.maskhos.maskhosblogapi.Controller.Adapter.AdapterRecyclerViewPost;
import com.application.maskhos.maskhosblogapi.Controller.Adapter.RecyclerViewItemClick;
import com.application.maskhos.maskhosblogapi.Model.TokenResponse;
import com.application.maskhos.maskhosblogapi.Model.interfaces.ActivityMain;
import com.application.maskhos.maskhosblogapi.Model.interfaces.BitmapCache.BitmapLruCache;
import com.application.maskhos.maskhosblogapi.Model.interfaces.BitmapCache.CacheableBitmapDrawable;
import com.application.maskhos.maskhosblogapi.Model.interfaces.BitmapCache.CacheableImageView;
import com.application.maskhos.maskhosblogapi.Model.interfaces.ControllerMain;
import com.application.maskhos.maskhosblogapi.Model.interfaces.httpzoid.Http;
import com.application.maskhos.maskhosblogapi.Model.interfaces.httpzoid.HttpFactory;
import com.application.maskhos.maskhosblogapi.Model.interfaces.httpzoid.HttpResponse;
import com.application.maskhos.maskhosblogapi.Model.interfaces.httpzoid.NetworkError;
import com.application.maskhos.maskhosblogapi.Model.interfaces.httpzoid.ResponseHandler;
import com.application.maskhos.maskhosblogapi.R;
import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;

import java.util.HashMap;

/**
 * Created by Matthew on 20/05/2016.
 */
public class PostDetailActivity extends ActivityMain {
    Bundle bun;
    private ControllerMain controller;
    private HashMap<String, String> post;
    private AdapterListMain adapter;
    private TokenResponse user;
    private CacheableImageView iv;
    private AwesomeTextView titulo;
    private FloatingActionButton button;
    private BootstrapCircleThumbnail circle;
    private TextView content;
    private BitmapLruCache cache;
    private ListView lvComment;
    private BootstrapButton btn_toWeb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_post_layout);
        controller = new ControllerMain(this) {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.spl_btn_toweb:
                        abrirWeb();
                        break;
                }
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        };
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setResources();
        setResourcesFormat();
        setEvents();
    }

    private void abrirWeb() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(controller.getWeb(post.get("id"))));
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cache.trimMemory();

    }


    @Override
    public void setResources() {
        bun = getIntent().getBundleExtra("bun");
        titulo = (AwesomeTextView) findViewById(R.id.spl_tv_titulo);
        iv = (CacheableImageView) findViewById(R.id.spl_iv_iv);
        content = (TextView) findViewById(R.id.spl_tv_content);
        post = (HashMap<String, String>) bun.getSerializable("post");
        user = (TokenResponse) bun.getSerializable("user");
        lvComment = (ListView) findViewById(R.id.spl_lv_list);
        btn_toWeb = (BootstrapButton) findViewById(R.id.spl_btn_toweb);
        btn_toWeb.setOnClickListener(controller);
        Http http = HttpFactory.create(this);

        http.get(controller.getUrl("comment/show/"+post.get("id")))
                .handler(new ResponseHandler<TokenResponse>() {
                    @Override
                    public void failure(NetworkError error) {
                        super.failure(error);
                        error.name();
                    }

                    @Override
                    public void success(TokenResponse user, HttpResponse response) {
                        adapter = new AdapterListMain(PostDetailActivity.this, R.layout.item_user_comment, user, controller.getUrl(""));
                        lvComment.setAdapter(adapter);
                    }
                }).send();
        cache = new BitmapLruCache.Builder().build();
        if (user != null) {
            //poner avatar del usuario en el post
            circle = (BootstrapCircleThumbnail) findViewById(R.id.spl_iv_user);
            Object[] object = new Object[]{user.getData()[0].get("uspicture"), user.getData()[0].get("usname")};
            new myTaskCircle(circle).execute(object);
        }
        if (post != null) {
            content.setText(post.get("poscontent"));
            titulo.setText(post.get("postitle"));
            Object[] object = new Object[]{post.get("posphoto"), post.get("postitle")};
            new myTask(iv).execute(object);
        }


    }

    @Override
    public void setResourcesFormat() {

    }

    @Override
    public void setEvents() {

    }

    private class myTask extends AsyncTask<Object, Object, Bitmap> {
        private CacheableImageView iv;

        public myTask(CacheableImageView iv) {


            options.inPurgeable = true; // inPurgeable is used to free up memory while required

            this.iv = iv;
        }

        protected Bitmap doInBackground(Object... params) {
            byte[] decodedByte = new byte[0];
            String name = (String) params[1];
            Bitmap b = null;
            if (!cache.contains(name)) {

                decodedByte = Base64.decode((String) params[0], 0);

                b = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length, options);
                //Bitmap created = Bitmap.createScaledBitmap(b, 200, 200, true);// convert decoded bitmap into well scalled Bitmap format.
                cache.put(name, b);

            } else {
                b = cache.get(name).getBitmap();
            }

            return b;
        }

        private BitmapFactory.Options options = new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use

        @Override
        protected void onPostExecute(Bitmap result) {
            iv.setImageBitmap(result);
            // result.recycle();

        }
    }

    private class myTaskCircle extends AsyncTask<Object, Object, Bitmap> {
        private BootstrapCircleThumbnail iv;

        public myTaskCircle(BootstrapCircleThumbnail iv) {


            options.inPurgeable = true; // inPurgeable is used to free up memory while required

            this.iv = iv;
        }

        protected Bitmap doInBackground(Object... params) {
            byte[] decodedByte = new byte[0];
            String name = (String) params[1];
            Bitmap b = null;
            if (!cache.contains(name)) {

                decodedByte = Base64.decode((String) params[0], 0);

                b = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length, options);
                //Bitmap created = Bitmap.createScaledBitmap(b, 200, 200, true);// convert decoded bitmap into well scalled Bitmap format.
                cache.put(name, b);

            } else {
                b = cache.get(name).getBitmap();
            }

            return b;
        }

        private BitmapFactory.Options options = new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use

        @Override
        protected void onPostExecute(Bitmap result) {
            iv.setImageBitmap(result);
            // result.recycle();

        }
    }
}

