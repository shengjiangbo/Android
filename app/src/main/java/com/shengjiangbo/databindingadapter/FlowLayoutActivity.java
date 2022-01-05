package com.shengjiangbo.databindingadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sheng.flowlayout.FlowLayout;
import com.sheng.mvvm.BaseBindVMActivity;
import com.sheng.mvvm.BaseViewModel;
import com.shengjiangbo.databindingadapter.databinding.ActivityFlowLayoutBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建人：Bobo
 * 创建时间：2021/6/23 11:53
 * 类描述：
 */
public class FlowLayoutActivity extends BaseBindVMActivity<BaseViewModel, ActivityFlowLayoutBinding> {

    private List<String> mList = new ArrayList<>();

    @Override
    protected void initView() {
        for (int i = 0; i < 23; i++) {
            mList.add("item" + i);
        }
        binding.flowLayout.setListData(mList, new FlowLayout.OnItemViewListener<String>() {
            @Override
            public View getView(String itemView) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.flow_item, binding.flowLayout, false);
                TextView text = view.findViewById(R.id.tv_text);
                text.setText(itemView);
                return view;
            }

            @Override
            public void onItemClick(View view, String itemView) {
                Toast.makeText(mContext, itemView, Toast.LENGTH_SHORT).show();
            }
        });

        binding.flowLayout.setHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.dp_10));
        binding.flowLayout.setVerticalSpacing(getResources().getDimensionPixelSize(R.dimen.dp_10));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_flow_layout;
    }
}
