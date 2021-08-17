package com.jayud.scm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.mapper.BookingOrderEntryMapper;
import com.jayud.scm.mapper.BookingOrderFollowMapper;
import com.jayud.scm.mapper.BookingOrderMapper;
import com.jayud.scm.model.bo.BookingOrderForm;
import com.jayud.scm.model.bo.QueryBookingOrderForm;
import com.jayud.scm.model.enums.NoCodeEnum;
import com.jayud.scm.model.enums.VoidedEnum;
import com.jayud.scm.model.po.BookingOrder;
import com.jayud.scm.model.po.BookingOrderEntry;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.BookingOrderEntryVO;
import com.jayud.scm.model.vo.BookingOrderVO;
import com.jayud.scm.service.IBookingOrderEntryService;
import com.jayud.scm.service.IBookingOrderService;
import com.jayud.scm.service.ICommodityService;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 委托订单主表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@Service
public class BookingOrderServiceImpl extends ServiceImpl<BookingOrderMapper, BookingOrder> implements IBookingOrderService {

    @Autowired
    BookingOrderMapper bookingOrderMapper;//委托订单主表
    @Autowired
    BookingOrderEntryMapper bookingOrderEntryMapper;//委托订单明细表
    @Autowired
    BookingOrderFollowMapper bookingOrderFollowMapper;//委托单跟踪记录表

    @Autowired
    private ISystemUserService systemUserService;//后台用户表
    @Autowired
    IBookingOrderEntryService bookingOrderEntryService;//委托订单明细表
    @Autowired
    ICommodityService commodityService;//商品表

