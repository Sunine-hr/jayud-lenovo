package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.*;
import com.jayud.mall.model.bo.OfferInfoForm;
import com.jayud.mall.model.bo.PicUrlArrForm;
import com.jayud.mall.model.bo.QueryOfferInfoFareForm;
import com.jayud.mall.model.bo.QueryOfferInfoForm;
import com.jayud.mall.model.po.*;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.IOfferInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    QuotationTypeMapper quotationTypeMapper;

    @Autowired
    TaskGroupMapper taskGroupMapper;

    @Autowired
    TemplateCopeReceivableMapper templateCopeReceivableMapper;

    @Autowired
    TemplateCopeWithMapper templateCopeWithMapper;

    @Autowired
    TemplateFileMapper templateFileMapper;

    @Autowired
    OrderCaseMapper orderCaseMapper;

    @Autowired
    BaseService baseService;

    @Override
    public IPage<OfferInfoVO> findOfferInfoByPage(QueryOfferInfoForm form) {
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
        if(form.getId() == null){
            AuthUser user = baseService.getUser();
            offerInfo.setStatus("1");//状态(0无效 1有效)
            offerInfo.setUserId(user.getId().intValue());
            offerInfo.setUserName(user.getName());
            offerInfo.setCreateTime(LocalDateTime.now());
        }
        this.saveOrUpdate(offerInfo);
    }

    @Override
    public CommonResult<OfferInfoVO> lookOfferInfo(Long id) {
        OfferInfoVO offerInfoVO = offerInfoMapper.selectOfferInfoVO(id);
        Integer qie = offerInfoVO.getQie();//报价模板id

        //图片picUrl
        List<PicUrlArrForm> picUrlarr = new ArrayList<>();
        String picUrl = offerInfoVO.getPicUrl();
        if(picUrl != null && picUrl != ""){
            String[] picUrlArr = picUrl.split(",");
            List<String> picUrlList = Arrays.asList(picUrlArr);
            picUrlList.forEach(filePath -> {
                PicUrlArrForm picUrlArrForm = new PicUrlArrForm();
                picUrlArrForm.setFilePath(filePath);
                picUrlarr.add(picUrlArrForm);
            });
            offerInfoVO.setPicUrlarr(picUrlarr);
        }

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
            offerInfoVO.setGidarr(gList);
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
            List<QuotationType> quotationTypes = quotationTypeMapper.selectBatchIds(qidList);
            List<QuotationTypeVO> qList = ConvertUtil.convertList(quotationTypes, QuotationTypeVO.class);
            offerInfoVO.setQidarr(qList);
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
        offerInfoVO.setTemplateFileVOList(templateFileVOList);
        return CommonResult.success(offerInfoVO);
    }

    @Override
    public IPage<OfferInfoVO> findOfferInfoFareByPage(QueryOfferInfoFareForm form) {
        //定义分页参数
        Page<OfferInfoVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.create_time"));
        IPage<OfferInfoVO> pageInfo = offerInfoMapper.findOfferInfoFareByPage(page, form);

        List<OfferInfoVO> records = pageInfo.getRecords();
        records.forEach(offerInfoVO -> {
            Integer qie = offerInfoVO.getQie();
            /*查询运价的规格：报价对应应收费用明细，海运费，尺寸*/
            List<TemplateCopeReceivableVO> oceanFeeList =
                    templateCopeReceivableMapper.findTemplateCopeReceivableOceanFeeByQie(qie);
            offerInfoVO.setOceanFeeList(oceanFeeList);
        });
        return pageInfo;
    }

    @Override
    public OfferInfoVO lookOfferInfoFare(Long id) {
        OfferInfoVO offerInfoVO = offerInfoMapper.lookOfferInfoFare(id);
        Integer qie = offerInfoVO.getQie();//报价模板id

        /*订柜尺寸：海运费规格*/
        List<TemplateCopeReceivableVO> oceanFeeList =
                templateCopeReceivableMapper.findTemplateCopeReceivableOceanFeeByQie(qie);
        offerInfoVO.setOceanFeeList(oceanFeeList);

        /*集货仓库：陆运费规格*/
        List<TemplateCopeReceivableVO> inlandFeeList =
                templateCopeReceivableMapper.findTemplateCopeReceivableInlandFeeListByQie(qie);
        offerInfoVO.setInlandFeeList(inlandFeeList);

//        /*集货仓库*/
//        String areaId = offerInfoVO.getAreaId();
//        if(areaId != null && areaId != ""){
//            String[] areaIdArr = areaId.split(",");
//            List<String> areaIdList = Arrays.asList(areaIdArr);
//            List<ShippingArea> shippingAreas = shippingAreaMapper.selectBatchIds(areaIdList);
//            List<ShippingAreaVO> shippingAreaVOList = ConvertUtil.convertList(shippingAreas, ShippingAreaVO.class);
//            offerInfoVO.setShippingAreaVOList(shippingAreaVOList);
//        }

        /*货物类型*/
        String gid = offerInfoVO.getGid();
        if(gid != null && gid != ""){
            String[] gidArr = gid.split(",");
            List<String> gidList = Arrays.asList(gidArr);
            List<GoodsType> goodsTypes = goodsTypeMapper.selectBatchIds(gidList);
            List<GoodsTypeVO> gList = ConvertUtil.convertList(goodsTypes, GoodsTypeVO.class);
            offerInfoVO.setGidarr(gList);
        }

        /*目的地仓库：可达仓库*/
        String arriveWarehouse = offerInfoVO.getArriveWarehouse();
        if(arriveWarehouse != null && arriveWarehouse != ""){
            String[] arriveWarehouseArr = arriveWarehouse.split(",");
            List<String> arriveWarehouseList = Arrays.asList(arriveWarehouseArr);
            List<FabWarehouse> fabWarehouses = fabWarehouseMapper.selectBatchIds(arriveWarehouseList);
            List<FabWarehouseVO> fabWarehouseVOList = ConvertUtil.convertList(fabWarehouses, FabWarehouseVO.class);
            offerInfoVO.setFabWarehouseVOList(fabWarehouseVOList);
        }
        return offerInfoVO;
    }

    @Override
    public OfferInfoVO purchaseOrders(Long id) {
        OfferInfoVO offerInfoVO = offerInfoMapper.lookOfferInfoFare(id);
        Integer qie = offerInfoVO.getQie();//报价模板id
        /*订柜尺寸：海运费规格*/
        List<TemplateCopeReceivableVO> oceanFeeList =
                templateCopeReceivableMapper.findTemplateCopeReceivableOceanFeeByQie(qie);
        offerInfoVO.setOceanFeeList(oceanFeeList);

        /*集货仓库：陆运费规格*/
        List<TemplateCopeReceivableVO> inlandFeeList =
                templateCopeReceivableMapper.findTemplateCopeReceivableInlandFeeListByQie(qie);
        offerInfoVO.setInlandFeeList(inlandFeeList);

        /*目的地仓库：可达仓库*/
        String arriveWarehouse = offerInfoVO.getArriveWarehouse();
        if(arriveWarehouse != null && arriveWarehouse != ""){
            String[] arriveWarehouseArr = arriveWarehouse.split(",");
            List<String> arriveWarehouseList = Arrays.asList(arriveWarehouseArr);
            List<FabWarehouse> fabWarehouses = fabWarehouseMapper.selectBatchIds(arriveWarehouseList);
            List<FabWarehouseVO> fabWarehouseVOList = ConvertUtil.convertList(fabWarehouses, FabWarehouseVO.class);
            offerInfoVO.setFabWarehouseVOList(fabWarehouseVOList);
        }

        return offerInfoVO;
    }

    @Override
    public List<FabWarehouseVO> findFabWarehouse(Long id) {
        OfferInfoVO offerInfoVO = offerInfoMapper.lookOfferInfoFare(id);
        Integer qie = offerInfoVO.getQie();//报价模板id
        List<FabWarehouseVO> fabWarehouseVOList = new ArrayList<>();
        //可达仓库List
        String arriveWarehouse = offerInfoVO.getArriveWarehouse();
        if(arriveWarehouse != null && arriveWarehouse != ""){
            String[] arriveWarehouseArr = arriveWarehouse.split(",");
            List<String> arriveWarehouseList = Arrays.asList(arriveWarehouseArr);
            List<FabWarehouse> fabWarehouses = fabWarehouseMapper.selectBatchIds(arriveWarehouseList);
            fabWarehouseVOList = ConvertUtil.convertList(fabWarehouses, FabWarehouseVO.class);
        }
        return fabWarehouseVOList;
    }

}
