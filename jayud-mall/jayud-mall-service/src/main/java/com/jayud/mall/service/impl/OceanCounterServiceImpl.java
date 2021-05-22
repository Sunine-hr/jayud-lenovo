package com.jayud.mall.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.mall.mapper.FeeCopeWithMapper;
import com.jayud.mall.mapper.OceanCounterMapper;
import com.jayud.mall.model.po.OceanCounter;
import com.jayud.mall.model.vo.FeeCopeWithVO;
import com.jayud.mall.model.vo.OceanCounterVO;
import com.jayud.mall.service.IOceanCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 提单对应货柜信息 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Service
public class OceanCounterServiceImpl extends ServiceImpl<OceanCounterMapper, OceanCounter> implements IOceanCounterService {

    @Autowired
    OceanCounterMapper oceanCounterMapper;
    @Autowired
    FeeCopeWithMapper feeCopeWithMapper;

    @Override
    public OceanCounterVO findOceanCounterById(Long id) {
        OceanCounterVO oceanCounterVO = oceanCounterMapper.findOceanCounterById(id);
        if(ObjectUtil.isEmpty(oceanCounterVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "没有找到(提单)柜子");
        }
        Long counterId = oceanCounterVO.getId();
        Integer businessType = 2;//业务类型(1提单费用 2柜子费用)
        List<FeeCopeWithVO> feeCopeWithList = feeCopeWithMapper.findFeeCopeWithByQie(counterId.intValue(), businessType);
        oceanCounterVO.setFeeCopeWithList(feeCopeWithList);
        return oceanCounterVO;
    }
}
