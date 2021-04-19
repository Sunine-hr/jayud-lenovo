package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.model.bo.CabinetTypeForm;
import com.jayud.mall.model.po.CabinetType;
import com.jayud.mall.mapper.CabinetTypeMapper;
import com.jayud.mall.model.vo.CabinetTypeVO;
import com.jayud.mall.service.ICabinetTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 柜型基本信息 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-01-07
 */
@Service
public class CabinetTypeServiceImpl extends ServiceImpl<CabinetTypeMapper, CabinetType> implements ICabinetTypeService {

    @Override
    public List<CabinetTypeVO> findCabinetType(CabinetTypeForm form) {
        QueryWrapper<CabinetType> queryWrapper = new QueryWrapper<>();
        String name = form.getName();
        if(name != null && name != ""){
            queryWrapper.like("name", name);
        }
        String idCode = form.getIdCode();
        if(idCode != null && idCode != ""){
            queryWrapper.like("id_code", idCode);
        }
        List<CabinetType> list = this.list(queryWrapper);
        List<CabinetTypeVO> cabinetTypeVOS = ConvertUtil.convertList(list, CabinetTypeVO.class);
        return cabinetTypeVOS;
    }

}
