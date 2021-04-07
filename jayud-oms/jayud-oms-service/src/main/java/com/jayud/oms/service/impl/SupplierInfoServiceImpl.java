package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.config.ImportExcelUtil;
import com.jayud.oms.config.LoadExcelUtil;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.mapper.SupplierInfoMapper;
import com.jayud.oms.model.bo.AddSupplierInfoForm;
import com.jayud.oms.model.bo.QueryAuditSupplierInfoForm;
import com.jayud.oms.model.bo.QuerySupplierInfoForm;
import com.jayud.oms.model.enums.AuditStatusEnum;
import com.jayud.oms.model.enums.AuditTypeDescEnum;
import com.jayud.oms.model.enums.SettlementTypeEnum;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.*;
import com.jayud.oms.model.vo.LegalEntityVO;
import com.jayud.oms.model.vo.SupplierInfoVO;
import com.jayud.oms.service.IAuditInfoService;
import com.jayud.oms.service.IProductClassifyService;
import com.jayud.oms.service.ISupplierInfoService;
import com.jayud.oms.service.ISupplierRelaLegalService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 供应商信息 服务实现类
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-29
 */
@Service
public class SupplierInfoServiceImpl extends ServiceImpl<SupplierInfoMapper, SupplierInfo> implements ISupplierInfoService {
    //服务类型
    @Autowired
    private IProductClassifyService productClassifyService;
    @Autowired
    private IAuditInfoService auditInfoService;

    @Autowired
    private OauthClient oauthClient;

    @Autowired
    private ISupplierRelaLegalService supplierRelaLegalServic;

    /**
     * 列表分页查询
     *
     * @param form
     * @return
     */
    @Override
    public IPage<SupplierInfoVO> findSupplierInfoByPage(QuerySupplierInfoForm form) {
        Page<SupplierInfoVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        page.addOrder(OrderItem.desc("s.id"));
        //获取当前用户的法人主体
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(form.getLoginUserName());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        IPage<SupplierInfoVO> iPage = this.baseMapper.findSupplierInfoByPage(page, form, legalIds);
        for (SupplierInfoVO record : iPage.getRecords()) {
            if (StringUtils.isEmpty(record.getProductClassify())) {
                continue;
            }
            String[] tmps = record.getProductClassify().split(",");
            List<Long> ids = new ArrayList<>();
            for (String tmp : tmps) {
                ids.add(Long.parseLong(tmp));
            }
            List<ProductClassify> productClassifies = productClassifyService.getByIds(ids);
            StringBuilder sb = new StringBuilder();
            for (ProductClassify productClassify : productClassifies) {
                sb.append(productClassify.getName()).append(",");
            }
            record.setProductClassify(sb.substring(0, sb.length() - 1));
            record.setSettlementType(SettlementTypeEnum.getDesc(record.getSettlementType()));

            //获取法人主体
//            LegalEntityVO legalEntityVO = ConvertUtil.convert(oauthClient.getLegalEntityByLegalId(record.getLegalEntityIds().get(0)).getData(), LegalEntityVO.class);
//            record.setLegalEntityName(legalEntityVO.getLegalName());
            //查询审核状态
            AuditInfo auditInfo = this.auditInfoService.getAuditInfoLatestByExtId(record.getId(), AuditTypeDescEnum.ONE.getTable());
            record.setAuditStatus(AuditStatusEnum.getDesc(auditInfo.getAuditStatus()));
        }

        return iPage;
    }

