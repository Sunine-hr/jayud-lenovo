package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.po.Fees;
import com.jayud.scm.mapper.FeesMapper;
import com.jayud.scm.model.vo.FeesComboxVO;
import com.jayud.scm.model.vo.FeesVO;
import com.jayud.scm.service.IFeesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 费用计算公式表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-10
 */
@Service
public class FeesServiceImpl extends ServiceImpl<FeesMapper, Fees> implements IFeesService {

    @Override
    public List<FeesComboxVO> formulaSettingMaintenance(String modelType) {
        List<FeesComboxVO> feesComboxVOS = new ArrayList<>();
        List<String> strings = this.baseMapper.formulaSettingMaintenance(modelType);
        for (String string : strings) {
            QueryWrapper<Fees> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Fees::getFeeAlias,string);
            queryWrapper.lambda().eq(Fees::getVoided,0);
            List<Fees> list = this.list(queryWrapper);
            FeesComboxVO feesComboxVO = new FeesComboxVO();
            feesComboxVO.setFeeAlias(string);
            feesComboxVO.setFeesVOS(ConvertUtil.convertList(list, FeesVO.class));
            feesComboxVOS.add(feesComboxVO);
        }
        return feesComboxVOS;
    }

    @Override
    public Fees getFeesById(Integer feesId) {
        return this.getById(feesId);
    }
}
