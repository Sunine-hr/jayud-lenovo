import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

/**
 * 南京新智慧，API接口:
 * API 4.0
 * 1、 创建运单
 * [POST] http://<域名>/api/v4/shipment/create
 * 2、 标签获取
 * [POST] http://<域名>/api/v4/shipment/get_labels
 * 3、 服务类型获取
 * [POST] http://<域名>/api/v4/shipment/get_services
 * 4、 取消订单
 * [POST] http://<域名>/api/v4/shipment/void
 * 5、 查询路由信息
 * [POST] http://<域名>/api/v4/shipment/tracking
 * 6、 获取运单信息
 * [POST] http://<域名>/api/v4/shipment/info
 * 7、 修改客户重量尺寸
 * [POST] http://<域名>/api/v4/shipment/update_weight
 * 8、 查看账户余额
 * [POST] http://<域名>/api/v4/shipment/account
 *
 * 域名:jydexp.nextsls.com
 */
@Slf4j
public class APITest {

    @Test
    public void test(){
        //入参键值对
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> validation = new HashMap<>();
        validation.put("access_token", "606d57393aff974fe93c6574606d5739e96f98326");
        requestMap.put("validation", validation);
        //8、 查看账户余额
        String url = "http://jydexp.nextsls.com/api/v4/shipment/account";
        String feedback = HttpRequest
                .post(url)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(JSONUtil.toJsonStr(requestMap))
                .execute().body();
        log.info("feedback: " + feedback);
        try{
            Map map = JSONUtil.toBean(feedback, Map.class);
            Integer status = MapUtil.getInt(map, "status");//状态
            String info = MapUtil.getStr(map, "info");//消息
            Long time = MapUtil.getLong(map, "time");//时间
            Map data = MapUtil.get(map, "data", Map.class);//数据
            log.info("map: " + map);
            log.info("状态status:{}, 消息info:{}, 时间time:{}", status, info, time);
            log.info("数据data:{} ", data);
        }catch (JSONException exception){
            log.info("feedback: " + feedback);
        }
    }


    @Test
    public void test2(){

        //入参键值对
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> validation = new HashMap<>();
        validation.put("access_token", "606d57393aff974fe93c6574606d5739e96f98326");
        requestMap.put("validation", validation);
        //6、 获取运单信息
        String url = "http://jydexp.nextsls.com/api/v4/shipment/info";
        String feedback = HttpRequest
                .post(url)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(JSONUtil.toJsonStr(requestMap))
                .execute().body();
        log.info("feedback: " + feedback);
        try{
            Map map = JSONUtil.toBean(feedback, Map.class);
            Integer status = MapUtil.getInt(map, "status");//状态
            String info = MapUtil.getStr(map, "info");//消息
            Long time = MapUtil.getLong(map, "time");//时间
            Map data = MapUtil.get(map, "data", Map.class);//数据
            log.info("map: " + map);
            log.info("状态status:{}, 消息info:{}, 时间time:{}", status, info, time);
            log.info("数据data:{} ", data);
        }catch (JSONException exception){
            log.info("feedback: " + feedback);
        }

    }

    @Test
    public void test3(){
        long epoch = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        log.info("epoch:{}", epoch);
        LocalDateTime ldt = Instant.ofEpochMilli(epoch).atZone(ZoneId.systemDefault()).toLocalDateTime();
        log.info("ldt:{}", ldt);


        long l = 1569292358*1000L;//秒转为毫秒
        LocalDateTime ldt2 = Instant.ofEpochMilli(l).atZone(ZoneId.systemDefault()).toLocalDateTime();
        log.info("ldt2:{}", ldt2);

    }



    public String push(){
        return "1";
    }


}
