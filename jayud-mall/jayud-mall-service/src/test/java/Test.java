import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.util.Md5Utils;
import com.jayud.mall.model.bo.QueryRoleForm;
import com.jayud.mall.model.bo.QueryUserForm;
import com.jayud.mall.model.bo.SaveRoleForm;

import java.util.HashMap;
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
        map.put("id", null);
        map.put("userName", "张三");
        map.put("companyId", 1L);
//        map.put("", "");//工号
        map.put("enUserName", "zhangsan");
        map.put("phone", "13028280001");
        map.put("email", "13028280001@qq.com");
//        map.put("", "");//角色

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
//        form.setId(0L);
        form.setRoleName("管理员");
        form.setRoleDescribe("管理员，管理系统");
//        form.setMenuIds();
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




}
