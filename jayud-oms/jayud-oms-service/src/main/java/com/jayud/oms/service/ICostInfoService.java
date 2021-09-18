package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.bo.AddCostInfoForm;
import com.jayud.oms.model.bo.QueryCostInfoForm;
import com.jayud.oms.model.po.CostGenre;
import com.jayud.oms.model.po.CostInfo;
import com.jayud.oms.model.po.CostType;
import com.jayud.oms.model.vo.CostInfoVO;
import com.jayud.oms.model.vo.InitComboxStrVO;
import com.jayud.oms.model.vo.InitComboxVO;

import java.util.List;
import java.util.Map;

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
     *
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
//    CostInfoVO getById(Long id);

    /**
     * 更改启用/禁用状态
     *
     * @param id
     * @return
     */
    boolean enableOrDisableCostInfo(Long id);

    /**
     * 校验唯一性
     *
     * @return
     */
    boolean checkUnique(CostInfo costInfo);

    List<CostInfo> getCostInfoByStatus(String status);

    /**
     * 下拉根据费用类别名称查询费用名称
     *
     * @return
     */
    List<InitComboxStrVO> getCostInfoByCostTypeName(String costTypeName);

    /**
     * 下拉根据费用类别code查询费用名称
     *
     * @return
     */
    List<InitComboxStrVO> getCostInfoByCostTypeCode(String costTypeCde);

    /**
     * 下拉根据费用类别查询费用名称
     *
     * @return
     */
    public List<InitComboxStrVO> getCostInfoByCostType(List<CostType> costTypes);

    /**
     * 根据code获取费用名称
     * @param costCode
     * @return
     */
    String getCostNameByCostCode(String costCode);

    Map<String, List<InitComboxVO>> initCostTypeByCostInfoCode();

}
