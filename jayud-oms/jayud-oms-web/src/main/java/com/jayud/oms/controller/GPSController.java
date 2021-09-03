package com.jayud.oms.controller;

import cn.hutool.core.map.MapUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.google.gson.JsonObject;
import com.jayud.common.CommonResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.GPSUtil;
import com.jayud.oms.model.bo.GetOrderDetailForm;
import com.jayud.oms.model.po.GpsPositioning;
import com.jayud.oms.model.po.LogisticsTrack;
import com.jayud.oms.model.vo.*;
import com.jayud.oms.service.IGpsPositioningService;
import com.jayud.oms.service.ILogisticsTrackService;
import com.jayud.oms.service.IOrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.lettuce.DefaultLettucePool;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * GPS管理 前端控制器
 */
@RestController
@RequestMapping("/GPS")
@Api(tags = "GPS管理")
@Slf4j
public class GPSController {

    @Value("${address.positionAddress}")
    private String positionAddress;

    @Value("${address.positionsAddress}")
    private String positionsAddress;

    @Value("${address.historyAddress}")
    private String historyAddress;
    @Autowired
    private IOrderInfoService orderInfoService;
    @Autowired
    private ILogisticsTrackService logisticsTrackService;
    @Autowired
    private IGpsPositioningService gpsPositioningService;
    @Autowired
    private RedisUtils redisUtils;

//    @ApiOperation(value = "获取车辆实时定位")
//    @PostMapping("/getPosition")
//    public CommonResult getPosition(@RequestBody Map<String,Object> map) {
//        Long mainOrderId = MapUtil.getLong(map, "mainOrderId");
//        String classCode = MapUtil.getStr(map, "classCode");
//        GetOrderDetailForm getOrderDetailForm = new GetOrderDetailForm();
//        getOrderDetailForm.setMainOrderId(mainOrderId);
//        getOrderDetailForm.setClassCode(classCode);
//        InputOrderVO orderDetail = orderInfoService.getOrderDetail(getOrderDetailForm);
//        InputOrderTransportVO orderTransportForm = orderDetail.getOrderTransportForm();
//
//        String url = orderTransportForm.getGpsAddress();
//        String urlParam = "";
//        String[] split = positionAddress.split(",");
//        for (String s : split) {
//            String[] s1 = s.split("_");
//            if(orderTransportForm.getDefaultSupplierCode().equals(s1[0])){
//                url = url + s1[1];
//                urlParam = s;
//                break;
//            }
//        }
//        JSONObject params = new JSONObject();
//        try {
//            params = this.getJsonObjectParam(urlParam,orderDetail);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        log.warn("订单信息："+orderDetail);
//        log.warn("请求地址："+url);
//        log.warn("请求数据："+params);
//
//        String post = Post(url, params);
//        JSONObject jsonObject = JSON.parseObject(post);
//
////        PositionVO positionVO = ConvertUtil.convert(orderTransportForm, PositionVO.class);
////        positionVO.setAccState(jsonObject.getInteger("AccState"));
////        positionVO.setDirection(jsonObject.getInteger("Direction") == null ? 0 : jsonObject.getInteger("Direction"));
////        positionVO.setLatitude(jsonObject.getDouble("Latitude"));
////        positionVO.setLongitude(jsonObject.getDouble("Longitude"));
////        positionVO.setReportTime(jsonObject.getString("ReportTime"));
////        positionVO.setSpeed(jsonObject.getDouble("Speed") == null ? 0 : jsonObject.getDouble("Speed"));
////        positionVO.setStarkMileage(jsonObject.getDouble("StarkMileage"));
////
////        positionVO.setMainOrderNo(orderDetail.getOrderForm().getOrderNo());
////        positionVO.setCustomerName(orderDetail.getOrderForm().getCustomerName());
////
////        //获取提货地址
////        List<InputOrderTakeAdrVO> orderTakeAdrForms1 = orderTransportForm.getOrderTakeAdrForms1();
////        StringBuffer stringBuffer = new StringBuffer();
////        for (InputOrderTakeAdrVO inputOrderTakeAdrVO : orderTakeAdrForms1) {
////            stringBuffer.append(inputOrderTakeAdrVO.getGoodsDesc()).append(" ")
////                    .append(inputOrderTakeAdrVO.getPlateAmount() == null ? 0+"板" : inputOrderTakeAdrVO.getPlateAmount()+"板").append("/")
////                    .append(inputOrderTakeAdrVO.getPieceAmount()+"件").append("/")
////                    .append(inputOrderTakeAdrVO.getWeight()+"KG").append(";");
////        }
////        positionVO.setGoodInfo(stringBuffer.toString().substring(0,stringBuffer.length()-1));
//
//        PositionVO positionVO = this.getPositionResult(urlParam, orderDetail, jsonObject);
//
//        return CommonResult.success(positionVO);
//    }

