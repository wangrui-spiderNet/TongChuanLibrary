package alpha.cyber.intelmain.business.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import alpha.cyber.intelmain.business.login.LoginActivity;
import alpha.cyber.intelmain.business.login.LoginPresenter;
import alpha.cyber.intelmain.util.DateUtils;
import alpha.cyber.intelmain.util.IntentUtils;

/**
 * Created by wangrui on 2018/1/29.
 */

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private RadioGroup rg_tabs;
    private Banner banner;
    private ImageView ivPhoto;

    private List<String> images;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        presenter = new LoginPresenter(this);
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

        images = new ArrayList<String>();
        images.add("http://zl-teacher.oss-cn-hangzhou.aliyuncs.com/1516932966773%3D2.jpg");
        images.add("http://zl-teacher.oss-cn-hangzhou.aliyuncs.com/1516933006088%3D4.jpg");
        images.add("http://zl-teacher.oss-cn-hangzhou.aliyuncs.com/1516933029446%3D5.jpg");

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

        rg_tabs.check(R.id.rb_news);
        Glide.with(MyApplication.getInstance().getApplicationContext())
                .load(images.get(0))
                .into(ivPhoto);

        rg_tabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_news:
                        Glide.with(MyApplication.getInstance().getApplicationContext())
                                .load(images.get(0))
                                .into(ivPhoto);
                        break;

                    case R.id.rb_apply_card:
                        Glide.with(MyApplication.getInstance().getApplicationContext())
                                .load(images.get(1))
                                .into(ivPhoto);
                        break;
                    case R.id.rb_introduction:
                        Glide.with(MyApplication.getInstance().getApplicationContext())
                                .load(images.get(2))
                                .into(ivPhoto);
                        break;
                    case R.id.rb_open_time:
                        Glide.with(MyApplication.getInstance().getApplicationContext())
                                .load(images.get(0))
                                .into(ivPhoto);
                        break;
                    case R.id.rb_use_gide:
                        Glide.with(MyApplication.getInstance().getApplicationContext())
                                .load(images.get(1))
                                .into(ivPhoto);
                        break;
                    case R.id.rb_more:

                        Glide.with(MyApplication.getInstance().getApplicationContext())
                                .load(images.get(2))
                                .into(ivPhoto);
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

    }

    @Override
    public void onClick(View v) {
        if (v == btnRightButton) {
            IntentUtils.startAty(this, LoginActivity.class);

            requestTest();
        }
    }

    private String cardnum="6101008880085324";
    private String pwd = "666666";
    private String book_code = "00834463";

    private void requestTest() {

        String time = DateUtils.getSystemTime();

        String time1 = time.substring(0, 8);
        String time2 = time.substring(8, time.length());
        Log.e(Constant.TAG, "time:" + time);

        //读者状态信息
        String userstate_request = getResources().getString(R.string.userstate_request);
        String userstate_format = String.format(userstate_request, time1, time2,"", cardnum, pwd);
        presenter.getUserState(userstate_format);
        //读者信息
        String userinfo_request = getResources().getString(R.string.userinfo_request);
        String userinfo_format = String.format(userinfo_request,time,cardnum,pwd);
        presenter.getUserInfo(userinfo_format);
        //书籍信息
        String bookinfo_request=getResources().getString(R.string.bookinfo_request);
        String bookinfo_format = String.format(bookinfo_request,time,book_code);
        presenter.getBookInfo(bookinfo_format);

        //借书
        String borrow_book_request =getResources().getString(R.string.borrowbook_reuqest);
        String borrow_book_format = String.format(borrow_book_request,time1,time2,time1,time2,cardnum,book_code);
        presenter.borrowBook(borrow_book_format);

        //续借
        String continue_borrow_request = getResources().getString(R.string.re_borrow_book_request);
        String continue_borrow_format = String.format(continue_borrow_request,time1,time2,time1,time2,pwd,book_code);
        presenter.continueBorrowBook(continue_borrow_format);

        //还书
        String back_book_request = getResources().getString(R.string.back_book_request);
        String back_book_format = String.format(back_book_request,time1,time2,book_code);
        presenter.backBook(back_book_format);


    }

    @Override
    protected void getIntentData() {

    }


}
