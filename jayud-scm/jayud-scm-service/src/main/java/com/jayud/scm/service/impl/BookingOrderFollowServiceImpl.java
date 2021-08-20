package com.jayud.scm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.mapper.BookingOrderFollowMapper;
import com.jayud.scm.model.bo.BookingOrderFollowForm;
import com.jayud.scm.model.po.BookingOrderFollow;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.BookingOrderFollowVO;
import com.jayud.scm.service.IBookingOrderFollowService;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 委托单跟踪记录表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@Service
public class BookingOrderFollowServiceImpl extends ServiceImpl<BookingOrderFollowMapper, BookingOrderFollow> implements IBookingOrderFollowService {

    @Autowired
    BookingOrderFollowMapper bookingOrderFollowMapper;//委托单跟踪记录表

    @Autowired
    private ISystemUserService systemUserService;//后台用户表

    /**
     * 跟踪记录，查询
     * @param bookingId
     * @return
     */
    @Override
    public List<BookingOrderFollowVO> findBookingOrderFollow(Integer bookingId) {
        return bookingOrderFollowMapper.findBookingOrderFollow(bookingId);
    }

    /**
     * 跟踪记录，新增
     * @param form
     */
    @Override
    public void saveBookingOrderFollow(BookingOrderFollowForm form) {
        //登录用户信息
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        if(ObjectUtil.isEmpty(systemUser)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录");
        }
        Integer id = form.getId();
        if(ObjectUtil.isEmpty(id)){
            BookingOrderFollow bookingOrderFollow = ConvertUtil.convert(form, BookingOrderFollow.class);

            //设置创建人
            bookingOrderFollow.setCrtBy(systemUser.getId().intValue());
            bookingOrderFollow.setCrtByName(systemUser.getUserName());
            bookingOrderFollow.setCrtByDtm(LocalDateTime.now());

            this.saveOrUpdate(bookingOrderFollow);
        }else{
            BookingOrderFollow bookingOrderFollow = bookingOrderFollowMapper.selectById(id);

            BeanUtil.copyProperties(form, bookingOrderFollow);

            //设置修改人
            bookingOrderFollow.setMdyBy(systemUser.getId().intValue());
            bookingOrderFollow.setMdyByName(systemUser.getUserName());
            bookingOrderFollow.setMdyByDtm(LocalDateTime.now());

            this.saveOrUpdate(bookingOrderFollow);
        }




    }
}
