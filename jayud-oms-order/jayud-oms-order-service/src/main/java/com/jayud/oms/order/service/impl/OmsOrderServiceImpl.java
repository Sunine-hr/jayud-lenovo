package com.jayud.oms.order.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.BaseResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.order.feign.AuthClient;
import com.jayud.oms.order.feign.CrmClient;
import com.jayud.oms.order.model.bo.*;
import com.jayud.oms.order.model.enums.StatusFlagEnums;
import com.jayud.oms.order.model.po.OmsOrderEntry;
import com.jayud.oms.order.model.po.OmsOrderFollow;
import com.jayud.oms.order.model.vo.OmsOrderEntryVO;
import com.jayud.oms.order.model.vo.OmsOrderFollowVO;
import com.jayud.oms.order.model.vo.OmsOrderVO;
import com.jayud.oms.order.model.vo.OrderVO;
import com.jayud.oms.order.service.IOmsOrderEntryService;
import com.jayud.oms.order.service.IOmsOrderFollowService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.oms.order.model.po.OmsOrder;
import com.jayud.oms.order.mapper.OmsOrderMapper;
import com.jayud.oms.order.service.IOmsOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 订单管理——订单主表 服务实现类
 *
 * @author jayud
 * @since 2022-03-23
 */
@Slf4j
@Service
public class OmsOrderServiceImpl extends ServiceImpl<OmsOrderMapper, OmsOrder> implements IOmsOrderService {


    @Autowired
    private OmsOrderMapper omsOrderMapper;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private IOmsOrderEntryService omsOrderEntryService;

    @Autowired
    private IOmsOrderFollowService omsOrderFollowService;

    @Autowired
    private CrmClient crmClient;

    @Override
    public IPage<OmsOrder> selectPage(OmsOrder omsOrder,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<OmsOrder> page=new Page<OmsOrder>(currentPage,pageSize);
        IPage<OmsOrder> pageList= omsOrderMapper.pageList(page, omsOrder);
        return pageList;
    }

