package alpha.cyber.intelmain.business.borrowbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.base.BaseActivity;
import alpha.cyber.intelmain.bean.BoxBean;
import alpha.cyber.intelmain.util.IntentUtils;

/**
 * Created by wangrui on 2018/2/1.
 */

public class BorrowBookActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    private GridView gvBoxes;
    private BoxesAdapter mAdapter;
    private List<BoxBean> boxBeans;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_book);
    }

    @Override
    protected void findWidgets() {
        gvBoxes= findView(R.id.gv_boxes);

    }

    @Override
    protected void initComponent() {
        boxBeans = new ArrayList<BoxBean>();
        for(int i=0;i<10;i++){
            BoxBean boxBean=new BoxBean();
            if(i!=9){
                boxBean.setName("0"+(i+1));
            }else{
                boxBean.setName(""+(i+1));
            }

            boxBeans.add(boxBean);
        }

        mAdapter = new BoxesAdapter(this,boxBeans);

        gvBoxes.setAdapter(mAdapter);

        gvBoxes.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        IntentUtils.startAty(this,BorrowDetailActivity.class);
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