    /**
     * 根据供应商获取实时位置返回对象
     *
     * @param urlParam
     * @param orderDetail
     * @return
     * @throws UnsupportedEncodingException
     */
    public static PositionVO getPositionResult(String urlParam, InputOrderVO orderDetail, JSONObject jsonObject) {
        PositionVO positionVO = new PositionVO();
        InputOrderTransportVO orderTransportForm = orderDetail.getOrderTransportForm();
        String[] s = urlParam.split("_");
        if (s[0].equals(orderTransportForm.getDefaultSupplierCode()) && s[1].equals("GetPosition")) {
            positionVO = ConvertUtil.convert(orderTransportForm, PositionVO.class);
            positionVO.setAccState(jsonObject.getInteger("AccState"));
            positionVO.setDirection(jsonObject.getInteger("Direction") == null ? 0 : jsonObject.getInteger("Direction"));
            positionVO.setLatitude(jsonObject.getDouble("Latitude"));
            positionVO.setLongitude(jsonObject.getDouble("Longitude"));
            positionVO.setReportTime(jsonObject.getString("ReportTime"));
            positionVO.setSpeed(jsonObject.getDouble("Speed") == null ? 0 : jsonObject.getDouble("Speed"));
            positionVO.setStarkMileage(jsonObject.getDouble("StarkMileage"));

            positionVO.setMainOrderNo(orderDetail.getOrderForm().getOrderNo());
            positionVO.setCustomerName(orderDetail.getOrderForm().getCustomerName());

            //获取提货地址
            List<InputOrderTakeAdrVO> orderTakeAdrForms1 = orderTransportForm.getOrderTakeAdrForms1();
            StringBuffer stringBuffer = new StringBuffer();
            for (InputOrderTakeAdrVO inputOrderTakeAdrVO : orderTakeAdrForms1) {
                stringBuffer.append(inputOrderTakeAdrVO.getGoodsDesc()).append(" ")
                        .append(inputOrderTakeAdrVO.getPlateAmount() == null ? 0 + "板" : inputOrderTakeAdrVO.getPlateAmount() + "板").append("/")
                        .append(inputOrderTakeAdrVO.getPieceAmount() + "件").append("/")
                        .append(inputOrderTakeAdrVO.getWeight() + "KG").append(";");
            }
            positionVO.setGoodInfo(stringBuffer.toString().substring(0, stringBuffer.length() - 1));
        }

        return positionVO;
    }

