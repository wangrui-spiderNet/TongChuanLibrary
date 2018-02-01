package alpha.cyber.intellib.utils;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import alpha.cyber.intelmain.MyApplication;
import alpha.cyber.intelmain.R;

public class ToastUtils {

	public static final String TAG = "ToastUtils";
	
	static final int GRAVITY = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
	
	
	public static void showShortToast(int resId){
		makeText(resId, Toast.LENGTH_SHORT).show();
	}
	
	public static void showShortToast(CharSequence text){
		if(TextUtils.isEmpty(text)) return;
		makeText(text, Toast.LENGTH_SHORT).show();
	}
	
	public static void showLongToast(int resId){
		if(resId<=0) return;
		makeText(resId, Toast.LENGTH_LONG).show();
	}
	
	public static void showLongToast(CharSequence text){
		if(TextUtils.isEmpty(text)) return;
		makeText(text, Toast.LENGTH_LONG).show();
	}
	
	private static Toast makeText(CharSequence text, int duration) {
		Toast toast = new Toast(MyApplication.getInstance());
        
        View v = View.inflate(MyApplication.getInstance(), R.layout.toast, null);
        TextView message = (TextView)v.findViewById(R.id.message);
        message.setText(text);
        toast.setView(v);
        toast.setGravity(GRAVITY, 0, 100);
        toast.setDuration(duration);
        return toast;
//		return Toast.makeText(NiuNiuParking.getAppContext(), text, duration);
    }

	private static Toast makeText(int resId, int duration) {
		Toast toast = new Toast(MyApplication.getInstance());
        
        View v = View.inflate(MyApplication.getInstance(), R.layout.toast, null);
        TextView message = (TextView)v.findViewById(R.id.message);
        message.setText(resId);
        toast.setView(v);
        toast.setGravity(GRAVITY, 0, 100);
        toast.setDuration(duration);
        return toast;
//		return Toast.makeText(NiuNiuParking.getAppContext(), resId, duration);
    }
	
}
