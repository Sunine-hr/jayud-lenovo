package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.BillCustomsCounterListMapper;
import com.jayud.mall.model.bo.BillCustomsCounterListForm;
import com.jayud.mall.model.po.BillCustomsCounterList;
import com.jayud.mall.model.vo.BillCustomsCounterListVO;
import com.jayud.mall.service.IBillCustomsCounterListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * （提单)报关、清关 关联 柜子清单 表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-07-06
 */
@Service
public class BillCustomsCounterListServiceImpl extends ServiceImpl<BillCustomsCounterListMapper, BillCustomsCounterList> implements IBillCustomsCounterListService {

    @Autowired
    BillCustomsCounterListMapper billCustomsCounterListMapper;

    @Override
    public List<BillCustomsCounterListVO> findBillCustomsCounterListByTypeAndCustomsId(BillCustomsCounterListForm form) {
        List<BillCustomsCounterListVO> billCustomsCounterLists = billCustomsCounterListMapper.findBillCustomsCounterListByTypeAndCustomsId(form);
        return billCustomsCounterLists;
    }
}
