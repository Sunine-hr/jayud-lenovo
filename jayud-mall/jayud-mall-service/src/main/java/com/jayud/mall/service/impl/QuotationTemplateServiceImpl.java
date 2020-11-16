package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.*;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.po.*;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.service.IQuotationTemplateService;
import com.jayud.mall.service.ITemplateCopeReceivableService;
import com.jayud.mall.service.ITemplateCopeWithService;
import com.jayud.mall.service.ITemplateFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 报价模板 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-02
 */
@Service
public class QuotationTemplateServiceImpl extends ServiceImpl<QuotationTemplateMapper, QuotationTemplate> implements IQuotationTemplateService {

    @Autowired
    QuotationTemplateMapper quotationTemplateMapper;

    @Autowired
    FabWarehouseMapper fabWarehouseMapper;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    GoodsTypeMapper goodsTypeMapper;

    @Autowired
    ShippingAreaMapper shippingAreaMapper;

    @Autowired
    TemplateCopeReceivableMapper templateCopeReceivableMapper;

    @Autowired
    TemplateCopeWithMapper templateCopeWithMapper;

    @Autowired
    TemplateFileMapper templateFileMapper;

    @Autowired
    ITemplateCopeReceivableService templateCopeReceivableService;

    @Autowired
    ITemplateCopeWithService templateCopeWithService;

    @Autowired
    ITemplateFileService templateFileService;

    @Override
    public IPage<QuotationTemplateVO> findQuotationTemplateByPage(QueryQuotationTemplateForm form) {
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
        Page<QuotationTemplateVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<QuotationTemplateVO> pageInfo = quotationTemplateMapper.findQuotationTemplateByPage(page, form);
        return pageInfo;
    }

    @Override
    public void disabledQuotationTemplate(Long id) {
        QuotationTemplate quotationTemplate = quotationTemplateMapper.selectById(id);
        quotationTemplate.setStatus("0");
        this.saveOrUpdate(quotationTemplate);
    }

