package alpha.cyber.intelmain.business.manage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

    private RecordAdapter mAdapter;

    private List<YearRecordBean> recordBeans;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
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

        mAdapter = new RecordAdapter(this,recordBeans);
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

    class RecordAdapter extends BaseAdapter{

        private Context context;
        private List<YearRecordBean> recordBeans;

        public RecordAdapter (Context context,List<YearRecordBean> recordBeans){
            this.context = context;
            this.recordBeans = recordBeans;

        }

        @Override
        public int getCount() {
            return recordBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return recordBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if(convertView==null){
                convertView= LayoutInflater.from(context).inflate(R.layout.item_listview_table,null);
                holder  = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder =(ViewHolder) convertView.getTag();
            }

            holder.tvAddCount.setText(""+recordBeans.get(position).getAddCount());
            holder.tvTime.setText(""+recordBeans.get(position).getTime());
            holder.tvOutCount.setText(""+recordBeans.get(position).getOutCount());
            holder.tvSum.setText(""+recordBeans.get(position).getSum());

            return convertView;
        }

        class ViewHolder{

            private TextView tvTime;
            private TextView tvOutCount;
            private TextView tvAddCount;
            private TextView tvSum;

            public ViewHolder (View view){
                this.tvTime = (TextView) view.findViewById(R.id.tv_time);
                this.tvOutCount = (TextView) view.findViewById(R.id.tv_out_count);
                this.tvAddCount =(TextView) view.findViewById(R.id.tv_add_count);
                this.tvSum =(TextView) view.findViewById(R.id.tv_sum);

            }
        }

    }


}
