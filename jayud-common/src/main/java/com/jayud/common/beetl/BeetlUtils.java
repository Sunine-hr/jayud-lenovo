package com.jayud.common.beetl;

import com.jayud.common.exception.JayudBizException;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.exception.BeetlException;
import org.beetl.core.exception.ErrorInfo;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class BeetlUtils {

    public static String strTemplate(String template, Map<String, Object> map) throws Exception {
        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
        Configuration cfg = Configuration.defaultConfiguration();
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
        //获取模板
        Template t = gt.getTemplate(template);
        t.fastBinding(map);
        BeetlException ex = gt.validateScript(template);
        if (ex != null) {
            ErrorInfo info = new ErrorInfo(ex);
            System.out.println(info.toString());
            throw new JayudBizException(info.getMsg());
        }
        //渲染结果
        String str = t.render();
        return str;
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", 18);
        String str = strTemplate("客户: ${name 年龄${age},合同号${contract}", map);
        System.out.println(str);
    }
}
