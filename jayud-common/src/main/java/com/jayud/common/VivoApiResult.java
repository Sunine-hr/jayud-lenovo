package com.jayud.common;

import lombok.Getter;

/**
 * vivo 异常处理回执
 * <br>（正常处理回执不被全局捕捉，因此在项目下的包中定义正常回执的包装类）
 *
 * @author william
 * @description
 * @Date: 2020-09-16 15:37
 */
@Getter
public class VivoApiResult extends BaseApiResult {
    private int status;
    private String message;

    public VivoApiResult(String message) {
        this.status = 0;
        this.message = message;
    }

    public VivoApiResult(int status ,String message) {
        this.status = status;
        this.message = message;
    }

    public static VivoApiResult error(String message){
        return new VivoApiResult(0,message);
    }

    public static VivoApiResult success(){
        return new VivoApiResult(1,"OK");
    }
}