    /**
     * 根据供应商获取历史轨迹返回对象
     *
     * @param urlParam
     * @param orderDetail
     * @return
     * @throws UnsupportedEncodingException
     */
    public static HistoryPositionVO getHistoryResult(String urlParam, InputOrderVO orderDetail, JSONObject jsonObject) {
        HistoryPositionVO historyPositionVO = new HistoryPositionVO();
        InputOrderTransportVO orderTransportForm = orderDetail.getOrderTransportForm();
        String[] s1 = urlParam.split("_");
        if (s1[0].equals(orderTransportForm.getDefaultSupplierCode()) && s1[1].equals("GetHistory")) {
            JSONArray data = jsonObject.getJSONArray("Data");

            List<HistoryVO> historyVOS = new ArrayList<>();
            List<List<Double>> lists = new ArrayList<>();
            if (data != null) {
                for (int i = 0; i < data.size(); i++) {
                    JSONObject json = data.getJSONObject(i);
//                HistoryVO historyVO = new HistoryVO();
//                historyVO.setAccState(json.getInteger("AccState"));
//                historyVO.setDirection(json.getInteger("Direction"));
//                String s = httpURLConvertGET(json.getDouble("Longitude").toString() + "," + json.getDouble("Latitude").toString());
//                String[] split1 = s.split(",");
                    List<Double> list = new ArrayList<>();
//                list.add(split1[0]);
//                list.add(split1[1]);
                    double[] doubles = GPSUtil.gps84_To_Gcj02(json.getDouble("Latitude"), json.getDouble("Longitude"));
                    list.add(Double.valueOf(doubles[1]));
                    list.add(Double.valueOf(doubles[0]));
//                historyVO.setReportTime(json.getString("ReportTime"));
//                historyVO.setSpeed(json.getDouble("Speed"));
//                historyVO.setStarkMileage(json.getDouble("StarkMileage"));
                    lists.add(list);
                }
            }

            List<OrderTakeAdrVO> orderTakeAdrVOS = ConvertUtil.convertList(orderTransportForm.getOrderTakeAdrForms1(), OrderTakeAdrVO.class);
            List<OrderTakeAdrVO> orderTakeAdrVOS1 = ConvertUtil.convertList(orderTransportForm.getOrderTakeAdrForms2(), OrderTakeAdrVO.class);
            for (OrderTakeAdrVO orderTakeAdrVO : orderTakeAdrVOS) {
                String s = httpURLConectionGET(orderTakeAdrVO.getAddress());
                String[] split1 = s.split(",");
                orderTakeAdrVO.setLatitude(Double.parseDouble(split1[1]));
                orderTakeAdrVO.setLongitude(Double.parseDouble(split1[0]));
            }
            for (OrderTakeAdrVO orderTakeAdrVO : orderTakeAdrVOS1) {
                String s = httpURLConectionGET(orderTakeAdrVO.getAddress());
                String[] split1 = s.split(",");
                orderTakeAdrVO.setLatitude(Double.parseDouble(split1[1]));
                orderTakeAdrVO.setLongitude(Double.parseDouble(split1[0]));
            }

            historyPositionVO.setLists(lists);
//        historyPositionVO.setData(data);
            historyPositionVO.setOrderTakeAdrForms1(orderTakeAdrVOS);
            historyPositionVO.setOrderTakeAdrForms2(orderTakeAdrVOS1);
        }

        return historyPositionVO;
    }

