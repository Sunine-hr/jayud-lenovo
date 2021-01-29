package com.jayud.oceanship.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.oceanship.model.po.SeaPort;
import com.jayud.oceanship.mapper.SeaPortMapper;
import com.jayud.oceanship.service.ISeaPortService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 船港口地址表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-01-29
 */
@Service
public class SeaPortServiceImpl extends ServiceImpl<SeaPortMapper, SeaPort> implements ISeaPortService {

    @Override
    public List<InitComboxStrVO> initSeaPort() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status",1);
        List<SeaPort> list = baseMapper.selectList(queryWrapper);
        List<InitComboxStrVO> initComboxStrVOS = new ArrayList<>();
        for (SeaPort seaPort : list) {
            InitComboxStrVO initComboxStrVO = new InitComboxStrVO();
            initComboxStrVO.setCode(seaPort.getCode());
            initComboxStrVO.setName(seaPort.getName());
            initComboxStrVOS.add(initComboxStrVO);
        }
        return initComboxStrVOS;
    }
}
