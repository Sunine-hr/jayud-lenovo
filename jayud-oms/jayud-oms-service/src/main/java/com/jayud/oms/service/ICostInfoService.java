package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oms.model.bo.AddCostInfoForm;
import com.jayud.oms.model.bo.QueryContractInfoForm;
import com.jayud.oms.model.bo.QueryCostInfoForm;
import com.jayud.oms.model.po.CostInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.vo.ContractInfoVO;
import com.jayud.oms.model.vo.CostInfoVO;
import com.jayud.oms.model.vo.CostTypeVO;

import java.util.List;

/**
 * <p>
 * 费用名描述 服务类
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-27
 */
public interface ICostInfoService extends IService<CostInfo> {

    /**
     * 获取费用类型
     * @return
     */
    List<CostInfo> findCostInfo();

    /**
     * 列表分页查询
     *
     * @param form
     * @return
     */
    IPage<CostInfoVO> findCostInfoByPage(QueryCostInfoForm form);

    /**
     * 新增编辑费用名称
     *
     * @param form
     * @return
     */
    boolean saveOrUpdateCostInfo(AddCostInfoForm form);

    /**
     * 根据id查询费用名称
     */
    CostInfoVO getById(Long id);

    /**
     * 更新为无效状态
     *
     * @param ids
     * @return
     */
    boolean deleteByIds(List<Long> ids);
}