    /**
     * 出口委托单，分页查询
     * @param form
     * @return
     */
    @Override
    public IPage<BookingOrderVO> findBookingOrderByPage(QueryBookingOrderForm form) {
        //定义分页参数
        Page<BookingOrderVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("t.id"));
        IPage<BookingOrderVO> pageInfo = bookingOrderMapper.findBookingOrderByPage(page, form);
        return pageInfo;
    }

    /**
     * 出口委托单，保存(新增、修改)
     * @param form
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBookingOrder(BookingOrderForm form) {
        //登录用户信息
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        if(ObjectUtil.isEmpty(systemUser)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录");
        }
        Integer id = form.getId();
        if(ObjectUtil.isEmpty(id)){
            //新增
            BookingOrder bookingOrder = ConvertUtil.convert(form, BookingOrder.class);
            //设置创建人信息
            bookingOrder.setCrtBy(systemUser.getId().intValue());
            bookingOrder.setCrtByName(systemUser.getUserName());
            bookingOrder.setCrtByDtm(LocalDateTime.now());

            this.saveOrUpdate(bookingOrder);
        }else{
            //修改
            BookingOrderVO bookingOrderVO = bookingOrderMapper.getBookingOrderById(id);
            BookingOrder bookingOrder = ConvertUtil.convert(bookingOrderVO, BookingOrder.class);
            //设置修改人信息
            bookingOrder.setMdyBy(systemUser.getId().intValue());
            bookingOrder.setMdyByName(systemUser.getUserName());
            bookingOrder.setMdyByDtm(LocalDateTime.now());

            this.saveOrUpdate(bookingOrder);
        }
    }

    /**
     * 出口委托单，删除
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delBookingOrder(Integer id) {
        //登录用户信息
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        if(ObjectUtil.isEmpty(systemUser)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录");
        }
        BookingOrderVO bookingOrderVO = bookingOrderMapper.getBookingOrderById(id);
        if(ObjectUtil.isEmpty(bookingOrderVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "没有找到委托单");
        }
        BookingOrder bookingOrder = ConvertUtil.convert(bookingOrderVO, BookingOrder.class);

        //设置删除信息
        bookingOrder.setVoided(VoidedEnum.ONE.getCode());//ONE 已标记
        bookingOrder.setVoidedBy(systemUser.getId().intValue());
        bookingOrder.setVoidedByDtm(LocalDateTime.now());

        this.saveOrUpdate(bookingOrder);
    }

    /**
     * 出口委托单，查看
     * @param id
     * @return
     */
    @Override
    public BookingOrderVO getBookingOrderById(Integer id) {
        BookingOrderVO bookingOrderVO = bookingOrderMapper.getBookingOrderById(id);
        return bookingOrderVO;
    }

    /**
     * 出口委托单，审核
     * @param id
     */
    @Override
    public void auditBookingOrder(Integer id) {
        //登录用户信息
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        if(ObjectUtil.isEmpty(systemUser)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录");
        }
        BookingOrderVO bookingOrderVO = bookingOrderMapper.getBookingOrderById(id);
        if(ObjectUtil.isEmpty(bookingOrderVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "没有找到委托单");
        }
        BookingOrder bookingOrder = ConvertUtil.convert(bookingOrderVO, BookingOrder.class);

        //设置审核 TODO
        bookingOrder.setFMultiLevei0(systemUser.getUserName());//审核人
        bookingOrder.setFDatetime(LocalDateTime.now());//审核时间
        bookingOrder.setCheckStateFlag("Y");//审核状态 Y | NO
        bookingOrder.setFLevel(null);//审核级别
        bookingOrder.setFStep(null);//审核步骤

        this.saveOrUpdate(bookingOrder);
    }

    /**
     * 出口委托单，反审
     * @param id
     */
    @Override
    public void cancelAuditBookingOrder(Integer id) {
        //登录用户信息
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        if(ObjectUtil.isEmpty(systemUser)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录");
        }
        BookingOrderVO bookingOrderVO = bookingOrderMapper.getBookingOrderById(id);
        if(ObjectUtil.isEmpty(bookingOrderVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "没有找到委托单");
        }
        BookingOrder bookingOrder = ConvertUtil.convert(bookingOrderVO, BookingOrder.class);

        //设置审核 TODO
        bookingOrder.setFMultiLevei0(systemUser.getUserName());//审核人
        bookingOrder.setFDatetime(LocalDateTime.now());//审核时间
        bookingOrder.setCheckStateFlag("NO");//审核状态 Y | NO
        bookingOrder.setFLevel(null);//审核级别
        bookingOrder.setFStep(null);//审核步骤

        this.saveOrUpdate(bookingOrder);
    }

    /**
     * 出口委托单，打印
     * @param id
     */
    @Override
    public void printBookingOrder(Integer id) {

    }

    /**
     * 出口委托单，复制
     * 1.委托订单主表 BookingOrder
     * 2.委托订单明细表 BookingOrderEntry
     * @param id
     * @return
     */
    @Override
    public BookingOrderVO copyBookingOrder(Integer id) {
        BookingOrderVO bookingOrderVO = bookingOrderMapper.getBookingOrderById(id);//1.委托订单主表 BookingOrder
        if(ObjectUtil.isEmpty(bookingOrderVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "委托单不存在");
        }
        List<BookingOrderEntryVO> bookingOrderEntryList = bookingOrderEntryMapper.findBookingOrderEntryByBookingId(bookingOrderVO.getId());//2.委托订单明细表 BookingOrderEntry

        //1.复制委托单主单
        BookingOrder bookingOrder = ConvertUtil.convert(bookingOrderVO, BookingOrder.class);
        bookingOrder.setId(null);
        bookingOrder.setBookingNo(null);//TODO 单号的生成规则 待定
        this.saveOrUpdate(bookingOrder);

        BookingOrderVO bookingOrderVO1 = ConvertUtil.convert(bookingOrder, BookingOrderVO.class);

        //2.复制委托订单明细表
        Integer bookingId = bookingOrderVO1.getId();//新生成的 委托单id
        List<BookingOrderEntry> bookingOrderEntryList1 = ConvertUtil.convertList(bookingOrderEntryList, BookingOrderEntry.class);
        bookingOrderEntryList1.forEach(bookingOrderEntry -> {
            bookingOrderEntry.setId(null);
            bookingOrderEntry.setBookingId(bookingId);
        });
        if(CollUtil.isEmpty(bookingOrderEntryList1)){
            bookingOrderEntryService.saveOrUpdateBatch(bookingOrderEntryList1);
        }

        return bookingOrderVO1;
    }

    @Override
    public BookingOrderVO prepareBookingOrder(Integer modelType) {
        BookingOrderVO bookingOrderVO = new BookingOrderVO();
        bookingOrderVO.setModelType(modelType);//业务类型/工作单类型 0进口  1出口 2国内 4香港  5采购  6销售
        bookingOrderVO.setBookingNo(commodityService.getOrderNo(NoCodeEnum.D001.getCode(), LocalDateTime.now()));

        return bookingOrderVO;
    }
}
