package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddHubShippingEntryForm;
import com.jayud.scm.model.bo.AddHubShippingForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.enums.NoCodeEnum;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.mapper.HubShippingMapper;
import com.jayud.scm.model.vo.CheckOrderEntryVO;
import com.jayud.scm.model.vo.CheckOrderVO;
import com.jayud.scm.model.vo.HubShippingEntryVO;
import com.jayud.scm.model.vo.HubShippingVO;
import com.jayud.scm.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 出库单主表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@Service
public class HubShippingServiceImpl extends ServiceImpl<HubShippingMapper, HubShipping> implements IHubShippingService {

    @Autowired
    private IHubShippingFollowService hubShippingFollowService;

    @Autowired
    private IHubShippingEntryService hubShippingEntryService;

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ICommodityService commodityService;

    @Autowired
    private IBookingOrderFollowService bookingOrderFollowService;

    @Override
    public boolean delete(DeleteForm deleteForm) {
        List<HubShippingEntry> hubShippingEntries = new ArrayList<>();
        List<HubShippingFollow> hubShippingFollows = new ArrayList<>();
        for (Long id : deleteForm.getIds()) {

            List<HubShippingEntry> hubShippingEntries1 = hubShippingEntryService.getShippingEntryByShippingId(id);
            for (HubShippingEntry hubShippingEntry : hubShippingEntries1) {
                hubShippingEntry.setVoidedBy(deleteForm.getId().intValue());
                hubShippingEntry.setVoidedByDtm(deleteForm.getDeleteTime());
                hubShippingEntry.setVoidedByName(deleteForm.getName());
                hubShippingEntry.setVoided(1);
                hubShippingEntries.add(hubShippingEntry);
            }

            HubShippingFollow hubShippingFollow = new HubShippingFollow();
            hubShippingFollow.setShippingId(id.intValue());
            hubShippingFollow.setSType(OperationEnum.DELETE.getCode());
            hubShippingFollow.setFollowContext(OperationEnum.DELETE.getDesc()+id);
            hubShippingFollow.setCrtBy(deleteForm.getId().intValue());
            hubShippingFollow.setCrtByDtm(deleteForm.getDeleteTime());
            hubShippingFollow.setCrtByName(deleteForm.getName());
            hubShippingFollows.add(hubShippingFollow);
        }
        //删除出库明细
        boolean update = hubShippingEntryService.updateBatchById(hubShippingEntries);
        if(!update){
            log.warn("删除出库订单详情失败");
        }

        boolean b1 = hubShippingFollowService.saveBatch(hubShippingFollows);
        if(!b1){
            log.warn("操作记录表添加失败"+hubShippingFollows);
        }
        return b1;
    }

    @Override
    public HubShippingVO getHubShippingById(Integer id) {
        HubShipping hubShipping = this.getById(id);
        HubShippingVO hubShippingVO = ConvertUtil.convert(hubShipping, HubShippingVO.class);
        List<HubShippingEntry> hubShippingEntries = hubShippingEntryService.getShippingEntryByShippingId(hubShippingVO.getId().longValue());
        List<HubShippingEntryVO> hubShippingEntryVOS = ConvertUtil.convertList(hubShippingEntries, HubShippingEntryVO.class);
        hubShippingVO.setAddHubShippingEntryFormList(hubShippingEntryVOS);
        return hubShippingVO;
    }

