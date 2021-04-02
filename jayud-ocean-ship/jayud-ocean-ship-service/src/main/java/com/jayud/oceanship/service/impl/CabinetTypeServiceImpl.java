package com.jayud.oceanship.service.impl;

import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.oceanship.po.CabinetSize;
import com.jayud.oceanship.po.CabinetType;
import com.jayud.oceanship.mapper.CabinetTypeMapper;
import com.jayud.oceanship.service.ICabinetSizeService;
import com.jayud.oceanship.service.ICabinetTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oceanship.vo.CabinetTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ICabinetSizeService cabinetSizeService;

    @Override
    public List<CabinetTypeVO> initCabinetType() {
        List<CabinetType> cabinetTypes = baseMapper.selectList(null);
        List<CabinetTypeVO> cabinetTypeVOs = new ArrayList<>();
        for (CabinetType cabinetType : cabinetTypes) {
            CabinetTypeVO convert = ConvertUtil.convert(cabinetType, CabinetTypeVO.class);

            if(cabinetType.getCabinetSizeId()!=null) {
                String[] split = cabinetType.getCabinetSizeId().split(",");
                List<CabinetSize> cabinetSizes = new ArrayList<>();
                for (int i = 0; i < split.length; i++) {
                    CabinetSize byId = cabinetSizeService.getById(Long.parseLong(split[i]));
                    cabinetSizes.add(byId);
                }
                convert.setCabinetSizes(cabinetSizes);

            }
            cabinetTypeVOs.add(convert);
        }
        return cabinetTypeVOs;
    }
}
