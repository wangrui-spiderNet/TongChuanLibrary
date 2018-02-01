package alpha.cyber.intelmain.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import alpha.cyber.intelmain.R;
import alpha.cyber.intelmain.widget.recycle.BaseListAdapter;
import alpha.cyber.intelmain.widget.recycle.BasePullViewHolder;
import alpha.cyber.intelmain.widget.recycle.DividerItemDecoration;
import alpha.cyber.intelmain.widget.recycle.PullRecycler;
import alpha.cyber.intelmain.widget.recycle.layoutmanager.ILayoutManager;
import alpha.cyber.intelmain.widget.recycle.layoutmanager.MyLinearLayoutManager;

/**
 * 封装的recycle ,采用MVP模式 基本fragment页面
 */
public abstract class BaseListFragment<W> extends BaseFragment implements PullRecycler.OnRecyclerRefreshListener {
    public  ListAdapter adapter;
    public ArrayList<W> mDataList;
    protected PullRecycler recycler;//子类完成定义

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void findWidgets() {
        recycler = getRecycler();
    }

    @Override
    protected void initComponent() {
        setUpData();
    }

    private void setUpAdapter() {
        adapter = new ListAdapter();
    }
    public abstract PullRecycler getRecycler();
    private void setUpData() {
        setUpAdapter();
        recycler.setOnRefreshListener(this);
        recycler.setLayoutManager(getLayoutManager());
        recycler.addItemDecoration(getItemDecoration()); // 分割线
        recycler.setAdapter(adapter);
    }

    /**
     * 删除一个item
     *
     * @param positon
     */
    public void removeItem(int positon) {
        /**
         * 添加了头布局,集合索引位置position-1
         */
        mDataList.remove(positon - 1);
        Log.e("BaseListFragment", "mDataList移除了索引position:" + (positon - 1));
        adapter.notifyItemRemoved(positon);
        adapter.notifyItemRangeChanged(positon, adapter.getItemCount());//??

    }

    protected ILayoutManager getLayoutManager() {
        return new MyLinearLayoutManager(getActivity());
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(getActivity(), R.drawable.list_divider);
    }

    public class ListAdapter extends BaseListAdapter {

        @Override
        protected BasePullViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
            return getViewHolder(parent, viewType);
        }

        @Override
        protected int getDataCount() {
            return mDataList != null ? mDataList.size() : 0;
        }

        @Override
        protected int getDataViewType(int position) {
            return getItemType(position);
        }

        @Override
        public boolean isSectionHeader(int position) {
            return BaseListFragment.this.isSectionHeader(position);
        }


    }

    /**
     * 加载更多数据
     */
    public void pullLoadCommon(List<W> datas) {
        mDataList.addAll(datas);
        adapter.notifyDataSetChanged();
    }

    /**
     * 刷新数据添加
     */
    public void pullRefreshCommon(List<W> datas) {
        mDataList.clear();
        mDataList.addAll(datas);
        adapter.notifyDataSetChanged();
    }

    /**
     * 清除数据
     */
    public void clearData() {
        mDataList.clear();
        adapter.notifyDataSetChanged();
    }

    protected boolean isSectionHeader(int position) {
        return false;
    }


    protected int getItemType(int position) {
        return 0;
    }

    protected abstract BasePullViewHolder getViewHolder(ViewGroup parent, int viewType);

}
