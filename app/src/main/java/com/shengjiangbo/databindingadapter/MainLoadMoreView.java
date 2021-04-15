package com.shengjiangbo.databindingadapter;

import com.shengjiangbo.databingdingadapter.LoadMoreView;

/**
 * Created by 品智.
 * User: 波
 * Date: 2020/6/16
 * Time: 11:40
 */
public class MainLoadMoreView extends LoadMoreView {

    /**
     *
     * @return 布局id
     */
    @Override
    public int getLayoutId() {
        return R.layout.load_more_view;
    }

    /**
     *
     * @return BR
     */
    @Override
    public int getBindVariableId() {
        return BR.data;
    }

    @Override
    public int getBindNoDataId() {
        return 0;
    }

    /**
     *
     * @return 加载中的布局控件 id
     */
    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    /**
     *
     * @return 加载失败的布局控件 id
     */
    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }
    /**
     *
     * @return 没有更多的布局控件 id
     */
    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
