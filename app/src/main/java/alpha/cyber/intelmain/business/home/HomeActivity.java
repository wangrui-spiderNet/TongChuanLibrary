package alpha.cyber.intelmain.business.home;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.GlideImageLoaderImpl;
import com.youth.banner.transformer.DefaultTransformer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import alpha.cyber.intellib.lock.LockCallback;
import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.MyApplication;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.bean.AppUpgradeInfo;
import alpha.cyber.intelmain.bean.HomeNewsBean;
import alpha.cyber.intelmain.business.login.InPutPwdActivity;
import alpha.cyber.intelmain.business.login.LoginPresenter;
import alpha.cyber.intelmain.business.mechine_helper.CheckBookService;
import alpha.cyber.intelmain.business.mechine_helper.LockHelper;
import alpha.cyber.intelmain.db.BookDao;
import alpha.cyber.intelmain.util.AppSharedPreference;
import alpha.cyber.intelmain.util.AppThreadManager;
import alpha.cyber.intelmain.util.DateUtils;
import alpha.cyber.intelmain.util.DeviceUtils;
import alpha.cyber.intelmain.util.FileUtils;
import alpha.cyber.intelmain.util.IntentUtils;
import alpha.cyber.intelmain.util.LogSaveUtils;
import alpha.cyber.intelmain.util.PackageUtils;
import alpha.cyber.intelmain.util.ShellUtils;
import alpha.cyber.intelmain.util.StringUtils;
import alpha.cyber.intelmain.util.ToastUtil;
import alpha.cyber.intelmain.widget.CustomConfirmDialog;

/**
 * Created by wangrui on 2018/1/29.
 */

public class HomeActivity extends BaseActivity implements View.OnClickListener, IHomeView, LockCallback {

    private RadioGroup rg_tabs;
    private Banner banner;
    private ImageView ivPhoto;

    private List<String> images;
    private HomePresenter homePresenter;
    private HomeNewsBean homeNewsBean;

    private LockHelper lockHelper;
    private boolean hasDoorOpen = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
        btnUpdate.setOnClickListener(this);


        rg_tabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_news:
                        if (null != homeNewsBean && null != homeNewsBean.getOrgImgInfo()) {
                            Glide.with(MyApplication.getInstance().getApplicationContext())
                                    .load(homeNewsBean.getOrgImgInfo().getNews())
                                    .into(ivPhoto);
                        }

                        break;

                    case R.id.rb_apply_card:
                        if (null != homeNewsBean && null != homeNewsBean.getOrgImgInfo()) {
                            Glide.with(MyApplication.getInstance().getApplicationContext())
                                    .load(homeNewsBean.getOrgImgInfo().getNotice())
                                    .into(ivPhoto);
                        }

                        break;
                    case R.id.rb_introduction:

                        if (null != homeNewsBean && null != homeNewsBean.getOrgImgInfo()) {
                            Glide.with(MyApplication.getInstance().getApplicationContext())
                                    .load(homeNewsBean.getOrgImgInfo().getIntroduce())
                                    .into(ivPhoto);
                        }

                        break;
                    case R.id.rb_open_time:
                        if (null != homeNewsBean && null != homeNewsBean.getOrgImgInfo()) {
                            Glide.with(MyApplication.getInstance().getApplicationContext())
                                    .load(homeNewsBean.getOrgImgInfo().getOpentime())
                                    .into(ivPhoto);
                        }

                        break;
                    case R.id.rb_use_gide:

                        if (null != homeNewsBean && null != homeNewsBean.getOrgImgInfo()) {
                            Glide.with(MyApplication.getInstance().getApplicationContext())
                                    .load(homeNewsBean.getOrgImgInfo().getTip())
                                    .into(ivPhoto);
                        }

                        break;
                    case R.id.rb_more:

                        if (null != homeNewsBean && null != homeNewsBean.getOrgImgInfo()) {
                            Glide.with(MyApplication.getInstance().getApplicationContext())
                                    .load(homeNewsBean.getOrgImgInfo().getMore())
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
        tvVersion.setText(DeviceUtils.getVersionName(this));

        homePresenter.checkVersion();

    }