    @Override
    public void enableQuotationTemplate(Long id) {
        QuotationTemplate quotationTemplate = quotationTemplateMapper.selectById(id);
        quotationTemplate.setStatus("1");
        this.saveOrUpdate(quotationTemplate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveQuotationTemplate(QuotationTemplateForm form) {
        QuotationTemplate quotationTemplate = ConvertUtil.convert(form, QuotationTemplate.class);
        this.saveOrUpdate(quotationTemplate);
        //报价模板Id
        Long id = quotationTemplate.getId();
        System.out.println(id);

        /*应收费用明细List*/
        List<TemplateCopeReceivableForm> templateCopeReceivableFormList = form.getTemplateCopeReceivableFormList();
        //刪除
        QueryWrapper<TemplateCopeReceivable> queryWrapperTemplateCopeReceivable = new QueryWrapper<>();
        queryWrapperTemplateCopeReceivable.eq("qie", id);
        templateCopeReceivableService.remove(queryWrapperTemplateCopeReceivable);
        if(templateCopeReceivableFormList.size() > 0){
            List<TemplateCopeReceivable> list = new ArrayList<>();
            templateCopeReceivableFormList.forEach(templateCopeReceivableForm -> {
                TemplateCopeReceivable templateCopeReceivable = ConvertUtil.convert(templateCopeReceivableForm, TemplateCopeReceivable.class);
                templateCopeReceivable.setQie(id.intValue());
                list.add(templateCopeReceivable);
            });
            //保存
            templateCopeReceivableService.saveOrUpdateBatch(list);
        }

        /*应付费用明细list*/
        List<TemplateCopeWithForm> templateCopeWithFormList = form.getTemplateCopeWithFormList();
        //刪除
        QueryWrapper<TemplateCopeWith> queryWrapperTemplateCopeWith = new QueryWrapper<>();
        queryWrapperTemplateCopeWith.eq("qie", id);
        templateCopeWithService.remove(queryWrapperTemplateCopeWith);
        if(templateCopeWithFormList.size() > 0){
            List<TemplateCopeWith> list = new ArrayList<>();
            templateCopeWithFormList.forEach(templateCopeWithForm -> {
                TemplateCopeWith templateCopeWith = ConvertUtil.convert(templateCopeWithForm, TemplateCopeWith.class);
                templateCopeWith.setQie(id.intValue());
                list.add(templateCopeWith);
            });
            //保存
            templateCopeWithService.saveOrUpdateBatch(list);
        }

        /*文件信息明细list*/
        List<TemplateFileForm> templateFileFormList = form.getTemplateFileFormList();
        //刪除
        QueryWrapper<TemplateFile> queryWrapperTemplateFile = new QueryWrapper<>();
        queryWrapperTemplateFile.eq("qie", id);
        templateFileService.remove(queryWrapperTemplateFile);
        if(templateFileFormList.size() > 0){
            List<TemplateFile> list = new ArrayList<>();
            templateFileFormList.forEach(templateFileForm -> {
                TemplateFile templateFile = ConvertUtil.convert(templateFileForm, TemplateFile.class);
                templateFile.setQie(id.intValue());
                list.add(templateFile);
            });
            //保存
            templateFileService.saveOrUpdateBatch(list);
        }
    }

    @Override
    public CommonResult<QuotationTemplateVO> lookQuotationTemplate(Long id) {
        QuotationTemplateVO quotationTemplateVO = quotationTemplateMapper.lookQuotationTemplate(id);

        Long qie = quotationTemplateVO.getId();//报价模板id
        //可达仓库List
        String arriveWarehouse = quotationTemplateVO.getArriveWarehouse();
        if(arriveWarehouse != null && arriveWarehouse != ""){
            String[] arriveWarehouseArr = arriveWarehouse.split(",");
            List<String> arriveWarehouseList = Arrays.asList(arriveWarehouseArr);
            List<FabWarehouse> fabWarehouses = fabWarehouseMapper.selectBatchIds(arriveWarehouseList);
            List<FabWarehouseVO> fabWarehouseVOList = ConvertUtil.convertList(fabWarehouses, FabWarehouseVO.class);
            quotationTemplateVO.setFabWarehouseVOList(fabWarehouseVOList);
        }

        //可见客户list
        String visibleUid = quotationTemplateVO.getVisibleUid();
        if(visibleUid != null && visibleUid != ""){
            String[] visibleUidArr = visibleUid.split(",");
            List<String> visibleUidList = Arrays.asList(visibleUidArr);
            List<Customer> customers = customerMapper.selectBatchIds(visibleUidList);
            List<CustomerVO> customerVOList = ConvertUtil.convertList(customers, CustomerVO.class);
            quotationTemplateVO.setCustomerVOList(customerVOList);
        }

        //货物类型list
        String gid = quotationTemplateVO.getGid();
        if(gid != null && gid != ""){
            String[] gidArr = gid.split(",");
            List<String> gidList = Arrays.asList(gidArr);
            List<GoodsType> goodsTypes = goodsTypeMapper.selectBatchIds(gidList);
            List<GoodsTypeVO> gList = ConvertUtil.convertList(goodsTypes, GoodsTypeVO.class);
            quotationTemplateVO.setGList(gList);
        }

        //集货仓库list
        String areaId = quotationTemplateVO.getAreaId();
        if(areaId != null && areaId != ""){
            String[] areaIdArr = areaId.split(",");
            List<String> areaIdList = Arrays.asList(areaIdArr);
            List<ShippingArea> shippingAreas = shippingAreaMapper.selectBatchIds(areaIdList);
            List<ShippingAreaVO> shippingAreaVOList = ConvertUtil.convertList(shippingAreas, ShippingAreaVO.class);
            quotationTemplateVO.setShippingAreaVOList(shippingAreaVOList);
        }

        //报价类型list
        String qid = quotationTemplateVO.getQid();
        if(qid != null && qid != ""){
            String[] qidArr = qid.split(",");
            List<String> qidList = Arrays.asList(qidArr);
            List<GoodsType> goodsTypes = goodsTypeMapper.selectBatchIds(qidList);
            List<GoodsTypeVO> qList = ConvertUtil.convertList(goodsTypes, GoodsTypeVO.class);
            quotationTemplateVO.setQList(qList);
        }

        //报价对应应收费用明细list
        QueryWrapper<TemplateCopeReceivable> query1 = new QueryWrapper<>();
        query1.eq("qie", qie);
        List<TemplateCopeReceivable> templateCopeReceivables = templateCopeReceivableMapper.selectList(query1);
        List<TemplateCopeReceivableVO> templateCopeReceivableVOList =
                ConvertUtil.convertList(templateCopeReceivables, TemplateCopeReceivableVO.class);
        quotationTemplateVO.setTemplateCopeReceivableVOList(templateCopeReceivableVOList);

        //报价对应应付费用明细list
        QueryWrapper<TemplateCopeWith> query2 = new QueryWrapper<>();
        query2.eq("qie", qie);
        List<TemplateCopeWith> templateCopeWiths = templateCopeWithMapper.selectList(query2);
        List<TemplateCopeWithVO> templateCopeWithVOList =
                ConvertUtil.convertList(templateCopeWiths, TemplateCopeWithVO.class);
        quotationTemplateVO.setTemplateCopeWithVOList(templateCopeWithVOList);

        //模板对应模块信息list，文件信息
        List<TemplateFileVO> templateFileVOList = templateFileMapper.findTemplateFileByQie(qie);
        //根据类型分组
        Map<String, List<TemplateFileVO>> map =
                templateFileVOList.stream().collect(Collectors.groupingBy(TemplateFileVO::getTypes));
        List<TemplateFileVO> templateFileGroup = new ArrayList<>();
        map.forEach((k,v) -> {
            TemplateFileVO templateFileVO = new TemplateFileVO();
            templateFileVO.setGroupCode(k);
            if(k.equalsIgnoreCase("1")){
                templateFileVO.setGroupName("报关服务");
            }else if(k.equalsIgnoreCase("2")){
                templateFileVO.setGroupName("清关服务");
            }
            templateFileVO.setTemplateFileVOList(v);
            templateFileGroup.add(templateFileVO);
        });
        quotationTemplateVO.setTemplateFileVOList(templateFileGroup);
        return CommonResult.success(quotationTemplateVO);
    }

}
