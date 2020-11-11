package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.OceanBillMapper;
import com.jayud.mall.mapper.OceanCounterCustomerRelationMapper;
import com.jayud.mall.mapper.OceanCounterMapper;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.po.OceanBill;
import com.jayud.mall.model.po.OceanCounter;
import com.jayud.mall.model.po.OceanWaybill;
import com.jayud.mall.model.po.OceanWaybillCaseRelation;
import com.jayud.mall.model.vo.OceanBillVO;
import com.jayud.mall.model.vo.OceanCounterCustomerRelationVO;
import com.jayud.mall.service.IOceanBillService;
import com.jayud.mall.service.IOceanCounterService;
import com.jayud.mall.service.IOceanWaybillCaseRelationService;
import com.jayud.mall.service.IOceanWaybillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    OceanCounterCustomerRelationMapper oceanCounterCustomerRelationMapper;

    @Autowired
    IOceanCounterService oceanCounterService;

    @Autowired
    IOceanWaybillService oceanWaybillService;

    @Autowired
    IOceanWaybillCaseRelationService oceanWaybillCaseRelationService;

//    @Autowired
//    IOceanCounterCustomerRelationService oceanCounterCustomerRelationService;

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

    /**
     * <p>保存提单信息</p>
     * <p>1个提单对应1个柜子</p>
     * <p>1个柜子对应N个运单</p>
     * <p>1个运单对应N个箱号</p>
     * @param form
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOceanBill(OceanBillForm form) {
        //提单
        OceanBill oceanBill = ConvertUtil.convert(form, OceanBill.class);
        this.saveOrUpdate(oceanBill);
        Long obId = oceanBill.getId();// 提单id

        //1提单对应1货柜(PS:之前是1提单对应N货柜)
        List<OceanCounterForm> oceanCounterForms = form.getOceanCounterForms();
        oceanCounterForms.forEach(oceanCounterForm -> {
            OceanCounter oceanCounter = ConvertUtil.convert(oceanCounterForm, OceanCounter.class);
            oceanCounter.setObId(obId);//提单id
            oceanCounterService.saveOrUpdate(oceanCounter);
            Long oceanCounterId = oceanCounter.getId();//货柜信息id

            //1柜子对应N运单
            List<OceanWaybillForm> oceanWaybillFormList = oceanCounterForm.getOceanWaybillFormList();
            oceanWaybillFormList.forEach(oceanWaybillForm -> {
                OceanWaybill oceanWaybill = ConvertUtil.convert(oceanWaybillForm, OceanWaybill.class);
                oceanWaybill.setOceanCounterId(oceanCounterId.intValue());//货柜信息id(ocean_counter id)
                oceanWaybillService.saveOrUpdate(oceanWaybill);
                Long oceanWaybillId = oceanWaybill.getId();//运单id(ocean_waybill id)

                //1运单对应N箱号
                List<OceanWaybillCaseRelationForm> oceanWaybillCaseRelationFormList = oceanWaybillForm.getOceanWaybillCaseRelationFormList();
                oceanWaybillCaseRelationFormList.forEach(oceanWaybillCaseRelationForm -> {
                    OceanWaybillCaseRelation oceanWaybillCaseRelation = ConvertUtil.convert(oceanWaybillCaseRelationForm, OceanWaybillCaseRelation.class);
                    oceanWaybillCaseRelation.setOceanWaybillId(oceanWaybillId);//运单id(ocean_waybill id)
                    oceanWaybillCaseRelationService.saveOrUpdate(oceanWaybillCaseRelation);
                });
            });
        });

        //1柜子对应N运单

        //1运单对应N箱号

//        //提单对应货柜信息list
//        List<OceanCounterForm> oceanCounterForms = form.getOceanCounterForms();
//        oceanCounterForms.forEach(oceanCounterForm -> {
//            OceanCounter oceanCounter = ConvertUtil.convert(oceanCounterForm, OceanCounter.class);
//            oceanCounter.setObId(obId);
//            //保存提单货柜信息
//            oceanCounterService.saveOrUpdate(oceanCounter);
//
//
//            Long oceanCounterId = oceanCounter.getId();
//            //保存关联信息前，先删除信息
//            QueryWrapper<OceanCounterCustomerRelation> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("ocean_counter_id", oceanCounterId);
//            oceanCounterCustomerRelationService.remove(queryWrapper);
//
//            List<OceanCounterCustomerRelation> oceanCounterCustomerRelationList = oceanCounterForm.getOceanCounterCustomerRelationList();
//            oceanCounterCustomerRelationList.forEach(oceanCounterCustomerRelation -> {
//                oceanCounterCustomerRelation.setOceanCounterId(oceanCounterId);
//            });
//            //批量保存，提单对应货柜信息和客户关联表
//            oceanCounterCustomerRelationService.saveOrUpdateBatch(oceanCounterCustomerRelationList);
//
//        });

    }

    @Override
    public CommonResult<OceanBillVO> lookOceanBill(Long id) {
        //提单信息
        OceanBill oceanBill = oceanBillMapper.selectById(id);
        OceanBillVO oceanBillVO = ConvertUtil.convert(oceanBill, OceanBillVO.class);

        //提单关联柜号信息
        Long obId = oceanBillVO.getId();
        QueryWrapper<OceanCounter> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","cntr_no","cabinet_code","volume","cost","cid","`status`","ob_id","create_time");
        queryWrapper.eq("ob_id", obId);
        List<OceanCounter> oceanCounterList = oceanCounterMapper.selectList(queryWrapper);

        //柜号关联装柜信息
        oceanCounterList.forEach(oceanCounter -> {
            Long oceanCounterId = oceanCounter.getId();
            //装柜信息list
            List<OceanCounterCustomerRelationVO> zgxxList = oceanCounterCustomerRelationMapper.findZgxxListByOceanCounterId(oceanCounterId);
            oceanCounter.setOceanCounterCustomerRelationVOList(zgxxList);
        });
        oceanBillVO.setOceanCounterList(oceanCounterList);
        return CommonResult.success(oceanBillVO);
    }
}
