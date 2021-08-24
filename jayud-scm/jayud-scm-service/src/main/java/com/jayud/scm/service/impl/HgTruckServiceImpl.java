package com.jayud.scm.service.impl;

import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddHgTruckForm;
import com.jayud.scm.model.enums.NoCodeEnum;
import com.jayud.scm.model.po.HgTruck;
import com.jayud.scm.mapper.HgTruckMapper;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.HgTruckVO;
import com.jayud.scm.service.ICommodityService;
import com.jayud.scm.service.IHgTruckService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 港车运输主表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@Service
public class HgTruckServiceImpl extends ServiceImpl<HgTruckMapper, HgTruck> implements IHgTruckService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ICommodityService commodityService;

    @Override
    public HgTruckVO getHgTruckById(Integer id) {
        HgTruck hgTruck = this.getById(id);
        HgTruckVO hgTruckVO = ConvertUtil.convert(hgTruck, HgTruckVO.class);
        return hgTruckVO;
    }

    @Override
    public boolean saveOrUpdateHgTruck(AddHgTruckForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        HgTruck hgTruck = ConvertUtil.convert(form, HgTruck.class);
        if(hgTruck.getId() != null){
            hgTruck.setMdyBy(systemUser.getId().intValue());
            hgTruck.setMdyByDtm(LocalDateTime.now());
            hgTruck.setMdyByName(systemUser.getUserName());
        }else{
            hgTruck.setTruckNo(commodityService.getOrderNo(NoCodeEnum.HG_TRUCK.getCode(),LocalDateTime.now()));
            hgTruck.setMdyBy(systemUser.getId().intValue());
            hgTruck.setMdyByDtm(LocalDateTime.now());
            hgTruck.setMdyByName(systemUser.getUserName());
        }
        boolean update = this.saveOrUpdate(hgTruck);
        if(update){
           log.warn("添加或修改港车运输信息成功"+hgTruck);
        }
        return update;
    }
}
