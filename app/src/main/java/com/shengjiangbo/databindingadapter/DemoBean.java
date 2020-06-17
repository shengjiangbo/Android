package com.shengjiangbo.databindingadapter;

import androidx.databinding.Bindable;
import com.shengjiangbo.databingdingadapter.BaseDataBindingBean;

/**
 * Created by 品智.
 * User: 波
 * Date: 2020/6/16
 * Time: 11:06
 */
public class DemoBean extends BaseDataBindingBean {

    private int type = 0;

    public void setType(int type) {
        this.type = type;
    }

    @Bindable
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
        notifyPropertyChanged(BR.msg);
    }

    private String msg;

    @Override
    public int getItemType() {
        return type;
    }
}
