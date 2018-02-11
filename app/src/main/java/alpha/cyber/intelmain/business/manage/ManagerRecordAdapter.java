package alpha.cyber.intelmain.business.manage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.bean.YearRecordBean;

/**
 * Created by wangrui on 2018/2/11.
 */

public class ManagerRecordAdapter extends BaseAdapter {

    private Context context;
    private List<YearRecordBean> recordBeans;

    public ManagerRecordAdapter(Context context, List<YearRecordBean> recordBeans){
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