    /**
     * 新增编辑供应商
     *
     * @param form
     * @return
     */
    @Override
    @Transactional
    public boolean saveOrUpdateSupplierInfo(AddSupplierInfoForm form) {
        StringBuilder sb = new StringBuilder();
        for (Long id : form.getProductClassifyIds()) {
            sb.append(id).append(",");
        }

        SupplierInfo supplierInfo = ConvertUtil.convert(form, SupplierInfo.class);
        supplierInfo.setProductClassifyIds(sb.substring(0, sb.length() - 1));

        boolean isTrue;
        if (Objects.isNull(supplierInfo.getId())) {
            supplierInfo.setCreateTime(LocalDateTime.now())
                    .setCreateUser(UserOperator.getToken());

            isTrue = this.save(supplierInfo);
        } else {
            supplierInfo.setUpdateTime(LocalDateTime.now())
                    .setUpdateUser(UserOperator.getToken())
                    .setSupplierCode(null);
            isTrue = this.updateById(supplierInfo);
        }

        form.setId(supplierInfo.getId());
        //保存客户和法人主体关系
        this.supplierRelaLegalServic.saveSupplierRelLegal(form);

        //创建审核表
        auditInfoService.saveOrUpdateAuditInfo(new AuditInfo()
                .setExtId(supplierInfo.getId())
                .setExtDesc(AuditTypeDescEnum.ONE.getTable())
                .setAuditTypeDesc(AuditTypeDescEnum.ONE.getDesc())
                .setAuditStatus(AuditStatusEnum.CW_WAIT.getCode())
        );
        return isTrue;
    }

    /**
     * 分页查询供应商审核信息
     */
    @Override
    public IPage<SupplierInfoVO> findAuditSupplierInfoByPage(QueryAuditSupplierInfoForm form) {
        Page page = new Page(form.getPageNum(), form.getPageSize());

        page.addOrder(OrderItem.desc("s.id"));
        form.setAuditTableDesc(AuditTypeDescEnum.ONE.getTable());
        IPage<SupplierInfoVO> iPage = this.baseMapper.findAuditSupplierInfoByPage(page, form);
        for (SupplierInfoVO record : iPage.getRecords()) {
            if (StringUtils.isEmpty(record.getProductClassify())) {
                continue;
            }
            String[] tmps = record.getProductClassify().split(",");
            List<Long> ids = new ArrayList<>();
            for (String tmp : tmps) {
                ids.add(Long.parseLong(tmp));
            }
            List<ProductClassify> productClassifies = productClassifyService.getByIds(ids);
            StringBuilder sb = new StringBuilder();
            for (ProductClassify productClassify : productClassifies) {
                sb.append(productClassify.getName()).append(",");
            }
            record.setProductClassify(sb.substring(0, sb.length() - 1));
            record.setSettlementType(SettlementTypeEnum.getDesc(record.getSettlementType()));
            record.setAuditStatus(AuditStatusEnum.getDesc(record.getAuditStatus()));
        }
        return iPage;
    }


    /**
     * 获取启用审核通过供应商
     * TODO 暂时不能用
     */
    @Override
    public List<SupplierInfo> getApprovedSupplier(List<Long> supplierIds) {

        List<SupplierInfo> approvedSupplier = this.baseMapper.getApprovedSupplier(StatusEnum.ENABLE.getCode(),
                AuditStatusEnum.SUCCESS.getCode(), AuditTypeDescEnum.ONE.getTable(), supplierIds);
//        QueryWrapper<SupplierInfo> condition = new QueryWrapper<>();
//        if (fields != null) {
//            condition.select(fields);
//        }
//        condition.lambda().eq(SupplierInfo::getStatus, StatusEnum.ENABLE.getCode());
//        List<SupplierInfo> supplierInfos = this.baseMapper.selectList(condition);
//        //查询所有审核通过的供应商
//        List<SupplierInfo> tmp = new ArrayList<>();
//        for (SupplierInfo supplierInfo : supplierInfos) {
//            AuditInfo info = this.auditInfoService.getAuditInfoLatestByExtId(supplierInfo.getId()
//                    , AuditTypeDescEnum.ONE.getTable());
//            if (info == null) {
//                continue;
//            }
//            if (AuditStatusEnum.SUCCESS.getCode().equals(info.getAuditStatus())) {
//                tmp.add(supplierInfo);
//            }
//        }
        return approvedSupplier;
    }

