import com.alibaba.fastjson.JSONObject;
import com.jayud.common.enums.PushKingdeeEnum;
import com.jayud.customs.model.po.YunbaoguanKingdeePushLog;
import org.junit.Test;

import java.time.LocalDateTime;

public class YunbaoguanKingdeePushLogTest {

    @Test
    public void test1(){
        YunbaoguanKingdeePushLog form = new YunbaoguanKingdeePushLog();
        form.setApplyNo("530120201010576602");
        form.setPushStatusCode(PushKingdeeEnum.STEP1.getCode());
        form.setPushStatusMsg(PushKingdeeEnum.STEP1.getMsg());
        form.setIpAddress("127.0.0.1");
        form.setUserId(1);
        form.setCreateTime(LocalDateTime.now());
        form.setUpdateTime(LocalDateTime.now());
        String json = JSONObject.toJSONString(form);
        System.out.println(json);
    }

}
