package alpha.cyber.intelmain.base;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tendcloud.tenddata.TCAgent;
import com.youth.banner.transformer.BackgroundToForegroundTransformer;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.MyApplication;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.business.manage.ManagerLoginActivity;
import alpha.cyber.intelmain.http.BaseURL;
import alpha.cyber.intelmain.util.ActivityManager;
import alpha.cyber.intelmain.util.AppSharedPreference;
import alpha.cyber.intelmain.util.DateUtils;
import alpha.cyber.intelmain.util.DensityUtil;
import alpha.cyber.intelmain.util.DialogUtil;
import alpha.cyber.intelmain.util.IntentUtils;
import alpha.cyber.intelmain.util.StringUtils;


/**
 * Created by wangrui on 2016/6/22.
 */
public abstract class BaseActivity  extends FragmentActivity {

    public static Activity instance;
    private String session;
    private String runningActivityName;
    public Context mContext;
    protected int screenWidth;
    protected int screenHeight;

    public String baseUrl;

    /**
     * 中间内容区域的容器
     */
    public LinearLayout base_content;
    /**
     * 中间内容区域的布局
     */
    private View contentView;
    /**
     * 标题栏根布局
     */
    public RelativeLayout rl_common_title;

    /**
     * 返回按钮
     */
    public TextView tvBack;
    public Button btnRightButton;
    public Button btnUpdate;
    public ImageView ivLogo,ivQr;

    public TextView tvTel;
    public TextView tvTec;
    public TextView tvVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);

        instance = this;

        baseUrl = BaseURL.getBaseURL();

        // api15 以上打开硬件加速
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            if (!getComponentName().getClassName().equals("alpha.cyber.intelmain.base.BaseActivity")) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        }

        if (savedInstanceState != null) {
            session = savedInstanceState.getString("session");
//            AppSharedPreference.getInstance().setSession(session);
        }

        mContext = this;
        // 添加Activity到堆栈
        ActivityManager.getAppManager().addActivity(this);

        screenWidth = DensityUtil.getScreenW(mContext);
        screenHeight =DensityUtil.getScreenH(mContext);

        super.setContentView(R.layout.activity_base_layout);

        this.init();
        runningActivityName = MyApplication.getInstance().getRunningActivityName();
        Log.i("currentActivity:", "当前所在的Activity为:" + runningActivityName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String logourl=AppSharedPreference.getInstance().getLogo();

        if(!StringUtils.isEmpty(logourl)){
            Glide.with(MyApplication.getInstance().getApplicationContext())
                    .load(logourl)
                    .into(ivLogo);
        }

        TCAgent.onPageStart(this,this.getClass().getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        TCAgent.onPageStart(this,this.getClass().getSimpleName());
    }

    /**
     * 设置内容区域
     *
     * @param resId 资源文件id
     */
    @Override
    public void setContentView(int resId) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.contentView = inflater.inflate(resId, null);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        this.contentView.setLayoutParams(layoutParams);
        if (null != this.base_content) {
            this.base_content.addView(this.contentView);
        }

        getIntentData();
        findWidgets();
        initComponent();
        initListener();
        initHandler();
        asyncRetrive();
    }

    long temptime;
    int click_count;
    private void init() {
        this.rl_common_title = findView(R.id.rl_common_title);
        this.tvBack =  findView(R.id.iv_back);
        this.ivLogo = findView(R.id.iv_logo);
        this.ivQr = findView(R.id.iv_qr);
        this.base_content =  findView(R.id.base_content);
        this.btnRightButton = findView(R.id.btn_right_bottom);
        this.tvTel = findView(R.id.tv_tel);
        this.tvTec = findView(R.id.tv_tech_service);
        this.tvVersion = findView(R.id.tv_version);
        this.btnUpdate = findView(R.id.btn_update);

        btnUpdate.setVisibility(View.GONE);

        ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long time= DateUtils.getCurrentTime();

                if((time-temptime)<2000){
                    click_count=click_count+1;
                    if(click_count>=5){
                        IntentUtils.startAty(BaseActivity.this, ManagerLoginActivity.class);
                        click_count=0;
                        Log.e(Constant.TAG,"连击5次");
                    }
                }else{
                    click_count = 0;
                }

                temptime = time;
            }
        });

    }

    public void hideTitle(){
        rl_common_title.setVisibility(View.GONE);
    }

    /**
     * 初始化控件
     */
    protected abstract void findWidgets();

    /**
     * 初始化控件数据
     */
    protected abstract void initComponent();

    /**
     * 初始化数据
     */
    protected abstract void getIntentData();

    @Override
    public void onBackPressed() {
        if (DialogUtil.isProgressDialogShowing()) {
            DialogUtil.closeProgressDialog();
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束Activity&从堆栈中移除
        closeDialog();
        ActivityManager.getAppManager().finishActivity(this);
    }

    /**
     * 初始化控件
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T> T findView(int id) {
        return (T) findViewById(id);
    }

    /**
     * 初始化Listener，子类根据需要自行重写
     */
    protected void initListener() {
        return;
    }

    /**
     * 初始化Handler，子类根据需要自行重写
     */
    protected void initHandler() {
        return;
    }

    /**
     * 异步查询网络数据，子类根据需要自行重写
     */
    protected void asyncRetrive() {
        return;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (DialogUtil.isProgressDialogShowing()) {
                DialogUtil.closeProgressDialog();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public void showDialog(String string) {
        DialogUtil.showProgressDialog(this, string);
        DialogUtil.setDialogCancelable(true);
    }

    public void closeDialog() {
        DialogUtil.closeProgressDialog();
    }

    public void back(View v) {
        finish();
    }

    /**
     * 实现点击空白处，软键盘消失事件
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), /*right = left + v.getWidth();*/ right =screenWidth;
            return !(event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    protected void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
