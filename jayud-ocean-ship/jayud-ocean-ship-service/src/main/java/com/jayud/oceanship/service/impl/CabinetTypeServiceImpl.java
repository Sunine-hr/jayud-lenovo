package com.jayud.oceanship.service.impl;

import com.jayud.common.entity.InitComboxVO;
import com.jayud.oceanship.vo.CabinetType;
import com.jayud.oceanship.mapper.CabinetTypeMapper;
import com.jayud.oceanship.service.ICabinetTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 柜型表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-02-05
 */
@Service
public class CabinetTypeServiceImpl extends ServiceImpl<CabinetTypeMapper, CabinetType> implements ICabinetTypeService {

    @Override
    public List<InitComboxVO> initCabinetType() {
        List<CabinetType> cabinetTypes = baseMapper.selectList(null);
        List<InitComboxVO> comboxVOS = new ArrayList<>();
        for (CabinetType cabinetType : cabinetTypes) {
            InitComboxVO comboxVO = new InitComboxVO();
            comboxVO.setId(cabinetType.getId());
            comboxVO.setName(cabinetType.getName());
            comboxVOS.add(comboxVO);
        }
        return comboxVOS;
    }
}
