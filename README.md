# mogo-parent

#### 1、介绍
Mogo使用简单易懂的lambda操作mongoDb中的集合（Mogo名字乱敲的）

mongoDb对于一般的常用软件来说应该都不陌生了，相信使用过MP(mybatisPlus)的小伙伴都知道lambda形式的操作是多么的爽了，但是mongoTemplate用起来确实很令人脑壳疼，所以脑袋一热就参考了mp做了一个类似的封装，让我自己远离恶心的Criteria，周末肝出来的第一版，比较简陋，但是CRUD都可以正常使用，提供思想，希望有大佬带飞，期待有个好用的Mongo ORM


#### 2、使用说明
初期项目主要有三个模块：
1.  mogo（基础的封装）
2.  mogo-gen (提供的简单版代码生成器)
3.  mogo-web (暴露出去的http测试模块)

写法对比
![和mongoTemplate的写法对比](https://foruda.gitee.com/images/1675599372979246902/a1822f4c_5198545.png "屏幕截图")
![符合mp的lambda写法](https://foruda.gitee.com/images/1675599398243206516/5e4899a5_5198545.png "屏幕截图")


#### 核心接口


```java
package com.loser.core.sdk;

import com.loser.core.entity.Page;
import com.loser.core.wrapper.LambdaQueryWrapper;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * mongo 3、基础方法接口
 *
 * @author loser
 * @date 2023-02-04  18:52
 */
public interface MogoService<T> {

    /**
     * 查询单条数据
     *
     * @param queryWrapper 条件构造器
     * @return 查询到的集合数据
     */
    T getOne(LambdaQueryWrapper<T> queryWrapper);

    /**
     * 保存新的数据
     *
     * @param entity 需要保存的实体
     * @return 是否保存成功
     */
    boolean save(T entity);

    /**
     * 批量保存新的数据 内部递归调用单个保存
     *
     * @param entityList 需要保存的数据列表
     * @return 是否保存成功
     */
    boolean saveBatch(Collection<T> entityList);

    /**
     * 通过ID删除数据
     *
     * @param id 数据ID
     * @return 是否删除成功
     */
    boolean removeById(Serializable id);

    /**
     * 通过条件构建器删除数据
     *
     * @param queryWrapper 条件构建器
     * @return 是否删除成功
     */
    boolean remove(LambdaQueryWrapper<T> queryWrapper);

    /**
     * 通过ID更新数据 只有存在数据的字段才会更新
     *
     * @param entity 需要更新的数据
     * @return 是否更新成功
     */
    boolean updateById(T entity);

    /**
     * 通过条件构造器更新数据 只有存在数据的字段才会更新
     *
     * @param entity       需要更新的数据
     * @param queryWrapper 条件构建起
     * @return 是否更新成功
     */
    boolean update(T entity, LambdaQueryWrapper<T> queryWrapper);

    /**
     * 通过ID获取数据
     *
     * @param id 数据ID
     * @return 集合中的数据
     */
    T getById(Serializable id);

    /**
     * 通过数据ID集合获取数据集合
     *
     * @param idList 数据ID集合
     * @return 查询到的数据集合
     */
    Collection<T> listByIds(Collection<? extends Serializable> idList);

    /**
     * 通过条件构建起统计数据量
     *
     * @param queryWrapper 条件构建起
     * @return 数据两
     */
    long count(LambdaQueryWrapper<T> queryWrapper);

    /**
     * 通过条件构建起查询列表
     *
     * @param queryWrapper 条件构建器
     * @return 数据集合
     */
    List<T> list(LambdaQueryWrapper<T> queryWrapper);

    /**
     * 通过条件构建起进行分页查询
     *
     * @param queryWrapper 条件构建器
     * @param pageNo       页面
     * @param pageSize     页面大小
     * @return 分页对象
     */
    Page<T> page(LambdaQueryWrapper<T> queryWrapper, int pageNo, int pageSize);

    /**
     * 通过条件构建器判断是否存在数据
     *
     * @param queryWrapper 条件构建器
     * @return 是否存在数据
     */
    boolean exist(LambdaQueryWrapper<T> queryWrapper);

}

```