    /**
     * 获取启用审核通过供应商
     */
    @Override
    public List<SupplierInfo> getApprovedSupplier(String... fields) {
        QueryWrapper<SupplierInfo> condition = new QueryWrapper<>();
        if (fields != null) {
            condition.select(fields);
        }
        condition.lambda().eq(SupplierInfo::getStatus, StatusEnum.ENABLE.getCode());
        List<SupplierInfo> supplierInfos = this.baseMapper.selectList(condition);
        //查询所有审核通过的供应商
        List<SupplierInfo> tmp = new ArrayList<>();
        for (SupplierInfo supplierInfo : supplierInfos) {
            AuditInfo info = this.auditInfoService.getAuditInfoLatestByExtId(supplierInfo.getId()
                    , AuditTypeDescEnum.ONE.getTable());
            if (info == null) {
                continue;
            }
            if (AuditStatusEnum.SUCCESS.getCode().equals(info.getAuditStatus())) {
                tmp.add(supplierInfo);
            }
        }
        return tmp;
    }


    /**
     * 校验唯一性
     *
     * @return
     */
    @Override
    public boolean checkUnique(SupplierInfo supplierInfo) {
        QueryWrapper<SupplierInfo> condition = new QueryWrapper<>();
        if (supplierInfo.getId() != null) {
            //修改过滤自身名字
            condition.lambda().and(tmp -> tmp.eq(SupplierInfo::getId, supplierInfo.getId())
                    .eq(SupplierInfo::getSupplierChName, supplierInfo.getSupplierChName()));
            int count = this.count(condition);
            if (count > 0) {
                //匹配到自己名称,不进行唯一校验
                return false;
            }
        }
        condition = new QueryWrapper<>();
        condition.lambda().and(tmp -> tmp.eq(SupplierInfo::getSupplierCode, supplierInfo.getSupplierCode())
                .or().eq(SupplierInfo::getSupplierChName, supplierInfo.getSupplierChName()));
        return this.count(condition) > 0;
    }

    private HashMap<String, Object> hashMap = new HashMap<>();

    @Override
    public String importCustomerInfoExcel(HttpServletResponse response, MultipartFile file, String userName) throws Exception {
        InputStream in = null;
        List<List<String>> listob = null;

        if (file.isEmpty()) {
            throw new Exception("文件不存在！");
        }
        in = file.getInputStream();
        listob = ImportExcelUtil.getBankListByExcel(in, file.getOriginalFilename());

        in.close();

        int successCount = 0;
        int failCount = 0;
        //导入字段条件判断
        ArrayList<ArrayList<String>> fieldData = new ArrayList<ArrayList<String>>();//必填值为空的数据行
        SupplierInfo supplierInfo = new SupplierInfo();//格式无误的数据行
        int lisize = listob.size();
        for (int i = 0; i < lisize; i++) {
            List<String> lo = listob.get(i);

            if (com.alibaba.nacos.client.utils.StringUtils.isNotBlank(lo.get(0)) && com.alibaba.nacos.client.utils.StringUtils.isNotBlank(lo.get(1)) && com.alibaba.nacos.client.utils.StringUtils.isNotBlank(lo.get(2)) &&
                    com.alibaba.nacos.client.utils.StringUtils.isNotBlank(lo.get(3)) && com.alibaba.nacos.client.utils.StringUtils.isNotBlank(lo.get(4)) &&
                    com.alibaba.nacos.client.utils.StringUtils.isNotBlank(lo.get(5)) && com.alibaba.nacos.client.utils.StringUtils.isNotBlank(lo.get(6)) && com.alibaba.nacos.client.utils.StringUtils.isNotBlank(lo.get(7)) &&
                    com.alibaba.nacos.client.utils.StringUtils.isNotBlank(lo.get(8)) && com.alibaba.nacos.client.utils.StringUtils.isNotBlank(lo.get(9)) && com.alibaba.nacos.client.utils.StringUtils.isNotBlank(lo.get(11))) {//判断每行某个数据是否符合规范要求
                //符合要求，插入到数据库supplierInfo表中
                String s = saveSupplierInfoFromExcel(supplierInfo, lo, userName);
                if (s.equals("添加成功")) {
                    lo = null;
                    successCount += 1;
                } else {
                    //数据不符合要求
                    ArrayList<String> dataString = new ArrayList<String>();
                    for (int j = 0; j < lo.size(); j++) {
                        String a;
                        if (lo.get(j) == null || lo.get(j) == "") {
                            a = "null";
                        } else {
                            a = lo.get(j);
                        }

                        dataString.add(a);
                    }
                    dataString.add(s);
                    fieldData.add(dataString);
                }

            } else {
                //需要另外生成excel的不规范行，构造excel的数据
                ArrayList<String> dataString = new ArrayList<String>();
                for (int j = 0; j < lo.size(); j++) {
                    String a;
                    if (lo.get(j) == null || lo.get(j) == "") {
                        a = "null";
                    } else {
                        a = lo.get(j);
                    }

                    dataString.add(a);
                }
                dataString.add("有必填的数据未填写，请重新填写");
                fieldData.add(dataString);
            }

        }
        failCount = fieldData.size();
        String o = null;
        if (failCount > 0) {
            //不符合规范的数据保存在redis中，重新生成EXCEL表
            hashMap.put(userName, fieldData);

//            insExcel(fieldData,response);
            o = "成功导入" + successCount + "行，未成功导入" + failCount + "行,请在有误数据表内查看!";
        } else {
            o = "全部导入成功！";
        }

        return o;
    }

