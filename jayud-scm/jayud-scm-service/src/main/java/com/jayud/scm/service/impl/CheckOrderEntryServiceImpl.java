package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddCheckOrderEntryForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.BookingOrderEntry;
import com.jayud.scm.model.po.CheckOrderEntry;
import com.jayud.scm.mapper.CheckOrderEntryMapper;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.CheckOrderEntryVO;
import com.jayud.scm.model.vo.CustomerTaxVO;
import com.jayud.scm.service.IBookingOrderEntryService;
import com.jayud.scm.service.ICheckOrderEntryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 提验货单明细表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@Service
public class CheckOrderEntryServiceImpl extends ServiceImpl<CheckOrderEntryMapper, CheckOrderEntry> implements ICheckOrderEntryService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private IBookingOrderEntryService bookingOrderEntryService;

    @Override
    public List<CheckOrderEntry> getCheckOrderEntryByCheckOrderId(Long id) {
        QueryWrapper<CheckOrderEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CheckOrderEntry::getCheckId,id);
        queryWrapper.lambda().eq(CheckOrderEntry::getVoided,0);
        return this.list(queryWrapper);
    }

    @Override
    public boolean addCheckOrderEntry(List<AddCheckOrderEntryForm> form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        List<CheckOrderEntry> checkOrderEntries = ConvertUtil.convertList(form, CheckOrderEntry.class);
        for (CheckOrderEntry checkOrderEntry : checkOrderEntries) {
            checkOrderEntry.setCrtBy(systemUser.getId().intValue());
            checkOrderEntry.setCrtByDtm(LocalDateTime.now());
            checkOrderEntry.setCrtByName(systemUser.getUserName());
        }
        boolean b = this.saveBatch(checkOrderEntries);
        log.warn("新增提货验货明细成功");
        return b;
    }

    @Override
    public boolean updateCheckOrderEntry(List<AddCheckOrderEntryForm> form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        List<BookingOrderEntry> bookingOrderEntries = new ArrayList<>();

        List<CheckOrderEntry> checkOrderEntries = ConvertUtil.convertList(form, CheckOrderEntry.class);
        for (CheckOrderEntry checkOrderEntry : checkOrderEntries) {
            checkOrderEntry.setMdyBy(systemUser.getId().intValue());
            checkOrderEntry.setMdyByDtm(LocalDateTime.now());
            checkOrderEntry.setMdyByName(systemUser.getUserName());

            BookingOrderEntry bookingOrderEntry = new BookingOrderEntry();
            bookingOrderEntry.setId(checkOrderEntry.getBookingEntryId());
            bookingOrderEntry.setItemOrigin(checkOrderEntry.getItemOrigin());
            bookingOrderEntry.setPackages(checkOrderEntry.getPackages());
            bookingOrderEntry.setGw(checkOrderEntry.getGw());
            bookingOrderEntry.setNw(checkOrderEntry.getNw());
            bookingOrderEntry.setPackingType(checkOrderEntry.getPackagesType());
            bookingOrderEntry.setBn(checkOrderEntry.getBn());
            bookingOrderEntry.setCtnsNo(checkOrderEntry.getCtnsNo());
            bookingOrderEntry.setAccessories(checkOrderEntry.getItemAccs());
            bookingOrderEntries.add(bookingOrderEntry);

        }
        boolean b = this.updateBatchById(checkOrderEntries);
        if(b){
            log.warn("修改提货验货明细成功");
            boolean b1 = this.bookingOrderEntryService.updateBatchById(bookingOrderEntries);
            if(!b1){
                log.warn("回改订单明细失败"+bookingOrderEntries);
            }
        }

        return b;
    }

    @Override
    public IPage<CheckOrderEntryVO> findByPage(QueryCommonForm form) {
        Page<CheckOrderEntryVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }
}
