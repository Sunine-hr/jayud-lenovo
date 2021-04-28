package com.jayud.mall.service.impl;

import com.jayud.mall.model.po.BillCustomsInfo;
import com.jayud.mall.mapper.BillCustomsInfoMapper;
import com.jayud.mall.model.vo.BillCustomsInfoVO;
import com.jayud.mall.model.vo.CustomsInfoCaseVO;
import com.jayud.mall.service.IBillCustomsInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