    @Override
    public List<OmsOrder> selectList(OmsOrder omsOrder){
        return omsOrderMapper.list(omsOrder);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        omsOrderMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(List<Long> ids){
        List<OmsOrder> omsOrders = new ArrayList<>();
        for (Long id : ids) {
            OmsOrder omsOrder = new OmsOrder();
            omsOrder.setId(id);
            omsOrder.setUpdateBy(CurrentUserUtil.getUsername());
            omsOrder.setUpdateTime(new Date());
            omsOrder.setIsDeleted(true);
            omsOrders.add(omsOrder);
        }
        this.saveOrUpdateBatch(omsOrders);
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryOmsOrderForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryOmsOrderForExcel(paramMap);
    }

    @Override
    public BaseResult saveOmsOrder(InputOrderForm form) {
        OmsOrderForm orderForm = form.getOrderForm();
        OmsOrder omsOrder = ConvertUtil.convert(orderForm, OmsOrder.class);
        OmsOrderFollow omsOrderFollow = new OmsOrderFollow();
        if(form.getCmd().equals("preSubmit")){
            omsOrder.setStateFlag(StatusFlagEnums.PRE_SUBMIT.getCode());
        }else{
            omsOrder.setStateFlag(StatusFlagEnums.SUBMIT.getCode());
        }

         // 拼接业务类型字符串
        List<String> bizTypeList = orderForm.getBizTypeList();
        StringBuffer stringBuffer = new StringBuffer();
        for (String s : bizTypeList) {
            stringBuffer.append(s).append(",");
        }
        omsOrder.setBizType(stringBuffer.substring(0,stringBuffer.length()-1));

        if(null == omsOrder.getId()){
            BaseResult orderFeign = authClient.getOrderFeign("1002", new Date());
            HashMap data = (HashMap)orderFeign.getResult();
            omsOrder.setOrderNo(data.get("order").toString());
            omsOrder.setFLevel(Integer.parseInt(data.get("fLevel").toString()));
            omsOrder.setFStep(Integer.parseInt(data.get("fStep").toString()));
            omsOrder.setCheckStateFlag(data.get("checkStateFlag").toString());
            omsOrder.setCreateBy(CurrentUserUtil.getUsername());
            omsOrder.setCreateTime(new Date());
            omsOrderFollow.setFollowContext("创建了订单");
            omsOrderFollow.setFollowTime(new Date().toString());
        }else{
            omsOrder.setUpdateBy(CurrentUserUtil.getUsername());
            omsOrder.setUpdateTime(new Date());
            omsOrderFollow.setFollowContext("编辑了订单");
            omsOrderFollow.setFollowTime(new Date().toString());
        }
        boolean result = this.saveOrUpdate(omsOrder);
        if(result){
            log.warn("主订单新增成功");
        }
        //获取主订单id
        Long orderId = omsOrder.getId();

        //增加操作记录
        omsOrderFollow.setCreateBy(CurrentUserUtil.getUsername());
        omsOrderFollow.setCreateTime(new Date());
        omsOrderFollow.setOmsOrderId(orderId);
        boolean save = this.omsOrderFollowService.save(omsOrderFollow);
        if(save){
            log.warn("操作记录新增成功");
        }
        //主订单新增或修改成功,新增或修改子订单的信息
        //商品明细
        if(CollectionUtils.isNotEmpty(form.getOmsOrderEntryForms())){
            List<InputOmsOrderEntryForm> omsOrderEntryForms = form.getOmsOrderEntryForms();
            List<OmsOrderEntry> omsOrderEntries = ConvertUtil.convertList(omsOrderEntryForms, OmsOrderEntry.class);
            for (OmsOrderEntry omsOrderEntry : omsOrderEntries) {
                if(null != omsOrderEntry.getId()){
                    omsOrderEntry.setUpdateBy(CurrentUserUtil.getUsername());
                    omsOrderEntry.setUpdateTime(new Date());
                }else{
                    omsOrderEntry.setOmsOrderId(orderId);
                    omsOrderEntry.setCreateBy(CurrentUserUtil.getUsername());
                    omsOrderEntry.setCreateTime(new Date());
                }
            }
            boolean result1 = this.omsOrderEntryService.saveOrUpdateBatch(omsOrderEntries);
            if(result1){
                log.warn("商品明细新增成功");
            }
        }
        //附件
        if(CollectionUtils.isNotEmpty(form.getCrmFileForms())){
            List<CrmFileForm> crmFileForms = form.getCrmFileForms();
            BaseResult baseResult = crmClient.addFile(crmFileForms, orderId, "OmsOrder");
            if(baseResult.getCode().equals(200)){
                log.warn("附件新增成功");
            }
        }
        return BaseResult.ok();
    }

    @Override
    public BaseResult updateOmsOrderById(OmsOrderForm omsOrderForm) {
        OmsOrder omsOrder = ConvertUtil.convert(omsOrderForm, OmsOrder.class);
        omsOrder.setUpdateBy(CurrentUserUtil.getUsername());
        omsOrder.setUpdateTime(new Date());
        boolean result = this.saveOrUpdate(omsOrder);

        if(result){
            OmsOrderFollow omsOrderFollow = new OmsOrderFollow();
            omsOrderFollow.setOmsOrderId(omsOrderForm.getId());
            omsOrderFollow.setCreateBy(CurrentUserUtil.getUsername());
            omsOrderFollow.setCreateTime(new Date());
            omsOrderFollow.setFollowContext("修改了订单信息");
            boolean save = this.omsOrderFollowService.save(omsOrderFollow);
            if(save){
                log.warn("订单记录添加成功");
            }
            return BaseResult.ok();
        }
        return BaseResult.error(444,"修改失败");
    }

    @Override
    public BaseResult check(CheckForm checkForm) {
        BaseResult check = authClient.check(checkForm);
        OmsOrder omsOrder = this.getById(checkForm.getRecordId());
        OmsOrderFollow omsOrderFollow = new OmsOrderFollow();
        if(check.getCode().equals(200)){
            if(omsOrder.getFStep().equals(omsOrder.getFLevel())){
                omsOrderFollow.setFollowContext("完成了全部审核");
            }
            omsOrderFollow.setFollowContext("完成了第"+omsOrder.getFStep()+"级审核");
        }else {
            omsOrderFollow.setFollowContext("审核失败，"+check.getMsg());
        }
        omsOrderFollow.setOmsOrderId(checkForm.getRecordId());
        omsOrderFollow.setCreateBy(CurrentUserUtil.getUsername());
        omsOrderFollow.setCreateTime(new Date());
        boolean save = this.omsOrderFollowService.save(omsOrderFollow);
        if(save){
            log.warn("订单记录添加成功");
        }
        return check;
    }

    @Override
    public BaseResult unCheck(CheckForm checkForm) {
        BaseResult check = authClient.unCheck(checkForm);
        OmsOrder omsOrder = this.getById(checkForm.getRecordId());
        OmsOrderFollow omsOrderFollow = new OmsOrderFollow();
        if(check.getCode().equals(200)){
            if(omsOrder.getFStep().equals(0)){
                omsOrderFollow.setFollowContext("完成了全部反审核");
            }
            omsOrderFollow.setFollowContext("完成了第"+omsOrder.getFStep()+"级反审核");
        }else {
            omsOrderFollow.setFollowContext("反审核失败，"+check.getMsg());
        }
        omsOrderFollow.setOmsOrderId(checkForm.getRecordId());
        omsOrderFollow.setCreateBy(CurrentUserUtil.getUsername());
        omsOrderFollow.setCreateTime(new Date());
        boolean save = this.omsOrderFollowService.save(omsOrderFollow);
        if(save){
            log.warn("订单记录添加成功");
        }
        return check;
    }

    @Override
    public OrderVO getOrderById(Long id) {
        OmsOrder omsOrder = this.getById(id);
        OrderVO orderVO = new OrderVO();
        OmsOrderVO omsOrderVO = ConvertUtil.convert(omsOrder, OmsOrderVO.class);
        omsOrderVO.setBizTypeList(Arrays.asList(omsOrderVO.getBizType().split(",")));
        orderVO.setOrderForm(omsOrderVO);
        //填充订单明细信息
        List<OmsOrderEntryVO> omsOrderEntryForms = this.omsOrderEntryService.getByOrderId(id);
        if(CollectionUtils.isNotEmpty(omsOrderEntryForms)){
            orderVO.setOmsOrderEntryForms(omsOrderEntryForms);
        }
        //填充操作记录
        List<OmsOrderFollowVO> omsOrderFollowVOS = this.omsOrderFollowService.getByOrderId(id);
        if(CollectionUtils.isNotEmpty(omsOrderFollowVOS)){
            orderVO.setOmsOrderFollowVOS(omsOrderFollowVOS);
        }
        //填充附件信息
        List<CrmFileForm> files = this.crmClient.getFileList(id,"OmsOrder").getResult();
        if(CollectionUtils.isNotEmpty(files)){
            orderVO.setCrmFileForms(files);
        }
        //后续开发 海运、空运等  TODO

        return orderVO;
    }

    @Override
    public BaseResult copyNew(Long id) {
        OrderVO orderVO = this.getOrderById(id);
        //复制创建主订单
        OmsOrder omsOrder = ConvertUtil.convert(orderVO.getOrderForm(), OmsOrder.class);
        OmsOrderFollow omsOrderFollow = new OmsOrderFollow();
        omsOrder.setStateFlag(StatusFlagEnums.PRE_SUBMIT.getCode());

        BaseResult orderFeign = authClient.getOrderFeign("1002", new Date());
        HashMap data = (HashMap)orderFeign.getResult();
        omsOrder.setOrderNo(data.get("order").toString());
        omsOrder.setFLevel(Integer.parseInt(data.get("fLevel").toString()));
        omsOrder.setFStep(Integer.parseInt(data.get("fStep").toString()));
        omsOrder.setCheckStateFlag(data.get("checkStateFlag").toString());
        omsOrder.setId(null);
        omsOrder.setCreateBy(CurrentUserUtil.getUsername());
        omsOrder.setCreateTime(new Date());

        boolean result = this.saveOrUpdate(omsOrder);
        if(result){
            log.warn("复制主订单成功");
        }

        omsOrderFollow.setFollowContext("复制，创建了订单"+data.get("order").toString());
        omsOrderFollow.setFollowTime(new Date().toString());
        omsOrderFollow.setCreateBy(CurrentUserUtil.getUsername());
        omsOrderFollow.setCreateTime(new Date());
        omsOrderFollow.setOmsOrderId(orderVO.getOrderForm().getId());
        boolean save = this.omsOrderFollowService.save(omsOrderFollow);
        if(save){
            log.warn("操作记录新增成功");
        }

        Long orderId = omsOrder.getId();

        //复制订单明细
        List<OmsOrderEntryVO> omsOrderEntryForms = orderVO.getOmsOrderEntryForms();
        if(CollectionUtils.isNotEmpty(omsOrderEntryForms)){
            List<OmsOrderEntry> omsOrderEntries = ConvertUtil.convertList(omsOrderEntryForms, OmsOrderEntry.class);
            for (OmsOrderEntry omsOrderEntry : omsOrderEntries) {
                omsOrderEntry.setOmsOrderId(orderId);
                omsOrderEntry.setId(null);
                omsOrderEntry.setCreateBy(CurrentUserUtil.getUsername());
                omsOrderEntry.setCreateTime(new Date());
            }
            boolean result1 = this.omsOrderEntryService.saveBatch(omsOrderEntries);
            if(result1){
                log.warn("订单明细复制成功");
            }
        }

        //复制工作单 TODO

        return BaseResult.ok();
    }

}
