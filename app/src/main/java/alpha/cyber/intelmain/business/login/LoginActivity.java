package alpha.cyber.intelmain.business.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.util.IntentUtils;
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

        String qrcode_path="";

        setQrCode(qrcode_path);

    }

    private void setQrCode(String qrcode_path) {
        if (StringUtils.isEmpty(qrcode_path)) {
            if (ivQrCode.isFocused()){
                ivQrCode.setBackgroundResource(R.drawable.qr_code_choose);
            }else{
                ivQrCode.setBackgroundResource(R.drawable.qr_code_not_choose);
            }

        } else {
//            ivQrCode.setBackground(new BitmapDrawable(QRCodeUtil.generateQRCode(presenter.getLoginUrl(JiGuangUtils.getJiGuangRegId(mContext)), 100, 100)));
        }

//        ivQrCode.invalidate();
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
