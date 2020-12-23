import cn.hutool.core.map.MapUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayud.customs.model.po.CustomsReceivable;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class ApiControllerTest {


    /**
     * 单个推送
     */
    @Test
    public void singleDelivery(){
        String apply_no = "530120200011124347";
        //推送：18位报关单号
        receiveFinanceFeed(apply_no);
    }

    /**
     * 批量获取excel中的数据
     */
    @Test
    public void batchExcel(){
        ExcelReader reader = ExcelUtil.getReader("D:\\文档\\work-A03-金蝶\\2020-12-21，数据推送\\1-418.xlsx");
        List<List<Object>> readAll = reader.read();
        List<String> apply_no_list = new ArrayList<>();
        log.info("报关单号...");
        readAll.forEach(list -> {
            list.forEach(object -> {
                if(object.toString().length() == 18){
                    log.info("18位: " + object.toString() + "\t" + "9位: " + object.toString().substring(9));
                    apply_no_list.add(object.toString());
                }
            });
        });
        log.info(apply_no_list.toString());


        batchReceiveFinanceFeed(apply_no_list);

    }

    /**
     * 批量推送-18位报关单号
     */
    public void batchReceiveFinanceFeed(List<String> list){
        list.forEach(apply_no -> {
            receiveFinanceFeed(apply_no);
        });

    }


    /**
     * 调用推送链接
     */
    public void receiveFinanceFeed(String apply_no) {

        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 创建Post请求
        HttpPost httpPost = new HttpPost("http://localhost:8091/customs/feedback/finance/approved");

        // 设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        // 设置json格式的参数
        JSONObject params = new JSONObject();
//        params.put("apply_no", "531620201160108051");
        params.put("apply_no", apply_no);
        StringEntity s = null;
        try {
            s = new StringEntity(params.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPost.setEntity(s);

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            System.err.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.err.println("响应内容长度为:" + responseEntity.getContentLength());
                System.err.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /****************************************************/

    /**
     * 执行线程
     * @param list
     * @param dealSize
     * @throws Exception
     */
    public void exec(List<String> list, int dealSize) throws Exception {
        if (!CollectionUtils.isEmpty(list)) {
            //数据总的大小
            int count = list.size();
            //每个线程数据集
            List<String>  threadList = null;
            //线程池
            int runSize = (count / dealSize) + 1;
            ThreadPoolExecutor executor = new ThreadPoolExecutor(
                    runSize,
                    350,
                    30L,
                    TimeUnit.SECONDS,
                    new SynchronousQueue<>());
            //计数器
            CountDownLatch countDownLatch = new CountDownLatch(runSize);
            for (int i = 0; i < runSize; i++) {
                //计算每个线程执行的数据
                int startIndex = (i * dealSize);
                if ((i + 1) == runSize) {
                    int endIndex = count;
                    threadList = list.subList(startIndex, endIndex);
                } else {
                    int endIndex = (i + 1) * dealSize;
                    threadList = list.subList(startIndex, endIndex);
                }
                //线程任务
                MyThread myThread = new MyThread(threadList, countDownLatch);
                executor.execute(myThread);
            }
            //计数
            countDownLatch.await();
            //关闭线程池
            executor.shutdown();
        }

    }

    /**
     * 测试我的线程方法
     */
    @Test
    public void myThread(){

        ExcelReader reader = ExcelUtil.getReader("D:\\文档\\work-A03-金蝶\\2020-12-21，数据推送\\1-418.xlsx");
        List<List<Object>> readAll = reader.read();
        List<String> apply_no_list = new ArrayList<>();
        log.info("报关单号...");
        readAll.forEach(list -> {
            list.forEach(object -> {
                if(object.toString().length() == 18){
                    log.info("18位: " + object.toString() + "\t" + "9位: " + object.toString().substring(9));
                    apply_no_list.add(object.toString());
                }
            });
        });
        log.info(apply_no_list.toString());

        try {
            //执行线程
            this.exec(apply_no_list, 5);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 测试json
     * <p>测试String(json)->Map</p>
     * <p>测试String(json)->List</p>
     * <p>测试Object->String(json)</p>
     */
    @Test
    public void testJson(){
        String str = "[{\"fee_cd\":\"BGF\",\"fee_name\":\"报关费\",\"target_name\":\"佳裕达国际\",\"cost\":50,\"fee_item\":null,\"container_type_no\":null,\"custom_apply_no\":\"530120201010577267\",\"goods_name\":\"集成电路\",\"shipper_name\":\"西安诺瓦星云科技股份有限公司\",\"cabin_no\":\"JYD65\",\"apply_dt\":\"2020-12-10 00:00:00\",\"container_no\":\"\"},{\"fee_cd\":\"BGF\",\"fee_name\":\"报关费\",\"target_name\":\"佳裕达国际\",\"cost\":50,\"fee_item\":null,\"container_type_no\":null,\"custom_apply_no\":\"530120201010577277\",\"goods_name\":\"集成电路\",\"shipper_name\":\"西安诺瓦星云科技股份有限公司\",\"cabin_no\":\"JYD65\",\"apply_dt\":\"2020-12-10 00:00:00\",\"container_no\":\"\"}]";
        System.out.println(str);
        Map<String, String> msg = new HashMap<>();
        msg.put("msg", str);
        String json = JSONObject.toJSONString(msg);//<p>测试Object->String(json)</p>
        Map param = JSONObject.parseObject(json, Map.class);//<p>测试String(json)->Map</p>
        String reqMsg = MapUtil.getStr(param, "msg");
        log.debug("金蝶接收到报关应收数据：{}", reqMsg);
        JSONArray receivableArray = JSONObject.parseArray(reqMsg);//<p>测试String(json)->List</p>
        for (Object o : receivableArray) {
            JSONObject jsonObject = (JSONObject) o;
            CustomsReceivable data = jsonObject.toJavaObject(CustomsReceivable.class);
            log.debug("data={}", data);
        }


        AtomicReference<String> applyNo = new AtomicReference<>("");
        JSONArray jsonArray = JSONObject.parseArray(str);
        jsonArray.forEach(o -> {
            JSONObject jsonObject = (JSONObject) o;
            String custom_apply_no = jsonObject.get("custom_apply_no").toString();
            applyNo.set(custom_apply_no);
        });
        System.out.println(applyNo);


    }

}

/**
 * <p>定义我的线程类</p>
 * <p>重写该类的run()方法</p>
 */
class MyThread implements Runnable{
    private List<String> list;
    private CountDownLatch countDownLatch;

    public MyThread(List<String> list, CountDownLatch countDownLatch){
        this.list = list;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run(){
        if (!CollectionUtils.isEmpty(list)) {
            ApiControllerTest test = new ApiControllerTest();
            test.batchReceiveFinanceFeed(list);
        }
        countDownLatch.countDown();
    }
}