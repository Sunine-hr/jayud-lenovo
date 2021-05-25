package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.QuotationDataTypeEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.*;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.po.*;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
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
    QuotationTypeMapper quotationTypeMapper;

    @Autowired
    TaskGroupMapper taskGroupMapper;

    @Autowired
    CurrencyInfoMapper currencyInfoMapper;

    @Autowired
    TemplateCopeReceivableMapper templateCopeReceivableMapper;

    @Autowired
    TemplateCopeWithMapper templateCopeWithMapper;

    @Autowired
    TemplateFileMapper templateFileMapper;

    @Autowired
    OrderCaseMapper orderCaseMapper;

    @Autowired
    QuotationTemplateMapper quotationTemplateMapper;

    @Autowired
    BaseService baseService;

    @Autowired
    ITemplateCopeReceivableService templateCopeReceivableService;

    @Autowired
    ITemplateCopeWithService templateCopeWithService;

    @Autowired
    ITemplateFileService templateFileService;

    @Autowired
    IQuotationTemplateService quotationTemplateService;

    @Override
    public IPage<OfferInfoVO> findOfferInfoByPage(QueryOfferInfoForm form) {
        //定义分页参数
        Page<OfferInfoVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
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
    public CommonResult saveOfferInfo(OfferInfoForm form) {
        OfferInfo offerInfo = ConvertUtil.convert(form, OfferInfo.class);
        Long id = form.getId();
        String names = form.getNames();
        //验证 预计到达时间>=开船日期>=（截单日期、截仓日期、截亏仓日期）
        LocalDateTime sailTime = form.getSailTime();
        LocalDateTime cutOffTime = form.getCutOffTime();
        LocalDateTime jcTime = form.getJcTime();
        LocalDateTime jkcTime = form.getJkcTime();
        LocalDateTime estimatedTime = form.getEstimatedTime();
        if(estimatedTime.compareTo(sailTime) < 0
                || sailTime.compareTo(cutOffTime) < 0
                || sailTime.compareTo(jcTime) < 0
                || sailTime.compareTo(jkcTime) < 0){
            return CommonResult.error(-1, "日期验证不通过，验证规则：预计到达时间>=开船日期>=（截单日期、截仓日期、截亏仓日期）");
        }
        if(ObjectUtil.isEmpty(id)){
//            //id 为空 ，代表新增
//            QueryWrapper<OfferInfo> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("names", names);
//            List<OfferInfo> list = this.list(queryWrapper);
//            if(CollUtil.isNotEmpty(list)){
//                return CommonResult.error(-1, "["+names+"],名称已存在");
//            }
            AuthUser user = baseService.getUser();
            offerInfo.setStatus("1");//状态(0无效 1有效)
            offerInfo.setUserId(user.getId().intValue());
            offerInfo.setUserName(user.getName());
            offerInfo.setCreateTime(LocalDateTime.now());
        }else{
//            //id 不为空 ，代表修改
//            QueryWrapper<OfferInfo> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("names", names);
//            queryWrapper.ne("id", id);
//            List<OfferInfo> list = this.list(queryWrapper);
//            if(CollUtil.isNotEmpty(list)){
//                return CommonResult.error(-1, "["+names+"],名称已存在");
//            }
        }

        // 获得报价模板数据
        QuotationTemplate quotationTemplate = quotationTemplateMapper.selectById(offerInfo.getQie());
        quotationTemplate.setId(null);
        quotationTemplate.setDataType(QuotationDataTypeEnum.MANAGEMENT.getCode());
        quotationTemplateService.saveOrUpdate(quotationTemplate);

        //报价模板id(quotation_template id)
        Integer qie = quotationTemplate.getId().intValue();
        offerInfo.setQie(qie);
        //修改保存报价模板的费用、文件
        List<TemplateCopeReceivableVO> templateCopeReceivableVOList = form.getTemplateCopeReceivableVOList();//报价对应应收费用明细list
        List<TemplateCopeWithVO> templateCopeWithVOList = form.getTemplateCopeWithVOList();//报价对应应付费用明细list
        List<TemplateFileVO> templateFileVOList = form.getTemplateFileVOList();//模板对应模块信息list，文件信息

        /*应收费用明细List*/
        List<TemplateCopeReceivableForm> templateCopeReceivableFormList = ConvertUtil.convertList(templateCopeReceivableVOList, TemplateCopeReceivableForm.class);
        //刪除
        QueryWrapper<TemplateCopeReceivable> queryWrapperTemplateCopeReceivable = new QueryWrapper<>();
        queryWrapperTemplateCopeReceivable.eq("qie", qie);
        templateCopeReceivableService.remove(queryWrapperTemplateCopeReceivable);

        if(CollUtil.isNotEmpty(templateCopeReceivableFormList)){
            List<TemplateCopeReceivable> list = new ArrayList<>();
            templateCopeReceivableFormList.forEach(templateCopeReceivableForm -> {
                TemplateCopeReceivable templateCopeReceivable = ConvertUtil.convert(templateCopeReceivableForm, TemplateCopeReceivable.class);
                templateCopeReceivable.setId(null);
                templateCopeReceivable.setQie(qie);
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
        List<TemplateCopeWithForm> templateCopeWithFormList = ConvertUtil.convertList(templateCopeWithVOList, TemplateCopeWithForm.class);
        //刪除
        QueryWrapper<TemplateCopeWith> queryWrapperTemplateCopeWith = new QueryWrapper<>();
        queryWrapperTemplateCopeWith.eq("qie", qie);
        templateCopeWithService.remove(queryWrapperTemplateCopeWith);
        if(CollUtil.isNotEmpty(templateCopeWithFormList)){
            List<TemplateCopeWith> list = new ArrayList<>();
            templateCopeWithFormList.forEach(templateCopeWithForm -> {
                TemplateCopeWith templateCopeWith = ConvertUtil.convert(templateCopeWithForm, TemplateCopeWith.class);
                templateCopeWith.setId(null);
                templateCopeWith.setQie(qie);
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
        List<TemplateFileForm> templateFileFormList = ConvertUtil.convertList(templateFileVOList, TemplateFileForm.class);
        //刪除
        QueryWrapper<TemplateFile> queryWrapperTemplateFile = new QueryWrapper<>();
        queryWrapperTemplateFile.eq("qie", qie);
        templateFileService.remove(queryWrapperTemplateFile);
        if(CollUtil.isNotEmpty(templateFileFormList)){
            List<TemplateFile> list = new ArrayList<>();
            templateFileFormList.forEach(templateFileForm -> {
                TemplateFile templateFile = ConvertUtil.convert(templateFileForm, TemplateFile.class);
                templateFile.setId(null);
                templateFile.setQie(qie);
                list.add(templateFile);
            });
            //保存
            templateFileService.saveOrUpdateBatch(list);
        }
        //保存报价
        this.saveOrUpdate(offerInfo);
        return CommonResult.success("保存报价，成功！");
    }

    @Override
    public CommonResult<OfferInfoVO> lookOfferInfo(Long id) {
        OfferInfoVO offerInfoVO = offerInfoMapper.selectOfferInfoVO(id);
        if(offerInfoVO == null){
            return CommonResult.error(-1, "报价不存在");
        }
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
//        QueryWrapper<TemplateCopeReceivable> query1 = new QueryWrapper<>();
//        query1.eq("qie", qie);
//        List<TemplateCopeReceivable> templateCopeReceivables = templateCopeReceivableMapper.selectList(query1);
//        List<TemplateCopeReceivableVO> templateCopeReceivableVOList =
//                ConvertUtil.convertList(templateCopeReceivables, TemplateCopeReceivableVO.class);
//        offerInfoVO.setTemplateCopeReceivableVOList(templateCopeReceivableVOList);
        List<TemplateCopeReceivableVO> templateCopeReceivableVOS = templateCopeReceivableMapper.findTemplateCopeReceivableByQie(qie);
        offerInfoVO.setTemplateCopeReceivableVOList(templateCopeReceivableVOS);

        //报价对应应付费用明细list
//        QueryWrapper<TemplateCopeWith> query2 = new QueryWrapper<>();
//        query2.eq("qie", qie);
//        List<TemplateCopeWith> templateCopeWiths = templateCopeWithMapper.selectList(query2);
//        List<TemplateCopeWithVO> templateCopeWithVOList =
//                ConvertUtil.convertList(templateCopeWiths, TemplateCopeWithVO.class);
//        offerInfoVO.setTemplateCopeWithVOList(templateCopeWithVOList);

        List<TemplateCopeWithVO> templateCopeWithVOS = templateCopeWithMapper.findTemplateCopeWithByQie(qie);
        offerInfoVO.setTemplateCopeWithVOList(templateCopeWithVOS);


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
        if(records.size() > 0){
            records.forEach(offerInfoVO -> {
                Integer qie = offerInfoVO.getQie();
                /*查询运价的规格：报价对应应收费用明细，海运费，尺寸*/
                List<TemplateCopeReceivableVO> oceanFeeList =
                        templateCopeReceivableMapper.findTemplateCopeReceivableOceanFeeByQie(qie);
                offerInfoVO.setOceanFeeList(oceanFeeList);
            });
        }
        return pageInfo;
    }

    @Override
    public OfferInfoVO lookOfferInfoFare(Long id) {
        List<CurrencyInfoVO> currencyInfoVOList = currencyInfoMapper.allCurrencyInfo();
        //将币种信息转换为map，cid为键，币种信息为值
        Map<Long, CurrencyInfoVO> cidMap = currencyInfoVOList.stream().collect(Collectors.toMap(CurrencyInfoVO::getId, c -> c));

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

        /*报价其他应收费用*/
        List<TemplateCopeReceivableVO> otherFeeList =
                templateCopeReceivableMapper.findTemplateCopeReceivableOtherFeeListByQie(qie);
        offerInfoVO.setOtherFeeList(otherFeeList);

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

        //操作说明 字符串切割换行
        String remarks = offerInfoVO.getRemarks();
        if(ObjectUtil.isNotEmpty(remarks)){
            String[] split = remarks.split("\n");
            List<String> list = Arrays.asList(split);
            offerInfoVO.setRemarksList(list);
        }

        //设置报价展示图片
        String picUrl = offerInfoVO.getPicUrl();
        List<PicUrlArrForm> picUrlarr = new ArrayList<>();
        if(ObjectUtil.isNotEmpty(picUrl)){
            String[] picUrlArr = picUrl.split(",");
            List<String> picUrlList = Arrays.asList(picUrlArr);
            picUrlList.forEach(filePath -> {
                PicUrlArrForm picUrlArrForm = new PicUrlArrForm();
                picUrlArrForm.setFilePath(filePath);
                picUrlarr.add(picUrlArrForm);
            });
        }
        offerInfoVO.setPicUrlarr(picUrlarr);

        //设置报价的海运费 最小值到最大值
        Map<Integer, BigDecimal> minMap = new HashMap<>();//最小值
        Map<Integer, BigDecimal> maxMap = new HashMap<>();//最大值
        BigDecimal min = oceanFeeList.get(0).getAmount();//最小值
        BigDecimal max = oceanFeeList.get(0).getAmount();//最大值
        Integer cid = oceanFeeList.get(0).getCid();
        for (int i =1; i<oceanFeeList.size(); i++){
            BigDecimal amount = oceanFeeList.get(i).getAmount();
            Integer cid2 = oceanFeeList.get(i).getCid();
            if(cid.equals(cid2)){
                cid = cid2;
            }
            if(min.compareTo(amount) == 1){ //min > amount
                min = amount;
            }
            if(max.compareTo(amount) == -1 ){ //max < amount
                max = amount;
            }
            minMap.put(cid, min);
            maxMap.put(cid, max);
        }
        String minAmountFormat = "";
        String maxAmountFormat = "";
        for (Map.Entry<Integer, BigDecimal> entry : minMap.entrySet()) {
            Integer key = entry.getKey();
            BigDecimal value = entry.getValue();
            CurrencyInfoVO currencyInfoVO = cidMap.get(Long.valueOf(key));
            minAmountFormat = value+" "+currencyInfoVO.getCurrencyCode();
        }
        for (Map.Entry<Integer, BigDecimal> entry : maxMap.entrySet()) {
            Integer key = entry.getKey();
            BigDecimal value = entry.getValue();
            CurrencyInfoVO currencyInfoVO = cidMap.get(Long.valueOf(key));
            maxAmountFormat = value+" "+currencyInfoVO.getCurrencyCode();
        }
        String amountRange = minAmountFormat+"~"+maxAmountFormat;
        offerInfoVO.setAmountRange(amountRange);
        return offerInfoVO;
    }

    @Override
    public OfferInfoVO purchaseOrders(Long id) {
        OfferInfoVO offerInfoVO = offerInfoMapper.lookOfferInfoFare(id);
        Integer qie = offerInfoVO.getQie();//报价模板id

        //rec应收费用信息
        //rec1.海运费：订柜尺寸(应收费用明细)
        /*订柜尺寸：海运费规格*/
        List<TemplateCopeReceivableVO> oceanFeeList =
                templateCopeReceivableMapper.findTemplateCopeReceivableOceanFeeByQie(qie);
        offerInfoVO.setOceanFeeList(oceanFeeList);
        //rec2.内陆费：集货仓库(应收费用明细)
        /*集货仓库：陆运费规格*/
        List<TemplateCopeReceivableVO> inlandFeeList =
                templateCopeReceivableMapper.findTemplateCopeReceivableInlandFeeListByQie(qie);
        offerInfoVO.setInlandFeeList(inlandFeeList);
        //rec3.其他应收费用
        //TODO

        /*目的地仓库：可达仓库*/
        String arriveWarehouse = offerInfoVO.getArriveWarehouse();
        if(arriveWarehouse != null && arriveWarehouse != ""){
            String[] arriveWarehouseArr = arriveWarehouse.split(",");
            List<String> arriveWarehouseList = Arrays.asList(arriveWarehouseArr);
            List<FabWarehouse> fabWarehouses = fabWarehouseMapper.selectBatchIds(arriveWarehouseList);
            List<FabWarehouseVO> fabWarehouseVOList = ConvertUtil.convertList(fabWarehouses, FabWarehouseVO.class);
            offerInfoVO.setFabWarehouseVOList(fabWarehouseVOList);
        }

        //TODO 订单下单时，根据运价，计算费用  1.应收费用  2.应付费用
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

    @Override
    public List<OfferInfoVO> findOfferInfoFareTop4(QueryOfferInfoFareForm form) {

        List<CurrencyInfoVO> currencyInfoVOList = currencyInfoMapper.allCurrencyInfo();
        //将币种信息转换为map，cid为键，币种信息为值
        Map<Long, CurrencyInfoVO> cidMap = currencyInfoVOList.stream().collect(Collectors.toMap(CurrencyInfoVO::getId, c -> c));

        List<OfferInfoVO> list = offerInfoMapper.findOfferInfoFareTop4(form);

        if(CollUtil.isNotEmpty(list)){
            list.forEach(offerInfoVO -> {
                //设置报价展示图片
                String picUrl = offerInfoVO.getPicUrl();
                List<PicUrlArrForm> picUrlarr = new ArrayList<>();
                if(ObjectUtil.isNotEmpty(picUrl)){
                    String[] picUrlArr = picUrl.split(",");
                    List<String> picUrlList = Arrays.asList(picUrlArr);
                    picUrlList.forEach(filePath -> {
                        PicUrlArrForm picUrlArrForm = new PicUrlArrForm();
                        picUrlArrForm.setFilePath(filePath);
                        picUrlarr.add(picUrlArrForm);
                    });
                }
                offerInfoVO.setPicUrlarr(picUrlarr);

                //设置报价的海运费 最小值到最大值

                /*查询运价的规格：报价对应应收费用明细，海运费，尺寸*/
                Integer qie = offerInfoVO.getQie();
                List<TemplateCopeReceivableVO> oceanFeeList = templateCopeReceivableMapper.findTemplateCopeReceivableOceanFeeByQie(qie);
                offerInfoVO.setOceanFeeList(oceanFeeList);

                Map<Integer, BigDecimal> minMap = new HashMap<>();
                Map<Integer, BigDecimal> maxMap = new HashMap<>();
                BigDecimal min = oceanFeeList.get(0).getAmount();//最小值
                BigDecimal max = oceanFeeList.get(0).getAmount();//最大值
                Integer cid = oceanFeeList.get(0).getCid();
                for (int i =1; i<oceanFeeList.size(); i++){
                    BigDecimal amount = oceanFeeList.get(i).getAmount();
                    Integer cid2 = oceanFeeList.get(i).getCid();
                    if(cid != cid2){
                        cid = cid2;
                    }
                    if(min.compareTo(amount) == 1){ //min > amount
                        min = amount;
                    }
                    if(max.compareTo(amount) == -1 ){ //max < amount
                        max = amount;
                    }
                    minMap.put(cid, min);
                    maxMap.put(cid, max);
                }
                String minAmountFormat = "";
                String maxAmountFormat = "";
                for (Map.Entry<Integer, BigDecimal> entry : minMap.entrySet()) {
                    Integer key = entry.getKey();
                    BigDecimal value = entry.getValue();
                    CurrencyInfoVO currencyInfoVO = cidMap.get(Long.valueOf(key));
                    minAmountFormat = value+" "+currencyInfoVO.getCurrencyCode();
                }
                for (Map.Entry<Integer, BigDecimal> entry : maxMap.entrySet()) {
                    Integer key = entry.getKey();
                    BigDecimal value = entry.getValue();
                    CurrencyInfoVO currencyInfoVO = cidMap.get(Long.valueOf(key));
                    maxAmountFormat = value+" "+currencyInfoVO.getCurrencyCode();
                }
                String amountRange = minAmountFormat+"~"+maxAmountFormat;
                offerInfoVO.setAmountRange(amountRange);
            });
        }
        return list;
    }

    @Override
    public OfferInfoDateVO calcOtherDate(OfferInfoForm form) {
        Integer qie = form.getQie();//报价模板id
        LocalDateTime sailTime = form.getSailTime();
        QuotationTemplateVO quotationTemplateVO = quotationTemplateMapper.lookQuotationTemplateById(Long.valueOf(qie));
        if (ObjectUtil.isEmpty(quotationTemplateVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "没有找到报价模板");
        }
        Integer cutOffTimeCalc = quotationTemplateVO.getCutOffTimeCalc() == null ? 0 : quotationTemplateVO.getCutOffTimeCalc();
        Integer jcTimeCalc = quotationTemplateVO.getJcTimeCalc() == null ? 0 : quotationTemplateVO.getJcTimeCalc();
        Integer jkcTimeCalc = quotationTemplateVO.getJkcTimeCalc() == null ? 0 : quotationTemplateVO.getJkcTimeCalc();
        Integer estimatedTimeCalc = quotationTemplateVO.getEstimatedTimeCalc() == null ? 0 : quotationTemplateVO.getEstimatedTimeCalc();
        OfferInfoDateVO offerInfoDateVO = new OfferInfoDateVO();
        offerInfoDateVO.setId(form.getId());
        offerInfoDateVO.setQie(qie);
        offerInfoDateVO.setSailTime(sailTime);
        offerInfoDateVO.setCutOffTime(sailTime.plusDays(cutOffTimeCalc));//计算截单日期
        offerInfoDateVO.setJcTime(sailTime.plusDays(jcTimeCalc));
        offerInfoDateVO.setJkcTime(sailTime.plusDays(jkcTimeCalc));
        offerInfoDateVO.setEstimatedTime(sailTime.plusDays(estimatedTimeCalc));
        return offerInfoDateVO;
    }

}
