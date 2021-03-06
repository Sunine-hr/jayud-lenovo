package com.jayud.oms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.config.ImportExcelUtil;
import com.jayud.oms.config.LoadExcelUtil;
import com.jayud.oms.feign.FileClient;
import com.jayud.oms.feign.InlandTpClient;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.feign.TmsClient;
import com.jayud.oms.mapper.SupplierInfoMapper;
import com.jayud.oms.mapper.VehicleInfoMapper;
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
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * ??????????????? ???????????????
 * </p>
 *
 * @author ?????????
 * @since 2020-10-29
 */
@Service
public class SupplierInfoServiceImpl extends ServiceImpl<SupplierInfoMapper, SupplierInfo> implements ISupplierInfoService {
    //????????????
    @Autowired
    private IProductClassifyService productClassifyService;
    @Autowired
    private IAuditInfoService auditInfoService;
    @Autowired
    private OauthClient oauthClient;
    @Autowired
    private ISupplierRelaLegalService supplierRelaLegalServic;
    @Autowired
    private FileClient fileClient;
    @Autowired
    private VehicleInfoMapper vehicleInfoMapper;
    @Autowired
    private TmsClient tmsClient;
    @Autowired
    private InlandTpClient inlandTpClient;

    /**
     * ??????????????????
     *
     * @param form
     * @return
     */
    @Override
    public IPage<SupplierInfoVO> findSupplierInfoByPage(QuerySupplierInfoForm form) {
        Page<SupplierInfoVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        page.addOrder(OrderItem.desc("s.id"));
        //?????????????????????????????????
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(form.getLoginUserName());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        Object url = fileClient.getBaseUrl().getData();

        IPage<SupplierInfoVO> iPage = this.baseMapper.findSupplierInfoByPage(page, form, legalIds);
        for (SupplierInfoVO record : iPage.getRecords()) {
            if (StringUtils.isEmpty(record.getProductClassify())) {
                continue;
            }
            record.assembleAccessories(url);
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

            //??????????????????
//            LegalEntityVO legalEntityVO = ConvertUtil.convert(oauthClient.getLegalEntityByLegalId(record.getLegalEntityIds().get(0)).getData(), LegalEntityVO.class);
//            record.setLegalEntityName(legalEntityVO.getLegalName());
            //??????????????????
            AuditInfo auditInfo = this.auditInfoService.getAuditInfoLatestByExtId(record.getId(), AuditTypeDescEnum.ONE.getTable());
            record.setAuditStatus(AuditStatusEnum.getDesc(auditInfo.getAuditStatus()));
        }

        return iPage;
    }

    /**
     * ?????????????????????
     *
     * @param form
     * @return
     */
    @Override
    @Transactional
    public Long saveOrUpdateSupplierInfo(AddSupplierInfoForm form) {
        StringBuilder sb = new StringBuilder();
        for (Long id : form.getProductClassifyIds()) {
            sb.append(id).append(",");
        }

        SupplierInfo supplierInfo = ConvertUtil.convert(form, SupplierInfo.class);
        supplierInfo.setProductClassifyIds(sb.substring(0, sb.length() - 1));

        supplierInfo.setFilePath(com.jayud.common.utils.StringUtils.getFileStr(form.getFileViews()))
                .setFileName(com.jayud.common.utils.StringUtils.getFileNameStr(form.getFileViews()));

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
        Long supplierInfoId = null;
        if (isTrue) {
            supplierInfoId = supplierInfo.getId();
        }
        form.setId(supplierInfo.getId());
        //?????????????????????????????????
        this.supplierRelaLegalServic.saveSupplierRelLegal(form);

        //???????????????
        auditInfoService.saveOrUpdateAuditInfo(new AuditInfo()
                .setExtId(supplierInfo.getId())
                .setExtDesc(AuditTypeDescEnum.ONE.getTable())
                .setAuditTypeDesc(AuditTypeDescEnum.ONE.getDesc())
                .setAuditStatus(AuditStatusEnum.CW_WAIT.getCode())
        );
        return supplierInfoId;
    }

