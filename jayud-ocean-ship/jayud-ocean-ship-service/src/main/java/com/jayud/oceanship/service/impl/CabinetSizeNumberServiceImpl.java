package com.jayud.oceanship.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oceanship.po.CabinetSizeNumber;
import com.jayud.oceanship.mapper.CabinetSizeNumberMapper;
import com.jayud.oceanship.service.ICabinetSizeNumberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oceanship.vo.CabinetSizeNumberVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 柜型数量表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-03-17
 */
@Service
public class CabinetSizeNumberServiceImpl extends ServiceImpl<CabinetSizeNumberMapper, CabinetSizeNumber> implements ICabinetSizeNumberService {

    @Override
    public List<CabinetSizeNumberVO> getList(Long orderId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("sea_order_id",orderId);
        List<CabinetSizeNumber> list = baseMapper.selectList(queryWrapper);
        List<CabinetSizeNumberVO> list1 = ConvertUtil.convertList(list,CabinetSizeNumberVO.class);
        return list1;
    }
}
