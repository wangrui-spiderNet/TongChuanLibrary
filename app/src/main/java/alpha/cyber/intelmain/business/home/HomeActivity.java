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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.MyApplication;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.business.login.LoginActivity;
import alpha.cyber.intelmain.business.login.LoginPresenter;
import alpha.cyber.intelmain.http.socket.MyAsyncTask;
import alpha.cyber.intelmain.http.socket.SocketInstance;
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

//            getUserState();
            getUserInfo();
//            getBookInfo();
        }
    }

    private void getUserState() {

        String requeststr = "2300120180206    162150AO|AA6101008880085324|AC|AD666666|AY2AZ";


        new Thread() {
            @Override
            public void run() {
                try {
                    //客户端
//1、创建客户端Socket，指定服务器地址和端口
                    Socket socket = new Socket("113.200.60.162", 2002);
//2、获取输出流，向服务器端发送信息
                    OutputStream os = socket.getOutputStream();//字节输出流
                    PrintWriter pw = new PrintWriter(os);//将输出流包装成打印流
                    String requeststr = "2300120180206    162150AO|AA6101008880085324|AC|AD666666|AY2AZ";
                    requeststr = requeststr + calculateEndFour(requeststr);

                    Log.e(Constant.TAG, "请求：" + requeststr);
                    pw.write(requeststr);
                    pw.flush();
                    socket.shutdownOutput();
//3、获取输入流，并读取服务器端的响应信息
                    InputStream is = socket.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String info = null;
                    while ((info = br.readLine()) != null) {
                        System.out.println("我是客户端，服务器说：" + info);
                        Log.e(Constant.TAG, "返回:" + info);
                    }

//4、关闭资源
                    br.close();
                    is.close();
                    pw.close();
                    os.close();
                    socket.close();

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }.start();
    }


    private void getUserInfo() {
        String requeststr = "6300120180206162150Y        AO|AA6101008880085324|AC|AD666666|BP00001|BQ00005AY0AZ";
        presenter.getUserInfo(requeststr);


        String requeststr2 = "2300120180206    162150AO|AA6101008880085324|AC|AD666666|AY2AZ";
        presenter.getUserState(requeststr2);


    }

    private void getBookInfo() {

        new Thread() {
            @Override
            public void run() {
                try {
                    //客户端
//1、创建客户端Socket，指定服务器地址和端口
                    Socket socket = SocketInstance.getInstance();
//2、获取输出流，向服务器端发送信息
                    OutputStream os = socket.getOutputStream();//字节输出流
                    PrintWriter pw = new PrintWriter(os);//将输出流包装成打印流
                    /**
                     * 17001（18位时间）AO|AB00834463|AY0AZ（检验码）
                     */
                    String requeststr = "1700120180207105620AO|AB00834463|AY0A";
                    requeststr = requeststr + calculateEndFour(requeststr);

                    Log.e(Constant.TAG, "请求：" + requeststr);
                    pw.write(requeststr);
                    pw.flush();
                    socket.shutdownOutput();
//3、获取输入流，并读取服务器端的响应信息
                    InputStream is = socket.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String info = null;
                    while ((info = br.readLine()) != null) {
                        Log.e(Constant.TAG, "返回:" + info);
                    }

//4、关闭资源
                    br.close();
                    is.close();
                    pw.close();
                    os.close();
                    socket.close();

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }.start();
    }

    private String calculateEndFour(String str) {

        char[] chars = str.toCharArray();
        int sum = 0;

        for (int i = 0; i < chars.length; i++) {
            sum = sum + chars[i];
        }

        sum = (sum & 0xFFFF) * (-1);

        String hex = Integer.toHexString(sum);

        Log.e(Constant.TAG,"hex:"+hex);
        int o = hex.length() - 4;

        return hex.substring(o, hex.length());
    }

    @Override
    protected void getIntentData() {

    }


}
