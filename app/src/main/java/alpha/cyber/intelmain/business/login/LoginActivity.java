package alpha.cyber.intelmain.business.login;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import alpha.cyber.intelmain.MyApplication;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.util.FileUtils;
import alpha.cyber.intelmain.util.IntentUtils;
import alpha.cyber.intelmain.util.QRCodeUtils;
import alpha.cyber.intelmain.util.StringUtils;

/**
 * Created by wangrui on 2018/1/31.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private ImageView ivQrCode;
    private Button btnLoginByCard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void findWidgets() {

        btnRightButton.setVisibility(View.INVISIBLE);

        ivQrCode = findView(R.id.iv_qr_login);
        btnLoginByCard = findView(R.id.btn_login_by_card);

        btnLoginByCard.setOnClickListener(this);

        String qrcode_content="http://www.shuduier.com";

        setQrCode(qrcode_content);

    }

    private void setQrCode(String qrcode_content) {
        if (StringUtils.isEmpty(qrcode_content)) {
            if (ivQrCode.isFocused()){
                ivQrCode.setBackgroundResource(R.drawable.qr_code_choose);
            }else{
                ivQrCode.setBackgroundResource(R.drawable.qr_code_not_choose);
            }

        } else {

            Bitmap logobitmap= BitmapFactory.decodeResource(getResources(),R.drawable.app_logo);
            String filePath= FileUtils.getCacheQrImage(MyApplication.getInstance().getApplicationContext(),"qr.jpeg");
            ivQrCode.setBackground(new BitmapDrawable(QRCodeUtils.generateQRCode(qrcode_content,300,300,logobitmap,filePath)));
        }

    }

    @Override
    public void onClick(View v) {
        if(v==btnLoginByCard){
            IntentUtils.startAty(this,InPutPwdActivity.class);
        }
    }

    @Override
    protected void initComponent() {

    }

    @Override
    protected void getIntentData() {

    }
}
