package alpha.cyber.intelmain.base.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class BaseViewHolder {
    private final SparseArray<View> mViews = new SparseArray<View>();
    private View mConvertView;
    private int mPosition;
    private Context mContext;

//    private void LoadNative() {
    //加载资源图片
// Picasso.with(this).load(R.drawable.alipay).into(iv_picasso);
    //加载资产目录图片
// Picasso.with(this).load("file:///android_asset/heart.png").into(iv_picasso);
    //加载sd卡图片文件
    //   Picasso.with(this).load(new File("XXX")).into(iv_picasso);
    //设置大小
//Object tag = new Object();
//    Picasso.with( imageView.getContext() ).load(url).resize(dp2px(250),dp2px(250)).centerCrop().tag(tag).into(imageView);
//    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.mPosition = position;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return mPosition;
    }

    private BaseViewHolder(Context context, ViewGroup parent, int layoutID, int position) {
        mContext = context;
        mConvertView = LayoutInflater.from(mContext).inflate(layoutID, parent, false);
        mPosition = position;
        mConvertView.setTag(this);
    }

    /**
     * 拿到一个ViewHolder对象
     */
    @SuppressWarnings("null")
    public static BaseViewHolder get(Context context, View convertView, ViewGroup parent, int layoutID, int pos) {
        BaseViewHolder holder = null;
        if (convertView == null) {
            holder = new BaseViewHolder(context, parent, layoutID, pos);
        } else {
            holder = (BaseViewHolder) convertView.getTag();
            holder.mPosition = pos;
        }
        return holder;
    }

    /**
     * 通过控件的ID获取对于的控件，如果没有则加入views
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewID) {
        View view = mViews.get(viewID);
        if (view == null) {
            view = mConvertView.findViewById(viewID);
            mViews.put(viewID, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewID
     * @param text
     * @return
     */
    public void setText(int viewID, String text) {
        TextView view = getView(viewID);
        view.setText(text);
    }

    /**
     * 设置HTML字符串
     *
     * @param viewId
     * @param html
     */
    public void setHtml(int viewId, String html) {
        TextView textView = getView(viewId);
        textView.setText(Html.fromHtml(html));
    }

    /**
     * 为TextView设置中划线
     *
     * @param viewID
     */
    public void setTextFlags(int viewID) {
        TextView view = getView(viewID);
        view.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    /**
     * 为LinearLayout 动态添加内容
     *
     * @param viewID
     * @param
     * @return
     */
    public void addViewsInLinearLayout(int viewID, View child) {
        LinearLayout layout = getView(viewID);
        layout.addView(child);
    }

    /**
     * 为LinearLayout 清除内容
     *
     * @param viewID
     * @param
     * @return
     */
    public void removeViewsInLinearLayout(int viewID) {
        LinearLayout layout = getView(viewID);
        layout.removeAllViews();
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewID
     * @param drawableID
     * @return
     */
    public void setImageResource(int viewID, int drawableID) {
        ImageView view = getView(viewID);
        view.setImageResource(drawableID);
    }

    /**
     * 隐藏ImageView
     *
     * @param viewID
     * @return
     */
    public void showOrHideView(int viewID, boolean isShowView) {
        View view = getView(viewID);
        if (isShowView) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 显示或隐藏view
     *
     * @param
     * @return
     */
    public void showOrGoneView(int viewID, boolean isShowView) {
        View view = getView(viewID);
        if (isShowView) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 为SmratImageView设置图片、加载中图片、加载失败图片
     *
     * @param viewID
     * @param
     * @return
     */
    public void setImageByUrl(int viewID, String url, Integer defaultResouse, Integer errorResource) {
        ImageView view = getView(viewID);
        Picasso.with(mContext).load(url).placeholder(defaultResouse).error(errorResource).into(view);
    }

    /**
     * 设置带尺寸图片
     *
     * @param viewID
     * @param url
     * @param width
     * @param height
     * @param defaultResouse
     * @param errorResource
     */
    public void setImageByUrl(int viewID, String url, int width, int height, Integer defaultResouse, Integer errorResource) {
        ImageView view = getView(viewID);
        Picasso.with(mContext).load(url).resize(width, height).placeholder(defaultResouse).error(errorResource).centerCrop().into(view);
    }


    /**
     * 显示带尺寸图片
     */
    public void setImageByUrl(ImageView view, String url, int width, int height, Integer defaultResouse, Integer errorResource) {
        Picasso.with(mContext).load(url).resize(width, height).placeholder(defaultResouse).error(errorResource).centerCrop().into(view);
    }

    /**
     * 显示图片
     */
    public void setImageByUrl(ImageView view, String url, Integer defaultResouse, Integer errorResource) {
        Picasso.with(mContext).load(url).placeholder(defaultResouse).error(errorResource).centerCrop().into(view);
    }

    /**
     * 显示圆形图片
     *
     * @param viewID
     * @param url
     * @param defaultResouse
     * @param errorResource
     */
    public void setImageByUrlRoundImage(int viewID, String url, Integer defaultResouse, Integer errorResource) {
        ImageView view = getView(viewID);
        Picasso.with(mContext).load(url).placeholder(defaultResouse).error(errorResource).transform(
                new CircleTransform()).centerCrop().into(view);
    }

    /**
     * 显示带尺寸圆形图片
     *
     * @param viewID
     * @param url
     * @param width
     * @param height
     * @param defaultResouse
     * @param errorResource
     */
    public void setImageByUrlRoundImage(int viewID, String url, int width, int height, Integer defaultResouse, Integer errorResource) {
        ImageView view = getView(viewID);
        Picasso.with(mContext).load(url).resize(width, height).placeholder(defaultResouse).error(errorResource).centerCrop().into(view);
    }


    /**
     * 为 设置 点击事件
     *
     * @param viewID
     * @param onClickListener
     */
    public void setOnClickListener(int viewID, int width, int height, OnClickListener onClickListener) {
        View copyFancyBtn = getView(viewID);
        copyFancyBtn.setOnClickListener(onClickListener);
    }
    public void setOnClickListener(int viewID, OnClickListener onClickListener) {
        View copyFancyBtn = getView(viewID);
        copyFancyBtn.setOnClickListener(onClickListener);
    }

    /**
     * 获取指定id控件
     * @param id
     * @param <T>
     * @return
     */
    public  <T> T findView(int id) {

        return (T) getView(id);
    }
}
