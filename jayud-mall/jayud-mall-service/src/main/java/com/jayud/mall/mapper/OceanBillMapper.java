package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.ConfCaseForm;
import com.jayud.mall.model.bo.QueryOceanBillForm;
import com.jayud.mall.model.po.OceanBill;
import com.jayud.mall.model.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 提单表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Mapper
@Component
public interface OceanBillMapper extends BaseMapper<OceanBill> {

    /**
     * 分页
     * @param page
     * @param form
     * @return
     */
    IPage<OceanBillVO> findOceanBillByPage(Page<OceanBillVO> page, @Param("form") QueryOceanBillForm form);

    /**
     * 提单-录入费用(根据 提单id 查询)
     * @param id
     * @return
     */
    OceanBillVO billLadingCost(@Param("id") Long id);

    /**
     * 提单对应的订单 以及 费用信息
     * @param id
     * @return
     */
    List<BillOrderCostInfoVO> findBillOrderCostInfo(@Param("id") Long id);

    /**
     * 根据提单id，查询提单
     * @param obId 提单id
     * @return
     */
    OceanBillVO findOceanBillById(@Param("obId") Long obId);

    /**
     * 根据提单id，查询提单关联的任务，查看完成情况
     * @param obId 提单id
     * @return
     */
    List<BillTaskRelevanceVO> findBillTaskRelevanceByObId(@Param("obId") Long obId);

    /**
     * 根据提单id，查询提单操作日志
     * @param id
     * @return
     */
    List<BillTaskRelevanceVO> lookOperateLog(@Param("id") Long id);

    /**
     * 配载，提单（4个窗口），查看-清关
     * @param billId
     * @return
     */
    List<BillClearanceInfoVO> findBillClearanceInfoByBillId(@Param("billId") Long billId);

    /**
     * 配载，提单（4个窗口），查看-报关
     * @param billId
     * @return
     */
    List<BillCustomsInfoVO> findBillCustomsInfoByBillId(@Param("billId") Long billId);

    /**
     * 配载，提单（4个窗口），查看-柜子
     * @param obId
     * @return
     */
    List<OceanCounterVO> findOceanCounterByObId(@Param("obId") Long obId);

    /**
     * 提单，选择配载的箱子(配载，报价，订单，箱号 -> 展示 箱号) 分页
     * @param page
     * @param form
     * @return
     */
    IPage<ConfCaseVO> findConfCaseByPage(Page<ConfCaseVO> page, @Param("form") ConfCaseForm form);
}
