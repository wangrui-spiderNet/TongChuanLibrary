package alpha.cyber.intelmain.http.socket;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import alpha.cyber.intelmain.Constant;

/**
 * Created by wangrui on 2018/2/6.
 */

public class MyAsyncTask extends AsyncTask<String,Integer,String> {

    private OnSocketRequestListener requestListener;
    private String result;

    public MyAsyncTask(OnSocketRequestListener requestListener){
        this.requestListener = requestListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        requestListener.onStart();
    }

    @Override
    protected String doInBackground(String... requeststr) {

        try {
            Socket socket=SocketInstance.getInstance();

            OutputStream os = socket.getOutputStream();//字节输出流
            PrintWriter pw = new PrintWriter(os);//将输出流包装成打印流

            String request=requeststr[0];
//            String requeststr = "1700120180207105620AO|AB00834463|AY0A";
            request = request + calculateEndFour(request);
            Log.e(Constant.TAG, "请求：" + request);
            pw.write(request);
            pw.flush();
            socket.shutdownOutput();

            //3、获取输入流，并读取服务器端的响应信息
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String info = null;
            while ((info = br.readLine()) != null) {

                result = info;
            }

            br.close();
            is.close();
            pw.close();
            os.close();
            socket.close();

            return result;

        } catch (Exception e) {

            e.printStackTrace();

            Log.e(Constant.TAG,"异常信息："+e.getMessage());

            requestListener.onFail(e.getMessage());
        }

        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e(Constant.TAG,"返回:"+s);
        requestListener.onSuccess(s);

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

    public interface OnSocketRequestListener{
        void onStart();
        void onSuccess(String result);
        void onFail(String errorMessage);
        void onFinish();
    }

}