    private String saveSupplierInfoFromExcel(SupplierInfo supplierInfo, List<String> lo, String userName) {
        supplierInfo.setSupplierChName(lo.get(0));
        supplierInfo.setSupplierCode(lo.get(1));
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("supplier_ch_name", lo.get(0));

        SupplierInfo supplierInfo1 = baseMapper.selectOne(queryWrapper);
        if (supplierInfo1 != null) {
            return "供应商名称已存在";
        }
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("supplier_code", lo.get(1));
        SupplierInfo supplierInfo2 = baseMapper.selectOne(queryWrapper1);
        if (supplierInfo2 != null) {
            return "供应商代码已存在";
        }

        String[] str = lo.get(2).split("/");
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            ProductClassify productClassify = productClassifyService.getProductClassifyId(str[i]);
            if (productClassify == null) {
                return str[i] + "该服务类型不存在";
            }
            if (i == str.length - 1) {
                stringBuffer.append(productClassify.getId().toString());
            } else {
                stringBuffer.append(productClassify.getId().toString()).append(",");
            }
        }
        supplierInfo.setProductClassifyIds(stringBuffer.toString());

        supplierInfo.setContacts(lo.get(3));
        supplierInfo.setContactNumber(lo.get(4));
        supplierInfo.setAddress(lo.get(5));
        supplierInfo.setSettlementType(SettlementTypeEnum.getCode(lo.get(6)));
        supplierInfo.setPaymentDay(lo.get(7));
        supplierInfo.setTaxReceipt(lo.get(8));
        supplierInfo.setRate(lo.get(9));

