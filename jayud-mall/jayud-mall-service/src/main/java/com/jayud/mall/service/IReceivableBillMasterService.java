package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryReceivableBillMasterForm;
import com.jayud.mall.model.bo.ReceivableBillForm;
import com.jayud.mall.model.bo.ReceivableBillMasterForm;
import com.jayud.mall.model.po.ReceivableBillMaster;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.ReceivableBillExcelMasterVO;
import com.jayud.mall.model.vo.ReceivableBillMasterVO;

import java.util.List;

/**
 * <p>
 * 应收账单主单 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-01
 */
public interface IReceivableBillMasterService extends IService<ReceivableBillMaster> {

    /**
     * 生成应收账单
     * @param form
     * @return
     */
    CommonResult<ReceivableBillMasterVO> createReceivableBill(ReceivableBillForm form);

    /**
     * 生成应收账单-确认
     * @param form
     * @return
     */
    CommonResult<ReceivableBillMasterVO> affirmReceivableBill(ReceivableBillMasterForm form);

    /**
     * 应收账单分页查询
     * @param form
     * @return
     */
    IPage<ReceivableBillMasterVO> findReceivableBillMasterByPage(QueryReceivableBillMasterForm form);

    /**
     * 应收账单-查看明细
     * @param id
     * @return
     */
    CommonResult<ReceivableBillMasterVO> lookDetail(Long id);

    /**
     * 导出多个账单
     * @param customerId 客户id
     * @param ids 账单ids
     * @return
     */
    ReceivableBillExcelMasterVO downloadBills(Integer customerId, List<Long> ids);
}
