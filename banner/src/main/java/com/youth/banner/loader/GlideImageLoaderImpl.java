package com.youth.banner.loader;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.youth.banner.R;

public class GlideImageLoaderImpl extends ImageLoaderImpl {
    @Override
    public void displayImage(Context context, Object path, RelativeLayout layout) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择

        ImageView iv = (ImageView) layout.findViewById(R.id.iv_banner_item);

        Glide.with(context.getApplicationContext())
                .load(path)
                .into(iv);
    }

//    @Override
//    public ImageView createImageView(Context context) {
//        //圆角
//        return new RoundAngleImageView(context);
//    }
}