    @Override
    public boolean signOrder(QueryCommonForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        List<HubShipping> hubShippings = new ArrayList<>();
        List<HubShippingFollow> hubShippingFollows = new ArrayList<>();

        for (Integer id : form.getIds()) {
            HubShipping shipping = new HubShipping();
            shipping.setId(id);
            shipping.setSignDate(LocalDateTime.now());
            shipping.setSignState(1);
            shipping.setMdyBy(systemUser.getId().intValue());
            shipping.setMdyByDtm(LocalDateTime.now());
            shipping.setMdyByName(systemUser.getUserName());

            hubShippings.add(shipping);

            HubShippingFollow hubShippingFollow = new HubShippingFollow();
            hubShippingFollow.setShippingId(id);
            hubShippingFollow.setSType(OperationEnum.UPDATE.getCode());
            hubShippingFollow.setFollowContext(systemUser.getUserName() + "签收出库单");
            hubShippingFollow.setCrtBy(systemUser.getId().intValue());
            hubShippingFollow.setCrtByDtm(LocalDateTime.now());
            hubShippingFollow.setCrtByName(systemUser.getUserName());
            hubShippingFollows.add(hubShippingFollow);

        }
        boolean update = this.updateBatchById(hubShippings);

        if(update){
            boolean save = this.hubShippingFollowService.saveBatch(hubShippingFollows);
            if(save){
                log.warn("签收出库单成功"+form.getIds());
            }
        }


        return update;
    }

    @Override
    public boolean saveOrUpdateHubShipping(AddHubShippingForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        HubShipping hubShipping = ConvertUtil.convert(form, HubShipping.class);
        HubShippingFollow hubShippingFollow = new HubShippingFollow();

        List<AddHubShippingEntryForm> addHubShippingEntryFormList = form.getAddHubShippingEntryFormList();
        for (AddHubShippingEntryForm addHubShippingEntryForm : addHubShippingEntryFormList) {
            hubShipping.setTotalQty((addHubShippingEntryForm.getQty()!=null ?addHubShippingEntryForm.getQty():new BigDecimal(0)).add(addHubShippingEntryForm.getQty()!=null ?addHubShippingEntryForm.getQty():new BigDecimal(0)));
            hubShipping.setTotaCbm((addHubShippingEntryForm.getCbm()!=null ?addHubShippingEntryForm.getCbm():new BigDecimal(0)).add(addHubShippingEntryForm.getCbm()!=null ?addHubShippingEntryForm.getCbm():new BigDecimal(0)));
            hubShipping.setTotalGw((addHubShippingEntryForm.getGw()!=null ?addHubShippingEntryForm.getGw():new BigDecimal(0)).add(addHubShippingEntryForm.getGw()!=null ?addHubShippingEntryForm.getGw():new BigDecimal(0)));
            hubShipping.setTotaNw((addHubShippingEntryForm.getNw()!=null ?addHubShippingEntryForm.getNw():new BigDecimal(0)).add(addHubShippingEntryForm.getNw()!=null ?addHubShippingEntryForm.getNw():new BigDecimal(0)));
            hubShipping.setTotaPackages((addHubShippingEntryForm.getPackages()!=null ?addHubShippingEntryForm.getPackages(): 0) + (addHubShippingEntryForm.getPackages()!=null ?addHubShippingEntryForm.getPackages():0));
            hubShipping.setTotaPallets((addHubShippingEntryForm.getPallets()!=null ?addHubShippingEntryForm.getPallets():0) + (addHubShippingEntryForm.getPallets()!=null ?addHubShippingEntryForm.getPallets():0));
        }

        if(hubShipping.getId() != null){
            hubShipping.setMdyByName(systemUser.getUserName());
            hubShipping.setMdyBy(systemUser.getId().intValue());
            hubShipping.setMdyByDtm(LocalDateTime.now());
            hubShippingFollow.setSType(OperationEnum.UPDATE.getCode());
            hubShippingFollow.setFollowContext(systemUser.getUserName()+"修改出库单，单号为"+hubShipping.getShippingNo());
        }else{
            hubShipping.setShippingNo(commodityService.getOrderNo(NoCodeEnum.HUB_SHIPPING.getCode(),LocalDateTime.now()));
            hubShipping.setCrtBy(systemUser.getId().intValue());
            hubShipping.setCrtByDtm(LocalDateTime.now());
            hubShipping.setCrtByName(systemUser.getUserName());
            hubShippingFollow.setSType(OperationEnum.INSERT.getCode());
            hubShippingFollow.setFollowContext(systemUser.getUserName()+"增加出库单，单号为"+hubShipping.getShippingNo());
        }
        boolean update = this.saveOrUpdate(hubShipping);
        if(update){
            log.warn("修改或新增出库单成功"+hubShipping);

            List<HubShippingEntry> hubShippingEntries = ConvertUtil.convertList(addHubShippingEntryFormList, HubShippingEntry.class);
            for (HubShippingEntry hubShippingEntry : hubShippingEntries) {
                if(hubShippingEntry.getId() != null){
                    hubShippingEntry.setShippingId(hubShipping.getId());
                    hubShippingEntry.setMdyBy(systemUser.getId().intValue());
                    hubShippingEntry.setMdyByDtm(LocalDateTime.now());
                    hubShippingEntry.setMdyByName(systemUser.getUserName());

                }else{
                    hubShippingEntry.setShippingId(hubShipping.getId());
                    hubShippingEntry.setCrtBy(systemUser.getId().intValue());
                    hubShippingEntry.setCrtByDtm(LocalDateTime.now());
                    hubShippingEntry.setCrtByName(systemUser.getUserName());
                }

            }
            boolean b = this.hubShippingEntryService.saveOrUpdateBatch(hubShippingEntries);
            if(!b){
                log.warn("修改或新增出库单明细失败"+hubShippingEntries);
            }
            hubShippingFollow.setCrtBy(systemUser.getId().intValue());
            hubShippingFollow.setCrtByDtm(LocalDateTime.now());
            hubShippingFollow.setCrtByName(systemUser.getUserName());
            hubShippingFollow.setShippingId(hubShipping.getId());
            boolean save = this.hubShippingFollowService.save(hubShippingFollow);
            if(save){
                log.warn("修改或者新增出库单，出库操作日志添加成功"+hubShippingFollow);
            }
        }
        return update;
    }

