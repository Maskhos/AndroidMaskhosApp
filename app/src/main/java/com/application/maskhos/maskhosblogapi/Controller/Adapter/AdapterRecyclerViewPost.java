package com.application.maskhos.maskhosblogapi.Controller.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.maskhos.maskhosblogapi.Model.Post;
import com.application.maskhos.maskhosblogapi.Model.TokenResponse;
import com.application.maskhos.maskhosblogapi.Model.interfaces.BitmapCache.BitmapLruCache;
import com.application.maskhos.maskhosblogapi.Model.interfaces.BitmapCache.CacheableBitmapDrawable;
import com.application.maskhos.maskhosblogapi.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Matthew on 17/05/2016.
 */
public class AdapterRecyclerViewPost extends RecyclerView.Adapter<AdapterRecyclerViewPost.ViewHolder> {
    private HashMap<String, String>[] mDataset;
    private BitmapLruCache cache;

    public void clearCache() {
        cache.trimMemory();

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView titulo;
        public TextView contenido;


        public ImageView imgView;

        public ViewHolder(View v) {
            super(v);
            titulo = (TextView) v.findViewById(R.id.icv_tv_title);
            contenido = (TextView) v.findViewById(R.id.icv_tv_content);
            imgView = (ImageView) v.findViewById(R.id.icv_iv_image);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterRecyclerViewPost(HashMap<String, String>[] myDataset) {
        mDataset = myDataset;
        BitmapLruCache.Builder bit = new BitmapLruCache.Builder();
        cache = bit.build();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterRecyclerViewPost.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_view, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.titulo.setText(mDataset[position].get("postitle"));
        // byte[] decodedByte = new byte[0];

        Object[] objects = new Object[]{mDataset[position].get("posphoto"), mDataset[position].get("postitle")};
        try {
            new myTask(holder.imgView).execute(objects);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        holder.contenido.setText(mDataset[position].get("poscontent"));
    }

    public HashMap<String, String> getComponentAt(int position) {
        return mDataset[position];
    }

    private class myTask extends AsyncTask<Object, Object, Bitmap> {
        private ImageView iv;
        private BitmapFactory.Options options = new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use

        public myTask(ImageView iv) {
            this.iv = iv;
            options.inPurgeable = true;
        }

        protected Bitmap doInBackground(Object... params) {
            byte[] decodedByte = new byte[0];
            String name = (String) params[1];
            Bitmap b = null;
            if (!cache.contains(name)) {

                decodedByte = Base64.decode((String) params[0], 0);
                b = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length,options);
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

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}

