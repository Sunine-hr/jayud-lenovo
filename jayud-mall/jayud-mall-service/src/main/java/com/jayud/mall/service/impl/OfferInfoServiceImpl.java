package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.OfferInfoMapper;
import com.jayud.mall.model.bo.QueryOfferInfoForm;
import com.jayud.mall.model.po.OfferInfo;
import com.jayud.mall.model.vo.OfferInfoVO;
import com.jayud.mall.service.IOfferInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 报价管理 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-05
 */
@Service
public class OfferInfoServiceImpl extends ServiceImpl<OfferInfoMapper, OfferInfo> implements IOfferInfoService {


    @Autowired
    OfferInfoMapper offerInfoMapper;

    @Override
    public IPage<OfferInfoVO> findOfferInfoByPage(QueryOfferInfoForm form) {
        //处理时间区间
        if(form.getSailTime() != null){
            form.setSailTimeStart(form.getSailTime().toLocalDate().toString() + " 00:00:00");
            form.setSailTimeEnd(form.getSailTime().toLocalDate().toString() + " 23:23:59");
        }
        if(form.getCutOffTime() != null){
            form.setCutOffTimeStart(form.getSailTime().toLocalDate().toString() + " 00:00:00");
            form.setCutOffTimeEnd(form.getSailTime().toLocalDate().toString() + " 23:23:59");
        }
        //定义分页参数
        Page<OfferInfoVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<OfferInfoVO> pageInfo = offerInfoMapper.findOfferInfoByPage(page, form);
        return pageInfo;
    }
}