    /**
     * 高德地图通过地址获取经纬度
     */
    public static String httpURLConvertGET(String location) {
        //"http://restapi.amap.com/v3/geocode/geo?address=上海市东方明珠&output=JSON&key=xxxxxxxxx";
//        https://restapi.amap.com/v3/assistant/coordinate/convert?locations=116.481499,39.990475&coordsys=gps&output=xml&key=<用户的key>
        String geturl = "http://restapi.amap.com/v3/assistant/coordinate/convert?key=026b5609481d2fb3e1ef6790a69e961b&coordsys=gps&locations=" + location;
        String location1 = "";
        try {
            URL url = new URL(geturl);    // 把字符串转换为URL请求地址
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 打开连接
            connection.connect();// 连接会话
            // 获取输入流
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {// 循环读取流
                sb.append(line);
            }
            br.close();// 关闭流
            connection.disconnect();// 断开连接
//            System.out.println("Get=="+sb.toString());
            JSONObject a = JSON.parseObject(sb.toString());
//            System.out.println(a);
            location1 = a.get("locations").toString();
//            System.out.println(location);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("失败!");
        }
        return location1;
    }

//    @ApiOperation(value = "获取车辆历史轨迹")
//    @PostMapping("/getHistory")
//    public CommonResult getHistory(@RequestBody Map<String, Object> map) {
//
//        Long mainOrderId = MapUtil.getLong(map, "mainOrderId");
//        String classCode = MapUtil.getStr(map, "classCode");
//        GetOrderDetailForm getOrderDetailForm = new GetOrderDetailForm();
//        getOrderDetailForm.setMainOrderId(mainOrderId);
//        getOrderDetailForm.setClassCode(classCode);
//        InputOrderVO orderDetail = orderInfoService.getOrderDetail(getOrderDetailForm);
//        InputOrderTransportVO orderTransportForm = orderDetail.getOrderTransportForm();
//
//        LogisticsTrack logisticsTrackByOrderIdAndStatusAndType = logisticsTrackService.getLogisticsTrackByOrderIdAndStatusAndType(orderTransportForm.getId(), orderTransportForm.getStatus(), 2);
//        if (null == logisticsTrackByOrderIdAndStatusAndType) {
//            return CommonResult.error(444, "该订单还没有确认派车");
//        }
//
//
//        String url = orderTransportForm.getGpsAddress();
//        String urlParam = "";
//        String[] split = historyAddress.split(",");
//        for (String s : split) {
//            String[] s1 = s.split("_");
//            if (orderTransportForm.getDefaultSupplierCode().equals(s1[0])) {
//                url = url + s1[1];
//                urlParam = s;
//                break;
//            }
//        }
//        JSONObject params = new JSONObject();
//        try {
////            params.put("AccessToken", orderTransportForm.getAppKey());
////            params.put("LicenceNumber",new String (orderTransportForm.getLicensePlate().getBytes(),"ISO-8859-1"));
//////            params.put("Begin",logisticsTrackByOrderIdAndStatusAndType.getOperatorTime().toString().replace("T"," "));
//////            params.put("End",endTime);
////            params.put("Begin","2021-07-23 00:00:00");
////            params.put("End","2021-07-23 23:59:59");
//            params = this.getJsonObjectParam(urlParam, orderDetail);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        log.warn("订单信息：" + orderDetail);
//        log.warn("请求地址：" + url);
//        log.warn("请求数据：" + params);
//
//        String post = Post(url, params);
//        JSONObject jsonObject = JSON.parseObject(post);
//
////        System.out.println("jsonObject===="+jsonObject);
////        JSONArray data = jsonObject.getJSONArray("Data");
////
////        List<HistoryVO> historyVOS = new ArrayList<>();
////        List<List<Double>> lists = new ArrayList<>();
////        if(data != null){
////            for (int i = 0; i < data.size(); i++) {
////                JSONObject json = data.getJSONObject(i);
//////                HistoryVO historyVO = new HistoryVO();
//////                historyVO.setAccState(json.getInteger("AccState"));
//////                historyVO.setDirection(json.getInteger("Direction"));
//////                String s = httpURLConvertGET(json.getDouble("Longitude").toString() + "," + json.getDouble("Latitude").toString());
//////                String[] split1 = s.split(",");
////                List<Double> list = new ArrayList<>();
//////                list.add(split1[0]);
//////                list.add(split1[1]);
////                double[] doubles = GPSUtil.gps84_To_Gcj02( json.getDouble("Latitude"),json.getDouble("Longitude"));
////                list.add(Double.valueOf(doubles[1]));
////                list.add(Double.valueOf(doubles[0]));
//////                historyVO.setReportTime(json.getString("ReportTime"));
//////                historyVO.setSpeed(json.getDouble("Speed"));
//////                historyVO.setStarkMileage(json.getDouble("StarkMileage"));
////                lists.add(list);
////            }
////        }
////
////        List<OrderTakeAdrVO> orderTakeAdrVOS = ConvertUtil.convertList(orderTransportForm.getOrderTakeAdrForms1(), OrderTakeAdrVO.class);
////        List<OrderTakeAdrVO> orderTakeAdrVOS1 = ConvertUtil.convertList(orderTransportForm.getOrderTakeAdrForms2(), OrderTakeAdrVO.class);
////        for (OrderTakeAdrVO orderTakeAdrVO : orderTakeAdrVOS) {
////            String s = httpURLConectionGET(orderTakeAdrVO.getAddress());
////            String[] split1 = s.split(",");
////            orderTakeAdrVO.setLatitude(Double.parseDouble(split1[1]));
////            orderTakeAdrVO.setLongitude(Double.parseDouble(split1[0]));
////        }
////        for (OrderTakeAdrVO orderTakeAdrVO : orderTakeAdrVOS1) {
////            String s = httpURLConectionGET(orderTakeAdrVO.getAddress());
////            String[] split1 = s.split(",");
////            orderTakeAdrVO.setLatitude(Double.parseDouble(split1[1]));
////            orderTakeAdrVO.setLongitude(Double.parseDouble(split1[0]));
////        }
////
////        HistoryPositionVO historyPositionVO = new HistoryPositionVO();
////        historyPositionVO.setLists(lists);
//////        historyPositionVO.setData(data);
////        historyPositionVO.setOrderTakeAdrForms1(orderTakeAdrVOS);
////        historyPositionVO.setOrderTakeAdrForms2(orderTakeAdrVOS1);
//
//        HistoryPositionVO historyPositionVO = this.getHistoryResult(urlParam, orderDetail, jsonObject);
//        log.warn("响应数据：" + jsonObject);
//
//        return CommonResult.success(historyPositionVO);
//    }

