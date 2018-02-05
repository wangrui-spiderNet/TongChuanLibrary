package alpha.cyber.intelmain.business.manage;

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
 * Created by wangrui on 2018/2/2.
 */

public class ManagerBoxesAdapter extends BaseAdapter {
    private Context mContext;
    private List<BoxBean> boxBeans;

    public ManagerBoxesAdapter (Context context,List<BoxBean> boxBeans){
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
            convertView= LayoutInflater.from(mContext).inflate(R.layout.manager_item_box,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder =(ViewHolder) convertView.getTag();
        }



        if(boxBeans.get(position).getOpen()==0){

            holder.tvBoxName.setBackgroundResource(R.drawable.rect_gray_bg_shape);
        }else {
            holder.tvBoxName.setBackgroundResource(R.drawable.rect_red_bg_shape);
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
