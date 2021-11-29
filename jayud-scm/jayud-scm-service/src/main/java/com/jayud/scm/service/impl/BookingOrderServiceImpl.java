package com.jayud.scm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.mapper.BookingOrderEntryMapper;
import com.jayud.scm.mapper.BookingOrderFollowMapper;
import com.jayud.scm.mapper.BookingOrderMapper;
import com.jayud.scm.model.bo.BookingOrderForm;
import com.jayud.scm.model.bo.PermissionForm;
import com.jayud.scm.model.bo.QueryBookingOrderForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.enums.NoCodeEnum;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.enums.StateFlagEnum;
import com.jayud.scm.model.enums.VoidedEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.model.vo.BookingOrderEntryVO;
import com.jayud.scm.model.vo.BookingOrderVO;
import com.jayud.scm.model.vo.QtyVO;
import com.jayud.scm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    ISystemUserService systemUserService;//后台用户表
    @Autowired
    IBookingOrderEntryService bookingOrderEntryService;//委托订单明细表
    @Autowired
    ICommodityService commodityService;//商品表
    @Autowired
    ICustomerService customerService;//客户表

    @Autowired
    private IBYbfService ybfService;

    @Autowired
    private IBookingOrderFollowService bookingOrderFollowService;

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
            if(bookingOrder.getModelType().equals(1)){
                bookingOrder.setBookingNo(commodityService.getOrderNo(NoCodeEnum.D002.getCode(),LocalDateTime.now()));
            }
            if(bookingOrder.getModelType().equals(2)){
                bookingOrder.setBookingNo(commodityService.getOrderNo(NoCodeEnum.D001.getCode(),LocalDateTime.now()));
            }

            bookingOrder.setCrtBy(systemUser.getId().intValue());
            bookingOrder.setCrtByName(systemUser.getUserName());
            bookingOrder.setCrtByDtm(LocalDateTime.now());

            //设置状态
            bookingOrder.setStateFlag(StateFlagEnum.STATE_FLAG_0.getCode());//STATE_FLAG_0(0, "未确认"),

            this.saveOrUpdate(bookingOrder);

            BookingOrderFollow bookingOrderFollow = new BookingOrderFollow();
            bookingOrderFollow.setCrtBy(systemUser.getId().intValue());
            bookingOrderFollow.setCrtByDtm(LocalDateTime.now());
            bookingOrderFollow.setCrtByName(systemUser.getUserName());
            bookingOrderFollow.setBookingId(bookingOrder.getId());
            bookingOrderFollow.setSType(OperationEnum.INSERT.getCode());
            bookingOrderFollow.setFollowContext(systemUser.getUserName()+"添加委托单"+bookingOrder.getBookingNo());
            boolean save = bookingOrderFollowService.save(bookingOrderFollow);
            if(save){
                log.warn("添加委托单，操作日志添加成功");
            }
        }else{
            //修改
            BookingOrderVO bookingOrderVO = bookingOrderMapper.getBookingOrderById(id);
            BookingOrder bookingOrder = ConvertUtil.convert(bookingOrderVO, BookingOrder.class);

            BeanUtil.copyProperties(form, bookingOrder);

            //设置修改人信息
            bookingOrder.setMdyBy(systemUser.getId().intValue());
            bookingOrder.setMdyByName(systemUser.getUserName());
            bookingOrder.setMdyByDtm(LocalDateTime.now());

            bookingOrder.setStateFlag(StateFlagEnum.STATE_FLAG_0.getCode());//STATE_FLAG_0(0, "未确认"),

            this.saveOrUpdate(bookingOrder);

            BookingOrderFollow bookingOrderFollow = new BookingOrderFollow();
            bookingOrderFollow.setCrtBy(systemUser.getId().intValue());
            bookingOrderFollow.setCrtByDtm(LocalDateTime.now());
            bookingOrderFollow.setCrtByName(systemUser.getUserName());
            bookingOrderFollow.setBookingId(bookingOrder.getId());
            bookingOrderFollow.setSType(OperationEnum.UPDATE.getCode());
            bookingOrderFollow.setFollowContext(systemUser.getUserName()+"修改委托单"+bookingOrder.getBookingNo());
            boolean save = bookingOrderFollowService.save(bookingOrderFollow);
            if(save){
                log.warn("修改委托单，操作日志添加成功");
            }
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

        BookingOrderFollow bookingOrderFollow = new BookingOrderFollow();
        bookingOrderFollow.setCrtBy(systemUser.getId().intValue());
        bookingOrderFollow.setCrtByDtm(LocalDateTime.now());
        bookingOrderFollow.setCrtByName(systemUser.getUserName());
        bookingOrderFollow.setBookingId(bookingOrder.getId());
        bookingOrderFollow.setSType(OperationEnum.DELETE.getCode());
        bookingOrderFollow.setFollowContext(systemUser.getUserName()+"删除委托单"+bookingOrder.getBookingNo());
        boolean save = bookingOrderFollowService.save(bookingOrderFollow);
        if(save){
            log.warn("删除委托单，操作日志添加成功");
        }

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
        Integer bookingId = bookingOrderVO.getId();//委托单id
        //商品明细list
        List<BookingOrderEntryVO> bookingOrderEntryList = bookingOrderEntryMapper.findBookingOrderEntryByBookingId(bookingId);
        bookingOrderVO.setBookingOrderEntryList(bookingOrderEntryList);
        return bookingOrderVO;
    }

    /**
     * 出口委托单，审核
     * @param form
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditBookingOrder(PermissionForm form) {
        //获取登录用户
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        //1.调用通用的审核
        CommonResult commonResult = customerService.toExamine(form);

        Integer bookingId = form.getId();//委托单ID

        //出口委托单审核为 “Y”，即最后一次审核，调用 up_e_order_price_sp
        BookingOrder bookingOrder = this.getById(bookingId);
        if(bookingOrder.getCheckStateFlag().equals("Y")){
            CommonResult commonResult1 = this.estimatedUnitPrice(bookingId);
            if(commonResult1.getCode().equals(1)){
                log.warn(commonResult1.getMsg());
            }
            BYbf bYbf = ybfService.getBYbfByDtaTime(LocalDateTime.now());
            List<BookingOrderEntryVO> bookingOrderEntryVOS = bookingOrderEntryMapper.findBookingOrderEntryByBookingId(bookingId);
            if(bookingOrder.getIncoterms().equals("CIF")){
                for (BookingOrderEntryVO bookingOrderEntryVO : bookingOrderEntryVOS) {
                    bookingOrderEntryVO.setCipPrice(bYbf.getCif().multiply(bookingOrderEntryVO.getHgPrice()));
                }
            }else{
                for (BookingOrderEntryVO bookingOrderEntryVO : bookingOrderEntryVOS) {
                    bookingOrderEntryVO.setCipPrice(bookingOrderEntryVO.getHgPrice());
                }
            }

            boolean result = this.bookingOrderEntryService.updateBatchById(ConvertUtil.convertList(bookingOrderEntryVOS,BookingOrderEntry.class));
            if(result){
                log.warn("修改运保费单价成功");
            }

        }

        //2.记录 委托单跟踪记录表
        BookingOrderFollow bookingOrderFollow = new BookingOrderFollow();
        bookingOrderFollow.setBookingId(bookingId);
        bookingOrderFollow.setSType("系统");//1系统 2人工
        if(commonResult.getCode().equals(ResultEnum.SUCCESS.getCode())){
            //操作成功
            if(bookingOrder.getModelType().equals(1)){
                bookingOrderFollow.setFollowContext("进口委托单，审核，成功！");
            }else if(bookingOrder.getModelType().equals(2)){
                bookingOrderFollow.setFollowContext("出口委托单，审核，成功！");
            }
        }else{
            //操作失败
            if(bookingOrder.getModelType().equals(1)){
                bookingOrderFollow.setFollowContext("进口委托单，审核，异常！");
            }else if(bookingOrder.getModelType().equals(2)){
                bookingOrderFollow.setFollowContext("出口委托单，审核，异常！");
            }
        }
        //设置创建人
        bookingOrderFollow.setCrtBy(systemUser.getId().intValue());
        bookingOrderFollow.setCrtByName(systemUser.getUserName());
        bookingOrderFollow.setCrtByDtm(LocalDateTime.now());
        bookingOrderFollowMapper.insert(bookingOrderFollow);

    }

    /**
     * 出口委托单，反审
     * @param form
     */
    @Override
    public void cancelAuditBookingOrder(PermissionForm form) {
        //获取登录用户
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        //1.调用通用的反审
        CommonResult commonResult = customerService.deApproval(form);

        Integer bookingId = form.getId();//委托单ID

        //2.记录 委托单跟踪记录表
        BookingOrderFollow bookingOrderFollow = new BookingOrderFollow();
        bookingOrderFollow.setBookingId(bookingId);
        bookingOrderFollow.setSType("系统");//1系统 2人工
        if(commonResult.getCode().equals(ResultEnum.SUCCESS.getCode())){
            //操作成功
            bookingOrderFollow.setFollowContext("出口委托单，反审，成功！");
        }else{
            //操作失败
            bookingOrderFollow.setFollowContext("出口委托单，反审，异常！");
        }
        //设置创建人
        bookingOrderFollow.setCrtBy(systemUser.getId().intValue());
        bookingOrderFollow.setCrtByName(systemUser.getUserName());
        bookingOrderFollow.setCrtByDtm(LocalDateTime.now());
        bookingOrderFollowMapper.insert(bookingOrderFollow);

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
    @Transactional(rollbackFor = Exception.class)
    public BookingOrderVO copyBookingOrder(Integer id) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        BookingOrderVO bookingOrderVO = bookingOrderMapper.getBookingOrderById(id);//1.委托订单主表 BookingOrder
        if(ObjectUtil.isEmpty(bookingOrderVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "委托单不存在");
        }
        List<BookingOrderEntryVO> bookingOrderEntryList = bookingOrderEntryMapper.findBookingOrderEntryByBookingId(bookingOrderVO.getId());//2.委托订单明细表 BookingOrderEntry

        //1.复制委托单主单
        BookingOrder bookingOrder = ConvertUtil.convert(bookingOrderVO, BookingOrder.class);
        bookingOrder.setId(null);
        if(bookingOrder.getModelType().equals(2)){
            bookingOrder.setBookingNo(commodityService.getOrderNo(NoCodeEnum.D001.getCode(), LocalDateTime.now()));//单号的生成规则
        }else if(bookingOrder.getModelType().equals(1)){
            bookingOrder.setBookingNo(commodityService.getOrderNo(NoCodeEnum.D002.getCode(), LocalDateTime.now()));//单号的生成规则
        }

        bookingOrder.setCheckStateFlag("N0");
        bookingOrder.setStateFlag(0);
        bookingOrder.setFStep(0);
        bookingOrder.setBillId(null);
        bookingOrder.setContractNo(null);
        this.saveOrUpdate(bookingOrder);

        BookingOrderVO bookingOrderVO1 = ConvertUtil.convert(bookingOrder, BookingOrderVO.class);

        //2.复制委托订单明细表
        Integer bookingId = bookingOrderVO1.getId();//新生成的 委托单id
        List<BookingOrderEntry> bookingOrderEntryList1 = ConvertUtil.convertList(bookingOrderEntryList, BookingOrderEntry.class);
        bookingOrderEntryList1.forEach(bookingOrderEntry -> {
            bookingOrderEntry.setId(null);
            bookingOrderEntry.setExRmbPrice(null);
            bookingOrderEntry.setBookingId(bookingId);
        });
        if(CollUtil.isNotEmpty(bookingOrderEntryList1)){
            bookingOrderEntryService.saveOrUpdateBatch(bookingOrderEntryList1);
        }

        BookingOrderFollow bookingOrderFollow = new BookingOrderFollow();
        bookingOrderFollow.setCrtBy(systemUser.getId().intValue());
        bookingOrderFollow.setCrtByDtm(LocalDateTime.now());
        bookingOrderFollow.setCrtByName(systemUser.getUserName());
        bookingOrderFollow.setBookingId(bookingOrder.getId());
        bookingOrderFollow.setSType(OperationEnum.DELETE.getCode());
        bookingOrderFollow.setFollowContext(systemUser.getUserName()+"复制委托单"+bookingOrder.getBookingNo());
        boolean save = bookingOrderFollowService.save(bookingOrderFollow);
        if(save){
            log.warn("复制委托单，操作日志添加成功");
        }
        return bookingOrderVO1;
    }

    @Override
    public BookingOrderVO prepareBookingOrder(Integer modelType) {
        BookingOrderVO bookingOrderVO = new BookingOrderVO();
        bookingOrderVO.setModelType(modelType);//业务类型/工作单类型 1进口  2出口 3国内 4香港  5采购  6销售
        bookingOrderVO.setBookingNo(commodityService.getOrderNo(NoCodeEnum.D001.getCode(), LocalDateTime.now()));//单号的生成规则
        return bookingOrderVO;
    }

    @Override
    public BookingOrder getBookingOrderByBillId(Integer id) {
        QueryWrapper<BookingOrder> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(BookingOrder::getBillId,id);
        queryWrapper.lambda().eq(BookingOrder::getVoided,0);
        return this.getOne(queryWrapper);
    }

    @Override
    public List<BookingOrder> getBookingOrderByHgTrackId(Integer id) {
        QueryWrapper<BookingOrder> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(BookingOrder::getHgTruckId,id);
        queryWrapper.lambda().eq(BookingOrder::getVoided,0);
        return this.list(queryWrapper);
    }

    @Override
    public IPage<BookingOrderVO> findByPage(QueryBookingOrderForm form) {
        //定义分页参数
        Page<BookingOrderVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("t.id"));
        IPage<BookingOrderVO> pageInfo = bookingOrderMapper.findByPage(page, form);
        return pageInfo;
    }

    @Override
    public CommonResult upOrderCheckValidate(Integer id,Integer type) {
        BookingOrder byId = this.getById(id);
        Map map = new HashMap();
        map.put("orderId",byId.getId());
        map.put("step",type);
        this.baseMapper.upOrderCheckValidate(map);
        if(map.get("result").equals(1)){
            return CommonResult.error((Integer)map.get("result") , (String)map.get("msg"));
        }
        return CommonResult.success();
    }

    @Override
    public boolean isCommplete(Integer bookingId) {
        QtyVO qtyVO = this.baseMapper.isCommplete(bookingId);
        QtyVO qtyVO1 = this.baseMapper.isCommplete2(bookingId);
        if(qtyVO.getBookingQty().compareTo(qtyVO1.getReceivingQty()) == 0){
            return true;
        }
        return false;
    }

    @Override
    public CommonResult settlement(QueryCommonForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        Map<String,Object> map = new HashMap<>();
        map.put("id",form.getId());
        map.put("userId",systemUser.getId());
        map.put("userName",systemUser.getUserName());
        this.baseMapper.settlement(map);
        if(map.get("state").equals(1)){
            return CommonResult.error((Integer)map.get("state"),(String)map.get("string"));
        }
        return CommonResult.success();
    }

    @Override
    public CommonResult importSettlement(QueryCommonForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        Map<String,Object> map = new HashMap<>();
        map.put("id",form.getId());
        map.put("userId",systemUser.getId());
        map.put("userName",systemUser.getUserName());
        this.baseMapper.importSettlement(map);
        if(map.get("state").equals(1)){
            return CommonResult.error((Integer)map.get("state"),(String)map.get("string"));
        }
        return CommonResult.success();
    }

    @Override
    public CommonResult estimatedUnitPrice(Integer id) {

        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        this.baseMapper.estimatedUnitPrice(map);
        if(map.get("state").equals(1)){
            return CommonResult.error((Integer)map.get("state"),(String)map.get("string"));
        }
        return CommonResult.success();
    }

    @Override
    public CommonResult reverseCalculation(Integer id) {
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        this.baseMapper.reverseCalculation(map);
        if(map.get("state").equals(1)){
            return CommonResult.error((Integer)map.get("state"),(String)map.get("string"));
        }
        return CommonResult.success();
    }

    @Override
    public void temporaryStorageBookingOrder(BookingOrderForm form) {
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
            if(bookingOrder.getModelType().equals(1)){
                bookingOrder.setBookingNo(commodityService.getOrderNo(NoCodeEnum.D002.getCode(),LocalDateTime.now()));
            }
            if(bookingOrder.getModelType().equals(2)){
                bookingOrder.setBookingNo(commodityService.getOrderNo(NoCodeEnum.D001.getCode(),LocalDateTime.now()));
            }

            bookingOrder.setCrtBy(systemUser.getId().intValue());
            bookingOrder.setCrtByName(systemUser.getUserName());
            bookingOrder.setCrtByDtm(LocalDateTime.now());

            //设置状态
            bookingOrder.setStateFlag(StateFlagEnum.STATE_FLAG_NEGATIVE_4.getCode());//STATE_FLAG_0(0, "未确认"),

            this.saveOrUpdate(bookingOrder);

            BookingOrderFollow bookingOrderFollow = new BookingOrderFollow();
            bookingOrderFollow.setCrtBy(systemUser.getId().intValue());
            bookingOrderFollow.setCrtByDtm(LocalDateTime.now());
            bookingOrderFollow.setCrtByName(systemUser.getUserName());
            bookingOrderFollow.setBookingId(bookingOrder.getId());
            bookingOrderFollow.setSType(OperationEnum.INSERT.getCode());
            bookingOrderFollow.setFollowContext(systemUser.getUserName()+"暂存委托单"+bookingOrder.getBookingNo());
            boolean save = bookingOrderFollowService.save(bookingOrderFollow);
            if(save){
                log.warn("添加委托单，操作日志添加成功");
            }
        }else{
            //修改
            BookingOrderVO bookingOrderVO = bookingOrderMapper.getBookingOrderById(id);
            BookingOrder bookingOrder = ConvertUtil.convert(bookingOrderVO, BookingOrder.class);

            BeanUtil.copyProperties(form, bookingOrder);

            //设置修改人信息
            bookingOrder.setMdyBy(systemUser.getId().intValue());
            bookingOrder.setMdyByName(systemUser.getUserName());
            bookingOrder.setMdyByDtm(LocalDateTime.now());
            //设置状态
            bookingOrder.setStateFlag(StateFlagEnum.STATE_FLAG_NEGATIVE_4.getCode());//STATE_FLAG_0(0, "未确认"),

            this.saveOrUpdate(bookingOrder);

            BookingOrderFollow bookingOrderFollow = new BookingOrderFollow();
            bookingOrderFollow.setCrtBy(systemUser.getId().intValue());
            bookingOrderFollow.setCrtByDtm(LocalDateTime.now());
            bookingOrderFollow.setCrtByName(systemUser.getUserName());
            bookingOrderFollow.setBookingId(bookingOrder.getId());
            bookingOrderFollow.setSType(OperationEnum.UPDATE.getCode());
            bookingOrderFollow.setFollowContext(systemUser.getUserName()+"暂存委托单"+bookingOrder.getBookingNo());
            boolean save = bookingOrderFollowService.save(bookingOrderFollow);
            if(save){
                log.warn("修改委托单，操作日志添加成功");
            }
        }
    }
}
