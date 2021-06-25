package com.jayud.oceanship.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.oceanship.bo.*;
import com.jayud.oceanship.feign.OauthClient;
import com.jayud.oceanship.feign.OmsClient;
import com.jayud.oceanship.po.SeaBill;
import com.jayud.oceanship.mapper.SeaBillMapper;
import com.jayud.oceanship.po.SeaContainerInformation;
import com.jayud.oceanship.po.SeaOrder;
import com.jayud.oceanship.service.ISeaBillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oceanship.service.ISeaContainerInformationService;
import com.jayud.oceanship.service.ISeaOrderService;
import com.jayud.oceanship.vo.SeaBillFormVO;
import com.jayud.oceanship.vo.SeaBillVO;
import com.jayud.oceanship.vo.SeaContainerInformationVO;
import com.jayud.oceanship.vo.SeaOrderFormVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 提单信息表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-06-23
 */
@Service
public class SeaBillServiceImpl extends ServiceImpl<SeaBillMapper, SeaBill> implements ISeaBillService {

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private OauthClient oauthClient;

    @Autowired
    private ISeaContainerInformationService seaContainerInformationService;

    @Autowired
    private ISeaOrderService seaOrderService;

    @Override
    public boolean createSeaBill(AddSeaReplenishment addSeaReplenishment) {
        //创建提单
        SeaBill seaBill = ConvertUtil.convert(addSeaReplenishment, SeaBill.class);
        seaBill.setOrderNo(createOrderNo());
        //获取发货人信息
        if(CollectionUtils.isNotEmpty(addSeaReplenishment.getDeliveryAddress())){
            seaBill.setShipperInformation(addSeaReplenishment.getDeliveryAddress().get(0).getAddress());
        }
        if(CollectionUtils.isNotEmpty(addSeaReplenishment.getShippingAddress())){
            seaBill.setConsigneeInformation(addSeaReplenishment.getShippingAddress().get(0).getAddress());
        }
        if(CollectionUtils.isNotEmpty(addSeaReplenishment.getNotificationAddress())){
            seaBill.setNotifierInformation(addSeaReplenishment.getNotificationAddress().get(0).getAddress());
        }
        if(CollectionUtils.isNotEmpty(addSeaReplenishment.getAgentAddress())){
            seaBill.setAgentInformation(addSeaReplenishment.getAgentAddress().get(0).getAddress());
        }
        if(CollectionUtils.isNotEmpty(addSeaReplenishment.getGoodsForms())){
            List<AddGoodsForm> goodsForms = addSeaReplenishment.getGoodsForms();
            AddGoodsForm addGoodsForm = goodsForms.get(0);
            seaBill.setShippingMark(addGoodsForm.getLabel() != null ? addGoodsForm.getLabel() : null);
            seaBill.setGoodName(addGoodsForm.getName() != null ? addGoodsForm.getName() : null);
            seaBill.setBoardNumber(addGoodsForm.getPlateAmount() != null ? addGoodsForm.getPlateAmount() : null);
            seaBill.setPlateUnit(addGoodsForm.getPlateUnit() != null ? addGoodsForm.getPlateUnit() : null);
            seaBill.setNumber(addGoodsForm.getBulkCargoAmount() != null ? addGoodsForm.getBulkCargoAmount() : null);
            seaBill.setNumberUnit(addGoodsForm.getBulkCargoUnit() != null ? addGoodsForm.getBulkCargoUnit() : null);
            seaBill.setWeight(addGoodsForm.getTotalWeight() != null ? addGoodsForm.getTotalWeight() : null);
            seaBill.setVolume(addGoodsForm.getVolume() != null ? addGoodsForm.getVolume() : null);

        }
        seaBill.setCreateTime(LocalDateTime.now());
        seaBill.setCreateUser(UserOperator.getToken());
        seaBill.setType(1);
        seaBill.setIsSpell(false);
        if(seaBill.getBillNo() == null){
            SeaOrder byId = seaOrderService.getById(addSeaReplenishment.getSeaOrderId());
            seaBill.setBillNo(byId.getMainOrderNo());
        }

        boolean save = this.save(seaBill);
        if(!save){
            return false;
        }
        //增加或修改货柜信息
        if (seaBill.getCabinetType().equals(1)) {
            //修改或保存
            List<SeaContainerInformation> seaContainerInformations = addSeaReplenishment.getSeaContainerInformations();
            for (SeaContainerInformation seaContainerInformation : seaContainerInformations) {
                seaContainerInformation.toUP();
                seaContainerInformation.setSeaRepId(seaBill.getId());
                seaContainerInformation.setSeaRepNo(seaBill.getOrderNo());

                seaContainerInformation.setCreateTime(LocalDateTime.now());
                seaContainerInformation.setCreateUser(UserOperator.getToken());
                boolean b = seaContainerInformationService.saveOrUpdate(seaContainerInformation);
                if (!b) {
                    log.warn("提单货柜信息添加失败");
                }
            }

        }
        return true;
    }

