package com.jayud.customs.service.impl;

import com.jayud.common.enums.CustomsBizModelTypeEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.DateUtils;
import com.jayud.customs.model.po.CustomsFiling;
import com.jayud.customs.mapper.CustomsFilingMapper;
import com.jayud.customs.service.ICustomsFilingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 报关归档表 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-07-08
 */
@Service
public class CustomsFilingServiceImpl extends ServiceImpl<CustomsFilingMapper, CustomsFiling> implements ICustomsFilingService {

    @Override
    public String generateBoxNum(String archiveDate, Integer goodsType, Integer bizModel) {
        CustomsBizModelTypeEnum bizModelTypeEnum = CustomsBizModelTypeEnum.getEnum(bizModel);
        if (bizModelTypeEnum==null){
            throw new JayudBizException("不存在业务类型");
        }
        String date = DateUtils.LocalDateTime2Str(LocalDateTime.now(), DateUtils.DATE_PATTERN);
        //货物流向(1进口 2出口)
        StringBuilder sb=new StringBuilder();
        sb.append(bizModelTypeEnum.name()).append(goodsType==1?"JK":"CK")
                .append(date).append("");


        return null;
    }
}
