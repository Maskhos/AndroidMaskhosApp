package com.application.maskhos.maskhosblogapi.Controller.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.application.maskhos.maskhosblogapi.Model.Post;
import com.application.maskhos.maskhosblogapi.Model.TokenResponse;
import com.application.maskhos.maskhosblogapi.Model.UserComment;
import com.application.maskhos.maskhosblogapi.Model.interfaces.BitmapCache.BitmapLruCache;
import com.application.maskhos.maskhosblogapi.Model.interfaces.httpzoid.Http;
import com.application.maskhos.maskhosblogapi.Model.interfaces.httpzoid.HttpFactory;
import com.application.maskhos.maskhosblogapi.Model.interfaces.httpzoid.HttpResponse;
import com.application.maskhos.maskhosblogapi.Model.interfaces.httpzoid.NetworkError;
import com.application.maskhos.maskhosblogapi.Model.interfaces.httpzoid.ResponseHandler;
import com.application.maskhos.maskhosblogapi.R;
import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.beardedhen.androidbootstrap.BootstrapThumbnail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Matthew on 17/05/2016.
 */
public class AdapterListMain extends ArrayAdapter<HashMap<String, String>> {
    private HashMap<String, String>[] map;
    private BitmapLruCache cache;

    public AdapterListMain(Context context, int resource, TokenResponse token) {

        super(context, resource, Arrays.asList(token.getData()));
        map = token.getData();
        cache = new BitmapLruCache.Builder().build();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // inflate the layout
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user_comment, parent, false);
        }
        final BootstrapCircleThumbnail iv = (BootstrapCircleThumbnail) convertView.findViewById(R.id.iuc_iv_image);


        final TextView userName = (TextView) convertView.findViewById(R.id.iuc_tv_user);


        final TextView comment = (TextView) convertView.findViewById(R.id.iuc_tv_comment);
        comment.setText(map[position].get("comcontent"));
        comment.setVisibility(View.GONE);
        iv.setVisibility(View.GONE);
        userName.setVisibility(View.GONE);
        final ProgressBar loading = (ProgressBar) convertView.findViewById(R.id.loading);

        Http http = HttpFactory.create(getContext());
        http.get(getContext().getString(R.string.api) + "user/" + map[position].get("user_id"))
                .handler(new ResponseHandler<TokenResponse>() {
                    @Override
                    public void failure(NetworkError error) {
                        super.failure(error);
                        error.name();
                        loading.setVisibility(View.GONE);
                    }


                    @Override
                    public void success(TokenResponse user, HttpResponse response) {
                        loading.setVisibility(View.GONE);
                        comment.setVisibility(View.VISIBLE);
                        iv.setVisibility(View.VISIBLE);
                        userName.setVisibility(View.VISIBLE);
                        userName.setText(user.getData()[0].get("usname"));
                        try {
                            Object[] objects = new Object[]{user.getData()[0].get("uspicture"), user.getData()[0].get("usname")};

                            new myTask(iv).execute(objects);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }).send();


        return convertView;
    }

    private class myTask extends AsyncTask<Object, Object, Bitmap> {
        private BootstrapCircleThumbnail iv;
        private BitmapFactory.Options options = new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use

        public myTask(BootstrapCircleThumbnail iv) {
            this.iv = iv;
            options.inPurgeable = true;
        }

        protected Bitmap doInBackground(Object... params) {
            byte[] decodedByte = new byte[0];
            String name = (String) params[1];
            Bitmap b = null;
            if (!cache.contains(name)) {

                decodedByte = Base64.decode((String) params[0], 0);
                b = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length, options);
                cache.put(name, b);

            } else {
                b = cache.get(name).getBitmap();
            }

            return b;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            iv.setImageBitmap(result);
        }
    }
}
