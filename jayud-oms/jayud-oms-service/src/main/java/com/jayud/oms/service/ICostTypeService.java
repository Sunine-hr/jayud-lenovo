package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.bo.AddCostTypeForm;
import com.jayud.oms.model.bo.QueryCostTypeForm;
import com.jayud.oms.model.po.CostGenre;
import com.jayud.oms.model.po.CostType;
import com.jayud.oms.model.vo.CostTypeVO;

import java.util.List;

/**
 * <p>
 * 费用类型 服务类
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-27
 */
public interface ICostTypeService extends IService<CostType> {


    /**
     * 列表分页查询
     *
     * @param form
     * @return
     */
    IPage<CostTypeVO> findCostTypeByPage(QueryCostTypeForm form);

    /**
     * 根据id集合查询费用类别
     */
    List<CostTypeVO> getCostTypeByIds(List<Long> ids);

    /**
     * 新增编辑费用类别
     *
     * @param form
     * @return
     */
    boolean saveOrUpdateCostType(AddCostTypeForm form);

    /**
     * 根据id查询费用类别
     */
    CostTypeVO getById(Long id);

    /**
     * 更改启用/禁用状态
     * @param id
     * @return
     */
    boolean enableOrDisableCostType(Long id);

    /**
     * 查询所有启用费用类别
     * @return
     */
    List<CostType> getEnableCostType();

    /**
     * 校验唯一性
     * @return
     */
    boolean checkUnique(CostType costType);

    List<CostType> getByCondition(CostType setCodeName);
}
