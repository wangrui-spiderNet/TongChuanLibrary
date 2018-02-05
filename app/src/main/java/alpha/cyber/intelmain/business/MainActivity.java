package alpha.cyber.intelmain.business;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.widget.MyTableView;

public class MainActivity extends BaseActivity implements OnClickListener {

	private Button mLockTest;
	private Button mHfTest;
	private Button mIDReaderTest;
	private Button mKeyBoardTest;
	private LinearLayout layout_container;

	
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

		layout_container = findView(R.id.layout_container);

		mLockTest.setOnClickListener(this);
		mHfTest.setOnClickListener(this);
		mIDReaderTest.setOnClickListener(this);
		mKeyBoardTest.setOnClickListener(this);
	}

	@Override
	protected void initComponent() {

		MyTableView stv1=new MyTableView(this,4);
		stv1.AddRow(new String[]{"已借图书"},true);
		stv1.AddRow(new String[]{"书名","借阅时间","到期归还","逾期天数"},false);
		stv1.AddRow(new Object[]{"笑傲江湖","2018-1-23","2019-01-24","5"},false);
		stv1.AddRow(new Object[]{"射雕英雄传","2018-1-23","2019-01-24","5"},false);
		stv1.AddRow(new Object[]{"倚天屠龙记","2018-1-23","2019-01-24","5"},false);
		stv1.AddRow(new Object[]{"天龙八部","2018-1-23","2019-01-24","5"},false);
		stv1.AddRow(new Object[]{"神雕侠侣","2018-1-23","2019-01-24","5"},false);
		stv1.AddRow(new Object[]{"碧血剑","2018-1-23","2019-01-24","5"},false);

		layout_container.addView(stv1);

	}

	@Override
	protected void getIntentData() {

	}
}
