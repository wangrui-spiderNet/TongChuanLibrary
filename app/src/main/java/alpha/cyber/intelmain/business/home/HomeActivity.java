package alpha.cyber.intelmain.business.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.GlideImageLoaderImpl;
import com.youth.banner.transformer.DefaultTransformer;

import java.util.ArrayList;
import java.util.List;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.MyApplication;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.bean.HomeNewsBean;
import alpha.cyber.intelmain.business.login.InPutPwdActivity;
import alpha.cyber.intelmain.business.login.LoginActivity;
import alpha.cyber.intelmain.business.login.LoginPresenter;
import alpha.cyber.intelmain.db.BookDao;
import alpha.cyber.intelmain.util.AppSharedPreference;
import alpha.cyber.intelmain.util.DateUtils;
import alpha.cyber.intelmain.util.IntentUtils;

/**
 * Created by wangrui on 2018/1/29.
 */

public class HomeActivity extends BaseActivity implements View.OnClickListener, IHomeView {

    private RadioGroup rg_tabs;
    private Banner banner;
    private ImageView ivPhoto;

    private List<String> images;
    private LoginPresenter presenter;
    private HomePresenter homePresenter;

    private RadioButton rbNews, rbApplyCard, rbIntroduction, rbOpenTime, rbUseGuide, rbMore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        presenter = new LoginPresenter(this);
        homePresenter = new HomePresenter(this, this);
        homePresenter.getHomeNews();
        new BookDao(this).deleteAll();
    }

    @Override
    protected void initComponent() {

    }

    @Override
    protected void findWidgets() {
        rg_tabs = findView(R.id.rg_tabs);
        banner = findView(R.id.banner);
        ivPhoto = findView(R.id.iv_photo);
        tvBack.setVisibility(View.INVISIBLE);
        rbNews = findView(R.id.rb_news);
        rbApplyCard = findView(R.id.rb_apply_card);
        rbOpenTime = findView(R.id.rb_open_time);
        rbUseGuide = findView(R.id.rb_use_gide);
        rbMore = findView(R.id.rb_more);
        rbIntroduction = findView(R.id.rb_introduction);

        images = new ArrayList<String>();

    }

    private void setBanner() {
        banner.setImages(images).setImageLoader(new GlideImageLoaderImpl()).start();
        banner.setBannerAnimation(DefaultTransformer.class);
        banner.setDelayTime(3000);
        banner.setIndicatorGravity(BannerConfig.RIGHT);
    }

    @Override
    protected void initListener() {
        super.initListener();
        btnRightButton.setVisibility(View.VISIBLE);
        btnRightButton.setOnClickListener(this);

        rg_tabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_news:
                        if (null != rbNews.getTag()) {
                            Glide.with(MyApplication.getInstance().getApplicationContext())
                                    .load(rbNews.getTag().toString())
                                    .into(ivPhoto);
                        }

                        break;

                    case R.id.rb_apply_card:
                        if (null != rbApplyCard.getTag()) {
                            Glide.with(MyApplication.getInstance().getApplicationContext())
                                    .load(rbApplyCard.getTag().toString())
                                    .into(ivPhoto);
                        }

                        break;
                    case R.id.rb_introduction:

                        if (null != rbIntroduction.getTag()) {
                            Glide.with(MyApplication.getInstance().getApplicationContext())
                                    .load(rbIntroduction.getTag().toString())
                                    .into(ivPhoto);
                        }

                        break;
                    case R.id.rb_open_time:
                        if (null != rbOpenTime.getTag()) {
                            Glide.with(MyApplication.getInstance().getApplicationContext())
                                    .load(rbOpenTime.getTag().toString())
                                    .into(ivPhoto);
                        }

                        break;
                    case R.id.rb_use_gide:

                        if (null != rbUseGuide.getTag()) {
                            Glide.with(MyApplication.getInstance().getApplicationContext())
                                    .load(rbUseGuide.getTag().toString())
                                    .into(ivPhoto);
                        }

                        break;
                    case R.id.rb_more:

                        if (null != rbMore.getTag()) {
                            Glide.with(MyApplication.getInstance().getApplicationContext())
                                    .load(rbMore.getTag().toString())
                                    .into(ivPhoto);
                        }

                        break;
                    default:
                        break;
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        tvBack.setVisibility(View.INVISIBLE);

//        if (AppSharedPreference.getInstance().isLogin()) {
//            btnRightButton.setVisibility(View.INVISIBLE);
//        } else {
//            btnRightButton.setVisibility(View.VISIBLE);
//        }

    }

    @Override
    public void onClick(View v) {
        if (v == btnRightButton) {
            IntentUtils.startAty(this, InPutPwdActivity.class);
//            requestTest();
        }
    }

    private void requestTest() {

        String time = DateUtils.getSystemTime();

        String time1 = time.substring(0, 8);
        String time2 = time.substring(8, time.length());
        Log.e(Constant.TAG, "time:" + time);

        //读者状态信息
//        String userstate_request = getResources().getString(R.string.userstate_request);
//        String userstate_format = String.format(userstate_request, time1, time2,"", cardnum, pwd);
//        presenter.getUserState(userstate_format);
//        //读者信息
//        String userinfo_request = getResources().getString(R.string.userinfo_request);
//        String userinfo_format = String.format(userinfo_request,time,cardnum,pwd);
//        presenter.getUserInfo(userinfo_format);

//



    }

    @Override
    public void showLoadingDialog() {

    }

    @Override
    public void hideLoadingDialog() {

    }

    @Override
    public void showErrorMsg(String msg) {

    }

    @Override
    public void onGetHomePageSuccess(List<HomeNewsBean> newsBeanList) {

        if (null != newsBeanList && newsBeanList.size() > 0) {
            rg_tabs.check(R.id.rb_news);
            Glide.with(MyApplication.getInstance().getApplicationContext())
                    .load(newsBeanList.get(0).getImgUrl())
                    .into(ivPhoto);

            for (int i = 0; i < newsBeanList.size(); i++) {
                String name = newsBeanList.get(i).getName();
                Log.e(Constant.TAG, "name:" + name);
                if (name.equals(getString(R.string.hot_news))) {
                    rbNews.setTag(newsBeanList.get(i).getImgUrl());
                } else if (name.equals(getString(R.string.apply_card))) {
                    rbApplyCard.setTag(newsBeanList.get(i).getImgUrl());
                } else if (name.equals(getString(R.string.open_time))) {
                    rbOpenTime.setTag(newsBeanList.get(i).getImgUrl());
                } else if (name.equals(getString(R.string.use_guide))) {
                    rbUseGuide.setTag(newsBeanList.get(i).getImgUrl());
                } else if (name.equals(getString(R.string.more))) {
                    rbMore.setTag(newsBeanList.get(i).getImgUrl());
                } else if (name.equals(getString(R.string.lib_intro))) {
                    rbIntroduction.setTag(newsBeanList.get(i).getImgUrl());
                } else if (name.equals("banner")) {
                    images.add(newsBeanList.get(i).getImgUrl());
                }
            }

            setBanner();
        }

    }

    @Override
    protected void getIntentData() {

    }


}
