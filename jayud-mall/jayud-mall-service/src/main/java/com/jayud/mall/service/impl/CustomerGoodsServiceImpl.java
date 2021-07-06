package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.CustomerGoodsEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.*;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.po.*;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.model.vo.domain.CustomerUser;
import com.jayud.mall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户商品表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Service
public class CustomerGoodsServiceImpl extends ServiceImpl<CustomerGoodsMapper, CustomerGoods> implements ICustomerGoodsService {

    @Autowired
    CustomerGoodsMapper customerGoodsMapper;
    @Autowired
    ServiceGroupMapper serviceGroupMapper;
    @Autowired
    GoodsServiceCostMapper goodsServiceCostMapper;
    @Autowired
    BusinessLogMapper businessLogMapper;
    @Autowired
    GoodsCustomsValueMapper goodsCustomsValueMapper;
    @Autowired
    GoodsClearanceValueMapper goodsClearanceValueMapper;
    @Autowired
    GoodsCustomsFileMapper goodsCustomsFileMapper;
    @Autowired
    GoodsClearanceFileMapper goodsClearanceFileMapper;

    @Autowired
    BaseService baseService;
    @Autowired
    IGoodsServiceCostService goodsServiceCostService;
    @Autowired
    IBusinessLogService businessLogService;
    @Autowired
    IGoodsCustomsValueService goodsCustomsValueService;
    @Autowired
    IGoodsClearanceValueService goodsClearanceValueService;
    @Autowired
    IGoodsCustomsFileService goodsCustomsFileService;
    @Autowired
    IGoodsClearanceFileService goodsClearanceFileService;


    @Override
    public IPage<CustomerGoodsVO> findCustomerGoodsByPage(QueryCustomerGoodsForm form) {
        //定义分页参数
        Page<CustomerGoodsVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.asc("t.id"));

        Long offerInfoId = ObjectUtil.isEmpty(form.getOfferInfoId()) ? 0 : form.getOfferInfoId();
        form.setOfferInfoId(offerInfoId);
        IPage<CustomerGoodsVO> pageInfo = customerGoodsMapper.findCustomerGoodsByPage(page, form);

