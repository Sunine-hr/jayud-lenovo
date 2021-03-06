package com.jayud.oms.service.impl;


import com.alibaba.nacos.client.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.utils.DateUtils;
import com.jayud.oms.config.ImportExcelUtil;
import com.jayud.oms.config.LoadExcelUtil;
import com.jayud.oms.config.TypeUtils;
import com.jayud.oms.feign.FileClient;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.mapper.CustomerInfoMapper;
import com.jayud.oms.model.bo.AddCustomerInfoForm;
import com.jayud.oms.model.bo.QueryCusAccountForm;
import com.jayud.oms.model.bo.QueryCustomerInfoForm;
import com.jayud.oms.model.bo.QueryRelUnitInfoListForm;
import com.jayud.oms.model.enums.CustomerInfoStatusEnum;
import com.jayud.oms.model.po.ClientSecretKey;
import com.jayud.oms.model.po.CustomerInfo;
import com.jayud.oms.model.po.CustomerRelaLegal;
import com.jayud.oms.model.vo.CustAccountVO;
import com.jayud.oms.model.vo.CustomerInfoVO;
import com.jayud.oms.model.vo.InitComboxStrVO;
import com.jayud.oms.service.IClientSecretKeyService;
import com.jayud.oms.service.ICustomerInfoService;
import com.jayud.oms.service.ICustomerRelaLegalService;
import com.jayud.oms.service.ICustomerRelaUnitService;
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
import java.util.stream.Collectors;


@Service
@Transactional
public class CustomerInfoServiceImpl extends ServiceImpl<CustomerInfoMapper, CustomerInfo> implements ICustomerInfoService {

    @Autowired
    private OauthClient oauthClient;
    @Autowired
    private ICustomerRelaLegalService customerRelaLegalService;
    @Autowired
    private ICustomerInfoService customerInfoService;
    @Autowired
    private ICustomerRelaUnitService customerRelaUnitService;
    @Autowired
    private FileClient fileClient;
    @Autowired
    private IClientSecretKeyService clientSecretKeyService;

    @Override
    public IPage<CustomerInfoVO> findCustomerInfoByPage(QueryCustomerInfoForm form) {
        //??????????????????
        Page<CustomerInfoVO> page = new Page(form.getPageNum(), form.getPageSize());
        //??????????????????
        page.addOrder(OrderItem.desc("ci.id"));

        //????????????????????????????????????
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(form.getLoginUserName());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();
        IPage<CustomerInfoVO> pageInfo = baseMapper.findCustomerInfoByPage(page, form, legalIds);

        Object url = fileClient.getBaseUrl().getData();
        pageInfo.getRecords().forEach(e -> {
            e.assembleAccessories(url);
        });
        return pageInfo;
    }

    @Override
    public CustomerInfoVO getCustomerInfoById(Long id) {
        return baseMapper.getCustomerInfoById(id);
    }

    @Override
    public List<CustomerInfo> getCustomerInfoByCondition(List<Long> legalIds) {
        List<CustomerInfo> list = baseMapper.getCustomerInfoByCondition(legalIds);
        return list;
    }

    @Override
    public List<CustomerInfo> findCustomerInfoByCondition(Map<String, Object> param) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", "1");
        for (String key : param.keySet()) {
            String value = String.valueOf(param.get(key));
            queryWrapper.eq(key, value);
        }
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public CustAccountVO getCustAccountByCondition(Map<String, Object> param) {
        return baseMapper.getCustAccountByCondition(param);
    }

    @Override
    public IPage<CustAccountVO> findCustAccountByPage(QueryCusAccountForm form) {
        //??????????????????
        Page<CustomerInfoVO> page = new Page(form.getPageNum(), form.getPageSize());
        //??????????????????
        page.addOrder(OrderItem.asc("su.id"));
        IPage<CustAccountVO> pageInfo = baseMapper.findCustAccountByPage(page, form);
        return pageInfo;
    }

