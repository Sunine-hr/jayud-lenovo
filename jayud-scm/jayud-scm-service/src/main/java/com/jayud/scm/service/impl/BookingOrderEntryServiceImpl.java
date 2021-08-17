package com.jayud.scm.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.mapper.BookingOrderEntryMapper;
import com.jayud.scm.mapper.BookingOrderMapper;
import com.jayud.scm.model.bo.BookingOrderEntryForm;
import com.jayud.scm.model.enums.VoidedEnum;
import com.jayud.scm.model.po.BookingOrderEntry;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.BookingOrderEntryVO;
import com.jayud.scm.model.vo.BookingOrderVO;
import com.jayud.scm.service.IBookingOrderEntryService;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 委托订单明细表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@Service
public class BookingOrderEntryServiceImpl extends ServiceImpl<BookingOrderEntryMapper, BookingOrderEntry> implements IBookingOrderEntryService {

    @Autowired
    BookingOrderEntryMapper bookingOrderEntryMapper;//委托订单明细表
    @Autowired
    BookingOrderMapper bookingOrderMapper;//委托订单主表

    @Autowired
    private ISystemUserService systemUserService;//后台用户表

    /**
     * 商品明细表，list查询
     * @param bookingId
     * @return
     */
    @Override
    public List<BookingOrderEntryVO> findBookingOrderEntryByBookingId(Integer bookingId) {
        BookingOrderVO bookingOrderVO = bookingOrderMapper.getBookingOrderById(bookingId);
        if(ObjectUtil.isEmpty(bookingOrderVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "委托单不存在");
        }
        List<BookingOrderEntryVO> bookingOrderEntryList = bookingOrderEntryMapper.findBookingOrderEntryByBookingId(bookingId);
        return bookingOrderEntryList;
    }

    /**
     * 商品明细表，保存(新增、修改)
     * @param form
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBookingOrderEntry(BookingOrderEntryForm form) {
        //登录用户信息
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        if(ObjectUtil.isEmpty(systemUser)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录");
        }
        Integer id = form.getId();
        if (ObjectUtil.isEmpty(id)){
            //新增
            BookingOrderEntry bookingOrderEntry = ConvertUtil.convert(form, BookingOrderEntry.class);

            //设置创建人
            bookingOrderEntry.setCrtBy(systemUser.getId().intValue());
            bookingOrderEntry.setCrtByName(systemUser.getUserName());
            bookingOrderEntry.setCrtByDtm(LocalDateTime.now());

            this.saveOrUpdate(bookingOrderEntry);
        }else{
            //修改
            BookingOrderEntryVO bookingOrderEntryVO = bookingOrderEntryMapper.getBookingOrderEntryById(id);
            BookingOrderEntry bookingOrderEntry = ConvertUtil.convert(bookingOrderEntryVO, BookingOrderEntry.class);

            //设置修改人
            bookingOrderEntry.setMdyBy(systemUser.getId().intValue());
            bookingOrderEntry.setMdyByName(systemUser.getUserName());
            bookingOrderEntry.setMdyByDtm(LocalDateTime.now());

            this.saveOrUpdate(bookingOrderEntry);
        }
    }

    /**
     * 商品明细表，删除
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delBookingOrderEntry(Integer id) {
        //登录用户信息
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        if(ObjectUtil.isEmpty(systemUser)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录");
        }
        BookingOrderEntryVO bookingOrderEntryVO = bookingOrderEntryMapper.getBookingOrderEntryById(id);
        if (ObjectUtil.isEmpty(bookingOrderEntryVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "商品明细不存在");
        }
        BookingOrderEntry bookingOrderEntry = ConvertUtil.convert(bookingOrderEntryVO, BookingOrderEntry.class);

        //设置删除
        bookingOrderEntry.setVoided(VoidedEnum.ONE.getCode());
        bookingOrderEntry.setVoidedBy(systemUser.getId().intValue());
        bookingOrderEntry.setVoidedByName(systemUser.getUserName());
        bookingOrderEntry.setVoidedByDtm(LocalDateTime.now());
        this.saveOrUpdate(bookingOrderEntry);
    }

    /**
     * 商品明细表，查看
     * @param id
     * @return
     */
    @Override
    public BookingOrderEntryVO getBookingOrderEntryById(Integer id) {
        BookingOrderEntryVO bookingOrderEntryVO = bookingOrderEntryMapper.getBookingOrderEntryById(id);
        return bookingOrderEntryVO;
    }

    /**
     * 商品明细表，复制
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BookingOrderEntryVO copyBookingOrderEntry(Integer id) {
        BookingOrderEntryVO bookingOrderEntryVO = bookingOrderEntryMapper.getBookingOrderEntryById(id);
        if(ObjectUtil.isEmpty(bookingOrderEntryVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "商品明细不存在");
        }
        BookingOrderEntry bookingOrderEntry = ConvertUtil.convert(bookingOrderEntryVO, BookingOrderEntry.class);

        //复制单条数据，设置新的id
        bookingOrderEntry.setId(null);//id设为null
        this.saveOrUpdate(bookingOrderEntry);//重新生成id

        BookingOrderEntryVO bookingOrderEntryVO1 = ConvertUtil.convert(bookingOrderEntry, BookingOrderEntryVO.class);
        return bookingOrderEntryVO1;
    }
}