    /**
     * ?????????????????????????????????
     */
    @Override
    public IPage<SupplierInfoVO> findAuditSupplierInfoByPage(QueryAuditSupplierInfoForm form) {
        Page page = new Page(form.getPageNum(), form.getPageSize());

        page.addOrder(OrderItem.desc("s.id"));
        form.setAuditTableDesc(AuditTypeDescEnum.ONE.getTable());
        IPage<SupplierInfoVO> iPage = this.baseMapper.findAuditSupplierInfoByPage(page, form);

        Object url = fileClient.getBaseUrl().getData();
        for (SupplierInfoVO record : iPage.getRecords()) {
            if (StringUtils.isEmpty(record.getProductClassify())) {
                continue;
            }
            record.assembleAccessories(url);
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
     * ?????????????????????????????????
     * TODO ???????????????
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
//        //????????????????????????????????????
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
     * ?????????????????????????????????
     */
    @Override
    public List<SupplierInfo> getApprovedSupplier(String... fields) {
        QueryWrapper<SupplierInfo> condition = new QueryWrapper<>();
        if (fields != null) {
            condition.lambda().select(SupplierInfo::getId);
            condition.select(fields);
        }
        condition.lambda().eq(SupplierInfo::getStatus, StatusEnum.ENABLE.getCode());
        List<SupplierInfo> supplierInfos = this.baseMapper.selectList(condition);
        List<Long> ids = supplierInfos.stream().map(SupplierInfo::getId).collect(Collectors.toList());
        //????????????????????????????????????
        List<SupplierInfo> tmp = new ArrayList<>();
        //????????????
        List<AuditInfo> list = this.auditInfoService.getAuditInfoLatestsByExtIds(ids, AuditTypeDescEnum.ONE.getTable());
        Map<Long, AuditInfo> auditInfoMap = list.stream().collect(Collectors.toMap(AuditInfo::getExtId, e -> e));

        for (SupplierInfo supplierInfo : supplierInfos) {
//            AuditInfo info = this.auditInfoService.getAuditInfoLatestByExtId(supplierInfo.getId()
//                    , AuditTypeDescEnum.ONE.getTable());
            AuditInfo auditInfo = auditInfoMap.get(supplierInfo.getId());
            if (auditInfo == null) {
                continue;
            }
            if (AuditStatusEnum.SUCCESS.getCode().equals(auditInfo.getAuditStatus())) {
                tmp.add(supplierInfo);
            }
        }
        return tmp;
    }


    /**
     * ???????????????
     *
     * @return
     */
    @Override
    public boolean checkUnique(SupplierInfo supplierInfo) {
        QueryWrapper<SupplierInfo> condition = new QueryWrapper<>();
        if (supplierInfo.getId() != null) {
            //????????????????????????
            condition.lambda().and(tmp -> tmp.eq(SupplierInfo::getId, supplierInfo.getId())
                    .eq(SupplierInfo::getSupplierChName, supplierInfo.getSupplierChName()));
            int count = this.count(condition);
            if (count > 0) {
                //?????????????????????,?????????????????????
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
            throw new Exception("??????????????????");
        }
        in = file.getInputStream();
        listob = ImportExcelUtil.getBankListByExcel(in, file.getOriginalFilename());

        in.close();

        int successCount = 0;
        int failCount = 0;
        //????????????????????????
        ArrayList<ArrayList<String>> fieldData = new ArrayList<ArrayList<String>>();//???????????????????????????
        SupplierInfo supplierInfo = new SupplierInfo();//????????????????????????
        int lisize = listob.size();
        for (int i = 0; i < lisize; i++) {
            List<String> lo = listob.get(i);

            if (com.alibaba.nacos.client.utils.StringUtils.isNotBlank(lo.get(0)) && com.alibaba.nacos.client.utils.StringUtils.isNotBlank(lo.get(1)) && com.alibaba.nacos.client.utils.StringUtils.isNotBlank(lo.get(2)) &&
                    com.alibaba.nacos.client.utils.StringUtils.isNotBlank(lo.get(3)) && com.alibaba.nacos.client.utils.StringUtils.isNotBlank(lo.get(4)) &&
                    com.alibaba.nacos.client.utils.StringUtils.isNotBlank(lo.get(5)) && com.alibaba.nacos.client.utils.StringUtils.isNotBlank(lo.get(6)) && com.alibaba.nacos.client.utils.StringUtils.isNotBlank(lo.get(7)) &&
                    com.alibaba.nacos.client.utils.StringUtils.isNotBlank(lo.get(8)) && com.alibaba.nacos.client.utils.StringUtils.isNotBlank(lo.get(9)) && com.alibaba.nacos.client.utils.StringUtils.isNotBlank(lo.get(11))) {//????????????????????????????????????????????????
                //?????????????????????????????????supplierInfo??????
                String s = saveSupplierInfoFromExcel(supplierInfo, lo, userName);
                if (s.equals("????????????")) {
                    lo = null;
                    successCount += 1;
                } else {
                    //?????????????????????
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
                //??????????????????excel????????????????????????excel?????????
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
                dataString.add("?????????????????????????????????????????????");
                fieldData.add(dataString);
            }

        }
        failCount = fieldData.size();
        String o = null;
        if (failCount > 0) {
            //?????????????????????????????????redis??????????????????EXCEL???
            hashMap.put(userName, fieldData);

//            insExcel(fieldData,response);
            o = "????????????" + successCount + "?????????????????????" + failCount + "???,??????????????????????????????!";
        } else {
            o = "?????????????????????";
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
            return "????????????????????????";
        }
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("supplier_code", lo.get(1));
        SupplierInfo supplierInfo2 = baseMapper.selectOne(queryWrapper1);
        if (supplierInfo2 != null) {
            return "????????????????????????";
        }

        String[] str = lo.get(2).split("/");
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            ProductClassify productClassify = productClassifyService.getProductClassifyId(str[i]);
            if (productClassify == null) {
                return str[i] + "????????????????????????";
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
                return "??????????????????????????????????????????";
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
                return "????????????????????????????????????";
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
                    return "????????????????????????";
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
            return "????????????????????????";
        }
        return "????????????";
    }


    public void insExcel(HttpServletResponse response, String userName) throws Exception {
        ArrayList<ArrayList<String>> fieldData = (ArrayList<ArrayList<String>>) hashMap.get(userName);
        if (fieldData != null && fieldData.size() > 0) {//?????????????????????????????????????????????
            //??????ExcelFileGenerator????????????
            LoadExcelUtil loadExcelUtil = new LoadExcelUtil(fieldData);
            OutputStream os = response.getOutputStream();
            //??????excel????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            response.reset();
            //?????????//?????????
            String fileName = "??????????????????" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "???.xls";
            //????????????
            fileName = new String(fileName.getBytes("utf-8"), "iso-8859-1");

            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            response.setBufferSize(1024);
            //??????excel?????????
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
     * ??????????????????????????????????????????
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
            //????????????????????????
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
            //????????????????????????
            condition.lambda().ne(SupplierInfo::getId, id);
        }
        return this.count(condition) > 0;
    }

    /**
     * ??????????????????????????????
     *
     * @param status
     * @param legalIds
     * @return
     */
    @Override
    public Integer getNumByStatus(String status, List<Long> legalIds) {
        Integer num = this.baseMapper.getNumByStatus(status, legalIds);
        return num == null ? 0 : num;
    }

    @Override
    public List<SupplierInfo> getByCondition(SupplierInfo supplierInfo) {
        return this.baseMapper.selectList(new QueryWrapper<>(supplierInfo));
    }

    /**
     * ???????????????????????????tree
     *
     * @return ?????????????????????tree
     */
    @Override
    public List<Map<String, Object>> getSupplierVehicleTree(Map<String, Object> param) {

        String supName = MapUtil.getStr(param, "supName");
        String plateNumber = MapUtil.getStr(param, "plateNumber");

        //????????????????????????
        QueryWrapper<SupplierInfo> supplierInfoQueryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(supName)) {
            supplierInfoQueryWrapper.lambda().like(SupplierInfo::getSupplierChName, supName);
        }
        supplierInfoQueryWrapper.lambda().ne(SupplierInfo::getGpsAddress, "");
        List<SupplierInfo> supplierInfos = baseMapper.selectList(supplierInfoQueryWrapper);
        //????????????????????????????????????
        QueryWrapper<VehicleInfo> vehicleInfoQueryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(plateNumber)) {
            vehicleInfoQueryWrapper.lambda().like(VehicleInfo::getPlateNumber, plateNumber);
        }
        List<VehicleInfo> vehicleInfos = vehicleInfoMapper.selectList(vehicleInfoQueryWrapper);
        List<Map<String, Object>> supplierVehicleTree = new ArrayList<>();
        supplierInfos.forEach(supplierInfo -> {
            Map<String, Object> supMap = new HashMap<>();
            supMap.put("id", supplierInfo.getId());//id
            supMap.put("label", supplierInfo.getSupplierChName());//name
            supMap.put("level", 1);//level
            supMap.put("description", "?????????");//description
            List<Map<String, Object>> children = new ArrayList<>();
            vehicleInfos.forEach(vehicleInfo -> {
                //???????????????id?????????
                if (supplierInfo.getId().equals(vehicleInfo.getSupplierId())) {
                    Map<String, Object> vehMap = new HashMap<>();
                    vehMap.put("id", vehicleInfo.getId());
                    vehMap.put("label", vehicleInfo.getPlateNumber());
                    vehMap.put("level", 2);//level
                    vehMap.put("description", "???????????????");//description
                    vehMap.put("children", new ArrayList<>());
                    children.add(vehMap);
                }
            });
            //??????????????????????????????
            if (CollUtil.isNotEmpty(children)) {
                supMap.put("children", children);
                supplierVehicleTree.add(supMap);
            }
        });
        return supplierVehicleTree;
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @return ?????????list
     */
    @Override
    public List<Map<String, Object>> getList() {
        //????????????????????????
        QueryWrapper<SupplierInfo> supplierInfoQueryWrapper = new QueryWrapper<>();
        supplierInfoQueryWrapper.lambda().ne(SupplierInfo::getGpsAddress, "");
        List<SupplierInfo> supplierInfos = baseMapper.selectList(supplierInfoQueryWrapper);
        //????????????????????????????????????
        QueryWrapper<VehicleInfo> vehicleInfoQueryWrapper = new QueryWrapper<>();
        List<VehicleInfo> vehicleInfos = vehicleInfoMapper.selectList(vehicleInfoQueryWrapper);

        List<Map<String, Object>> list = new ArrayList<>();

        supplierInfos.forEach(supplierInfo -> {
            Map<String, Object> supMap = new HashMap<>();
            supMap.put("supId", supplierInfo.getId());
            supMap.put("supName", supplierInfo.getSupplierChName());
            List<Map<String, Object>> children = new ArrayList<>();
            vehicleInfos.forEach(vehicleInfo -> {
                //???????????????id?????????
                if (supplierInfo.getId().equals(vehicleInfo.getSupplierId())) {
                    Map<String, Object> vehMap = new HashMap<>();
                    vehMap.put("id", vehicleInfo.getId());
                    vehMap.put("label", vehicleInfo.getPlateNumber());
                    vehMap.put("level", 2);//level
                    vehMap.put("description", "???????????????");//description
                    vehMap.put("children", new ArrayList<>());
                    children.add(vehMap);
                }
            });
            //??????????????????????????????
            if (CollUtil.isNotEmpty(children)) {
                list.add(supMap);
            }
        });
        return list;
    }

    /**
     * ????????????tree
     *
     * @param orderTypeCode   ????????????
     * @param pickUpTimeStart ????????????Start
     * @param pickUpTimeEnd   ????????????End
     * @param orderNo         ?????????
     * @return ??????tree
     */
    @Override
    public List<Map<String, Object>> getOrderTree(String orderTypeCode, String pickUpTimeStart, String pickUpTimeEnd, String orderNo) {
        //ZGYS("zgys", "order_transport", "????????????"),
        //NL("nl", "order_inland_transport", "????????????"),
        LocalDateTime localDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (StrUtil.isEmpty(pickUpTimeStart)) {
            //???30???
            LocalDateTime minus = localDate.minus(30, ChronoUnit.DAYS);
            pickUpTimeStart = minus.format(formatter);
        }
        if (StrUtil.isEmpty(pickUpTimeEnd)) {
            pickUpTimeEnd = localDate.format(formatter);
        }

        List<Map<String, Object>> orderTree = new ArrayList<>();
        if (StrUtil.isEmpty(orderTypeCode)) {
            //????????????
            //??????????????????
            Map<String, Object> zgys = zgysExtracted(pickUpTimeStart, pickUpTimeEnd, orderNo);
            if (ObjectUtil.isNotEmpty(zgys)) {
                orderTree.add(zgys);
            }
            //??????????????????
            Map<String, Object> nl = nlExtracted(pickUpTimeStart, pickUpTimeEnd, orderNo);
            if (ObjectUtil.isNotEmpty(nl)) {
                orderTree.add(nl);
            }
        } else if (StrUtil.isNotEmpty(orderTypeCode) && orderTypeCode.equals(SubOrderSignEnum.ZGYS.getSignTwo())) {
            //??????????????????
            Map<String, Object> zgys = zgysExtracted(pickUpTimeStart, pickUpTimeEnd, orderNo);
            if (ObjectUtil.isNotEmpty(zgys)) {
                orderTree.add(zgys);
            }

        } else if (StrUtil.isNotEmpty(orderTypeCode) && orderTypeCode.equals(SubOrderSignEnum.NL.getSignTwo())) {
            //??????????????????
            Map<String, Object> nl = nlExtracted(pickUpTimeStart, pickUpTimeEnd, orderNo);
            if (ObjectUtil.isNotEmpty(nl)) {
                orderTree.add(nl);
            }
        }

        return orderTree;
    }

    /**
     * ???????????????
     *
     * @param pickUpTimeStart ????????????Start
     * @param pickUpTimeEnd   ????????????End
     * @param orderNo         ?????????
     */
    private Map<String, Object> zgysExtracted(String pickUpTimeStart, String pickUpTimeEnd, String orderNo) {
        //??????????????????
        Map<String, Object> zgys = new HashMap<>();
        zgys.put("id", SubOrderSignEnum.ZGYS.getSignTwo());
        zgys.put("label", SubOrderSignEnum.ZGYS.getDesc());
        zgys.put("level", 1);//level
        zgys.put("description", "????????????");//description

        ApiResult orderTransport = tmsClient.getOrderTransportList(pickUpTimeStart, pickUpTimeEnd, orderNo);
        String s = JSON.toJSONString(orderTransport.getData());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy/MM/dd");


        JSONArray jsonArray = JSON.parseArray(s);//???list????????????

        List<Map<String, Object>> maps1 = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Map<String, Object> jsonMap = JSONObject.toJavaObject(jsonObject, Map.class);
            maps1.add(jsonMap);
        }
        //LinkedHashMap ???????????????????????????
        Map<String, List<Map<String, Object>>> pickUpTime = maps1.stream().collect(Collectors.groupingBy(
                m -> {
                    String signTime = MapUtil.getStr(m, "signTime");//????????????
                    LocalDateTime signTime2 = LocalDateTime.parse(signTime, formatter);//string ??? localDateTime
                    String signTime3 = signTime2.format(formatter2);//localDateTime ??? string
                    return signTime3;
                },
                LinkedHashMap::new,
                Collectors.toList()));

        List<Map<String, Object>> pickUpTimeList = new ArrayList<>();
        for (String s1 : pickUpTime.keySet()) {
            List<Map<String, Object>> list = pickUpTime.get(s1);
            List<Map<String, Object>> orderList = new ArrayList<>();
            list.forEach(m -> {
                String orderId = MapUtil.getStr(m, "id");
                String mainOrderNo = MapUtil.getStr(m, "mainOrderNo");
                String orderNo1 = MapUtil.getStr(m, "orderNo");
                String takeTimeStr = MapUtil.getStr(m, "takeTimeStr");
                String signTime = MapUtil.getStr(m, "signTime");
                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("id", orderId);
                orderMap.put("label", orderNo1);
                orderMap.put("level", 3);//level
                orderMap.put("description", "?????????");
                orderMap.put("children", new ArrayList<>());
                //????????????
                JSONObject extend = new JSONObject();
                extend.put("mainOrderNo", mainOrderNo);//????????????
                extend.put("takeTimeStr", takeTimeStr);//????????????
                extend.put("signTime", signTime);//????????????
                extend.put("subType", SubOrderSignEnum.ZGYS.getSignOne());//??????
                orderMap.put("extend", extend);//????????????
                orderList.add(orderMap);
            });

            Map<String, Object> time = new HashMap<>();
            time.put("id", s1);
            time.put("label", s1);
            time.put("level", 2);//level
            time.put("description", "????????????");
            time.put("children", orderList);
            pickUpTimeList.add(time);
        }
        if (CollUtil.isNotEmpty(pickUpTimeList)) {
            zgys.put("children", pickUpTimeList);
            return zgys;
        } else {
            //????????????????????????
            return null;
        }
    }

    /**
     * ???????????????
     *
     * @param pickUpTimeStart ????????????Start
     * @param pickUpTimeEnd   ????????????End
     * @param orderNo         ?????????
     */
    private Map<String, Object> nlExtracted(String pickUpTimeStart, String pickUpTimeEnd, String orderNo) {
        //??????????????????
        Map<String, Object> nl = new HashMap<>();
        nl.put("id", SubOrderSignEnum.NL.getSignTwo());
        nl.put("label", SubOrderSignEnum.NL.getDesc());
        nl.put("level", 1);//level
        nl.put("description", "????????????");//description

        ApiResult orderInlandTransportList = inlandTpClient.getOrderInlandTransportList(pickUpTimeStart, pickUpTimeEnd, orderNo);
        String s = JSON.toJSONString(orderInlandTransportList.getData());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        JSONArray jsonArray = JSON.parseArray(s);//???list????????????

        List<Map<String, Object>> maps1 = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Map<String, Object> jsonMap = JSONObject.toJavaObject(jsonObject, Map.class);
            maps1.add(jsonMap);
        }
        //LinkedHashMap ???????????????????????????
        Map<String, List<Map<String, Object>>> pickUpTime2 = maps1.stream().collect(Collectors.groupingBy(
                m -> {
                    String signTime = MapUtil.getStr(m, "signTime");//????????????
                    LocalDateTime signTime2 = LocalDateTime.parse(signTime, formatter);//string ??? localDateTime
                    String signTime3 = signTime2.format(formatter2);//localDateTime ??? string
                    return signTime3;
                },
                LinkedHashMap::new,
                Collectors.toList()));

        List<Map<String, Object>> pickUpTimeList = new ArrayList<>();
        for (String s1 : pickUpTime2.keySet()) {
            List<Map<String, Object>> list = pickUpTime2.get(s1);
            List<Map<String, Object>> orderList = new ArrayList<>();
            list.forEach(m -> {
                String orderId = MapUtil.getStr(m, "id");
                String mainOrderNo = MapUtil.getStr(m, "mainOrderNo");
                String orderNo1 = MapUtil.getStr(m, "orderNo");
                String takeTimeStr = MapUtil.getStr(m, "deliveryDate");
                String signTime = MapUtil.getStr(m, "signTime");
                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("id", orderId);
                orderMap.put("label", orderNo1);
                orderMap.put("level", 3);//level
                orderMap.put("description", "?????????");
                orderMap.put("children", new ArrayList<>());
                //????????????
                JSONObject extend = new JSONObject();
                extend.put("mainOrderNo", mainOrderNo);//????????????
                extend.put("takeTimeStr", takeTimeStr);//????????????
                extend.put("signTime", signTime);//????????????
                extend.put("subType", SubOrderSignEnum.NL.getSignOne());//??????
                orderMap.put("extend", extend);//????????????
                orderList.add(orderMap);
            });
            Map<String, Object> time = new HashMap<>();
            time.put("id", s1);
            time.put("label", s1);
            time.put("level", 2);//level
            time.put("description", "????????????");
            time.put("children", orderList);
            pickUpTimeList.add(time);
        }
        if (CollUtil.isNotEmpty(pickUpTimeList)) {
            nl.put("children", pickUpTimeList);
            return nl;
        } else {
            //????????????????????????
            return null;
        }
    }


}