    //获取海运订单号
    public String createOrderNo(){
        return (String)omsClient.getOrderNo("TD","TD").getData();
    }

    @Override
    public IPage<SeaBillFormVO> findBillByPage(QuerySeaBillForm form) {

        //获取当前用户所属法人主体
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(form.getLoginUserName());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        Page<SeaOrderFormVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findBillByPage(page, form, legalIds);
    }

    @Override
    public boolean saveOrUpdateSeaBill(AddSeaBillForm form) {
        SeaBill seaBill = ConvertUtil.convert(form, SeaBill.class);
        boolean update = this.saveOrUpdate(seaBill);
        if(!update){
            return false;
        }
        //增加或修改货柜信息
        if (seaBill.getCabinetType().equals(1)) {
            //修改或保存
            List<SeaContainerInformation> seaContainerInformations = form.getSeaContainerInformations();
            for (SeaContainerInformation seaContainerInformation : seaContainerInformations) {
                seaContainerInformation.toUP();
                seaContainerInformation.setSeaRepId(seaBill.getId());

                seaContainerInformation.setSeaRepNo(seaBill.getOrderNo());
                seaContainerInformation.setCreateTime(LocalDateTime.now());
                seaContainerInformation.setCreateUser(UserOperator.getToken());
                boolean b = seaContainerInformationService.saveOrUpdate(seaContainerInformation);
                if (!b) {
                    log.warn("提单货柜信息修改失败");
                }
            }

        }
        return true;
    }

    @Override
    public SeaBillVO getSeaBillById(Long id) {
        SeaBill seaBill = this.getById(id);
        SeaBillVO seaBillVO = ConvertUtil.convert(seaBill, SeaBillVO.class);
        if (seaBillVO.getCabinetType().equals(1)) {
            List<SeaContainerInformationVO> seaContainerInformations = seaContainerInformationService.getList(seaBillVO.getId());
            seaBillVO.setSeaContainerInformations(seaContainerInformations);
        }
        return seaBillVO;
    }

    @Override
    public List<SeaBillVO> getSeaBillByCondition(QueryBulkCargolForm form) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("cabinet_type",2);
        queryWrapper.eq("is_spell",0);
        if(form.getOrderNo() != null){
            queryWrapper.eq("order_no",form.getOrderNo());
        }
        if(form.getPortDeparture() != null){
            queryWrapper.like("port_departure_code",form.getPortDeparture());
        }
        if(form.getPortDestination() != null){
            queryWrapper.like("port_destination_code",form.getPortDestination());
        }
        if(form.getStartCutReplenishTime() != null){
            queryWrapper.ge("cut_replenish_time",form.getStartCutReplenishTime());
        }
        if(form.getEndCutReplenishTime() != null){
            queryWrapper.le("cut_replenish_time",form.getEndCutReplenishTime());
        }
        List list = this.list(queryWrapper);
        List list1 = ConvertUtil.convertList(list, SeaBillVO.class);
        return list1;
    }

    @Override
    public boolean deleteSeaBill(Long id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("sea_order_id",id);
        boolean remove = this.remove(queryWrapper);
        return remove;
    }

}
