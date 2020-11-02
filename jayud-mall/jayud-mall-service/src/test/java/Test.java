import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.util.Md5Utils;
import com.jayud.mall.model.bo.QueryQuotationTemplateFrom;
import com.jayud.mall.model.bo.QueryRoleForm;
import com.jayud.mall.model.bo.QueryUserForm;
import com.jayud.mall.model.bo.SaveRoleForm;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mfc
 * @description:
 * @date 2020/10/23 17:16
 */
public class Test {

    /**
     * 测试密码文本字符串是否相同
     */
    @org.junit.Test
    public void test1(){
        String pwd = "123456";
        System.out.println(Md5Utils.getMD5(pwd.getBytes()).toUpperCase());//E10ADC3949BA59ABBE56E057F20F883E
//        System.out.println("lll");
        if("E10ADC3949BA59ABBE56E057F20F883E".equalsIgnoreCase(Md5Utils.getMD5(pwd.getBytes()))){
            System.out.println("true");
        }
    }

    /**
     * 生成登录的json测试文本
     */
    @org.junit.Test
    public void test2(){
        Map<String, String> map = new HashMap<>();
        map.put("loginname","123@qq.com");
        map.put("password","123456");
        String json = JSONObject.toJSONString(map);
        System.out.println(json);
    }

    /**
     * 测试不同数据类型的数值判断是否相等
     */
    @org.junit.Test
    public void test3(){
        //不同数据类型的值比较是否相等
        Integer a = 0;
        long b = 0;
        if(a.equals(b)){
            System.out.println(true);
        }else{
            System.out.println(false);//为false
        }
        System.out.println("-----------");

        if(Long.valueOf(a).equals(b)){
            System.out.println(true);//为true
        }else{
            System.out.println(false);
        }
    }

    /**
     * 生成新增测试用户json
     */
    @org.junit.Test
    public void test4(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", 141);
        map.put("userName", "张三");
        map.put("companyId", 1L);
//        map.put("", "");//工号
        map.put("enUserName", "zhangsan");
        map.put("phone", "13028280001");
        map.put("email", "13028280001@qq.com");
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        roleIds.add(3L);
        map.put("roleIds", roleIds);//角色

        String json = JSONObject.toJSONString(map);
        System.out.println(json);
    }

    /**
     * 生成修改测试用户json
     */
    @org.junit.Test
    public void test5(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", 134);
        map.put("userName", "张三三");
        map.put("companyId", 1L);
//        map.put("", "");//工号
        map.put("enUserName", "zhangsansan");
        map.put("phone", "13028280001");
        map.put("email", "13028280001@qq.com");
//        map.put("", "");//角色

        String json = JSONObject.toJSONString(map);
        System.out.println(json);
    }

    /**
     * 密码大小写
     */
    @org.junit.Test
    public void test6(){
        String pwd = Md5Utils.getMD5("123456".getBytes());//e10adc3949ba59abbe56e057f20f883e
        System.out.println(pwd.toLowerCase());//e10adc3949ba59abbe56e057f20f883e    小写
        System.out.println(pwd.toUpperCase());//E10ADC3949BA59ABBE56E057F20F883E    大写
    }

    /**
     * 查询用户表单json参数
     */
    @org.junit.Test
    public void Test7(){

        QueryUserForm form = new QueryUserForm();
        form.setUserName("张");
        form.setStatus(1);
        form.setPageNum(1);
        form.setPageSize(10);
        String json = JSONObject.toJSONString(form);
        System.out.println(json);

    }

    /**
     * 保存角色表单json参数
     */
    @org.junit.Test
    public void test8(){

        SaveRoleForm form = new SaveRoleForm();
        form.setId(1L);
        form.setRoleName("管理员");
        form.setRoleDescribe("管理员，管理系统");

        List<Long> menuIds = new ArrayList<>();
        menuIds.add(1L);
        menuIds.add(2L);
        menuIds.add(3L);

        form.setMenuIds(menuIds);
        String json = JSONObject.toJSONString(form);
        System.out.println(json);

    }

    /**
     * 查询角色表单json参数
     */
    @org.junit.Test
    public void test9(){
        QueryRoleForm form = new QueryRoleForm();
        form.setPageNum(1);
        form.setPageSize(2);
        form.setRoleName("经理");
        String json = JSONObject.toJSONString(form);
        System.out.println(json);
    }

    /**
     * BCryptPasswordEncoder 判断密码是否相同
     */
    @org.junit.Test
    public void test10() {
        //加密
        //BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
        //encode.encode(password);

        //比较
        //matches(CharSequence rawPassword, String encodedPassword)

        String pass = "admin";
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String hashPass = bcryptPasswordEncoder.encode(pass.trim());
        System.out.println(hashPass);

        boolean flag = bcryptPasswordEncoder.matches("admin",hashPass);
        System.out.println(flag);

        //每次输出的hashPass 都不一样，但是最终的flag都为 true,即匹配成功。

        pass = "123456";
        hashPass = bcryptPasswordEncoder.encode(pass.trim());
        System.out.println(hashPass);
        hashPass = "$2a$10$HnfCQXhPiEWcO8eRNUb/guVWDsMozTpyf8Nc9qDIDloYUK7kspslK";
        flag = bcryptPasswordEncoder.matches("123456",hashPass);
        System.out.println(flag);

    }

    /**
     * LocalDateTime
     */
    @org.junit.Test
    public void test11(){
        LocalDateTime l = LocalDateTime.now();
        System.out.println(l);
        System.out.println(l.toLocalTime());
        System.out.println(l.toLocalDate());

        String str = "1986-04-08 12:30:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        System.out.println(dateTime);
    }

    @org.junit.Test
    public void test12(){
        QueryQuotationTemplateFrom form = new QueryQuotationTemplateFrom();
        form.setNames("美西-海卡大船");
        form.setSailTime(LocalDateTime.now());
        form.setCutOffTime(LocalDateTime.now());
        form.setDestinationPort("纽约州");
        String json = JSON.toJSONString(form);
        System.out.println(json);
    }


}
