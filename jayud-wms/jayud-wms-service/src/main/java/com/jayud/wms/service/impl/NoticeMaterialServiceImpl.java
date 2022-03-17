package com.jayud.wms.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.po.NoticeMaterial;
import com.jayud.wms.mapper.NoticeMaterialMapper;
import com.jayud.wms.service.INoticeMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 通知单物料信息 服务实现类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Service
public class NoticeMaterialServiceImpl extends ServiceImpl<NoticeMaterialMapper, NoticeMaterial> implements INoticeMaterialService {


    @Autowired
    private NoticeMaterialMapper noticeMaterialMapper;

    @Override
    public IPage<NoticeMaterial> selectPage(NoticeMaterial noticeMaterial,
                                            Integer pageNo,
                                            Integer pageSize,
                                            HttpServletRequest req) {

        Page<NoticeMaterial> page = new Page<NoticeMaterial>(pageNo, pageSize);
        IPage<NoticeMaterial> pageList = noticeMaterialMapper.pageList(page, noticeMaterial);
        return pageList;
    }

    @Override
    public List<NoticeMaterial> selectList(NoticeMaterial noticeMaterial) {
        return noticeMaterialMapper.list(noticeMaterial);
    }

    @Override
    public List<NoticeMaterial> getByCondition(NoticeMaterial condition) {
        return this.baseMapper.selectList(new QueryWrapper<>(condition));
    }

}