        List<CustomerGoodsVO> records = pageInfo.getRecords();
        records.forEach(customerGoodsVO -> {
            String isNeedFee = customerGoodsVO.getIsNeedFee();//是否需要附加费(1需要 2不需要)
            String serviceUnitPrice = customerGoodsVO.getServiceUnitPrice();
            if(ObjectUtil.isNotEmpty(isNeedFee)){
                if(isNeedFee.equals("2")){
                    serviceUnitPrice = "无附加费";
                }else{
                    if(ObjectUtil.isEmpty(serviceUnitPrice)){
                        serviceUnitPrice = "附加费待定";
                    }
                }
            }else{
                serviceUnitPrice = "附加费待定";
            }
            customerGoodsVO.setServiceUnitPrice(serviceUnitPrice);
        });

        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult auditCustomerGoods(CustomerGoodsAuditForm form) {
        //记录审核业务日志，审核操作前的数据 和 审核操作后的数据，作对比，不同则记录日志
        AuthUser user = baseService.getUser();
        //查询商品list  list 转 map  审核操作前的数据
        List<CustomerGoodsVO> customerGoodsVOList = customerGoodsMapper.findCustomerGoodsByIds(form.getIds());
        Map<Integer, CustomerGoodsVO> customerGoodsMap = customerGoodsVOList.stream().collect(Collectors.toMap(CustomerGoodsVO::getId, Function.identity()));
        //查询服务list list 转 map
        ServiceGroupForm serviceGroupForm = new ServiceGroupForm();
        List<ServiceGroupVO> serviceGroupList = serviceGroupMapper.findServiceGroup(serviceGroupForm);
        Map<Long, ServiceGroupVO> serviceGroupMap = serviceGroupList.stream().collect(Collectors.toMap(ServiceGroupVO::getId, Function.identity()));

        List<Integer> ids = form.getIds();
        Integer status = form.getStatus();
        String statusName = CustomerGoodsEnum.getName(status);
        String dataCode = form.getDataCode();
        String clearanceCode = form.getClearanceCode();
        List<CustomerGoods> customerGoodsList = new ArrayList<>();//商品list
        List<GoodsServiceCost> goodsServiceCostList = new ArrayList<>();//商品服务费用list
        ids.forEach(id -> {
            CustomerGoodsVO customerGoodsVO = customerGoodsMap.get(id);

            CustomerGoods customerGoods = new CustomerGoods();
            customerGoods.setId(id);
            customerGoods.setStatus(status);
            customerGoods.setStatusName(statusName);
            customerGoods.setDataCode(dataCode);
            customerGoods.setClearanceCode(clearanceCode);
            customerGoodsList.add(customerGoods);

            List<GoodsServiceCost> goodsServiceCostList1 = form.getGoodsServiceCostList();
            if(CollUtil.isNotEmpty(goodsServiceCostList1)){
                List<GoodsServiceCost> goodsServiceCosts = new ArrayList<>();
                goodsServiceCostList1.forEach(goodsServiceCost -> {

                    if(ObjectUtil.isNotEmpty(goodsServiceCost.getServiceId())){
                        Long serviceId = goodsServiceCost.getServiceId();
                        ServiceGroupVO serviceGroupVO = serviceGroupMap.get(serviceId);

                        goodsServiceCost.setGoodId(Long.valueOf(id));
                        goodsServiceCost.setCustomerId(Long.valueOf(customerGoodsVO.getCustomerId()));
                        goodsServiceCost.setNameCn(customerGoodsVO.getNameCn());
                        goodsServiceCost.setServiceName(serviceGroupVO.getCodeName());
                        goodsServiceCost.setCustomerName(customerGoodsVO.getCustomerName());

                        goodsServiceCosts.add(goodsServiceCost);
                    }

                });

                goodsServiceCostList.addAll(goodsServiceCosts);
            }


        });
        //1.批量保存，商品
        this.saveOrUpdateBatch(customerGoodsList);
        //2.批量保存，商品服务费用
        QueryWrapper<GoodsServiceCost> qw = new QueryWrapper<>();
        qw.in("good_id", ids);
        goodsServiceCostService.remove(qw);
        if(CollUtil.isNotEmpty(goodsServiceCostList)){
            goodsServiceCostService.saveOrUpdateBatch(goodsServiceCostList);
        }

        //3.保存商品审核，业务操作日志
        List<BusinessLog> businessLogs1 = new ArrayList<>();
        customerGoodsVOList.forEach(customerGoodsVO -> {
            String frontJson = JSONUtil.toJsonStr(customerGoodsVO);
            CustomerGoodsVO front = JSONUtil.toBean(frontJson, CustomerGoodsVO.class);
            Integer id = customerGoodsVO.getId();
            CustomerGoodsVO customerGoodsById = customerGoodsMapper.findCustomerGoodsById(id);
            String afterJson = JSONUtil.toJsonStr(customerGoodsById);
            CustomerGoodsVO after = JSONUtil.toBean(afterJson, CustomerGoodsVO.class);
            if(!frontJson.equals(afterJson)){
                //对象操作前后json不相同，保存日志
                BusinessLog businessLog = new BusinessLog();
                businessLog.setUserId(user.getId().intValue());//操作人id(system_user id)
                businessLog.setUserName(user.getName());//操作人name(system_user name)
                businessLog.setBusinessTb("customer_goods");//业务表tb
                businessLog.setBusinessName("客户商品表");//业务表(中文)name
                businessLog.setBusinessOperation("update");//业务操作(insert update delete)
                businessLog.setOperationFront(frontJson);//操作前(text)
                businessLog.setOperationAfter(afterJson);//操作后(text)
                businessLogs1.add(businessLog);
            }

        });
        if(CollUtil.isNotEmpty(businessLogs1)){
            businessLogService.saveOrUpdateBatch(businessLogs1);
        }
        return CommonResult.success("审核成功!");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerGoodsVO saveCustomerGoods(CustomerGoodsForm form) {
        Integer customerId = form.getCustomerId();
        CustomerGoods customerGoods = ConvertUtil.convert(form, CustomerGoods.class);
        if(ObjectUtil.isEmpty(customerId)){
            CustomerUser customerUser = baseService.getCustomerUser();
            customerGoods.setCustomerId(customerUser.getId());
        }
        //1.保存客户商品信息
        this.saveOrUpdate(customerGoods);

        Integer goodId = customerGoods.getId();
        //2.保存商品关联表信息
        //保存-商品报关申报价值
        List<GoodsCustomsValueForm> goodsCustomsValueList = form.getGoodsCustomsValueList();
        if(CollUtil.isNotEmpty(goodsCustomsValueList)){
            goodsCustomsValueList.forEach(goodsCustomsValueForm -> {
                goodsCustomsValueForm.setGoodId(goodId);
            });
            QueryWrapper<GoodsCustomsValue> qw = new QueryWrapper<>();
            qw.eq("good_id", goodId);
            goodsCustomsValueService.remove(qw);
            List<GoodsCustomsValue> goodsCustomsValues = ConvertUtil.convertList(goodsCustomsValueList, GoodsCustomsValue.class);
            goodsCustomsValueService.saveOrUpdateBatch(goodsCustomsValues);
        }
        //保存-商品清关申报价值
        List<GoodsClearanceValueForm> goodsClearanceValueList = form.getGoodsClearanceValueList();
        if(CollUtil.isNotEmpty(goodsClearanceValueList)){
            goodsClearanceValueList.forEach(goodsClearanceValueForm -> {
                goodsClearanceValueForm.setGoodId(goodId);
            });
            QueryWrapper<GoodsClearanceValue> qw = new QueryWrapper<>();
            qw.eq("good_id", goodId);
            goodsClearanceValueService.remove(qw);
            List<GoodsClearanceValue> goodsClearanceValues = ConvertUtil.convertList(goodsClearanceValueList, GoodsClearanceValue.class);
            goodsClearanceValueService.saveOrUpdateBatch(goodsClearanceValues);
        }
        //保存-商品报关文件列表
        List<GoodsCustomsFileForm> goodsCustomsFileList = form.getGoodsCustomsFileList();
        if(CollUtil.isNotEmpty(goodsCustomsFileList)){
            goodsCustomsFileList.forEach(goodsCustomsFileForm -> {
                goodsCustomsFileForm.setGoodId(goodId);
                List<TemplateUrlVO> templateUrls = goodsCustomsFileForm.getTemplateUrls();
                if(CollUtil.isNotEmpty(templateUrls)){
                    String json = JSONUtil.toJsonStr(templateUrls);
                    goodsCustomsFileForm.setTemplateUrl(json);//模版文件地址
                }
            });
            QueryWrapper<GoodsCustomsFile> qw = new QueryWrapper<>();
            qw.eq("good_id", goodId);
            goodsCustomsFileService.remove(qw);
            List<GoodsCustomsFile> goodsCustomsFiles = ConvertUtil.convertList(goodsCustomsFileList, GoodsCustomsFile.class);
            goodsCustomsFileService.saveOrUpdateBatch(goodsCustomsFiles);
        }
        //保存-商品清关文件列表
        List<GoodsClearanceFileForm> goodsClearanceFileList = form.getGoodsClearanceFileList();
        if(CollUtil.isNotEmpty(goodsClearanceFileList)){
            goodsClearanceFileList.forEach(goodsClearanceFileForm -> {
                goodsClearanceFileForm.setGoodId(goodId);
                List<TemplateUrlVO> templateUrls = goodsClearanceFileForm.getTemplateUrls();
                if(CollUtil.isNotEmpty(templateUrls)){
                    String json = JSONUtil.toJsonStr(templateUrls);
                    goodsClearanceFileForm.setTemplateUrl(json);//模版文件地址
                }
            });
            QueryWrapper<GoodsClearanceFile> qw = new QueryWrapper<>();
            qw.eq("good_id", goodId);
            goodsClearanceFileService.remove(qw);
            List<GoodsClearanceFile> goodsClearanceFiles = ConvertUtil.convertList(goodsClearanceFileList, GoodsClearanceFile.class);
            goodsClearanceFileService.saveOrUpdateBatch(goodsClearanceFiles);
        }
        CustomerGoodsVO customerGoodsVO = ConvertUtil.convert(customerGoods, CustomerGoodsVO.class);
        return customerGoodsVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveCustomerGoods(List<CustomerGoodsVO> list) {
        CustomerUser customerUser = baseService.getCustomerUser();
        list.forEach(customerGoodsVO -> {
            String typesName = customerGoodsVO.getTypesName();
            customerGoodsVO.setCustomerId(customerUser.getId());//客户ID(customer id)
            customerGoodsVO.setStatus(0);//审核状态代码：1-审核通过，0-等待审核，-1-审核不通过
            if(typesName != null && typesName.equalsIgnoreCase("普货")){
                customerGoodsVO.setTypes(1);
            }else if(typesName != null && typesName.equalsIgnoreCase("特货")){
                customerGoodsVO.setTypes(2);
            }
        });
        List<CustomerGoods> customerGoods = ConvertUtil.convertList(list, CustomerGoods.class);
        this.saveOrUpdateBatch(customerGoods);
    }

    @Override
    public CommonResult<CustomerGoodsVO> findCustomerGoodsById(Integer id) {
        //1.查询商品
        CustomerGoodsVO customerGoodsVO = customerGoodsMapper.findCustomerGoodsById(id);
        if(ObjectUtil.isEmpty(customerGoodsVO)){
            return CommonResult.error(-1, "没有找到商品");
        }
        Integer goodId = customerGoodsVO.getId();
        //2.查询商品关联信息
        //查询-商品报关申报价值
        List<GoodsCustomsValueVO> goodsCustomsValueList = goodsCustomsValueMapper.findGoodsCustomsValueByGoodId(goodId);
        customerGoodsVO.setGoodsCustomsValueList(goodsCustomsValueList);
        //查询-商品清关申报价值
        List<GoodsClearanceValueVO> goodsClearanceValueList = goodsClearanceValueMapper.findGoodsClearanceValueByGoodId(goodId);
        customerGoodsVO.setGoodsClearanceValueList(goodsClearanceValueList);
        //查询-商品报关文件列表
        List<GoodsCustomsFileVO> goodsCustomsFileList = goodsCustomsFileMapper.findGoodsCustomsFileByGoodId(goodId);
        if(CollUtil.isNotEmpty(goodsCustomsFileList)){
            goodsCustomsFileList.forEach(goodsCustomsFileVO -> {
                String templateUrl = goodsCustomsFileVO.getTemplateUrl();
                if(StrUtil.isNotEmpty(templateUrl)){
                    try {
                        List<TemplateUrlVO> templateUrls = JSON.parseObject(templateUrl, new TypeReference<List<TemplateUrlVO>>() {});
                        goodsCustomsFileVO.setTemplateUrls(templateUrls);
                    } catch (Exception e) {
                        goodsCustomsFileVO.setTemplateUrls(new ArrayList<>());
                    }
                }else{
                    goodsCustomsFileVO.setTemplateUrls(new ArrayList<>());
                }
            });
        }
        customerGoodsVO.setGoodsCustomsFileList(goodsCustomsFileList);
        //查询-商品清关文件列表
        List<GoodsClearanceFileVO> goodsClearanceFileList = goodsClearanceFileMapper.findGoodsClearanceFileByGoodId(goodId);
        if(CollUtil.isNotEmpty(goodsClearanceFileList)){
            goodsClearanceFileList.forEach(goodsClearanceFileVO -> {
                String templateUrl = goodsClearanceFileVO.getTemplateUrl();
                if(StrUtil.isNotEmpty(templateUrl)){
                    try {
                        List<TemplateUrlVO> templateUrls = JSON.parseObject(templateUrl, new TypeReference<List<TemplateUrlVO>>() {});
                        goodsClearanceFileVO.setTemplateUrls(templateUrls);
                    } catch (Exception e) {
                        goodsClearanceFileVO.setTemplateUrls(new ArrayList<>());
                    }
                }else{
                    goodsClearanceFileVO.setTemplateUrls(new ArrayList<>());
                }
            });
        }
        customerGoodsVO.setGoodsClearanceFileList(goodsClearanceFileList);
        return CommonResult.success(customerGoodsVO);
    }

    @Override
    public CustomerGoodsVO findCustomerGoodsCostById(Integer id) {
        CustomerGoodsVO customerGoodsVO = customerGoodsMapper.findCustomerGoodsById(id);
        Long goodId = Long.valueOf(id);
        List<GoodsServiceCostVO> goodsServiceCostList = goodsServiceCostMapper.findGoodsServiceCostByGoodId(goodId);
        customerGoodsVO.setGoodsServiceCostList(goodsServiceCostList);
        return customerGoodsVO;
    }

    @Override
    public void stopOrEnabled(CustomerGoodsIsValidForm form) {
        Integer id = form.getId();
        CustomerGoodsVO customerGoodsVO = customerGoodsMapper.findCustomerGoodsById(id);
        if(ObjectUtil.isEmpty(customerGoodsVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "没有找到客户商品");
        }
        CustomerGoods customerGoods = ConvertUtil.convert(customerGoodsVO, CustomerGoods.class);
        Integer isValid = form.getIsValid();
        customerGoods.setIsValid(isValid);
        this.saveOrUpdate(customerGoods);
    }
}