    @Override
    public void onClick(View v) {
        if (v == btnRightButton) {
            IntentUtils.startAty(this, InPutPwdActivity.class);
        } else if (v == btnUpdate) {
            homePresenter.checkVersion();
        }
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
    public void onGetHomePageSuccess(HomeNewsBean newsBean) {

        if (null != newsBean && null != newsBean.getOrgImgSlideList() && newsBean.getOrgImgSlideList().size() > 0) {
            rg_tabs.check(R.id.rb_news);
            homeNewsBean = newsBean;

            Glide.with(MyApplication.getInstance().getApplicationContext())
                    .load(newsBean.getOrgImgInfo().getNews())
                    .into(ivPhoto);

            for (int i = 0; i < newsBean.getOrgImgSlideList().size(); i++) {
                images.add(newsBean.getOrgImgSlideList().get(i).getUrl());
            }

            setBanner();

            Glide.with(MyApplication.getInstance().getApplicationContext())
                    .load(newsBean.getLogo())
                    .into(ivLogo);

            AppSharedPreference.getInstance().saveLogo(newsBean.getLogo());

            tvTel.setText("TEL:" + newsBean.getService_telephone());
            tvTec.setText(newsBean.getTechnical_support());

        }

    }

    @Override
    protected void getIntentData() {

    }

    private Handler mHandler = new MyHandler();

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case LockHelper.HAS_DOOR_NOT_CLOSED:
                    showTipDialog(getString(R.string.box_not_closed),true);
                    break;
            }
        }
    }

    private byte openedId;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        openedId =(byte) AppSharedPreference.getInstance().getOpenBoxId();
        lockHelper = new LockHelper(mHandler, this);
        lockHelper.open();

        if (null != intent && null!=intent.getExtras()) {
            boolean hasDoorOpen = intent.getExtras().getBoolean(Constant.HAS_DOOR_OPEN);
            if (hasDoorOpen) {
                showTipDialog("请先关闭所有柜门，才能借还书！谢谢您的配合！", hasDoorOpen);
            }
        }

    }

    @Override
    public void onGetProtocalVerison(int version) {

    }

    @Override
    public void onGetLockState(int id, byte state) {
        if (id == openedId && state == 0) {
            hasDoorOpen = true;
            mHandler.sendEmptyMessage(LockHelper.HAS_DOOR_NOT_CLOSED);
        } else {//打开的柜门已经关了，盘点书籍
            closeTipDialog();
            hasDoorOpen = false;
        }
    }

    private void closeTipDialog() {
        if (null != customDialog ) {
            customDialog.dismiss();
        }
    }

    @Override
    public void onGetAllLockState(byte[] state) {
        hasDoorOpen = hasDoorOpen(state);
        if(hasDoorOpen){
            mHandler.sendEmptyMessage(LockHelper.HAS_DOOR_NOT_CLOSED);

        }
    }

    private CustomConfirmDialog customDialog;

    private void showTipDialog(String content, boolean hasDoorOpen) {
        customDialog = new CustomConfirmDialog(this);
        customDialog.setContent(content);
        customDialog.setConfirmButtonText("确认");
        customDialog.setCancelable(false);
        customDialog.setHasDoorOpen(hasDoorOpen);
        customDialog.setConfirmListener(new CustomConfirmDialog.CustomDialogConfirmListener() {
            @Override
            public void onButtonClick(View view) {
                lockHelper.getLockState((byte) AppSharedPreference.getInstance().getOpenBoxId());
            }
        });

        if (!isFinishing() && !customDialog.isShowing()) {
            customDialog.show();
        }

    }

    /**
     * 检查版本更新
     *
     * @author hwp
     * @since v0.0.1
     */
    @Override
    public void checkVersion(final AppUpgradeInfo appUpgradeInfo) {

//        ToastUtil.showToast(HomeActivity.this, "新版本：" + appUpgradeInfo.getNew_version_name());

        try {
            AppThreadManager.getInstance().start(new Runnable() {

                @Override
                public void run() {
                    if (appUpgradeInfo != null && appUpgradeInfo.getIs_update() != 0 && !isUpgrade) {
                        LogSaveUtils.d(Constant.TAG,
                                "当前版本信息===" + DeviceUtils.getVersionCode(MyApplication.getAppContext()) + "  name=" + DeviceUtils.getVersionName(mContext));
                        LogSaveUtils.d(Constant.TAG, "server版本信息===" + appUpgradeInfo.getNew_version_code() + "  name=" + appUpgradeInfo.getNew_version_name());
                        if (DeviceUtils.getVersionCode(MyApplication.getAppContext()) < appUpgradeInfo.getNew_version_code()) {
                            FileUtils.deleteDir(FileUtils.getAppRootDir());
                            downloadUpdate(appUpgradeInfo.getNew_version_name(), appUpgradeInfo.getDownurl());
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HttpUtils mHttpUtils;
    private boolean isUpgrade = false;

    private void downloadUpdate(String versionName, String downloadURL) {
        isUpgrade = true;
        final String update_localpath = FileUtils.getAppRootDir() + versionName + ".apk";

        mHttpUtils = new HttpUtils();

        FileUtils.deleteFile(update_localpath);

        mHttpUtils.download(downloadURL, update_localpath, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                new RequestCallBack<File>() {

                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {

                        LogSaveUtils.e(Constant.TAG, "安装包下载成功!");


                        if (!TextUtils.isEmpty(update_localpath)) {
                            File f = new File(FileUtils.getAppRootDir());
                            File[] files = f.listFiles();
                            if (null != files && files.length > 0) {
                                for (File file : files) {
                                    String apkFilePath = file.getAbsolutePath();
                                    PackageManager pm = MyApplication.getAppContext().getPackageManager();
                                    PackageInfo info = pm.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
                                    ApplicationInfo appInfo = null;
                                    if (info != null) {
                                        appInfo = info.applicationInfo;
                                        String packageName = appInfo.packageName;
                                        if (!packageName.equalsIgnoreCase(DeviceUtils.getAppPackageName(MyApplication.getAppContext()))) {
                                            file.delete();
                                        }
                                    }
                                }
                            }

//                            String apkPath = FileUtils.getRootPath(MyApplication.getInstance().getApplicationContext(), true) + "/library";

                            LogSaveUtils.e(Constant.TAG, "安装包路劲：" + update_localpath);

//                            int resultCode = PackageUtils.installSilent(MyApplication.getInstance().getApplicationContext(), update_localpath);


//                            try {
//                                FileUtils.ApkInstall(mContext, update_localpath);
//                            }catch (Exception e){
//                                e.printStackTrace();
//                                LogSaveUtils.e(Constant.TAG,"APK 安装失败");
//                            }

//                            isUpgrade = false;
//                            if (resultCode != PackageUtils.INSTALL_SUCCEEDED) {
//                                LogSaveUtils.e(Constant.TAG, "升级失败");
//                                ToastUtil.showToast(HomeActivity.this, "升级失败");
//                                isUpgrade = false;
//                            } else {
//                                LogSaveUtils.e(Constant.TAG, "升级成功!");
//                                tvVersion.setText(DeviceUtils.getVersionName(HomeActivity.this));
//                            }

                        }
                    }

                    @Override
                    public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
                        LogSaveUtils.e(Constant.TAG, "下载失败!" + arg1);
                        arg0.printStackTrace();
                        FileUtils.deleteDir(FileUtils.getAppRootDir());
                        isUpgrade = false;
                    }
                });
    }

    private boolean hasDoorOpen(byte[] state) {
        for (int i = 0; i < 1; i++) {
            if (state[i] == 0) {
                return true;

            } else {
                hasDoorOpen = false;
            }
        }

        return false;
    }

}