        String s1 = lo.get(10);
        if (s1 == null || s1.equals("") || s1 + "" == "") {
            supplierInfo.setBuyerId(null);
        } else {
            ApiResult systemUserBySystemName = oauthClient.getSystemUserBySystemName(s1);
            if (systemUserBySystemName.getMsg().equals("fail")) {
                return "采购人员名称数据与系统不匹配";
            }
            Long buyerId = Long.parseLong(systemUserBySystemName.getData().toString());
            supplierInfo.setBuyerId(buyerId);
        }
        String s = lo.get(11);
        String[] legalNames = s.split("/");
        List<Long> legalId = new ArrayList<>();
        for (String legalName : legalNames) {
            ApiResult legalEntityByLegalName = oauthClient.getLegalEntityByLegalName(legalName);
            if (legalEntityByLegalName.getMsg().equals("fail")) {
                return "法人主体数据与系统不匹配";
            }
            legalId.add(Long.parseLong(legalEntityByLegalName.getData().toString()));
        }
//        supplierInfo.setLegalEntityIds(Long.parseLong(legalEntityByLegalName.getData().toString()));
        supplierInfo.setCreateUser(userName);
        boolean b1 = this.saveOrUpdate(supplierInfo);
        if (b1) {
            for (int i = 0; i < legalId.size(); i++) {
                SupplierRelaLegal supplierRelaLegal = new SupplierRelaLegal();
                supplierRelaLegal.setSupplierInfoId(supplierInfo.getId());
                supplierRelaLegal.setLegalEntityId(legalId.get(i));
                supplierRelaLegal.setCreatedUser(userName);
                supplierRelaLegal.setCreatedTime(LocalDateTime.now());
                boolean save = supplierRelaLegalServic.save(supplierRelaLegal);
                if (!save) {
                    return "法人主体添加失败";
                }
            }
        }
        boolean b = auditInfoService.saveOrUpdateAuditInfo(new AuditInfo()
                .setExtId(supplierInfo.getId())
                .setExtDesc(AuditTypeDescEnum.ONE.getTable())
                .setAuditTypeDesc(AuditTypeDescEnum.ONE.getDesc())
                .setAuditStatus(AuditStatusEnum.CW_WAIT.getCode())
        );
        if (!b) {
            return "审核状态添加失败";
        }
        return "添加成功";
    }


    public void insExcel(HttpServletResponse response, String userName) throws Exception {
        ArrayList<ArrayList<String>> fieldData = (ArrayList<ArrayList<String>>) hashMap.get(userName);
        if (fieldData != null && fieldData.size() > 0) {//如果存在不规范行，则重新生成表
            //使用ExcelFileGenerator完成导出
            LoadExcelUtil loadExcelUtil = new LoadExcelUtil(fieldData);
            OutputStream os = response.getOutputStream();
            //导出excel建议加上重置输出流，可以不加该代码，但是如果不加必须要保证输出流中不应该在存在其他数据，否则导出会有问题
            response.reset();
            //配置：//文件名
            String fileName = "有误数据表（" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "）.xls";
            //处理乱码
            fileName = new String(fileName.getBytes("utf-8"), "iso-8859-1");

            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            response.setBufferSize(1024);
            //导出excel的操作
            loadExcelUtil.expordExcel2(os);
        }
    }

    @Override
    public boolean checkMes(String userName) {
        ArrayList<ArrayList<String>> fieldData = (ArrayList<ArrayList<String>>) hashMap.get(userName);
        if (fieldData != null) {
            return true;
        }
        return false;
    }

    @Override
    public List<SupplierInfo> findSupplierInfoByCondition() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", "1");
        List<SupplierInfo> list = baseMapper.selectList(queryWrapper);
        List<SupplierInfo> supplierInfos = new ArrayList<>();
        for (SupplierInfo supplierInfo : list) {
            AuditInfo auditInfo = this.auditInfoService.getAuditInfoLatestByExtId(supplierInfo.getId(), AuditTypeDescEnum.ONE.getTable());
            if (auditInfo.getAuditStatus().equals("10")) {
                supplierInfos.add(supplierInfo);
            }
        }
        return supplierInfos;
    }

    /**
     * 根据供应商名称查询供应商信息
     */
    @Override
    public SupplierInfo getByName(String name) {
        QueryWrapper<SupplierInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(SupplierInfo::getSupplierChName, name);
        return this.getOne(condition);
    }

    @Override
    public boolean exitName(Long id, String supplierChName) {
        QueryWrapper<SupplierInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(SupplierInfo::getSupplierChName, supplierChName);
        if (id != null) {
            //修改过滤自身名字
            condition.lambda().ne(SupplierInfo::getId, id);
        }
        return this.count(condition) > 0;
    }

    @Override
    public List<SupplierInfo> existSupplierInfo(String supplierCode) {
        QueryWrapper<SupplierInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(SupplierInfo::getSupplierCode, supplierCode);
        return this.baseMapper.selectList(condition);
    }

    @Override
    public boolean exitCode(Long id, String supplierCode) {
        QueryWrapper<SupplierInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(SupplierInfo::getSupplierCode, supplierCode);
        if (id != null) {
            //修改过滤自身名字
            condition.lambda().ne(SupplierInfo::getId, id);
        }
        return this.count(condition) > 0;
    }

}
