package com.jayud.mall.service;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.ActionItemForm;
import com.jayud.mall.model.bo.ActionItemQueryForm;
import com.jayud.mall.model.po.ActionItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.ActionItemVO;

import java.util.List;

/**
 * <p>
 * 操作项 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-30
 */
public interface IActionItemService extends IService<ActionItem> {

    /**
     * 查询操作项list
     * @param form
     * @return
     */
    List<ActionItemVO> findActionItem(ActionItemQueryForm form);

    /**
     * 保存操作项
     * @param form
     * @return
     */
    CommonResult<ActionItemVO> saveActionItem(ActionItemForm form);

    /**
     * 禁用操作项
     * @param id
     * @return
     */
    CommonResult<ActionItemVO> disabledActionItem(Integer id);
}
