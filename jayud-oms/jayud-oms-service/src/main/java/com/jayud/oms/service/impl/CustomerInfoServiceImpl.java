package com.jayud.oms.service.impl;


import com.alibaba.nacos.client.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.ApiResult;
import com.jayud.oms.config.ImportExcelUtil;
import com.jayud.oms.config.LoadExcelUtil;
import com.jayud.oms.config.TypeUtils;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.model.bo.QueryCustomerInfoForm;
import com.jayud.oms.model.bo.QueryRelUnitInfoListForm;
import com.jayud.oms.model.enums.CustomerInfoStatusEnum;
import com.jayud.oms.model.po.CustomerInfo;
import com.jayud.oms.model.vo.CustomerInfoVO;
import com.jayud.oms.model.bo.QueryCusAccountForm;
import com.jayud.oms.service.ICustomerInfoService;
import com.jayud.oms.model.vo.CustAccountVO;
import com.jayud.oms.mapper.CustomerInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Transactional
public class CustomerInfoServiceImpl extends ServiceImpl<CustomerInfoMapper, CustomerInfo> implements ICustomerInfoService {

    @Autowired
    private OauthClient oauthClient;

    @Override
    public IPage<CustomerInfoVO> findCustomerInfoByPage(QueryCustomerInfoForm form) {
        //定义分页参数
        Page<CustomerInfoVO> page = new Page(form.getPageNum(), form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("ci.id"));
        IPage<CustomerInfoVO> pageInfo = baseMapper.findCustomerInfoByPage(page, form);
        return pageInfo;
    }

    @Override
    public CustomerInfoVO getCustomerInfoById(Long id) {
        return baseMapper.getCustomerInfoById(id);
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
        //定义分页参数
        Page<CustomerInfoVO> page = new Page(form.getPageNum(), form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.asc("su.id"));
        IPage<CustAccountVO> pageInfo = baseMapper.findCustAccountByPage(page, form);
        return pageInfo;
    }

