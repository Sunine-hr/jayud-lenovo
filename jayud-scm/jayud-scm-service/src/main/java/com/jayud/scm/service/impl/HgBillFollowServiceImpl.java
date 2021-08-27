package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.HgBillFollow;
import com.jayud.scm.mapper.HgBillFollowMapper;
import com.jayud.scm.model.vo.HgBillFollowVO;
import com.jayud.scm.model.vo.HgBillVO;
import com.jayud.scm.model.vo.HubShippingFollowVO;
import com.jayud.scm.service.IHgBillFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 入库单跟踪记录表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-25
 */
@Service
public class HgBillFollowServiceImpl extends ServiceImpl<HgBillFollowMapper, HgBillFollow> implements IHgBillFollowService {

    @Override
    public IPage<HgBillFollowVO> findListByHgBillId(QueryCommonForm form) {
        Page<HgBillFollowVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }
}
