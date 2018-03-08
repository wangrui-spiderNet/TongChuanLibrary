package alpha.cyber.intelmain.business.operation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.bean.BookInfoBean;
import alpha.cyber.intelmain.bean.CheckoutListBean;

/**
 * Created by wangrui on 2018/2/11.
 */

public class BorrowBookAdapter extends BaseAdapter {

    private Context context;
    private List<CheckoutListBean> recordBeans;

    public BorrowBookAdapter(Context context, List<CheckoutListBean> recordBeans){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.item_borrow_book_table,null);
            holder  = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }

        holder.tvBookName.setText(recordBeans.get(position).getTitle_identifier());
        holder.tvBorrowDate.setText(recordBeans.get(position).getHold_pickup_date());
        holder.tvEndDate.setText(recordBeans.get(position).getDue_date());
        holder.tvLateBack.setText(recordBeans.get(position).getOverdue_days());

        return convertView;
    }

    class ViewHolder{

        private TextView tvBookName;
        private TextView tvBorrowDate;
        private TextView tvEndDate;
        private TextView tvLateBack;

        public ViewHolder (View view){
            this.tvBookName = (TextView) view.findViewById(R.id.tv_book_name);
            this.tvBorrowDate = (TextView) view.findViewById(R.id.tv_borrow_date);
            this.tvEndDate =(TextView) view.findViewById(R.id.tv_end_date);
            this.tvLateBack =(TextView) view.findViewById(R.id.tv_late_back_days);

        }
    }

}
