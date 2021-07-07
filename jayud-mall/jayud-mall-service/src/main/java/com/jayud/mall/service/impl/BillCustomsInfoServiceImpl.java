package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.BillCustomsInfoMapper;
import com.jayud.mall.mapper.CustomsInfoCaseMapper;
import com.jayud.mall.mapper.OrderCustomsFileMapper;
import com.jayud.mall.model.bo.BillCustomsInfoQueryForm;
import com.jayud.mall.model.po.BillCustomsInfo;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.service.IBillCustomsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * (提单)报关信息表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-27
 */
@Service
public class BillCustomsInfoServiceImpl extends ServiceImpl<BillCustomsInfoMapper, BillCustomsInfo> implements IBillCustomsInfoService {

    @Autowired
    BillCustomsInfoMapper billCustomsInfoMapper;
    @Autowired
    OrderCustomsFileMapper orderCustomsFileMapper;
    @Autowired
    CustomsInfoCaseMapper customsInfoCaseMapper;

    @Override
    public List<CustomsInfoCaseVO> findCustomsInfoCase(Long b_id) {
        return billCustomsInfoMapper.findCustomsInfoCase(b_id);
    }

    @Override
    public BillCustomsInfoVO findBillCustomsInfoById(Long id) {
        return billCustomsInfoMapper.findBillCustomsInfoById(id);
    }

    @Override
    public List<CustomsInfoCaseExcelVO> findCustomsInfoCaseBybid(Long b_id) {
        List<CustomsInfoCaseExcelVO> customsInfoCaseExcelVOS = billCustomsInfoMapper.findCustomsInfoCaseBybid(b_id);
        return customsInfoCaseExcelVOS;
    }

    @Override
    public CustomsListExcelVO findCustomsListExcelById(Long id) {
        CustomsListExcelVO customsListExcelVO = billCustomsInfoMapper.findCustomsListExcelById(id);
        if(ObjectUtil.isEmpty(customsListExcelVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "报关清单不存在");
        }

        //查询导出数据
        //1.根据报关清单id，查询关联的箱子
        //2.根据箱子，查询关联的商品
        //3.根据商品id，查询关联的报关资料（商品的报关资料，后面要做合并）
        Long billCustomsInfoId = customsListExcelVO.getBillCustomsInfoId();//(提单)报关信息表id(bill_customs_info id)
        List<CustomsGoodsExcelVO> customsGoodsExcelList = billCustomsInfoMapper.findCustomsGoodsExcelByBillCustomsInfoId(billCustomsInfoId);

        List<CustomsGoodsExcelVO> excelVOList = new ArrayList<>();

        //根据报关商品id，分组统计
        Map<String, List<CustomsGoodsExcelVO>> stringListMap1 = groupListByIdCode(customsGoodsExcelList);

        for (Map.Entry<String, List<CustomsGoodsExcelVO>> entry : stringListMap1.entrySet()) {
            String idCode = entry.getKey();//IDcode，报关ID
            List<CustomsGoodsExcelVO> customsGoodsExcelVOS = entry.getValue();

            CustomsGoodsExcelVO customsGoodsExcelVO1 = customsGoodsExcelVOS.get(0);//默认取第一条 设置值
            CustomsGoodsExcelVO customsGoodsExcelVO = ConvertUtil.convert(customsGoodsExcelVO1, CustomsGoodsExcelVO.class);

            //需要计算的字段  箱数、总数量、总价、净重、毛重、立方

            //箱数、总数量、毛重、立方
            Map<String, Object> paraMap = new HashMap<>();
            paraMap.put("billCustomsInfoId", billCustomsInfoId);
            paraMap.put("idCode", idCode);
            Map<String, Object> totalMap = billCustomsInfoMapper.findCustomsGoodsExcelTotalByParaMap(paraMap);

            String packages = MapUtil.getStr(totalMap, "packages") == null ? "0" : MapUtil.getStr(totalMap, "packages");//总箱数
            String qty = MapUtil.getStr(totalMap, "qty") == null ? "0" : MapUtil.getStr(totalMap, "qty");//总数量
            String grossWeight = MapUtil.getStr(totalMap, "grossWeight") == null ? "0" : MapUtil.getStr(totalMap, "grossWeight");//毛重
            String cbm = MapUtil.getStr(totalMap, "cbm") == null ? "0" : MapUtil.getStr(totalMap, "cbm");//立方


            String declarePrice = customsGoodsExcelVO.getDeclarePrice();//单价
            String suttle = customsGoodsExcelVO.getSuttle();//商品净重

            BigDecimal declarePriceBigDecimal = new BigDecimal(declarePrice);
            BigDecimal suttleBigDecimal = new BigDecimal(suttle);
            BigDecimal qtyBigDecimal = new BigDecimal(qty);
            //总价、(总)净重
            //总价 = 单价 * 总数量
            BigDecimal totalPrice = declarePriceBigDecimal.multiply(qtyBigDecimal);
            //(总)净重 = 净重 * 总数量
            BigDecimal jz = suttleBigDecimal.multiply(qtyBigDecimal);

            customsGoodsExcelVO.setPackages(packages);
            customsGoodsExcelVO.setQty(qty);
            customsGoodsExcelVO.setGrossWeight(grossWeight);
            customsGoodsExcelVO.setCbm(cbm);
            customsGoodsExcelVO.setTotalPrice(totalPrice.toString());
            customsGoodsExcelVO.setJz(jz.toString());

            excelVOList.add(customsGoodsExcelVO);
        }

        //统计总数据     箱数、总数量、总价、净重、毛重、立方

        //总数量、总价、净重
        BigDecimal qtyBigDecimal = new BigDecimal("0");
        BigDecimal totalPriceBigDecimal = new BigDecimal("0");
        BigDecimal jzBigDecimal = new BigDecimal("0");
        for (int i=0; i<excelVOList.size(); i++){
            CustomsGoodsExcelVO customsGoodsExcelVO = excelVOList.get(i);
            int serialNumber = i + 1;
            customsGoodsExcelVO.setSerialNumber(String.valueOf(serialNumber));

            String qty = customsGoodsExcelVO.getQty();
            String totalPrice = customsGoodsExcelVO.getTotalPrice();
            String jz = customsGoodsExcelVO.getJz();
            qtyBigDecimal = qtyBigDecimal.add(new BigDecimal(qty));
            totalPriceBigDecimal = totalPriceBigDecimal.add(new BigDecimal(totalPrice));
            jzBigDecimal = jzBigDecimal.add(new BigDecimal(jz));
        }
        //箱数、毛重、立方                                       单独查询
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("billCustomsInfoId", billCustomsInfoId);
        Map<String, Object> totalMap = billCustomsInfoMapper.findCustomsListExcelTotalByParaMap(paraMap);


        String packages = MapUtil.getStr(totalMap, "packages") == null ? "0" : MapUtil.getStr(totalMap, "packages");//总箱数
        String grossWeight = MapUtil.getStr(totalMap, "grossWeight") == null ? "0" : MapUtil.getStr(totalMap, "grossWeight");//毛重
        String cbm = MapUtil.getStr(totalMap, "cbm") == null ? "0" : MapUtil.getStr(totalMap, "cbm");//立方


        customsListExcelVO.setPackages(packages);//总箱数
        customsListExcelVO.setQty(qtyBigDecimal.toString());//总数量
        customsListExcelVO.setTotalPrice(totalPriceBigDecimal.toString());//总价
        customsListExcelVO.setJz(jzBigDecimal.toString());//总净重
        customsListExcelVO.setGrossWeight(grossWeight);//总毛重
        customsListExcelVO.setCbm(cbm);//总立方

        customsListExcelVO.setCustomsGoodsExcelList(excelVOList);//报关商品Excel list

        return customsListExcelVO;
    }

