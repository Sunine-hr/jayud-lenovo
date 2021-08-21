package com.jayud.scm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.mapper.BookingOrderEntryMapper;
import com.jayud.scm.mapper.BookingOrderMapper;
import com.jayud.scm.mapper.CommodityMapper;
import com.jayud.scm.model.bo.BookingOrderEntryForm;
import com.jayud.scm.model.enums.VoidedEnum;
import com.jayud.scm.model.po.BookingOrder;
import com.jayud.scm.model.po.BookingOrderEntry;
import com.jayud.scm.model.po.Commodity;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.BookingOrderEntryVO;
import com.jayud.scm.model.vo.BookingOrderVO;
import com.jayud.scm.service.IBookingOrderEntryService;
import com.jayud.scm.service.ICommodityService;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    CommodityMapper commodityMapper;//商品表

    @Autowired
    ISystemUserService systemUserService;//后台用户表
    @Autowired
    ICommodityService commodityService;//商品表

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

            BeanUtil.copyProperties(form, bookingOrderEntry);

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

    /**
     * 商品明细表，导入
     * @param file
     * @param bookingId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importByBookingOrderEntry(MultipartFile file, Integer bookingId) {
        BookingOrder bookingOrder = bookingOrderMapper.selectById(bookingId);
        if(ObjectUtil.isEmpty(bookingOrder)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "委托单id不存在，不能导入商品明细");
        }
        //1.准备导入数据list
        //获取上传文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //调用用 hutool 方法读取数据 默认调用第一个sheet
        ExcelReader excelReader = ExcelUtil.getReader(inputStream);
        //配置别名
        //型号	商品名称	品牌	产地	单位	海关编码	数量	报关单价	报关总价	件数	料号	批号	毛重	净重	材积	唛头	入仓单号	外箱型号	包装方式	申报要素	商品报关备注	商品描述	PO	配件	箱号	目的国	境内货源地
        Map<String,String> aliasMap=new HashMap<>();
        aliasMap.put("型号", "itemModel");
        aliasMap.put("商品名称", "itemBrand");
        aliasMap.put("品牌", "itemOrigin");
        aliasMap.put("产地", "unit");
        aliasMap.put("单位", "customsCode");
        aliasMap.put("海关编码", "qty");
        aliasMap.put("数量", "hgPrice");
        aliasMap.put("报关单价", "totalHgMoney");
        aliasMap.put("报关总价", "packages");
        aliasMap.put("件数", "packages");
        aliasMap.put("料号", "pn");
        aliasMap.put("批号", "bn");
        aliasMap.put("毛重", "gw");
        aliasMap.put("净重", "nw");
        aliasMap.put("材积", "cbm");
        aliasMap.put("唛头", "shippingM");
        aliasMap.put("入仓单号", "inStoreNum");
        aliasMap.put("外箱型号", "packmodel");
        aliasMap.put("包装方式", "packingType");
        aliasMap.put("申报要素", "elements");
        aliasMap.put("商品报关备注", "remark");
        aliasMap.put("商品描述", "itemNotes");
        aliasMap.put("PO", "po");
        aliasMap.put("配件", "accessories");
        aliasMap.put("箱号", "ctnsNo");
        aliasMap.put("目的国", "destination");
        aliasMap.put("境内货源地", "districtCode");
        excelReader.setHeaderAlias(aliasMap);

        // 第一个参数是指表头所在行，第二个参数是指从哪一行开始读取
        List<BookingOrderEntryForm> list= excelReader.read(0, 1, BookingOrderEntryForm.class);
        System.out.println(list);

        list.forEach(bookingOrderEntryForm -> {
            String itemModel = bookingOrderEntryForm.getItemModel();//型号
            BigDecimal qty = bookingOrderEntryForm.getQty();//数量
            BigDecimal hgPrice = bookingOrderEntryForm.getHgPrice();//报关单价
            if(ObjectUtil.isEmpty(itemModel)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "型号不能为空！");
            }
            if(ObjectUtil.isEmpty(qty)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "数量不能为空！");
            }
            if(ObjectUtil.isEmpty(hgPrice)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "报关单价不能为空！");
            }
            //根据型号查询商品
            QueryWrapper<Commodity> commodityQueryWrapper = new QueryWrapper<>();
            commodityQueryWrapper.eq("sku_model", itemModel);
            Commodity commodity = commodityMapper.selectOne(commodityQueryWrapper);
            if(ObjectUtil.isEmpty(commodity)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "根据型号["+itemModel+"]查询,没有找到对应的商品");
            }
            bookingOrderEntryForm.setBookingId(bookingId);//委托单id
            Integer itemId = commodity.getId();//产品id
            BookingOrderEntryVO bookingOrderEntryVO = bookingOrderEntryMapper.findBookingOrderEntryByBookingIdAndItemId(bookingId, itemId);
            if(ObjectUtil.isNotEmpty(bookingOrderEntryVO)){
                //进行修改
                Integer id = bookingOrderEntryVO.getId();
                bookingOrderEntryForm.setId(id);
                bookingOrderEntryForm.setVoided(VoidedEnum.ZERO.getCode());
            }
            bookingOrderEntryForm.setItemId(itemId);
            String skuModel = commodity.getSkuModel();//型号
            bookingOrderEntryForm.setItemModel(skuModel);
            String skuName = commodity.getSkuName();//商品名称
            bookingOrderEntryForm.setItemName(skuName);
            String skuBrand = commodity.getSkuBrand();//品牌
            bookingOrderEntryForm.setItemBrand(skuBrand);
            String skuOrigin = commodity.getSkuOrigin();//产地
            bookingOrderEntryForm.setItemOrigin(skuOrigin);
            String skuUnit = commodity.getSkuUnit();//单位
            bookingOrderEntryForm.setUnit(skuUnit);
            String hsCodeNo = commodity.getHsCodeNo();//海关编码
            bookingOrderEntryForm.setCustomsCode(hsCodeNo);
            String skuElements = commodity.getSkuElements();//申报要素
            bookingOrderEntryForm.setElements(skuElements);
            BigDecimal referencePrice = commodity.getReferencePrice();//参考价
            String skuNotes = commodity.getSkuNotes();//商品描述
            bookingOrderEntryForm.setItemNotes(skuNotes);
            BigDecimal totalHgMoney = hgPrice.multiply(qty);//报关总价(=hg_price*qty)
            bookingOrderEntryForm.setTotalHgMoney(totalHgMoney);
        });
        //2.开始导入(数据)
        List<BookingOrderEntry> bookingOrderEntries = ConvertUtil.convertList(list, BookingOrderEntry.class);
        this.saveOrUpdateBatch(bookingOrderEntries);

    }


}
