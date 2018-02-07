package alpha.cyber.intelmain.http.socket;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by wangrui on 2018/2/6.
 */

public class SocketInstance {

    private static Socket socket;

    private SocketInstance() {
    }

    public static Socket getInstance() {
        if (null == socket || socket.isClosed()) {
            synchronized (SocketInstance.class) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
                        try {
                            socket = new Socket(SocketConstants.BASE_IP_ADDRESS, SocketConstants.BASE_PORT);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

//                    }
//                }).start();

            }
        }
        return socket;
    }

}
