package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.CheckOrderFollow;
import com.jayud.scm.mapper.CheckOrderFollowMapper;
import com.jayud.scm.model.vo.CheckOrderFollowVO;
import com.jayud.scm.model.vo.CommodityFollowVO;
import com.jayud.scm.service.ICheckOrderFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 提验货单跟踪记录表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@Service
public class CheckOrderFollowServiceImpl extends ServiceImpl<CheckOrderFollowMapper, CheckOrderFollow> implements ICheckOrderFollowService {

    @Override
    public IPage<CheckOrderFollowVO> findListByCheckOrderId(QueryCommonForm form) {
        Page<CheckOrderFollowVO> page = new Page<>(form.getPageNum(),form.getPageSize() );
        return this.baseMapper.findListByCheckOrderId(form,page);
    }
}
