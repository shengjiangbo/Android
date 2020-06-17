# DataBindingAdapter
RecyclerView Adapter适配器


```
 mQuickAdapter = QuickBindingAdapter.Create()
                .bindingItem(0, R.layout.item, BR.data)//添加布局
                .bindingItem(1, R.layout.item1, BR.data)//添加第二个布局布局
                .setLoadMoreView(new MainLoadMoreView())//设置上拉加载更多布局  继承 LoadMoreView
                .setOnLoadMoreListener(this, binding.recyclerView)//上拉加载更多监听
                .addOnClickListener(R.id.msg, R.id.img)//设置控件的点击事件
                .addOnLongClickListener(R.id.msg, R.id.img)//设置控件长按事件
                .setOnQuickConvertListener(new QuickBindingAdapter.OnQuickConvertListener() {//用于自定义更多功能
                    @Override
                    public void convert(BaseViewHolder holder, ViewDataBinding binding, BaseDataBindingBean item) {
                        int type = holder.getItemViewType();
                        Log.d("", "convert: " + type);
                        if (binding instanceof ItemBinding) {
                            DemoBean bean = (DemoBean) item;
                            ItemBinding itemBinding = (ItemBinding) binding;
                            TextView msg = itemBinding.msg;
                            Log.e("", "onItemChildClick: position:" + holder.getLayoutPosition() + "type:" + bean.getItemType());
                        } else if (binding instanceof Item1Binding) {
                            Demo1Bean bean = (Demo1Bean) item;
                            Item1Binding itemBinding = (Item1Binding) binding;
                            ImageView img = itemBinding.img;
                            Log.e("", "onItemChildClick: position:" + holder.getLayoutPosition() + "type:" + bean.getItemType());
                        }
                    }
                })
                .setOnItemChildClickListener(new QuickBindingAdapter.OnItemChildClickListener() {//实现item子控件点击监听
                    @Override
                    public void onItemChildClick(QuickBindingAdapter adapter, ViewDataBinding binding, View view, int position) {
                        if(view.getId() == R.id.img){

                        }else if(view.getId() == R.id.msg){

                        }
                        //...
                        if (binding instanceof ItemBinding) {
                            DemoBean bean = (DemoBean) adapter.getData().get(position);
                            Log.e("", "onItemChildClick: " + position + "type:" + bean.getItemType());
                        } else if (binding instanceof Item1Binding) {
                            Demo1Bean bean = (Demo1Bean) adapter.getData().get(position);
                            Log.e("", "onItemChildClick: " + position + "type:" + bean.getItemType());
                        }
                    }
                }).setOnItemClickListener(new QuickBindingAdapter.OnItemClickListener() {//实现item控件点击监听
                    @Override
                    public void onItemClick(ViewDataBinding binding, View v, int position) {
                        Log.e("", "onItemClick: " + position);
                    }
                }).setOnItemLongClickListener(new QuickBindingAdapter.OnItemLongClickListener() {//实现item控件长按监听
                    @Override
                    public boolean onItemLongClick(ViewDataBinding binding, View v, int position) {
                        Log.e("", "onItemLongClick: " + position);
                        return true;
                    }
                }).setOnItemChildLongClickListener(new QuickBindingAdapter.OnItemChildLongClickListener() {//实现item子控件长按监听
                    @Override
                    public boolean onItemChildLongClick(QuickBindingAdapter adapter, ViewDataBinding binding, View view, int position) {
                        Log.e("", "onItemLongClick: " + position);
                        return true;
                    }
                });

        binding.recyclerView.setAdapter(mQuickAdapter);
        getData();
        mQuickAdapter.setNewData(list);//设置新数据

```
