package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.BOtherCostItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.BOtherCostItemVO;

/**
 * <p>
 * 费用名称表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-16
 */
public interface IBOtherCostItemService extends IService<BOtherCostItem> {

    IPage<BOtherCostItemVO> findByPage(QueryCommonForm form);
}
