package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.CustomerMapper;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.po.Customer;
import com.jayud.mall.model.vo.CustomerVO;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.ICustomerService;
import com.jayud.mall.service.INumberGeneratedService;
import com.jayud.mall.utils.NumberGeneratedUtils;
import com.jayud.mall.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 客户表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    BaseService baseService;
    @Autowired
    INumberGeneratedService numberGeneratedService;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public IPage<CustomerVO> findCustomerByPage(QueryCustomerForm form) {
        //定义分页参数
        Page<CustomerVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.asc("t.id"));
        IPage<CustomerVO> pageInfo = customerMapper.findCustomerByPage(page, form);
        return pageInfo;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult saveCustomer(CustomerEditForm form) {
        Customer customer = ConvertUtil.convert(form, Customer.class);
        Integer id = form.getId();
        String company = form.getCompany();
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company", company);
        queryWrapper.ne("id", id);
        int companyCount = this.count(queryWrapper);
        if(companyCount > 0){
            return CommonResult.error(-1, "客户(公司)名称,已存在");
        }
        String userName = form.getUserName();
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userName);
        queryWrapper.ne("id", id);
        int userNameCount = this.count(queryWrapper);
        if(userNameCount > 0){
            return CommonResult.error(-1, "登录名,已存在");
        }
        String phone = form.getPhone();
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        queryWrapper.ne("id", id);
        int phoneCount = this.count(queryWrapper);
        if(phoneCount > 0){
            return CommonResult.error(-1, "手机号,已存在");
        }
        this.saveOrUpdate(customer);
        return CommonResult.success("保存客户，成功！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<CustomerVO> auditCustomer(CustomerAuditForm form) {
        Customer customer = ConvertUtil.convert(form, Customer.class);
        AuthUser user = baseService.getUser();
        customer.setAuditUserId(user.getId().intValue());//审核人
        customer.setAuditTime(LocalDateTime.now());//审核时间
        this.saveOrUpdate(customer);
        CustomerVO customerVO = ConvertUtil.convert(customer, CustomerVO.class);
        return CommonResult.success(customerVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<CustomerVO> customerRegister(CustomerRegisterForm form) {
        Customer customer = ConvertUtil.convert(form, Customer.class);
        String passwd = form.getPasswd();
        String affirmPasswd = form.getAffirmPasswd();
        if(!passwd.equals(affirmPasswd)){
            return CommonResult.error(-1, "两次输入的密码不一致");
        }
        String userName = form.getUserName();
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userName);
        int userNameCount = this.count(queryWrapper);
        if(userNameCount > 0){
            return CommonResult.error(-1, "登录名已存在");
        }
        String phone = form.getPhone();
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        int phoneCount = this.count(queryWrapper);
        if (phoneCount > 0) {
            return CommonResult.error(-1, "手机号已存在");
        }
        String company = form.getCompany();
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company", company);
        int companyCount = this.count(queryWrapper);
        if(companyCount > 0){
            return CommonResult.error(-1, "公司名称已存在");
        }
        String verificationCode = form.getVerificationCode();//验证码(前端输入)
        String code = redisUtils.get(phone);//验证码redis
        if(code == null){
            return CommonResult.error(-1, "验证码已过期或不存在");
        }
        if(!verificationCode.equals(code)){
            return CommonResult.error(-1, "验证码不正确");
        }
        //mysql-生成单号，有规则
        String customerCode = NumberGeneratedUtils.getOrderNoByCode2("customer_code");
        customer.setCode(customerCode);//客户代码
        customer.setAuditStatus(0);//审核状态(0待审核1审核通过2审核不通过，默认为0）
        customer.setStatus(1);//启用状态，默认为1，1是0否
        customer.setCreateDate(LocalDateTime.now());//创建日期

        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePwd = bcryptPasswordEncoder.encode(passwd);
        customer.setPasswd(encodePwd);

        this.saveOrUpdate(customer);
        CustomerVO customerVO = ConvertUtil.convert(customer, CustomerVO.class);
        return CommonResult.success(customerVO);
    }

    @Override
    public CommonResult customerVerify(CustomerVerifyForm form) {
        String phone = form.getPhone();
        String verificationCode = form.getVerificationCode();
        String code = redisUtils.get(phone);
        if(code == null){
            return CommonResult.error(-1, "验证码不存在或者已过期");
        }
        if(!verificationCode.equals(code)){
            return CommonResult.error(-1, "验证码不正确");
        }
        return CommonResult.success(phone);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult customerUpdatePwd(CustomerPwdForm form) {
        String phone = form.getPhone();
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        List<Customer> list = this.list(queryWrapper);
        Customer customer = list.get(0);
        String passwd = form.getPasswd();
        String affirmPasswd = form.getAffirmPasswd();
        if(!passwd.equals(affirmPasswd)){
            return CommonResult.error(-1, "两次的密码不一致");
        }
        //BCryptPasswordEncoder 加密
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bcryptPasswordEncoder.encode(passwd.trim());
        customer.setPasswd(password);
        this.saveOrUpdate(customer);
        return CommonResult.success("修改密码成功");
    }

    @Override
    public CommonResult<CustomerVO> findCustomerById(Integer id) {
        Customer customer = this.getById(id);
        CustomerVO customerVO = ConvertUtil.convert(customer, CustomerVO.class);
        return CommonResult.success(customerVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<CustomerVO> customerUpdatePhone(CustomerPhoneForm form) {
        Integer id = form.getId();
        Customer customer = this.getById(id);
        String phone = form.getPhone();
        customer.setPhone(phone);
        this.saveOrUpdate(customer);
        CustomerVO customerVO = ConvertUtil.convert(customer, CustomerVO.class);
        return CommonResult.success(customerVO);
    }

    @Override
    public List<CustomerVO> findCustomer() {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        //queryWrapper.eq("","");
        List<Customer> list = this.list(queryWrapper);
        List<CustomerVO> customerVOS = ConvertUtil.convertList(list, CustomerVO.class);
        return customerVOS;
    }

    @Override
    public CustomerVO customerLogin(CustomerLoginForm form) {
        CustomerVO customerVO = customerMapper.customerLogin(form);
        return customerVO;
    }


}
