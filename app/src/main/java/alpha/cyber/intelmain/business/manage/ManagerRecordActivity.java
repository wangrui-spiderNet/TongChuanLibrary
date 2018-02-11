package alpha.cyber.intelmain.business.manage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.bean.YearRecordBean;

/**
 * Created by wangrui on 2018/2/5.
 */

public class ManagerRecordActivity extends BaseActivity implements View.OnClickListener{

    private Button btnPrePage,btnNextPage;
    private TextView tvYear;
//    private LinearLayout layout_record_container;
    private ListView listView;
    private View headerView;

    private ManagerRecordAdapter mAdapter;

    private List<YearRecordBean> recordBeans;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_record);
    }

    @Override
    protected void findWidgets() {

        btnNextPage = findView(R.id.btn_next_page);
        btnPrePage = findView(R.id.btn_pre_page);
        tvYear = findView(R.id.tv_year);
        listView = findView(R.id.listView_record);
        headerView = LayoutInflater.from(this).inflate(R.layout.item_listview_headview,null);

        listView.addHeaderView(headerView);
        btnPrePage.setOnClickListener(this);
        btnNextPage.setOnClickListener(this);
    }

    @Override
    protected void initComponent() {
        recordBeans=new ArrayList<YearRecordBean>();

        for(int i=0;i<50;i++){
            YearRecordBean recordBean=new YearRecordBean();
            recordBean.setAddCount(21+i);
            recordBean.setOutCount(90+i);
            recordBean.setSum(100+i);
            recordBean.setTime(20170801+i);

            recordBeans.add(recordBean);
        }

        mAdapter = new ManagerRecordAdapter(this,recordBeans);
        listView.setAdapter(mAdapter);


    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        btnRightButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        if(btnNextPage==v){

        }else if(btnPrePage == v){

        }
    }


}