    @Override
    public IPage<CustomerInfoVO> findCustomerBasicsInfoByPage(QueryCustomerInfoForm form) {
        Page<CustomerInfo> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findCustomerBasicsInfoByPage(page, form);
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

    private HashMap<String,Object> hashMap = new HashMap<>();

    @Override
    public String importCustomerInfoExcel(HttpServletResponse response,MultipartFile file)throws Exception{
//        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        InputStream in = null;
        List<List<String>>  listob = null;
//        MultipartFile file = multipartRequest.getFile("upfile");

        if (file.isEmpty()) {
            throw new Exception("文件不存在！");
        }
        in = file.getInputStream();
        listob = ImportExcelUtil.getBankListByExcel(in, file.getOriginalFilename());

        in.close();

        int successCount=0;
        int failCount=0;
        //导入字段条件判断
        ArrayList<ArrayList<String>> fieldData = new ArrayList<ArrayList<String>>();//必填值为空的数据行
        CustomerInfo customerInfo = new CustomerInfo();//格式无误的数据行
        String msg = null;
        int lisize = listob.size();
        for (int i = 0; i < lisize; i++) {
            List<String> lo = listob.get(i);

            if (StringUtils.isNotBlank(lo.get(0))&&StringUtils.isNotBlank(lo.get(2))&&StringUtils.isNotBlank(lo.get(3))&&StringUtils.isNotBlank(lo.get(4))&&StringUtils.isNotBlank(lo.get(5))&&
                    StringUtils.isNotBlank(lo.get(7))&&StringUtils.isNotBlank(lo.get(8))&&StringUtils.isNotBlank(lo.get(9))&&
                   StringUtils.isNotBlank(lo.get(15))&& StringUtils.isNotBlank(lo.get(17))) {//判断每行某个数据是否符合规范要求
                //符合要求，插入到数据库customerInfo表中
                String s = saveCustomerInfoFromExcel(customerInfo, lo);
                if(s.equals("添加成功")){
                    lo = null;
                    successCount += 1;
                }else{
                    //不符合要求的数据，返回给用户
                    ArrayList<String> dataString = new ArrayList<String>();
                    for (int j = 0; j < lo.size(); j++) {
                        String a;
                        if(lo.get(j)==null||lo.get(j)==""){
                            a = "";
                        }else{
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
                    if(lo.get(j)==null||lo.get(j)==""){
                        a = " ";
                    }else{
                        a = lo.get(j);
                    }

                    dataString.add(a);
                }
                msg = "有必填的数据未填写，请重新填写";
                dataString.add(msg);
                fieldData.add(dataString);
            }

        }
        failCount=fieldData.size();
        String message=null;
        if (failCount>0) {
            //不符合规范的数据保存在redis中，重新生成EXCEL表
            hashMap.put("errorMsg",fieldData);

//            insExcel(fieldData,response);
            message="成功导入"+successCount+"行，未成功导入"+failCount+"行,请在有误数据表内查看!";
        }else{
            message="全部导入成功！";
        }

        return message;
    }

    private String saveCustomerInfoFromExcel(CustomerInfo customerInfo, List<String> lo) {
        customerInfo.setName(lo.get(0));
        customerInfo.setIdCode(lo.get(1));
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name",lo.get(0));
        queryWrapper.eq("id_code",lo.get(1));
        CustomerInfo customerInfo1 = baseMapper.selectOne(queryWrapper);
        if(customerInfo1!=null){
            return "客户信息已存在";
        }
        Integer typeCode = TypeUtils.getTypeCode(lo.get(2));
        if(typeCode==null){
            return "该客户类型不存在";
        }
        customerInfo.setTypes(typeCode);
        customerInfo.setContact(lo.get(3));
        customerInfo.setPhone(lo.get(4));
        customerInfo.setAddress(lo.get(5));
        customerInfo.setEmail(lo.get(6));

        ApiResult legalEntityByLegalName = oauthClient.getLegalEntityByLegalName(lo.get(7));
        if(legalEntityByLegalName.getMsg().equals("fail")){
            return "法人主体数据与系统不匹配";
        }

        customerInfo.setTfn(lo.get(8));
        customerInfo.setIfContract(TypeUtils.getIfContract(lo.get(9)));
        customerInfo.setSettlementType(TypeUtils.getSettlementType(lo.get(10)));
        customerInfo.setAccountPeriod(lo.get(11));
        customerInfo.setTaxType(lo.get(12));
        customerInfo.setTaxRate(lo.get(13));
        customerInfo.setEstate(Integer.getInteger(lo.get(14)));

        ApiResult deptIdByDeptName = oauthClient.getDeptIdByDeptName(lo.get(15));
        if(deptIdByDeptName.getMsg().equals("fail")){
            return "部门名称数据与系统不匹配";
        }
        String departmentId = String.valueOf(deptIdByDeptName.getData());
        customerInfo.setDepartmentId(departmentId);

        if(lo.get(16)!=null){
            ApiResult systemUserBySystemName = oauthClient.getSystemUserBySystemName(lo.get(16));
            if(systemUserBySystemName.getMsg().equals("fail")){
                return "接单客服名称数据与系统不匹配";
            }
            Long kuId = Long.parseLong(systemUserBySystemName.getData().toString());
            customerInfo.setKuId(kuId);
        }else{
            customerInfo.setKuId(Long.parseLong(lo.get(16)));
        }

        ApiResult systemUserBySystemName1 = oauthClient.getSystemUserBySystemName(lo.get(17));
        if(systemUserBySystemName1.getMsg().equals("fail")){
            return "业务员名称数据与系统不匹配";
        }
        Long ywId = Long.parseLong(systemUserBySystemName1.getData().toString());
        customerInfo.setYwId(ywId);
        customerInfo.setAuditStatus(CustomerInfoStatusEnum.KF_WAIT_AUDIT.getCode());
        ApiResult loginUser = oauthClient.getLoginUser();
        customerInfo.setCreatedUser(loginUser.getData().toString());

        baseMapper.insert(customerInfo);
        return "添加成功";
    }


    public void insExcel(HttpServletResponse response) throws Exception{
        ArrayList<ArrayList<String>> fieldData = (ArrayList<ArrayList<String>>)hashMap.get("errorMsg");
        if(fieldData!=null&&fieldData.size()>0){//如果存在不规范行，则重新生成表
            //使用ExcelFileGenerator完成导出
            LoadExcelUtil loadExcelUtil = new LoadExcelUtil(fieldData);
            OutputStream os = response.getOutputStream();
            //导出excel建议加上重置输出流，可以不加该代码，但是如果不加必须要保证输出流中不应该在存在其他数据，否则导出会有问题
            response.reset();
            //配置：//文件名
            String fileName = "有误数据表（"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"）.xls";
            //处理乱码
            fileName = new String(fileName.getBytes("utf-8"),"iso-8859-1");
            //response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename="+fileName);
            response.setBufferSize(1024);
            //导出excel的操作
            loadExcelUtil.expordExcel(os);
        }
    }

    @Override
    public boolean checkMes() {
        ArrayList<ArrayList<String>> fieldData = (ArrayList<ArrayList<String>>)hashMap.get("errorMsg");
        if(fieldData!=null){
            return true;
        }
        return false;
    }

}
