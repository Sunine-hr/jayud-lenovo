package com.jayud.mall.service;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.ActionCombinationForm;
import com.jayud.mall.model.bo.ActionCombinationQueryForm;
import com.jayud.mall.model.po.ActionCombination;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.ActionCombinationVO;

import java.util.List;

/**
 * <p>
 * 操作项组合 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-30
 */
public interface IActionCombinationService extends IService<ActionCombination> {

    /**
     * 查询操作项组合list
     * @param form
     * @return
     */
    List<ActionCombinationVO> findActionCombination(ActionCombinationQueryForm form);

    /**
     * saveActionCombination
     * @param form
     * @return
     */
    CommonResult<ActionCombinationVO> saveActionCombination(ActionCombinationForm form);

    /**
     * 查询操作项组合详细接口
     * @param id
     * @return
     */
    CommonResult<ActionCombinationVO> findActionCombinationById(Integer id);

    /**
     * 停用-操作项组合详细接口
     * @param id
     * @return
     */
    CommonResult<ActionCombinationVO> disabledActionCombination(Integer id);
}
