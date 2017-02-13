package com.urv.adapter;

import java.util.List;

/**
 * @author gzejia 978862664@qq.com
 */
public interface RvDataInterface<T> {

    /**
     * 添加数据
     *
     * @param object
     */
    void add(T object);

    /**
     * 添加数据
     *
     * @param position
     * @param object
     */
    void add(int position, T object);

    /**
     * 添加数组数据
     *
     * @param list
     */
    void add(List<T> list);

    /**
     * 添加数组数据
     *
     * @param position
     * @param list
     */
    void add(int position, List<T> list);

    /**
     * 清空数组数据
     */
    void removeAll();

    /**
     * 移除数组数据
     *
     * @param position
     */
    void remove(int position);

    /**
     * 移除数组数据
     *
     * @param object
     */
    void remove(T object);

    /**
     * 更新单个数据
     *
     * @param position
     * @param object
     */
    void update(int position, T object);

    /**
     * 更新数组数据
     *
     * @param list
     */
    void updateAll(List<T> list);

    /**
     * 更新数组数据
     *
     * @param pageIndex 加载页面页码
     * @param list
     */
    void updateForPage(int pageIndex, List<T> list);

    /**
     * 获取数据对象
     *
     * @param position
     * @return
     */
    T get(int position);

    /**
     * 获取数组数据
     *
     * @return
     */
    List<T> getAll();

    /**
     * 当前数组是否为空
     *
     * @return
     */
    boolean isEmpty();
}
