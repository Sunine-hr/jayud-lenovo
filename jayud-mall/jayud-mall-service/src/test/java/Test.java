import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.util.Md5Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mfc
 * @description:
 * @date 2020/10/23 17:16
 */
public class Test {

    public static void main(String[] args) {

        String pwd = "123456";
        System.out.println(Md5Utils.getMD5(pwd.getBytes()).toUpperCase());//E10ADC3949BA59ABBE56E057F20F883E
//        System.out.println("lll");

        if("E10ADC3949BA59ABBE56E057F20F883E".equalsIgnoreCase(Md5Utils.getMD5(pwd.getBytes()))){
            System.out.println("true");
        }


        Map<String, String> map = new HashMap<>();
        map.put("loginname","123@qq.com");
        map.put("password","123456");

        String json = JSONObject.toJSONString(map);
        System.out.println(json);


    }
}
