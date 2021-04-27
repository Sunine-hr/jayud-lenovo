package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.BillCustomsInfoMapper;
import com.jayud.mall.model.bo.BillCustomsInfoForm;
import com.jayud.mall.model.po.BillCustomsInfo;
import com.jayud.mall.model.vo.BillCustomsInfoVO;
import com.jayud.mall.service.IBillCustomsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 应收对账单表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-17
 */
@Service
public class BillCustomsInfoServiceImpl extends ServiceImpl<BillCustomsInfoMapper, BillCustomsInfo> implements IBillCustomsInfoService {
    @Autowired
    IBillCustomsInfoService billCustomsInfoService;
    @Override
    public IPage<BillCustomsInfoVO> findBillCustomsInfoByPage(BillCustomsInfoForm form) {
        //定义分页参数
        Page<BillCustomsInfoVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<BillCustomsInfoVO> pageInfo = billCustomsInfoService.findBillCustomsInfoByPage(form);
        return  pageInfo;
    }

    @Override
    public void insertBillCustomsInfo(BillCustomsInfoForm billCustomsInfo) {
        billCustomsInfoService.insertBillCustomsInfo(billCustomsInfo);
    }

    @Override
    public void updateBillCustomsInfo(BillCustomsInfoForm billCustomsInfo) {
        billCustomsInfoService.updateBillCustomsInfo(billCustomsInfo);
    }

    @Override
    public void deleteBillCustomsInfo(Long id) {
        billCustomsInfoService.deleteBillCustomsInfo(id);
    }

}
