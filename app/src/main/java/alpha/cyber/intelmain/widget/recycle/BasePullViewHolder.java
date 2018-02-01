package alpha.cyber.intelmain.widget.recycle;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * ViewHolder封装
 */
public abstract class BasePullViewHolder extends RecyclerView.ViewHolder {
    public BasePullViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v, getAdapterPosition());
            }
        });
    }

    public abstract void onBindViewHolder(int position);
    public abstract void onItemClick(View view, int position);
}
