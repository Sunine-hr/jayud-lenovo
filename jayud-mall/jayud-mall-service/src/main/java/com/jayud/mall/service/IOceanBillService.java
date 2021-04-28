package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.*;
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

    /**
     * 添加修改-提单清关信息
     * @param form
     * @return
     */
    BillClearanceInfoVO saveBillClearanceInfo(BillClearanceInfoForm form);

    /**
     * 添加修改-提单报关信息
     * @param form
     * @return
     */
    BillCustomsInfoVO saveBillCustomsInfo(BillCustomsInfoForm form);

    /**
     * 添加修改-提单柜子信息
     * @param form
     * @return
     */
    OceanCounterVO saveOceanCounter(OceanCounterForm form);

    /**
     * 删除-提单清关信息
     * @param form
     */
    void delBillClearanceInfo(BillClearanceInfoIdForm form);

    /**
     * 删除-提单报关信息
     * @param form
     */
    void delBillCustomsInfo(BillCustomsInfoIdForm form);

    /**
     * 删除-提单柜子信息
     * @param form
     */
    void delOceanCounter(OceanCounterIdForm form);

    /**
     * 提单，选择配载的箱子(配载，报价，订单，箱号 -> 展示 箱号) 分页
     * @param form
     * @return
     */
    IPage<ConfCaseVO> findConfCaseByPage(ConfCaseForm form);

    /**
     * 查询-(提单)清关信息表
     * @param id
     * @return
     */
    BillClearanceInfoVO findBillClearanceInfoById(Long id);

    /**
     * 查询-(提单)报关信息表
     * @param id
     * @return
     */
    BillCustomsInfoVO findBillCustomsInfoById(Long id);

    /**
     * 添加修改-柜子清单
     * @param form
     * @return
     */
    CounterListInfoVO saveCounterListInfo(CounterListInfoForm form);

    /**
     * 删除-柜子清单
     * @param form
     */
    void delCounterListInfo(CounterListInfoIdForm form);

    /**
     * 查询-柜子清单
     * @param id
     * @return
     */
    CounterListInfoVO findCounterListInfoById(Long id);

    /**
     * 查询柜子下的清单文件列表
     * @param counterId
     * @return
     */
    List<CounterListInfoVO> findCounterListInfoByCounterId(Long counterId);
}