    @Override
    public IPage<CustomerInfoVO> findCustomerBasicsInfoByPage(QueryCustomerInfoForm form) {
        Page<CustomerInfo> page = new Page<>(form.getPageNum(), form.getPageSize());

        //????????????????????????????????????
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(form.getLoginUserName());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        return this.baseMapper.findCustomerBasicsInfoByPage(page, form, legalIds);
    }

    @Override
    public List<CustomerInfoVO> existCustomerInfo(String idCode) {
        return baseMapper.existCustomerInfo(idCode);
    }

    @Override
    public List<CustomerInfoVO> relateUnitList(Long id) {
        return baseMapper.relateUnitList(id);
    }

    @Override
    public List<CustomerInfoVO> findRelateUnitList(QueryRelUnitInfoListForm form) {
        return baseMapper.findRelateUnitList(form);
    }

    private HashMap<String, Object> hashMap = new HashMap<>();

    @Override
    public String importCustomerInfoExcel(HttpServletResponse response, MultipartFile file, String userName) throws Exception {
//        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        InputStream in = null;
        List<List<String>> listob = null;
//        MultipartFile file = multipartRequest.getFile("upfile");

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
//        CustomerInfo customerInfo = new CustomerInfo();//????????????????????????
        String msg = null;
        int lisize = listob.size();
        for (int i = 0; i < lisize; i++) {
            List<String> lo = listob.get(i);

            if (StringUtils.isNotBlank(lo.get(0)) && StringUtils.isNotBlank(lo.get(2)) && StringUtils.isNotBlank(lo.get(3)) && StringUtils.isNotBlank(lo.get(4)) && StringUtils.isNotBlank(lo.get(5)) &&
                    StringUtils.isNotBlank(lo.get(7)) && StringUtils.isNotBlank(lo.get(8)) && StringUtils.isNotBlank(lo.get(9)) &&
                    StringUtils.isNotBlank(lo.get(15)) && StringUtils.isNotBlank(lo.get(17))) {//????????????????????????????????????????????????
                //?????????????????????????????????customerInfo??????
                String s = saveCustomerInfoFromExcel(lo, userName);
                if (s.equals("????????????")) {
                    lo = null;
                    successCount += 1;
                } else {
                    //??????????????????????????????????????????
                    ArrayList<String> dataString = new ArrayList<String>();
                    for (int j = 0; j < lo.size(); j++) {
                        String a;
                        if (lo.get(j) == null || lo.get(j) == "") {
                            a = "";
                        } else {
                            a = lo.get(j);
                        }

                        dataString.add(a);
                    }
                    dataString.add(s);
                    fieldData.add(dataString);
                    lo = null;
                }

            } else {
                //??????????????????excel????????????????????????excel?????????
                ArrayList<String> dataString = new ArrayList<String>();
                for (int j = 0; j < lo.size(); j++) {
                    String a;
                    if (lo.get(j) == null || lo.get(j) == "") {
                        a = " ";
                    } else {
                        a = lo.get(j);
                    }

                    dataString.add(a);
                }
                msg = "?????????????????????????????????????????????";
                dataString.add(msg);
                fieldData.add(dataString);
            }

        }
        failCount = fieldData.size();
        String message = null;
        if (failCount > 0) {
            //?????????????????????????????????redis??????????????????EXCEL???
            hashMap.put(userName, fieldData);

//            insExcel(fieldData,response);
            message = "????????????" + successCount + "?????????????????????" + failCount + "???,??????????????????????????????!";
        } else {
            message = "?????????????????????";
        }

        return message;
    }

