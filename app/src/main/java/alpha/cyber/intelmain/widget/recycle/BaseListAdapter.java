package alpha.cyber.intelmain.widget.recycle;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import alpha.cyber.intelmain.R;


/**
 * adapter封装
 */
public abstract class BaseListAdapter extends RecyclerView.Adapter<BasePullViewHolder> {

    protected static final int VIEW_TYPE_LOAD_MORE_FOOTER = 100;//脚布局
    protected boolean isLoadMoreFooterShown;
    protected static final int TYPE_HEADER = 200;//头布局
    private View mHeaderView;
    protected LoadMoreFooterPullViewHolder viewHolder;


    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }


    @Override
    public BasePullViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOAD_MORE_FOOTER) {//脚布局
            return onCreateLoadMoreFooterViewHolder(parent);
        } else if (mHeaderView != null && viewType == TYPE_HEADER) {//头布局
            return new HeaderViewHolder(mHeaderView);

        }
        return onCreateNormalViewHolder(parent, viewType);//普通条目
    }

    @Override
    public void onBindViewHolder(BasePullViewHolder holder, int position) {
        if (isLoadMoreFooterShown && position == getItemCount() - 1) {
            if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                params.setFullSpan(true);
            }
        }
        holder.onBindViewHolder(position);
    }

    @Override
    public int getItemCount() {
        if (mHeaderView == null) {
            return getDataCount() + (isLoadMoreFooterShown ? 1 : 0);
        } else {
            return getDataCount() + 1 + (isLoadMoreFooterShown ? 1 : 0);
        }
    }


    /**
     * @param position
     * @return 0普通条目; 100脚布局
     */
    @Override
    public int getItemViewType(int position) {
        if (isLoadMoreFooterShown && position == getItemCount() - 1) {
            return VIEW_TYPE_LOAD_MORE_FOOTER;
        } else if (mHeaderView != null && position == 0) {
            return TYPE_HEADER;
        } else {
            return getDataViewType(position);
        }

    }

    protected abstract int getDataCount();

    protected abstract BasePullViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType);

    protected BasePullViewHolder onCreateLoadMoreFooterViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_pull_to_refresh_footer, parent, false);

        viewHolder = new LoadMoreFooterPullViewHolder(view);
        return viewHolder;
//        return new LoadMoreFooterPullViewHolder(view);
    }

    protected int getDataViewType(int position) {
        return 0;
    }

    public void onLoadMoreStateChanged(boolean isShown) {
        this.isLoadMoreFooterShown = isShown;
        if (isShown) {
            notifyItemInserted(getItemCount());//插入脚布局
        } else {
            notifyItemRemoved(getItemCount());//移除脚布局
        }
    }

    public boolean isLoadMoreFooter(int position) {
        if (mHeaderView == null) {
            return isLoadMoreFooterShown && position == getItemCount() - 1;
        } else {
            return isLoadMoreFooterShown && position == getItemCount() - 2;

        }

    }

    public boolean isSectionHeader(int position) {
        return false;
    }


     class LoadMoreFooterPullViewHolder extends BasePullViewHolder {
      protected TextView loadMore_text;

        public LoadMoreFooterPullViewHolder(View view) {
            super(view);
            loadMore_text = (TextView) view.findViewById(R.id.footer);
        }

        @Override
        public void onBindViewHolder(int position) {


        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }

    public class HeaderViewHolder extends BasePullViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {

        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }
}
