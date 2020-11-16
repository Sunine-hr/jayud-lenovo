package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.*;
import com.jayud.mall.model.bo.OfferInfoForm;
import com.jayud.mall.model.bo.QueryOfferInfoForm;
import com.jayud.mall.model.po.*;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.service.IOfferInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    ServiceGroupMapper serviceGroupMapper;

    @Autowired
    TransportWayMapper transportWayMapper;

    @Autowired
    FabWarehouseMapper fabWarehouseMapper;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    GoodsTypeMapper goodsTypeMapper;

    @Autowired
    ShippingAreaMapper shippingAreaMapper;

    @Autowired
    TaskGroupMapper taskGroupMapper;

    @Autowired
    TemplateCopeReceivableMapper templateCopeReceivableMapper;

    @Autowired
    TemplateCopeWithMapper templateCopeWithMapper;

    @Autowired
    TemplateFileMapper templateFileMapper;

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

    @Override
    public void disabledOfferInfo(Long id) {
        OfferInfo offerInfo = offerInfoMapper.selectById(id);
        offerInfo.setStatus("0");//状态(0无效 1有效)
        this.saveOrUpdate(offerInfo);
    }

    @Override
    public void enableOfferInfo(Long id) {
        OfferInfo offerInfo = offerInfoMapper.selectById(id);
        offerInfo.setStatus("1");//状态(0无效 1有效)
        this.saveOrUpdate(offerInfo);
    }

    @Override
    public void saveOfferInfo(OfferInfoForm form) {
        OfferInfo offerInfo = ConvertUtil.convert(form, OfferInfo.class);
        this.saveOrUpdate(offerInfo);
    }

    @Override
    public CommonResult<OfferInfoVO> lookOfferInfo(Long id) {
        OfferInfoVO offerInfoVO = offerInfoMapper.selectOfferInfoVO(id);
        Integer qie = offerInfoVO.getQie();//报价模板id

        //可达仓库List
        String arriveWarehouse = offerInfoVO.getArriveWarehouse();
        if(arriveWarehouse != null && arriveWarehouse != ""){
            String[] arriveWarehouseArr = arriveWarehouse.split(",");
            List<String> arriveWarehouseList = Arrays.asList(arriveWarehouseArr);
            List<FabWarehouse> fabWarehouses = fabWarehouseMapper.selectBatchIds(arriveWarehouseList);
            List<FabWarehouseVO> fabWarehouseVOList = ConvertUtil.convertList(fabWarehouses, FabWarehouseVO.class);
            offerInfoVO.setFabWarehouseVOList(fabWarehouseVOList);
        }
        //可见客户list
        String visibleUid = offerInfoVO.getVisibleUid();
        if(visibleUid != null && visibleUid != ""){
            String[] visibleUidArr = visibleUid.split(",");
            List<String> visibleUidList = Arrays.asList(visibleUidArr);
            List<Customer> customers = customerMapper.selectBatchIds(visibleUidList);
            List<CustomerVO> customerVOList = ConvertUtil.convertList(customers, CustomerVO.class);
            offerInfoVO.setCustomerVOList(customerVOList);
        }
        //货物类型list
        String gid = offerInfoVO.getGid();
        if(gid != null && gid != ""){
            String[] gidArr = gid.split(",");
            List<String> gidList = Arrays.asList(gidArr);
            List<GoodsType> goodsTypes = goodsTypeMapper.selectBatchIds(gidList);
            List<GoodsTypeVO> gList = ConvertUtil.convertList(goodsTypes, GoodsTypeVO.class);
            offerInfoVO.setGList(gList);
            //货物类型名称
            if(gList.size() > 0){
                GoodsTypeVO goodsTypeVO = gList.get(0);
                Integer fid = goodsTypeVO.getFid();
                GoodsType goodsType = goodsTypeMapper.selectById(fid);
                offerInfoVO.setGName(goodsType.getName());
            }
        }
        //集货仓库list
        String areaId = offerInfoVO.getAreaId();
        if(areaId != null && areaId != ""){
            String[] areaIdArr = areaId.split(",");
            List<String> areaIdList = Arrays.asList(areaIdArr);
            List<ShippingArea> shippingAreas = shippingAreaMapper.selectBatchIds(areaIdList);
            List<ShippingAreaVO> shippingAreaVOList = ConvertUtil.convertList(shippingAreas, ShippingAreaVO.class);
            offerInfoVO.setShippingAreaVOList(shippingAreaVOList);
        }
        //报价类型list
        String qid = offerInfoVO.getQid();
        if(qid != null && qid != ""){
            String[] qidArr = qid.split(",");
            List<String> qidList = Arrays.asList(qidArr);
            List<GoodsType> goodsTypes = goodsTypeMapper.selectBatchIds(qidList);
            List<GoodsTypeVO> qList = ConvertUtil.convertList(goodsTypes, GoodsTypeVO.class);
            offerInfoVO.setQList(qList);
            //报价类型名称
            if(qList.size() > 0){
                GoodsTypeVO goodsTypeVO = qList.get(0);
                Integer fid = goodsTypeVO.getFid();
                GoodsType goodsType = goodsTypeMapper.selectById(fid);
                offerInfoVO.setQName(goodsType.getName());
            }

        }
        //报价对应应收费用明细list
        QueryWrapper<TemplateCopeReceivable> query1 = new QueryWrapper<>();
        query1.eq("qie", qie);
        List<TemplateCopeReceivable> templateCopeReceivables = templateCopeReceivableMapper.selectList(query1);
        List<TemplateCopeReceivableVO> templateCopeReceivableVOList =
                ConvertUtil.convertList(templateCopeReceivables, TemplateCopeReceivableVO.class);
        offerInfoVO.setTemplateCopeReceivableVOList(templateCopeReceivableVOList);
        //报价对应应付费用明细list
        QueryWrapper<TemplateCopeWith> query2 = new QueryWrapper<>();
        query2.eq("qie", qie);
        List<TemplateCopeWith> templateCopeWiths = templateCopeWithMapper.selectList(query2);
        List<TemplateCopeWithVO> templateCopeWithVOList =
                ConvertUtil.convertList(templateCopeWiths, TemplateCopeWithVO.class);
        offerInfoVO.setTemplateCopeWithVOList(templateCopeWithVOList);

        //模板对应模块信息list，文件信息
        List<TemplateFileVO> templateFileVOList = templateFileMapper.findTemplateFileByQie(Long.valueOf(qie));
        //分组
        Map<String, List<TemplateFileVO>> map =
                templateFileVOList.stream().collect(Collectors.groupingBy(TemplateFileVO::getGroupCode));
        List<TemplateFileVO> templateFileGroup = new ArrayList<>();
        map.forEach((k,v) -> {
            TemplateFileVO templateFileVO = new TemplateFileVO();
            templateFileVO.setGroupCode(k);
            templateFileVO.setGroupName(v.get(0).getGroupName());
            templateFileVO.setTemplateFileVOList(v);
            templateFileGroup.add(templateFileVO);
        });
        offerInfoVO.setTemplateFileVOList(templateFileGroup);

        return CommonResult.success(offerInfoVO);
    }
}