    @Override
    public List<OrderInfoVO> findSelectOrderInfoByCustoms(BillCustomsInfoQueryForm form) {
        Long billCustomsInfoId = form.getId();//(提单)报关信息表id(bill_customs_info id)
        BillCustomsInfoVO billCustomsInfoVO = billCustomsInfoMapper.findBillCustomsInfoById(billCustomsInfoId);
        if(ObjectUtil.isEmpty(billCustomsInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "(提单)报关信息不存在");
        }
        Integer billId = billCustomsInfoVO.getBillId();//提单id(ocean_bill id)
        Integer type = billCustomsInfoVO.getType();//类型(0买单 1独立)
        form.setBillId(billId);
        form.setType(type);
        List<OrderInfoVO> orderInfoVOList = new ArrayList<>();
        if(type.equals(0)){
            //买单报关
            //根据 报关清单 关联的 订单箱子，查询订单list    need_declare = 0    0代表买单
            orderInfoVOList = customsInfoCaseMapper.findOrderInfoByBillCustomsInfoId(billCustomsInfoId);
        }else if(type.equals(1)){
            //独立报关
            //根据 报关清单 所属的 提单，查询订单list    need_declare = 1    1代表独立
            orderInfoVOList = customsInfoCaseMapper.findOrderInfoByBillId(billId);
        }
        //订单报关文件
        if(CollUtil.isNotEmpty(orderInfoVOList)){
            orderInfoVOList.forEach(orderInfoVO -> {
                Long orderId = orderInfoVO.getId();
                List<OrderCustomsFileVO> orderCustomsFileList = orderCustomsFileMapper.findOrderCustomsFileByOrderId(orderId);
                if(CollUtil.isNotEmpty(orderCustomsFileList)){
                    orderCustomsFileList.forEach(orderCustomsFileVO -> {
                        String templateUrl = orderCustomsFileVO.getTemplateUrl();
                        if(templateUrl != null && templateUrl.length() > 0){
                            String json = templateUrl;
                            try {
                                List<TemplateUrlVO> templateUrlVOS = JSON.parseObject(json, new TypeReference<List<TemplateUrlVO>>() {});
                                orderCustomsFileVO.setTemplateUrlVOS(templateUrlVOS);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                orderCustomsFileVO.setTemplateUrlVOS(new ArrayList<>());
                            }
                        }else{
                            orderCustomsFileVO.setTemplateUrlVOS(new ArrayList<>());
                        }
                    });
                }
                orderInfoVO.setOrderCustomsFileVOList(orderCustomsFileList);
            });
        }
        return orderInfoVOList;
    }

    /**
     * 根据报关商品id，分组统计
     * @param customsGoodsExcelList 查询导出数据
     * @return
     */
    public Map<String, List<CustomsGoodsExcelVO>> groupListByIdCode(List<CustomsGoodsExcelVO> customsGoodsExcelList) {
        Map<String, List<CustomsGoodsExcelVO>> map = new HashMap<>();
        for (CustomsGoodsExcelVO customsGoodsExcelVO : customsGoodsExcelList) {
            String key = customsGoodsExcelVO.getIdCode();
            List<CustomsGoodsExcelVO> tmpList = map.get(key);
            if (CollUtil.isEmpty(tmpList)) {
                tmpList = new ArrayList<>();
                tmpList.add(customsGoodsExcelVO);
                map.put(key, tmpList);
            } else {
                tmpList.add(customsGoodsExcelVO);
            }
        }
        return map;
    }





}
