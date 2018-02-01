package com.youth.banner.loader;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.youth.banner.R;


public abstract class ImageLoaderImpl implements ImageLoaderInterface<RelativeLayout> {

    @Override
    public RelativeLayout createImageView(final Context context) {
        RelativeLayout layout =(RelativeLayout) LayoutInflater.from(context).inflate(R.layout.item_banner,null);

        return layout;
    }

}
