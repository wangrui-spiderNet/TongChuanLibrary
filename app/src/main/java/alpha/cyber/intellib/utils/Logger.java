package alpha.cyber.intellib.utils;

import android.util.Log;

public class Logger {

	private static final String TAG = "IntellLibDemo";

	private static boolean DEBUG = true;

	public static void e(String msg) {
		if (DEBUG)
			Log.e(TAG, msg);
	}

	public static void d(String msg) {
		if (DEBUG)
			Log.d(TAG, msg);
	}

	public static void i(String msg) {
		if (DEBUG)
			Log.i(TAG, msg);
	}

	public static void v(String msg) {
		if (DEBUG)
			Log.v(TAG, msg);
	}

	public static void eLine(String msg) {
		if (DEBUG)
			Log.e(TAG, getObjectInfo(4) + msg);
	}

	public static void dLine(String msg) {
		if (DEBUG)
			Log.d(TAG, getObjectInfo(4) + msg);
	}

	public static void iLine(String msg) {
		if (DEBUG)
			Log.i(TAG, getObjectInfo(4) + msg);
	}

	public static void vLine(String msg) {
		if (DEBUG)
			Log.v(TAG, getObjectInfo(4) + msg);
	}

	public static void exception(String msg) {
		if (DEBUG)
			Log.e(TAG, msg, new Exception());
	}

	private static String getObjectInfo(int index) {
		StackTraceElement[] steArray = Thread.currentThread().getStackTrace();

		StackTraceElement ste = steArray[index];

		return ste.getClassName() + ", method: " + ste.getMethodName() + ", line: " + ste.getLineNumber() + "\n";
	}

}