    /**
     * 高德地图通过地址获取经纬度
     */
    @SneakyThrows
    public static String httpURLConectionGET(String address) {
        //"http://restapi.amap.com/v3/geocode/geo?address=上海市东方明珠&output=JSON&key=xxxxxxxxx";
//        5daef797f2a3134241fd7dee3ba06566
        String geturl = "http://restapi.amap.com/v3/geocode/geo?key=026b5609481d2fb3e1ef6790a69e961b&address=" + URLEncoder.encode(address, "utf-8");
        String location = "";
        try {
            URL url = new URL(geturl);    // 把字符串转换为URL请求地址
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 打开连接
            connection.connect();// 连接会话
            // 获取输入流
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {// 循环读取流
                sb.append(line);
            }
            br.close();// 关闭流
            connection.disconnect();// 断开连接
            System.out.println("Get==" + sb.toString());
            JSONObject a = JSON.parseObject(sb.toString());
//            System.out.println(a.get("geocodes"));
            JSONArray sddressArr = JSON.parseArray(a.get("geocodes").toString());
//            System.out.println(sddressArr.get(0));
            if (sddressArr.get(0) != null) {
                JSONObject c = JSON.parseObject(sddressArr.get(0).toString());
                location = c.get("location").toString();
            }
//            System.out.println(location);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("失败!");
        }
        return location;
    }

    public static void main(String[] args) {


//        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
//        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//
//        // 创建Post请求
//        HttpPost httpPost = new HttpPost("http://vdppservice.v-infonet.com/GetPosition");
//
//        // 设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
//        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
//        // 设置json格式的参数
//        JSONObject params = new JSONObject();
////        params.put("apply_no", "531620201160108051");
////        String st  = URLDecoder.decode("粤ZYM59港", "UTF-8");
//        StringEntity s = null;
//        try {
//            String name="粤ZYM17港";
////            name = new String (name.getBytes("ISO-8859-1"),"UTF-8");
//            String encode = URLEncoder.encode(name, "utf-8");
//            params.put("AccessToken", "2af07b07-c141-4f65-82ab-7b89c43eeb48");
//            params.put("LicenceNumber",new String (name.getBytes(),"ISO-8859-1"));
//            params.put("Begin","2021-07-01 00:00:00");
//            params.put("End","2021-07-05 23:59:59");
//            s = new StringEntity(params.toString());
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        httpPost.setEntity(s);
//
//        // 响应模型
//        CloseableHttpResponse response = null;
//        try {
//            // 由客户端执行(发送)Post请求
//            response = httpClient.execute(httpPost);
//            // 从响应模型中获取响应实体
//            HttpEntity responseEntity = response.getEntity();
//
//            System.err.println("响应状态为:" + response.getStatusLine());
//            if (responseEntity != null) {
//                System.err.println("响应内容长度为:" + responseEntity.getContentLength());
//                System.err.println("响应内容为:" + EntityUtils.toString(responseEntity));
//            }
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                // 释放资源
//                if (httpClient != null) {
//                    httpClient.close();
//                }
//                if (response != null) {
//                    response.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        String location = httpURLConectionGET("新界粉岭安居街18号安兴工贸中心307室,陈生 : 852-54226755");
        System.out.println(location);
//        String location1 = httpURLConvertGET("114.22229,22.33366");
//        System.out.println(location1);
//        double[] doubles = GPSUtil.gps84_To_Gcj02(22.33366,114.22229);
//        System.out.println(doubles[0]+","+doubles[1]);
    }

