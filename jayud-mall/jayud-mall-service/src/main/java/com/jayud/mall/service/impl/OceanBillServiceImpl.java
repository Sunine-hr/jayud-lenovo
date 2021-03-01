package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.BillCopePayMapper;
import com.jayud.mall.mapper.OceanBillMapper;
import com.jayud.mall.mapper.OceanCounterMapper;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.po.BillCopePay;
import com.jayud.mall.model.po.OceanBill;
import com.jayud.mall.model.po.OceanCounter;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.service.IBillCopePayService;
import com.jayud.mall.service.IBillTaskRelevanceService;
import com.jayud.mall.service.IOceanBillService;
import com.jayud.mall.service.IOceanCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    BillCopePayMapper billCopePayMapper;

    @Autowired
    IOceanCounterService oceanCounterService;

    @Autowired
    IBillTaskRelevanceService billTaskRelevanceService;

    @Autowired
    IBillCopePayService billCopePayService;

    @Override
    public IPage<OceanBillVO> findOceanBillByPage(QueryOceanBillForm form) {
        //定义分页参数
        Page<OceanBillVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<OceanBillVO> pageInfo = oceanBillMapper.findOceanBillByPage(page, form);

        //设置柜号list
        List<OceanBillVO> records = pageInfo.getRecords();
        records.forEach(oceanBillVO -> {
            Long obId = oceanBillVO.getId();
            QueryWrapper<OceanCounter> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ob_id", obId);
            List<OceanCounter> oceanCounters = oceanCounterMapper.selectList(queryWrapper);
            List<OceanCounterVO> oceanCounterVOList = ConvertUtil.convertList(oceanCounters, OceanCounterVO.class);
            oceanBillVO.setOceanCounterVOList(oceanCounterVOList);
        });
        return pageInfo;
    }

    /**
     * <p>保存提单信息</p>
     * <p>1个提单对应1个柜子</p>
     * <p>1个柜子对应N个运单箱号(订单)</p>
     * <p>1个运单对应N个箱号</p>
     * @param form
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<OceanBillVO> saveOceanBill(OceanBillForm form) {
        //1.保存提单
        OceanBill oceanBill = ConvertUtil.convert(form, OceanBill.class);
        this.saveOrUpdate(oceanBill);
        Long obId = oceanBill.getId();//提单id
        List<OceanCounterForm> oceanCounterForms = form.getOceanCounterForms();
        List<OceanCounter> oceanCounterList = new ArrayList<>();
        oceanCounterForms.forEach(oceanCounterForm -> {
            OceanCounter oceanCounter = ConvertUtil.convert(oceanCounterForm, OceanCounter.class);
            oceanCounter.setObId(obId);
            oceanCounterList.add(oceanCounter);
        });
        //先删除
        QueryWrapper<OceanCounter> oceanCounterQueryWrapper = new QueryWrapper<>();
        oceanCounterQueryWrapper.eq("ob_id", obId);
        oceanCounterService.remove(oceanCounterQueryWrapper);
        //2.保存提单对应的柜子
        oceanCounterService.saveOrUpdateBatch(oceanCounterList);
        OceanBillVO oceanBillVO = ConvertUtil.convert(oceanBill, OceanBillVO.class);

        //3.保存提单关联任务
        List<BillTaskRelevanceVO> billTaskRelevanceVOS =
                billTaskRelevanceService.savebillTaskRelevance(oceanBill);

        return CommonResult.success(oceanBillVO);
    }

    /**
     * <p>查看提单</p>
     * <p>1个提单对应1(N)个柜子</p>
     * <p>1个柜子对应N个运单</p>
     * <p>1个运单对应N个箱号</p>
     * @param id 提单id
     * @return
     */
    @Override
    public CommonResult<OceanBillVO> lookOceanBill(Long id) {
        //提单信息
        OceanBill oceanBill = oceanBillMapper.selectById(id);
        OceanBillVO oceanBillVO = ConvertUtil.convert(oceanBill, OceanBillVO.class);

        //1个提单对应1(N)个柜子
        Long obId = oceanBillVO.getId();
        QueryWrapper<OceanCounter> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","cntr_no","cabinet_code","volume","cost","cid","`status`","ob_id","create_time");
        queryWrapper.eq("ob_id", obId);
        List<OceanCounter> oceanCounterList = oceanCounterMapper.selectList(queryWrapper);
        List<OceanCounterVO> oceanCounterVOList = ConvertUtil.convertList(oceanCounterList, OceanCounterVO.class);

//        oceanCounterVOList.forEach( oceanCounterVO -> {
//            //1个柜子对应N个运单
//            Long oceanCounterId = oceanCounterVO.getId();
//            QueryWrapper<OceanWaybill> queryWrapperOceanWaybill = new QueryWrapper<>();
//            queryWrapperOceanWaybill.eq("ocean_counter_id", oceanCounterId);
//            List<OceanWaybill> oceanWaybillList = oceanWaybillMapper.selectList(queryWrapperOceanWaybill);
//            List<OceanWaybillVO> oceanWaybillVOList = ConvertUtil.convertList(oceanWaybillList, OceanWaybillVO.class);
//
//
//            oceanWaybillVOList.forEach(oceanWaybillVO -> {
//                //1个运单对应N个箱号
//                Long oceanWaybillId = oceanWaybillVO.getId();
//                QueryWrapper<OceanWaybillCaseRelation> queryWrapperOceanWaybillCaseRelation = new QueryWrapper<>();
//                List<OceanWaybillCaseRelationVO> xhxxList =
//                        oceanWaybillCaseRelationMapper.findXhxxByOceanWaybillId(oceanWaybillId);//根据运单id，查询箱号信息list
//                oceanWaybillVO.setOceanWaybillCaseRelationVOList(xhxxList);
//            });
//            oceanCounterVO.setOceanWaybillVOList(oceanWaybillVOList);
//        });
        oceanBillVO.setOceanCounterVOList(oceanCounterVOList);
        return CommonResult.success(oceanBillVO);
    }

    @Override
    public CommonResult<OceanBillVO> billLadingCost(Long id) {
        OceanBillVO oceanBillVO = oceanBillMapper.billLadingCost(id);
        if(oceanBillVO == null){
            return CommonResult.error(-1, "提单不存在");
        }
        Long billId = oceanBillVO.getId();
        //提单费用信息
        //根据提单id，查询提单费用（提单应收费用）
        List<BillCopePayVO> billCopePayVOS = billCopePayMapper.findBillCopePayByBillId(billId);

        BillCostInfoVO billCostInfoVO = new BillCostInfoVO();
        billCostInfoVO.setId(oceanBillVO.getId());
        billCostInfoVO.setSupplierId(oceanBillVO.getSupplierId());
        if(billCopePayVOS != null && billCopePayVOS.size() > 0){
            BigDecimal amountTotal = new BigDecimal("0");
            for (int i=0; i<billCopePayVOS.size(); i++){
                BillCopePayVO billCopePayVO = billCopePayVOS.get(i);
                BigDecimal amount = billCopePayVO.getAmount();
                amountTotal = amountTotal.add(amount);
            }
            String currencyName = billCopePayVOS.get(0).getCurrencyName();
            String billCopePayTotal = amountTotal.toString()+" "+currencyName;
            billCostInfoVO.setBillCopePayForms(billCopePayVOS);
            billCostInfoVO.setBillCopePayTotal(billCopePayTotal);
        }
        oceanBillVO.setBillCostInfoVO(billCostInfoVO);

        //提单对应的订单 以及 费用信息 TODO
        List<BillOrderCostInfoVO> billOrderCostInfoVOS = oceanBillMapper.findBillOrderCostInfo(id);
        oceanBillVO.setBillOrderCostInfoVOS(billOrderCostInfoVOS);

        return CommonResult.success(oceanBillVO);
    }

    @Override
    public CommonResult<BillCostInfoVO> saveBillCostInfo(BillCostInfoForm form) {
        Long billId = form.getId();//提单id
        OceanBillVO oceanBillVO = oceanBillMapper.billLadingCost(billId);
        if(oceanBillVO == null){
            return CommonResult.error(-1, "提单不存在");
        }
        Integer supplierId = form.getSupplierId();//供应商id
        List<BillCopePayForm> billCopePayForms = form.getBillCopePayForms();

        List<BillCopePay> billCopePays = new ArrayList<>();
        billCopePayForms.forEach(billCopePayForm -> {
            BillCopePay billCopePay = ConvertUtil.convert(billCopePayForm, BillCopePay.class);
            billCopePay.setBillId(billId);
            billCopePay.setSupplierId(supplierId);
            billCopePays.add(billCopePay);
        });
        //批量保存-提单费用
        billCopePayService.saveOrUpdateBatch(billCopePays);

        BillCostInfoVO billCostInfoVO = ConvertUtil.convert(form, BillCostInfoVO.class);
        List<BillCopePayVO> billCopePayVOS = ConvertUtil.convertList(billCopePays, BillCopePayVO.class);
        billCostInfoVO.setBillCopePayForms(billCopePayVOS);

        return CommonResult.success(billCostInfoVO);
    }

    @Override
    public CommonResult<OceanBillVO> shareEqually(BillCostInfoForm form) {
        Long billId = form.getId();//提单id
        //提单费用信息
        //根据提单id，查询提单费用（提单应收费用）
        List<BillCopePayVO> billCopePayVOS = billCopePayMapper.findBillCopePayByBillId(billId);
        if(billCopePayVOS == null || billCopePayVOS.size()==0){
            return CommonResult.error(-1, "提单费用不存在，请先保存提单费用，在一键均摊");
        }

        //提单对应的订单 以及 费用信息 TODO
        List<BillOrderCostInfoVO> billOrderCostInfoVOS = oceanBillMapper.findBillOrderCostInfo(billId);
        //分摊基数 base
        BigDecimal base = new BigDecimal("0");
        BigDecimal zero = new BigDecimal("0");
        for(int i=0; i<billOrderCostInfoVOS.size(); i++){
            BillOrderCostInfoVO billOrderCostInfoVO = billOrderCostInfoVOS.get(i);
            BigDecimal chargeWeight = billOrderCostInfoVO.getChargeWeight() == null ? new BigDecimal("0") : billOrderCostInfoVO.getChargeWeight();
            if (chargeWeight.compareTo(zero) != 1){
                // chargeWeight > zero ,返回1
                String orderNo = billOrderCostInfoVO.getOrderNo();
                return CommonResult.error(-1, "订单号["+orderNo+"],计费重不能小于或等于0");
            }
            base = base.add(chargeWeight);
        }




        return null;
    }
}
