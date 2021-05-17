package com.sheng.mvvm

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import kotlin.reflect.KClass

/**
 * 创建人：Bobo
 * 创建时间：2021/5/17 10:45
 * 类描述：
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class Bind(val layoutId: Int = -1, val viewModelId: Int = -1, val viewModel: Array<KClass<BaseViewModel>> = [])