package com.atguigu.crowd.mapper;

import com.atguigu.crowd.entity.po.OrderProjectPO;
import com.atguigu.crowd.entity.vo.OrderProjectVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (OrderProject)表数据库访问层
 *
 * @author makejava
 * @since 2021-08-22 16:05:30
 */
@Mapper
public interface OrderProjectDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    OrderProjectPO queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<OrderProjectPO> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param orderProjectPO 实例对象
     * @return 对象列表
     */
    List<OrderProjectPO> queryAll(OrderProjectPO orderProjectPO);

    /**
     * 新增数据
     *
     * @param orderProjectPO 实例对象
     * @return 影响行数
     */
    int insert(OrderProjectPO orderProjectPO);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<OrderProject> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<OrderProjectPO> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<OrderProject> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<OrderProjectPO> entities);

    /**
     * 修改数据
     *
     * @param orderProjectPO 实例对象
     * @return 影响行数
     */
    int update(OrderProjectPO orderProjectPO);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    OrderProjectVO selectOrderProjectVO(@Param("returnId") Integer returnId);
}

