package alpha.cyber.intelmain.business.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;

/**
 * Created by wangrui on 2018/2/2.
 */

public class SearchActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
    }

    @Override
    protected void findWidgets() {

    }

    @Override
    protected void initComponent() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        btnRightButton.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void getIntentData() {

    }
}
