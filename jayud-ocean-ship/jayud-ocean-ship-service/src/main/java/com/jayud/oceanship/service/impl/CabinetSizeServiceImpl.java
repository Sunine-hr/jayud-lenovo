package com.jayud.oceanship.service.impl;

import com.jayud.common.entity.InitComboxVO;
import com.jayud.oceanship.po.CabinetSize;
import com.jayud.oceanship.mapper.CabinetSizeMapper;
import com.jayud.oceanship.service.ICabinetSizeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 柜型大小表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-02-19
 */
@Service
public class CabinetSizeServiceImpl extends ServiceImpl<CabinetSizeMapper, CabinetSize> implements ICabinetSizeService {

    @Override
    public List<InitComboxVO> initCabinetSize() {
        List<CabinetSize> cabinetSizes = baseMapper.selectList(null);
        List<InitComboxVO> comboxVOS = new ArrayList<>();
        for (CabinetSize cabinetSize : cabinetSizes) {
            InitComboxVO comboxVO = new InitComboxVO();
            comboxVO.setId(cabinetSize.getId());
            comboxVO.setName(cabinetSize.getName());
            comboxVOS.add(comboxVO);
        }
        return comboxVOS;
    }
}
