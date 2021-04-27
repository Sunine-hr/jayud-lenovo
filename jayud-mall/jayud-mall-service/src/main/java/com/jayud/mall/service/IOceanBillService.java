package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.BillCostInfoForm;
import com.jayud.mall.model.bo.OceanBillForm;
import com.jayud.mall.model.bo.QueryOceanBillForm;
import com.jayud.mall.model.po.OceanBill;
import com.jayud.mall.model.vo.*;

import java.util.List;

/**
 * <p>
 * 提单表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
public interface IOceanBillService extends IService<OceanBill> {

    /**
     * 分页查询
     * @param form
     * @return
     */
    IPage<OceanBillVO> findOceanBillByPage(QueryOceanBillForm form);

    /**
     * 保存提单信息
     * @param form
     */
    CommonResult<OceanBillVO> saveOceanBill(OceanBillForm form);

    /**
     * 查看提单详情
     * @param id
     * @return
     */
    CommonResult<OceanBillVO> lookOceanBill(Long id);

    /**
     * 提单-录入费用(根据 提单id 查询)
     * 1.查询提单详情
     * 2.查询提单费用明细
     * 3.查询提单对应的订单
     * @param id
     * @return
     */
    CommonResult<OceanBillVO> billLadingCost(Long id);

    /**
     * 保存提单费用信息(录入提单费用保存)
     * @param form
     * @return
     */
    CommonResult<BillCostInfoVO> saveBillCostInfo(BillCostInfoForm form);

    /**
     * 一键均摊提单费用到订单(根据订单计费重,均摊)
     * @param form
     * @return
     */
    CommonResult<OceanBillVO> shareEqually(BillCostInfoForm form);

    /**
     * 提单任务-反馈状态(根据 提单id 查询)
     * @param obId 提单id
     * @return
     */
    CommonResult<OceanBillVO> lookOceanBillTask(Long obId);

    /**
     * 提单任务-反馈状态(点击已完成)
     * @param id
     * @return
     */
    CommonResult<BillTaskRelevanceVO> confirmCompleted(Long id);

    /**
     * 运单任务-订单操作日志（根据订单id查看）
     * @param id
     * @return
     */
    CommonResult<List<BillTaskRelevanceVO>> lookOperateLog(Long id);

    /**
     * 在配载下，新增提单信息
     * @param form
     * @return
     */
    CommonResult<OceanBillVO> saveOceanBillByConf(OceanBillForm form);

    /**
     * 查询提单信息
     * @param id
     * @return
     */
    CommonResult<OceanBillVO> findOceanBillById(Long id);

    /**
     * 配载，提单（4个窗口），查看-清关
     * @param billId 提单id
     * @return
     */
    List<BillClearanceInfoVO> findBillClearanceInfoByBillId(Long billId);

    /**
     * 配载，提单（4个窗口），查看-报关
     * @param billId 提单id
     * @return
     */
    List<BillCustomsInfoVO> findBillCustomsInfoByBillId(Long billId);

    /**
     * 配载，提单（4个窗口），查看-柜子
     * @param obId 提单id
     * @return
     */
    List<OceanCounterVO> findOceanCounterByObId(Long obId);
}
