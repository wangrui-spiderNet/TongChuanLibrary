package alpha.cyber.intelmain.business;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;

public class MainActivity extends BaseActivity implements OnClickListener {

	private Button mLockTest;
	private Button mHfTest;
	private Button mIDReaderTest;
	private Button mKeyBoardTest;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch(v.getId()) {
			case R.id.lock_test:
				intent = new Intent(MainActivity.this,
						LockTestActivity.class);
				startActivity(intent);
				break;
			case R.id.hf_test:
				intent = new Intent(MainActivity.this,
						RifIdTestActivity.class);
				startActivity(intent);
				break;
			case R.id.id_reader:
				intent = new Intent(MainActivity.this,
						IdReaderActivity.class);
				startActivity(intent);
				break;
			case R.id.key_board:
				intent = new Intent(MainActivity.this,KeyBoardActivity.class);
				startActivity(intent);
				break;
			default:
				break;
		}
		
	}

	@Override
	protected void findWidgets() {
		// TODO Auto-generated method stub
		mLockTest = (Button)findViewById(R.id.lock_test);
		mHfTest = (Button)findViewById(R.id.hf_test);
		mIDReaderTest = (Button)findViewById(R.id.id_reader);
		mKeyBoardTest = (Button)findViewById(R.id.key_board);

		mLockTest.setOnClickListener(this);
		mHfTest.setOnClickListener(this);
		mIDReaderTest.setOnClickListener(this);
		mKeyBoardTest.setOnClickListener(this);
	}

	@Override
	protected void initComponent() {

	}

	@Override
	protected void getIntentData() {

	}
}