    private String saveCustomerInfoFromExcel(List<String> lo, String userName) {
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setCreatedUser(userName);
        customerInfo.setName(lo.get(0));
        customerInfo.setIdCode(lo.get(1));
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name", lo.get(0));
        CustomerInfo customerInfo1 = baseMapper.selectOne(queryWrapper);
        if (customerInfo1 != null) {
            return "?????????????????????";
        }

        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("id_code", lo.get(1));
        CustomerInfo customerInfo2 = baseMapper.selectOne(queryWrapper1);
        if (customerInfo2 != null) {
            return "?????????????????????";
        }
        Integer typeCode = TypeUtils.getTypeCode(lo.get(2));
        if (typeCode == null) {
            return "????????????????????????";
        }
        customerInfo.setTypes(typeCode);
        customerInfo.setContact(lo.get(3));
        customerInfo.setPhone(lo.get(4));
        customerInfo.setAddress(lo.get(5));
        customerInfo.setEmail(lo.get(6));

        String s = lo.get(7);
        String[] legalNames = s.split("/");
        List<Long> legalId = new ArrayList<>();
        for (String legalName : legalNames) {
            ApiResult legalEntityByLegalName = oauthClient.getLegalEntityByLegalName(legalName);
            if (legalEntityByLegalName.getMsg().equals("fail")) {
                return "????????????????????????????????????";
            }
            legalId.add(Long.parseLong(legalEntityByLegalName.getData().toString()));
        }

        customerInfo.setTfn(lo.get(8));
        customerInfo.setIfContract(TypeUtils.getIfContract(lo.get(9)));
        customerInfo.setSettlementType(TypeUtils.getSettlementType(lo.get(10)));
        customerInfo.setAccountPeriod(lo.get(11));
        customerInfo.setTaxType(lo.get(12));
        customerInfo.setTaxRate(lo.get(13));
        if (StringUtils.isEmpty(lo.get(14))) {
            customerInfo.setEstate(null);
        } else {
            customerInfo.setEstate(Integer.parseInt(lo.get(14)));
        }


        ApiResult deptIdByDeptName = oauthClient.getDeptIdByDeptName(lo.get(15));
        if (deptIdByDeptName.getMsg().equals("fail")) {
            return "????????????????????????????????????";
        }
        String departmentId = String.valueOf(deptIdByDeptName.getData());
        customerInfo.setDepartmentId(departmentId);

        String s1 = lo.get(16);
        if (s1 == null || s1.equals("") || s1 + "" == "") {
            customerInfo.setKuId(null);
        } else {
            ApiResult systemUserBySystemName = oauthClient.getSystemUserBySystemName(s1);
            if (systemUserBySystemName.getMsg().equals("fail")) {
                return "??????????????????????????????????????????";
            }
            Long kuId = Long.parseLong(systemUserBySystemName.getData().toString());
            customerInfo.setKuId(kuId);
        }

        ApiResult systemUserBySystemName1 = oauthClient.getSystemUserBySystemName(lo.get(17));
        if (systemUserBySystemName1.getMsg().equals("fail")) {
            return "???????????????????????????????????????";
        }
        Long ywId = Long.parseLong(systemUserBySystemName1.getData().toString());
        customerInfo.setYwId(ywId);
        customerInfo.setAuditStatus(CustomerInfoStatusEnum.KF_WAIT_AUDIT.getCode());
        boolean b = this.customerInfoService.saveOrUpdate(customerInfo);
        if (b) {
            for (int i = 0; i < legalId.size(); i++) {
                CustomerRelaLegal customerRelaLegal = new CustomerRelaLegal();
                customerRelaLegal.setCustomerInfoId(customerInfo.getId());
                customerRelaLegal.setLegalEntityId(legalId.get(i));
                customerRelaLegal.setCreatedUser(userName);
                customerRelaLegal.setCreatedTime(LocalDateTime.now());
                boolean save = customerRelaLegalService.save(customerRelaLegal);
                if (!save) {
                    return "????????????????????????";
                }
            }
        }
        customerInfo = null;
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
            //response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            response.setBufferSize(1024);
            //??????excel?????????
            loadExcelUtil.expordExcel(os);
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

    /**
     * ???????????????????????????
     */
    @Override
    public boolean exitCode(Long customerId, String idCode) {
        QueryWrapper<CustomerInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(CustomerInfo::getIdCode, idCode);
        if (customerId != null) {
            condition.lambda().ne(CustomerInfo::getId, customerId);
        }
        return this.count(condition) > 0;
    }

    @Override
    public List<InitComboxStrVO> initApprovedCustomer() {
        Map<String, Object> param = new HashMap<>();
        param.put(SqlConstant.AUDIT_STATUS, CustomerInfoStatusEnum.AUDIT_SUCCESS.getCode());

        List<CustomerInfo> allCustomerInfoList = customerInfoService.findCustomerInfoByCondition(param);
        List<CustomerInfo> customerInfoList = allCustomerInfoList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(CustomerInfo::getName))), ArrayList::new));
        List<InitComboxStrVO> comboxStrVOS = new ArrayList<>();
        for (CustomerInfo customerInfo : customerInfoList) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(customerInfo.getIdCode());
            comboxStrVO.setName(customerInfo.getName() + " (" + customerInfo.getIdCode() + ")");
            comboxStrVO.setId(customerInfo.getId());
            comboxStrVOS.add(comboxStrVO);
        }
        return comboxStrVOS;
    }


    /**
     * ??????/??????????????????
     */
    @Override
    @Transactional
    public Long saveOrUpdateCustomerInfo(AddCustomerInfoForm form, CustomerInfo customerInfo) {
        customerInfo.setIdCode(StringUtils.isEmpty(customerInfo.getIdCode()) ? null : customerInfo.getIdCode());

        customerInfo.setFilePath(com.jayud.common.utils.StringUtils.getFileStr(form.getFileViews()))
                .setFileName(com.jayud.common.utils.StringUtils.getFileNameStr(form.getFileViews()));
        if (customerInfo.getId() != null) {
            customerInfo.setUpdatedUser(UserOperator.getToken());
            customerInfo.setUpdatedTime(DateUtils.getNowTime());
        } else {
            customerInfo.setCreatedUser(UserOperator.getToken())
                    .setCreatedTime(DateUtils.getNowTime());
        }
        if (StringUtils.isEmpty(customerInfo.getAuditStatus())) {
            customerInfo.setAuditStatus(CustomerInfoStatusEnum.KF_WAIT_AUDIT.getCode());
        }

        customerInfoService.saveOrUpdate(customerInfo);//??????????????????
        form.setId(customerInfo.getId());
        //?????????????????????????????????
        this.customerRelaLegalService.saveCusRelLegal(form);
        //?????????????????????????????????
        this.customerRelaUnitService.saveBatchRelaUnit(customerInfo.getId(), form.getUnitCodeIds());
        //?????????????????? ???????????? ???????????????
        clientSecretKeyService.saveOrUpdateAddr(new ClientSecretKey().setCustomerInfoId(String.valueOf(customerInfo.getId())));
        return customerInfo.getId();
    }

    /**
     * ????????????????????????????????????
     */
    @Override
    public CustomerInfo getByName(String name) {
        QueryWrapper<CustomerInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(CustomerInfo::getName, name);
        return this.getOne(condition);
    }

    /**
     * ???????????????????????????
     */
    @Override
    public boolean exitName(Long customerId, String name) {
        QueryWrapper<CustomerInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(CustomerInfo::getName, name);
        if (customerId != null) {
            condition.lambda().ne(CustomerInfo::getId, customerId);
        }
        return this.count(condition) > 0;
    }

    /**
     * ??????code??????????????????
     */
    @Override
    public CustomerInfo getByCode(String code) {
        QueryWrapper<CustomerInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(CustomerInfo::getIdCode, code);
        return this.baseMapper.selectOne(condition);
    }

    @Override
    public List<CustomerInfo> getByCustomerName(String customerName) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("name", customerName);
        return this.baseMapper.selectList(queryWrapper);
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
    public Map<String, String> getCustomerName() {
        QueryWrapper<CustomerInfo> condition = new QueryWrapper<>();
        condition.lambda().isNotNull(CustomerInfo::getIdCode);
        return this.baseMapper.selectList(condition).stream().collect(Collectors.toMap(e -> e.getIdCode(), e -> e.getName()));
    }

}