    @ApiOperation(value = "获取车辆实时定位")
    @PostMapping("/getPosition")
    public CommonResult getPosition(@RequestBody Map<String, Object> map) {
        Long mainOrderId = MapUtil.getLong(map, "mainOrderId");
        String classCode = MapUtil.getStr(map, "classCode");
        GetOrderDetailForm getOrderDetailForm = new GetOrderDetailForm();
        getOrderDetailForm.setMainOrderId(mainOrderId);
        getOrderDetailForm.setClassCode(classCode);
        InputOrderVO orderDetail = orderInfoService.getOrderDetail(getOrderDetailForm);
        InputOrderTransportVO orderTransportForm = orderDetail.getOrderTransportForm();

        List<GpsPositioning> gpsPositionings = gpsPositioningService.getByOrderNo(Arrays.asList(orderTransportForm.getOrderNo()), 1);
        PositionVO convert = new PositionVO();
        if (!CollectionUtils.isEmpty(gpsPositionings)) {
            convert = ConvertUtil.convert(orderTransportForm, PositionVO.class);
            GpsPositioning positioning = gpsPositionings.get(0);
            double[] doubles = GPSUtil.gps84_To_Gcj02(Double.parseDouble(positioning.getLatitude()), Double.parseDouble(positioning.getLongitude()));
            convert.setLatitude(doubles[0]);
            convert.setLongitude(doubles[1]);
            convert.setReportTime(DateUtils.LocalDateTime2Str(positioning.getGpsTime(), DateUtils.DATE_TIME_PATTERN));
            convert.setSpeed(Double.valueOf(positioning.getSpeed()));
        }
        convert.setMainOrderNo(orderDetail.getOrderForm().getOrderNo());
        //获取提货地址
        List<InputOrderTakeAdrVO> orderTakeAdrForms1 = orderTransportForm.getOrderTakeAdrForms1();
        StringBuffer stringBuffer = new StringBuffer();
        for (InputOrderTakeAdrVO inputOrderTakeAdrVO : orderTakeAdrForms1) {
            stringBuffer.append(inputOrderTakeAdrVO.getGoodsDesc()).append(" ")
                    .append(inputOrderTakeAdrVO.getPlateAmount() == null ? 0 + "板" : inputOrderTakeAdrVO.getPlateAmount() + "板").append("/")
                    .append(inputOrderTakeAdrVO.getPieceAmount() + "件").append("/")
                    .append(inputOrderTakeAdrVO.getWeight() + "KG").append(";");
        }
        convert.setCustomerName(orderDetail.getOrderForm().getCustomerName());
        convert.setGoodInfo(stringBuffer.toString().substring(0, stringBuffer.length() - 1));
        return CommonResult.success(convert);
    }

    @ApiOperation(value = "获取车辆历史轨迹")
    @PostMapping("/getHistory")
    public CommonResult getHistory(@RequestBody Map<String, Object> map) {

        Long mainOrderId = MapUtil.getLong(map, "mainOrderId");
        String classCode = MapUtil.getStr(map, "classCode");
        GetOrderDetailForm getOrderDetailForm = new GetOrderDetailForm();
        getOrderDetailForm.setMainOrderId(mainOrderId);
        getOrderDetailForm.setClassCode(classCode);
        InputOrderVO orderDetail = orderInfoService.getOrderDetail(getOrderDetailForm);
        InputOrderTransportVO orderTransportForm = orderDetail.getOrderTransportForm();

        LogisticsTrack logisticsTrackByOrderIdAndStatusAndType = logisticsTrackService.getLogisticsTrackByOrderIdAndStatusAndType(orderTransportForm.getId(), orderTransportForm.getStatus(), 2);
        if (null == logisticsTrackByOrderIdAndStatusAndType) {
            return CommonResult.error(444, "该订单还没有确认派车");
        }

        if (!orderTransportForm.getStatus().equals(OrderStatusEnum.TMS_T_15.getCode())) {
            return CommonResult.success();
        }
        List<List<String>> list = this.redisUtils.get("GPS_" + orderTransportForm.getOrderNo(), List.class);
        List<List<Double>> lists = new ArrayList<>();
        HistoryPositionVO historyPositionVO = new HistoryPositionVO();
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(e -> {
                List<Double> tmp = new ArrayList<>();
                double[] doubles = GPSUtil.gps84_To_Gcj02(Double.parseDouble(e.get(0)), Double.parseDouble(e.get(1)));
                tmp.add(Double.valueOf(doubles[1]));
                tmp.add(Double.valueOf(doubles[0]));
                lists.add(tmp);
            });
        }
        List<OrderTakeAdrVO> orderTakeAdrVOS = ConvertUtil.convertList(orderTransportForm.getOrderTakeAdrForms1(), OrderTakeAdrVO.class);
        List<OrderTakeAdrVO> orderTakeAdrVOS1 = ConvertUtil.convertList(orderTransportForm.getOrderTakeAdrForms2(), OrderTakeAdrVO.class);
        for (OrderTakeAdrVO orderTakeAdrVO : orderTakeAdrVOS) {
            String s = httpURLConectionGET(orderTakeAdrVO.getAddress());
            String[] split1 = s.split(",");
            orderTakeAdrVO.setLatitude(Double.parseDouble(split1[1]));
            orderTakeAdrVO.setLongitude(Double.parseDouble(split1[0]));
        }
        for (OrderTakeAdrVO orderTakeAdrVO : orderTakeAdrVOS1) {
            String s = httpURLConectionGET(orderTakeAdrVO.getAddress());
            String[] split1 = s.split(",");
            orderTakeAdrVO.setLatitude(Double.parseDouble(split1[1]));
            orderTakeAdrVO.setLongitude(Double.parseDouble(split1[0]));
        }

