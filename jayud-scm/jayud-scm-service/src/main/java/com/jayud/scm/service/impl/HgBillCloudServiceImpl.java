package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.HgBillCloud;
import com.jayud.scm.mapper.HgBillCloudMapper;
import com.jayud.scm.model.vo.CustomerTaxVO;
import com.jayud.scm.model.vo.HgBillCloudVO;
import com.jayud.scm.service.IHgBillCloudService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 报关单一对接跟踪记录表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-26
 */
@Service
public class HgBillCloudServiceImpl extends ServiceImpl<HgBillCloudMapper, HgBillCloud> implements IHgBillCloudService {

    @Override
    public IPage<HgBillCloudVO> findByPage(QueryCommonForm form) {
        Page<HgBillCloudVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }
}
