package alpha.cyber.intelmain.business.borrowbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.bean.BoxBean;

/**
 * Created by wangrui on 2018/2/1.
 */

public class BoxesAdapter extends BaseAdapter{
    private Context mContext;
    private List<BoxBean> boxBeans;

    public BoxesAdapter (Context context,List<BoxBean> boxBeans){
        this.mContext = context;
        this.boxBeans = boxBeans;
    }

    @Override
    public int getCount() {
        return boxBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return boxBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_box,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder =(ViewHolder) convertView.getTag();
        }

        if(position%2==0){
            holder.tvBoxName.setBackgroundResource(R.drawable.box_item_left);
        }else{
            holder.tvBoxName.setBackgroundResource(R.drawable.box_item_right);
        }

        holder.tvBoxName.setText(boxBeans.get(position).getName());

        return convertView;
    }

    class ViewHolder{
        TextView tvBoxName;
        public ViewHolder(View view){
            tvBoxName = (TextView) view.findViewById(R.id.tv_box_name);
        }
    }
}
