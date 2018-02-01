package alpha.cyber.intelmain.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import alpha.cyber.intelmain.R;


/**
 * Created by huxin on 16/6/20.
 */
public class CustomConfirmDialog extends Dialog{
    private static int mTheme = R.style.CustomDialog;
    private TextView textViewContent;
    private Button buttonConfirm;
    private CustomDialogConfirmListener listener;

    public CustomConfirmDialog(Context context) {
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
                dismiss();
            }
        });
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
}
