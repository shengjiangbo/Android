package com.shengjiangbo.databindingadapter.paging

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.sheng.mvvm.BaseViewModel

/**
 * 创建人：Bobo
 * 创建时间：2021/5/20 16:53
 * 类描述：
 */
class PagingViewModel : BaseViewModel() {
    val title: MutableLiveData<String> = MutableLiveData()
    fun getListData() {
        //获取数据
        title.value = title.value + "刷新了"
    }

}