        historyPositionVO.setLists(lists);
        historyPositionVO.setOrderTakeAdrForms1(orderTakeAdrVOS);
        historyPositionVO.setOrderTakeAdrForms2(orderTakeAdrVOS1);
        return CommonResult.success(historyPositionVO);
    }

    @ApiOperation(value = "获取多辆车实时定位")
    @PostMapping("/getPositions")
    public CommonResult getPositions(@RequestBody Map<String, Object> map) {

//        String url = orderTransportForm.getGpsAddress();
//        String[] split = historyAddress.split(",");
//        for (String s : split) {
//            String[] s1 = s.split("_");
//            if(orderTransportForm.getDefaultSupplierCode().equals(s1[0])){
//                url = url + s1[1];
//                break;
//            }
//        }
//        JSONObject params = new JSONObject();
//        try {
//            params.put("AccessToken", orderTransportForm.getAppKey());
//            params.put("LicenceNumber",new String (orderTransportForm.getLicensePlate().getBytes(),"ISO-8859-1"));
//            params.put("Begin",logisticsTrackByOrderIdAndStatusAndType.getOperatorTime().toString().replace("T"," "));
//            params.put("End",endTime);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        String post = Post(url, params);
//        JSONObject jsonObject = JSON.parseObject(post);
//        JSONArray data = jsonObject.getJSONArray("Data");

        return CommonResult.success();
    }

    public String Post(String url, JSONObject params) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 创建Post请求
        HttpPost httpPost = new HttpPost(url);

        // 设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");

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
//                System.err.println("响应内容长度为:" + responseEntity.getContentLength());
//                System.err.println("响应内容为:" + EntityUtils.toString(responseEntity));
                return EntityUtils.toString(responseEntity);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (Exception e) {
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
        return null;
    }

    /**
     * 根据供应商获取请求参数
     *
     * @param urlParam
     * @param orderDetail
     * @return
     * @throws UnsupportedEncodingException
     */
    public JSONObject getJsonObjectParam(String urlParam, InputOrderVO orderDetail) throws UnsupportedEncodingException {
        JSONObject params = new JSONObject();
        InputOrderTransportVO orderTransportForm = orderDetail.getOrderTransportForm();
        String[] s = urlParam.split("_");
        if (s[0].equals(orderTransportForm.getDefaultSupplierCode()) && s[1].equals("GetPosition")) {
            params.put("AccessToken", orderTransportForm.getAppKey());
            params.put("LicenceNumber", new String(orderTransportForm.getLicensePlate().getBytes(), "ISO-8859-1"));
        }
        if (s[0].equals(orderTransportForm.getDefaultSupplierCode()) && s[1].equals("GetPositions")) {

        }
        if (s[0].equals(orderTransportForm.getDefaultSupplierCode()) && s[1].equals("GetHistory")) {
            LogisticsTrack logisticsTrackByOrderIdAndStatusAndType = this.logisticsTrackService.getLogisticsTrackByOrderIdAndStatusAndType(orderTransportForm.getId(), orderTransportForm.getStatus(), 2);
            String endTime = LocalDateTime.now().toString().replace("T", " ");
            if (orderTransportForm.getStatus().equals(OrderStatusEnum.TMS_T_15)) {
                LogisticsTrack logisticsTrack = this.logisticsTrackService.getLogisticsTrackByOrderIdAndStatusAndType(orderTransportForm.getId(), orderTransportForm.getStatus(), 2);
                endTime = logisticsTrack.getOperatorTime().toString().replace("T", " ");
            }
            params.put("AccessToken", orderTransportForm.getAppKey());
            params.put("LicenceNumber", new String(orderTransportForm.getLicensePlate().getBytes(), "ISO-8859-1"));
            params.put("Begin", logisticsTrackByOrderIdAndStatusAndType.getOperatorTime().toString().replace("T", " "));
            params.put("End", endTime);
//            params.put("Begin","2021-07-08 00:00:00");
//            params.put("End","2021-07-10 23:59:59");
        }
        return params;
    }
}
