package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.BOtherCostItem;
import com.jayud.scm.mapper.BOtherCostItemMapper;
import com.jayud.scm.model.vo.BDataDicVO;
import com.jayud.scm.model.vo.BOtherCostItemVO;
import com.jayud.scm.service.IBOtherCostItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 费用名称表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-16
 */
@Service
public class BOtherCostItemServiceImpl extends ServiceImpl<BOtherCostItemMapper, BOtherCostItem> implements IBOtherCostItemService {

    @Override
    public IPage<BOtherCostItemVO> findByPage(QueryCommonForm form) {
        Page<BOtherCostItemVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page,form);
    }
}
