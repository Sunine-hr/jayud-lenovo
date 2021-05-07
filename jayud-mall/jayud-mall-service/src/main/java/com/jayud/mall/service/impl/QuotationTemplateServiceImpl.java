package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.*;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.po.*;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    QuotationTypeMapper quotationTypeMapper;

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

    @Autowired
    BaseService baseService;

    @Override
    public IPage<QuotationTemplateVO> findQuotationTemplateByPage(QueryQuotationTemplateForm form) {
        //定义分页参数
        Page<QuotationTemplateVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<QuotationTemplateVO> quotationTemplateByPage = quotationTemplateMapper.findQuotationTemplateByPage(page, form);
        IPage<QuotationTemplateVO> pageInfo = quotationTemplateByPage;
        return pageInfo;
    }

    @Override
    public void disabledQuotationTemplate(Long id) {
        QuotationTemplate quotationTemplate = quotationTemplateMapper.selectById(id);
        quotationTemplate.setStatus("0");//status '状态(0无效 1有效)
        this.saveOrUpdate(quotationTemplate);
    }

    @Override
    public void enableQuotationTemplate(Long id) {
        QuotationTemplate quotationTemplate = quotationTemplateMapper.selectById(id);
        quotationTemplate.setStatus("1");//status '状态(0无效 1有效)
        this.saveOrUpdate(quotationTemplate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult saveQuotationTemplate(QuotationTemplateForm form) {
        QuotationTemplate quotationTemplate = ConvertUtil.convert(form, QuotationTemplate.class);
        Long id1 = quotationTemplate.getId();
        String names = quotationTemplate.getNames();//报价模板名称
        if(ObjectUtil.isNotEmpty(id1)){
            //id1 不为空
            QueryWrapper<QuotationTemplate> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("names", names);
            queryWrapper.ne("id", id1);
            List<QuotationTemplate> list = this.list(queryWrapper);
            if(CollUtil.isNotEmpty(list)){
                return CommonResult.error(-1, "["+names+"]"+",名称已存在");
            }
            quotationTemplate.setUpdateTime(LocalDateTime.now());
        }else{
            //id1 为空 新增
            QueryWrapper<QuotationTemplate> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("names", names);
            List<QuotationTemplate> list = this.list(queryWrapper);
            if(CollUtil.isNotEmpty(list)){
                return CommonResult.error(-1, "["+names+"]"+",名称已存在");
            }
            AuthUser user = baseService.getUser();
            quotationTemplate.setUserId(user.getId().intValue());
            quotationTemplate.setUserName(user.getName());
            quotationTemplate.setCreateTime(LocalDateTime.now());
            quotationTemplate.setUpdateTime(LocalDateTime.now());

            //状态(0无效 1有效)
            quotationTemplate.setStatus("1");

        }

        //模板类型
        quotationTemplate.setTypes(form.getQidtype());
        //报价图片
        List<PicUrlArrForm> picUrlarr = form.getPicUrlarr();
        if(CollUtil.isNotEmpty(picUrlarr)){
            StringBuffer picUrl = new StringBuffer();
            for(int i=0; i<picUrlarr.size(); i++) {
                PicUrlArrForm picUrlArrForm = picUrlarr.get(i);
                //解决磁盘满了，上传文件为空
                if(ObjectUtil.isNotEmpty(picUrlArrForm)){
                    if(i==0){
                        picUrl.append(picUrlArrForm.getFilePath());
                    }else{
                        picUrl.append(",").append(picUrlArrForm.getFilePath());
                    }
                }
            }
            quotationTemplate.setPicUrl(picUrl.toString());
        }

        //可达仓库
        List<FabWarehouseForm> arriveWarehousearr = form.getArriveWarehousearr();
        if(CollUtil.isNotEmpty(arriveWarehousearr)){
            StringBuffer arriveWarehouse = new StringBuffer();
            for(int i=0; i<arriveWarehousearr.size(); i++){
                FabWarehouseForm fabWarehouseForm = arriveWarehousearr.get(i);
                if(i == 0){
                    arriveWarehouse.append(fabWarehouseForm.getId());
                }else{
                    arriveWarehouse.append(",").append(fabWarehouseForm.getId());
                }
            }
            quotationTemplate.setArriveWarehouse(arriveWarehouse.toString());
        }

        //可见客户
        List<CustomerForm> visibleUidarr = form.getVisibleUidarr();
        if(CollUtil.isNotEmpty(visibleUidarr)){
            StringBuffer visibleUid = new StringBuffer();
            for(int i=0; i<visibleUidarr.size(); i++){
                CustomerForm customerForm = visibleUidarr.get(i);
                if(i == 0){
                    visibleUid.append(customerForm.getId());
                }else{
                    visibleUid.append(",").append(customerForm.getId());
                }
            }
            quotationTemplate.setVisibleUid(visibleUid.toString());
        }

        //货物类型
        List<GoodsTypeForm> gidarr = form.getGidarr();
        if(CollUtil.isNotEmpty(gidarr)){
            StringBuffer gid = new StringBuffer();
            for(int i=0; i<gidarr.size(); i++){
                GoodsTypeForm goodsTypeForm = gidarr.get(i);
                if(i == 0){
                    gid.append(goodsTypeForm.getId());
                }else{
                    gid.append(",").append(goodsTypeForm.getId());
                }
            }
            quotationTemplate.setGid(gid.toString());
        }

        //集货仓库
        List<ShippingAreaForm> areaIdarr = form.getAreaIdarr();
        if(CollUtil.isNotEmpty(areaIdarr)){
            StringBuffer areaId = new StringBuffer();
            for(int i=0; i<areaIdarr.size(); i++){
                ShippingAreaForm shippingAreaForm = areaIdarr.get(i);
                if(i == 0){
                    areaId.append(shippingAreaForm.getId());
                }else{
                    areaId.append(",").append(shippingAreaForm.getId());
                }
            }
            quotationTemplate.setAreaId(areaId.toString());
        }
        //报价类型
        List<QuotationTypeForm> qidarr = form.getQidarr();
        if(CollUtil.isNotEmpty(qidarr)){
            StringBuffer qid = new StringBuffer();
            for(int i=0; i<qidarr.size(); i++){
                QuotationTypeForm quotationTypeForm = qidarr.get(i);
                if(i == 0){
                    qid.append(quotationTypeForm.getId());
                }else{
                    qid.append(",").append(quotationTypeForm.getId());
                }
            }
            quotationTemplate.setQid(qid.toString());
        }

        this.saveOrUpdate(quotationTemplate);
        //报价模板Id
        Long id = quotationTemplate.getId();

        /*应收费用明细List*/
        List<TemplateCopeReceivableForm> templateCopeReceivableFormList = form.getTemplateCopeReceivableFormList();
        //刪除
        QueryWrapper<TemplateCopeReceivable> queryWrapperTemplateCopeReceivable = new QueryWrapper<>();
        queryWrapperTemplateCopeReceivable.eq("qie", id);
        templateCopeReceivableService.remove(queryWrapperTemplateCopeReceivable);

        if(CollUtil.isNotEmpty(templateCopeReceivableFormList)){
            List<TemplateCopeReceivable> list = new ArrayList<>();
            templateCopeReceivableFormList.forEach(templateCopeReceivableForm -> {
                TemplateCopeReceivable templateCopeReceivable = ConvertUtil.convert(templateCopeReceivableForm, TemplateCopeReceivable.class);
                templateCopeReceivable.setQie(id.intValue());
                //计算 总金额=数量 * 单价
                Integer c = templateCopeReceivable.getCount() == null ? 0 : templateCopeReceivable.getCount();//数量
                BigDecimal count = new BigDecimal(c.toString());
                BigDecimal unitPrice = templateCopeReceivable.getUnitPrice() == null ? new BigDecimal("0") : templateCopeReceivable.getUnitPrice();//单价
                BigDecimal amount = count.multiply(unitPrice);
                templateCopeReceivable.setAmount(amount);
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
        if(CollUtil.isNotEmpty(templateCopeWithFormList)){
            List<TemplateCopeWith> list = new ArrayList<>();
            templateCopeWithFormList.forEach(templateCopeWithForm -> {
                TemplateCopeWith templateCopeWith = ConvertUtil.convert(templateCopeWithForm, TemplateCopeWith.class);
                templateCopeWith.setQie(id.intValue());
                //计算 总金额=数量 * 单价
                Integer c = templateCopeWith.getCount() == null ? 0 : templateCopeWith.getCount();//数量
                BigDecimal count = new BigDecimal(c.toString());
                BigDecimal unitPrice = templateCopeWith.getUnitPrice() == null ? new BigDecimal("0") : templateCopeWith.getUnitPrice();//单价
                BigDecimal amount = count.multiply(unitPrice);
                templateCopeWith.setAmount(amount);
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
        if(CollUtil.isNotEmpty(templateFileFormList)){
            List<TemplateFile> list = new ArrayList<>();
            templateFileFormList.forEach(templateFileForm -> {
                TemplateFile templateFile = ConvertUtil.convert(templateFileForm, TemplateFile.class);
                templateFile.setQie(id.intValue());
                list.add(templateFile);
            });
            //保存
            templateFileService.saveOrUpdateBatch(list);
        }
        return CommonResult.success("保存报价模板成功");
    }

    @Override
    public CommonResult<QuotationTemplateVO> lookQuotationTemplate(Long id) {
        QuotationTemplateVO quotationTemplateVO = quotationTemplateMapper.lookQuotationTemplate(id);

        Long qie = quotationTemplateVO.getId();//报价模板id
        //图片picUrl
        List<PicUrlArrForm> picUrlarr = new ArrayList<>();
        String picUrl = quotationTemplateVO.getPicUrl();
        if(picUrl != null && picUrl != ""){
            String[] picUrlArr = picUrl.split(",");
            List<String> picUrlList = Arrays.asList(picUrlArr);
            picUrlList.forEach(filePath -> {
                PicUrlArrForm picUrlArrForm = new PicUrlArrForm();
                picUrlArrForm.setFilePath(filePath);
                picUrlarr.add(picUrlArrForm);
            });
            quotationTemplateVO.setPicUrlarr(picUrlarr);
        }
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
            quotationTemplateVO.setGidarr(gList);
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
            List<QuotationType> goodsTypes = quotationTypeMapper.selectBatchIds(qidList);
            List<QuotationTypeVO> qList = ConvertUtil.convertList(goodsTypes, QuotationTypeVO.class);
            quotationTemplateVO.setQidarr(qList);
        }

        //报价对应应收费用明细list
        QueryWrapper<TemplateCopeReceivable> query1 = new QueryWrapper<>();
        query1.eq("qie", qie);
        List<TemplateCopeReceivable> templateCopeReceivables = templateCopeReceivableMapper.selectList(query1);
        List<TemplateCopeReceivableVO> templateCopeReceivableVOList =
                ConvertUtil.convertList(templateCopeReceivables, TemplateCopeReceivableVO.class);
        quotationTemplateVO.setTemplateCopeReceivableVOList(templateCopeReceivableVOList);

        //报价对应应付费用明细list
        List<TemplateCopeWithVO> templateCopeWithVOList = templateCopeWithMapper.selectListByQie(qie);
        quotationTemplateVO.setTemplateCopeWithVOList(templateCopeWithVOList);

        //模板对应模块信息list，文件信息
        List<TemplateFileVO> templateFileVOList = templateFileMapper.findTemplateFileByQie(qie);
        quotationTemplateVO.setTemplateFileVOList(templateFileVOList);
        return CommonResult.success(quotationTemplateVO);
    }

}
