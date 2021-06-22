package com.jayud.mall.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.mall.mapper.BillCustomsInfoMapper;
import com.jayud.mall.model.po.BillCustomsInfo;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.service.IBillCustomsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * (提单)报关信息表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-27
 */
@Service
public class BillCustomsInfoServiceImpl extends ServiceImpl<BillCustomsInfoMapper, BillCustomsInfo> implements IBillCustomsInfoService {

    @Autowired
    BillCustomsInfoMapper billCustomsInfoMapper;

    @Override
    public List<CustomsInfoCaseVO> findCustomsInfoCase(Long b_id) {
        return billCustomsInfoMapper.findCustomsInfoCase(b_id);
    }

    @Override
    public BillCustomsInfoVO findBillCustomsInfoById(Long id) {
        return billCustomsInfoMapper.findBillCustomsInfoById(id);
    }

    @Override
    public List<CustomsInfoCaseExcelVO> findCustomsInfoCaseBybid(Long b_id) {
        List<CustomsInfoCaseExcelVO> customsInfoCaseExcelVOS = billCustomsInfoMapper.findCustomsInfoCaseBybid(b_id);
        return customsInfoCaseExcelVOS;
    }

    @Override
    public CustomsListExcelVO findCustomsListExcelById(Long id) {
        CustomsListExcelVO customsListExcelVO = billCustomsInfoMapper.findCustomsListExcelById(id);
        if(ObjectUtil.isEmpty(customsListExcelVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "报关清单不存在");
        }

        customsListExcelVO.setPackages("100");
        customsListExcelVO.setQty("200");
        customsListExcelVO.setTotalPrice("300");
        customsListExcelVO.setJz("400");
        customsListExcelVO.setGrossWeight("500");
        customsListExcelVO.setCbm("600");

        List<CustomsGoodsExcelVO> customsGoodsExcelList = new ArrayList<>();
        CustomsGoodsExcelVO customsGoodsExcelVO = new CustomsGoodsExcelVO();
        customsGoodsExcelVO.setSerialNumber("1");
        customsGoodsExcelVO.setChName("Ch");
        customsGoodsExcelVO.setEnName("En");
        customsGoodsExcelList.add(customsGoodsExcelVO);

        customsListExcelVO.setCustomsGoodsExcelList(customsGoodsExcelList);

        return customsListExcelVO;
    }

}
