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
import com.jayud.mall.model.vo.OfferInfoVO;
import com.jayud.mall.service.IOfferInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

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
        //服务名称
        Integer sid = offerInfoVO.getSid();
        if(sid != null){
            ServiceGroup serviceGroup = serviceGroupMapper.selectById(sid);
            offerInfoVO.setServiceGroup(serviceGroup);
        }
        //报价图片
        String picUrl = offerInfoVO.getPicUrl();
        if(picUrl != null && picUrl != ""){
            String[] split = picUrl.split(",");
            List<String> picUrlList = Arrays.asList(split);
            offerInfoVO.setPicUrlList(picUrlList);
        }
        //运输方式
        Integer tid = offerInfoVO.getTid();
        if(tid != null){
            TransportWay transportWay = transportWayMapper.selectById(tid);
            offerInfoVO.setTransportWay(transportWay);
        }
        //可达仓库
        String arriveWarehouse = offerInfoVO.getArriveWarehouse();
        if(arriveWarehouse != null && arriveWarehouse != ""){
            String[] split = arriveWarehouse.split(",");
            List<String> list = Arrays.asList(split);
            List<FabWarehouse> fabWarehouses = fabWarehouseMapper.selectBatchIds(list);
            offerInfoVO.setFabWarehouseList(fabWarehouses);
        }
        //可见客户
        String visibleUid = offerInfoVO.getVisibleUid();
        if(visibleUid != null && visibleUid != ""){
            String[] split = visibleUid.split(",");
            List<String> strings = Arrays.asList(split);
            List<Customer> customers = customerMapper.selectBatchIds(strings);
            offerInfoVO.setCustomerList(customers);
        }
        //货物类型
        String gid = offerInfoVO.getGid();
        if(gid != null && gid != ""){
            String[] split = gid.split(",");
            List<String> strings = Arrays.asList(split);
            List<GoodsType> goodsTypes = goodsTypeMapper.selectBatchIds(strings);
            offerInfoVO.setGList(goodsTypes);
        }
        //集货仓库
        String areaId = offerInfoVO.getAreaId();
        if(areaId != null && areaId != ""){
            String[] split = areaId.split(",");
            List<String> strings = Arrays.asList(split);
            List<ShippingArea> shippingAreas = shippingAreaMapper.selectBatchIds(strings);
            offerInfoVO.setShippingAreaList(shippingAreas);
        }
        //报价类型
        String qid = offerInfoVO.getQid();
        if(qid != null && qid != ""){
            String[] split = qid.split(",");
            List<String> strings = Arrays.asList(split);
            List<GoodsType> goodsTypes = goodsTypeMapper.selectBatchIds(strings);
            offerInfoVO.setQList(goodsTypes);
        }
        //任务分组
        Integer taskId = offerInfoVO.getTaskId();
        if(taskId != null){
            TaskGroup taskGroup = taskGroupMapper.selectById(taskId);
            offerInfoVO.setTaskGroup(taskGroup);
        }
        //报价关联的报价模板Id
        Integer qie = offerInfoVO.getQie();
        //应收费用list
        QueryWrapper<TemplateCopeReceivable> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("qie", qie);
        List<TemplateCopeReceivable> templateCopeReceivables = templateCopeReceivableMapper.selectList(queryWrapper1);
        offerInfoVO.setTemplateCopeReceivableList(templateCopeReceivables);

        //应付费用list
        QueryWrapper<TemplateCopeWith> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("qie", qie);
        List<TemplateCopeWith> templateCopeWiths = templateCopeWithMapper.selectList(queryWrapper2);
        offerInfoVO.setTemplateCopeWithList(templateCopeWiths);

        //文件信息list
        QueryWrapper<TemplateFile> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.eq("qie", qie);
        List<TemplateFile> TemplateFiles = templateFileMapper.selectList(queryWrapper3);
        offerInfoVO.setTemplateFileList(TemplateFiles);

        return CommonResult.success(offerInfoVO);
    }
}
