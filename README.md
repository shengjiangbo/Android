# DataBindingAdapter
  RecyclerView快速实现数据绑定以及多布局实现
  
  将其添加到存储库末尾的root build.gradle中：
  
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

  添加依赖项
  
  [![](https://jitpack.io/v/shengjiangbo/DataBindingAdapter.svg)](https://jitpack.io/#shengjiangbo/DataBindingAdapter)
  
```
	dependencies {
	        implementation 'com.github.shengjiangbo:DataBindingAdapter:1.1.5'
	}
```

# 实现方式
     直接继承 BaseBindAdapter
     如果要实现item复杂逻辑 请实现:
     
```
    @Override
    protected void convert(BaseBindHolder holder, ViewDataBinding binding, BaseBindBean item) {
        holder.addOnLongClickListener(R.id.img, R.id.msg);
        holder.addOnClickListener(R.id.img, R.id.msg);
        if (binding instanceof ItemBinding) {
            ItemBinding itemBinding = (ItemBinding) binding;
            DemoBean bean = (DemoBean) item;
        }
        if (binding instanceof Item1Binding) {
            Item1Binding item1Binding = (Item1Binding) binding;
            Demo1Bean bean = (Demo1Bean) item;
        }
    }
```
    
    单或多布局实现 直接在继承BaseBindAdapter类的构造方法添加 addItemType(0, R.layout.item, BR.data);
    
```
    public DemoAdapter() {
        //参数1:多布局区分type(数据Bean继承BaseBindBean 实现getItemType,对应以下布局)
        //参数2:布局
        //参数3:DataBinding BR 绑定数据Variable name
	//参数4:指定位置添加布局 但是不能超出 setNewData的数据size 
	//参数5:如果是StaggeredGridLayoutManager or GridLayoutManager 要现实一行的话 设置为true
        addItemType(0, R.layout.item, BR.data);
        addItemType(1, R.layout.item1, BR.data);
	addItemType(2, R.layout.ad_item, BR.data, 0, true);
	addItemType(3, R.layout.theme_item, BR.data, 1);
    }
```

    添加数据方式
    
```
    //直接使用BaseBindBean 添加数据的时候可以直接添加
    private List<BaseBindBean> list = new ArrayList<>();
    Demo1Bean bean1 = new Demo1Bean();
    bean1.setType(1);//type区分
    DemoBean bean = new DemoBean();
    bean.setType(0);
    list.add(bean1);
    list.add(bean);
    mAdapter.setNewData(list);
    mAdapter.addData(list);
    mAdapter.addData(index,list);
    mAdapter.addData(index,bean);
```

    设置上拉加载更多(注意一定一定要自己继承LoadMoreView自定义布局)
    
```
     mAdapter.setLoadMoreView(new MainLoadMoreView());//MainLoadMoreView 具体实现请查看demo
     mAdapter.setOnLoadMoreListener(this, binding.recyclerView);
     @Override
     public void onLoadMoreRequested() {
     //...获取数据
     }
```