    @Override
    public List<HubShippingVO> getHubShippingByBookingId(QueryCommonForm form) {

        List<HubShippingVO> hubShippingVOS = new ArrayList<>();
        for (Integer id : form.getIds()) {
            QueryWrapper<HubShipping> queryWrapper = new QueryWrapper();
            queryWrapper.lambda().eq(HubShipping::getBookingId,id);
            List<HubShipping> list = this.list(queryWrapper);
            List<HubShippingVO> shippingVOS = ConvertUtil.convertList(list, HubShippingVO.class);
            for (HubShippingVO hubShippingVO : shippingVOS) {
                List<HubShippingEntry> hubShippingEntryVOS = hubShippingEntryService.getShippingEntryByShippingId(hubShippingVO.getId().longValue());
                hubShippingVO.setAddHubShippingEntryFormList(ConvertUtil.convertList(hubShippingEntryVOS, HubShippingEntryVO.class));
            }
            hubShippingVOS.addAll(shippingVOS);
        }
        return hubShippingVOS;
    }

    @Override
    public CommonResult automaticGenerationHubShipping(QueryCommonForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        Map<String,Object> map = new HashMap<>();
        map.put("orderId",form.getId());
        map.put("userId",systemUser.getId());
        map.put("userName",systemUser.getUserName());
        this.baseMapper.automaticGenerationHubShipping(map);
        if(map.get("state").equals(1)){
            return CommonResult.error((Integer)map.get("state"),(String)map.get("string"));
        }
        BookingOrderFollow bookingOrderFollow = new BookingOrderFollow();
        bookingOrderFollow.setCrtBy(systemUser.getId().intValue());
        bookingOrderFollow.setCrtByDtm(LocalDateTime.now());
        bookingOrderFollow.setCrtByName(systemUser.getUserName());
        bookingOrderFollow.setBookingId(form.getId());
        bookingOrderFollow.setSType(OperationEnum.INSERT.getCode());
        bookingOrderFollow.setFollowContext("自动生成提验货成功");
        boolean save = bookingOrderFollowService.save(bookingOrderFollow);
        if(save){
            log.warn("自动生成提验货，操作日志添加成功");
        }
        return CommonResult.success();
    }
}
