package alpha.cyber.intelmain.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.util.ToastUtil;


/**
 * Created by huxin on 16/6/20.
 */
public class CustomConfirmDialog extends Dialog{
    private static int mTheme = R.style.CustomDialog;
    private TextView textViewContent;
    private Button buttonConfirm;
    private CustomDialogConfirmListener listener;
    private boolean hasDoorOpen = true;

    public CustomConfirmDialog(final Context context) {
        super(context,mTheme);
        setContentView(R.layout.dialog_confirm_custom);
        textViewContent = (TextView) findViewById(R.id.content_dialog);
        buttonConfirm = (Button)findViewById(R.id.button_confirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null) {
                    listener.onButtonClick(v);
                }

                if(!hasDoorOpen){
                    dismiss();
                }else {
//                    ToastUtil.showToast(context,"您还有未关闭的柜门，请检查柜门是否全部关闭！");
                }
            }
        });
    }

    public boolean isHasDoorOpen() {
        return hasDoorOpen;
    }

    public void setHasDoorOpen(boolean hasDoorOpen) {
        this.hasDoorOpen = hasDoorOpen;
    }

    public interface CustomDialogConfirmListener{
        public void onButtonClick(View view);
    }

    public void setContent(String content){
        textViewContent.setText(content);
    }

    public void setConfirmButtonText(String text){
        buttonConfirm.setText(text);
    }

    public void setConfirmListener(CustomDialogConfirmListener listener){
        this.listener = listener;
    }

    public Button getButtonConfirm(){
        return buttonConfirm;
    }
}
