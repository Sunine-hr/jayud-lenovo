package com.jayud.tools.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.tools.mapper.SensitiveCommodityMapper;
import com.jayud.tools.model.bo.QuerySensitiveCommodityForm;
import com.jayud.tools.model.bo.SensitiveCommodityForm;
import com.jayud.tools.model.po.SensitiveCommodity;
import com.jayud.tools.model.vo.SensitiveCommodityVO;
import com.jayud.tools.service.ISensitiveCommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 敏感品名表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-27
 */
@Service
public class SensitiveCommodityServiceImpl extends ServiceImpl<SensitiveCommodityMapper, SensitiveCommodity> implements ISensitiveCommodityService {

    @Autowired
    SensitiveCommodityMapper sensitiveCommodityMapper;

    @Override
    public List<SensitiveCommodity> getSensitiveCommodityList() {
        return sensitiveCommodityMapper.getSensitiveCommodityList();
    }

    @Override
    public void saveSensitiveCommodity(SensitiveCommodityForm sensitiveCommodityForm) {
        SensitiveCommodity sensitiveCommodity = new SensitiveCommodity();
        sensitiveCommodity.setId(sensitiveCommodityForm.getId());
        sensitiveCommodity.setName(sensitiveCommodityForm.getName());
        //hutool 5.4.6 和 4.1.19 版本冲突
//        SensitiveCommodity convert = ConvertUtil.convert(sensitiveCommodity, SensitiveCommodity.class);
        this.saveOrUpdate(sensitiveCommodity);
    }

    @Override
    public void deleteSensitiveCommodityById(Long id) {
        sensitiveCommodityMapper.deleteById(id);
    }

    @Override
    public IPage<SensitiveCommodityVO> findSensitiveCommodityByPage(QuerySensitiveCommodityForm form) {
        //定义分页参数
        Page<SensitiveCommodityVO> page = new Page(form.getPageNum(),form.getPageSize());
        IPage<SensitiveCommodityVO> pageInfo = sensitiveCommodityMapper.findSensitiveCommodityByPage(page, form);
        return pageInfo;
    }
}
