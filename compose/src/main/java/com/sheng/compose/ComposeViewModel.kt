package com.sheng.compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy

/**
 * 创建人：Bobo
 * 创建时间：2022/2/10 16:06
 * 类描述：
 */
class ComposeViewModel : ViewModel() {
    private var currentEditPosition by mutableStateOf(-1)

    var todoItems = mutableListOf<TodoItem>()


    fun addItem(item: TodoItem) {
        todoItems.add(item)
    }

    fun removeItem(item: TodoItem) {
        todoItems.remove(item)
    }
}