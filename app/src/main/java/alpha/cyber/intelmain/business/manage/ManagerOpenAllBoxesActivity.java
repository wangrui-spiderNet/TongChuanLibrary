package alpha.cyber.intelmain.business.manage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.bean.BoxBean;
import alpha.cyber.intelmain.util.Log;

/**
 * Created by wangrui on 2018/2/2.
 */

public class ManagerOpenAllBoxesActivity extends BaseActivity  {

    private TextView tvBoxNum;
    private GridView gvBoxes;
    private List<BoxBean> boxBeans;
    private ManagerBoxesAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_open_all_boxes);
    }

    @Override
    protected void findWidgets() {
        tvBoxNum=findView(R.id.tv_box_name);
        gvBoxes =findView(R.id.gv_boxes);

    }

    @Override
    protected void initComponent() {
        boxBeans = new ArrayList<BoxBean>();
        for (int i = 0; i < 10; i++) {
            BoxBean boxBean = new BoxBean();
            if (i != 9) {
                boxBean.setName("0" + (i + 1));
            } else {
                boxBean.setName("" + (i + 1));
            }

            Log.e(Constant.TAG,"数字取余数:"+(i%2==0));
            if(i%2==0){
                boxBean.setOpen(0);
            }else{
                boxBean.setOpen(1);
            }

            boxBeans.add(boxBean);
        }

        mAdapter = new ManagerBoxesAdapter(this, boxBeans);
        mAdapter.notifyDataSetChanged();
        gvBoxes.setAdapter(mAdapter);

    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        btnRightButton.setVisibility(View.VISIBLE);
        btnRightButton.setText(getString(R.string.sure));
    }
}
