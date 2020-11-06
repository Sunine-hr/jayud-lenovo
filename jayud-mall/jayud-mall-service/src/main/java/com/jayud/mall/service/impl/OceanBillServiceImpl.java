package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.OceanBillMapper;
import com.jayud.mall.mapper.OceanCounterMapper;
import com.jayud.mall.model.bo.OceanBillForm;
import com.jayud.mall.model.bo.OceanCounterForm;
import com.jayud.mall.model.bo.QueryOceanBillForm;
import com.jayud.mall.model.po.OceanBill;
import com.jayud.mall.model.po.OceanCounter;
import com.jayud.mall.model.po.OceanCounterCustomerRelation;
import com.jayud.mall.model.vo.OceanBillVO;
import com.jayud.mall.service.IOceanBillService;
import com.jayud.mall.service.IOceanCounterCustomerRelationService;
import com.jayud.mall.service.IOceanCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 提单表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Service
public class OceanBillServiceImpl extends ServiceImpl<OceanBillMapper, OceanBill> implements IOceanBillService {

    @Autowired
    OceanBillMapper oceanBillMapper;

    @Autowired
    OceanCounterMapper oceanCounterMapper;

    @Autowired
    IOceanCounterService oceanCounterService;

    @Autowired
    IOceanCounterCustomerRelationService oceanCounterCustomerRelationService;

    @Override
    public IPage<OceanBillVO> findOceanBillByPage(QueryOceanBillForm form) {
        //处理时间区间
        if(form.getSailTime() != null){
            form.setSailTimeStart(form.getSailTime().toLocalDate().toString() + " 00:00:00");
            form.setSailTimeEnd(form.getSailTime().toLocalDate().toString() + " 23:23:59");
        }
        //定义分页参数
        Page<OceanBillVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<OceanBillVO> pageInfo = oceanBillMapper.findOceanBillByPage(page, form);

        //设置柜号list
        List<OceanBillVO> records = pageInfo.getRecords();
        records.forEach(oceanBillVO -> {
            Long obId = oceanBillVO.getId();
            QueryWrapper<OceanCounter> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ob_id", obId);
            List<OceanCounter> oceanCounters = oceanCounterMapper.selectList(queryWrapper);
            oceanBillVO.setOceanCounterList(oceanCounters);
        });
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOceanBill(OceanBillForm form) {
        OceanBill oceanBill = ConvertUtil.convert(form, OceanBill.class);
        this.saveOrUpdate(oceanBill);

        Long obId = oceanBill.getId();

        List<OceanCounterCustomerRelation> oceanCounterCustomerRelationList = new ArrayList<>();
        List<OceanCounterForm> oceanCounterForms = form.getOceanCounterForms();
        oceanCounterForms.forEach(oceanCounterForm -> {
            OceanCounter oceanCounter = ConvertUtil.convert(oceanCounterForm, OceanCounter.class);
            oceanCounter.setObId(obId);
            //保存提单货柜信息
            oceanCounterService.saveOrUpdate(oceanCounter);

            //保存提单对应货柜信息，所属的客户，关联信息
            Long oceanCounterId = oceanCounter.getId();
            Long customerId = oceanCounterForm.getCustomerId();
            //先删除
            QueryWrapper<OceanCounterCustomerRelation> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ocean_counter_id", oceanCounterId);
            oceanCounterCustomerRelationService.remove(queryWrapper);
            //在保存
            OceanCounterCustomerRelation oceanCounterCustomerRelation = new OceanCounterCustomerRelation(oceanCounterId,customerId);
            oceanCounterCustomerRelationService.saveOrUpdate(oceanCounterCustomerRelation);
        });


    }
}
