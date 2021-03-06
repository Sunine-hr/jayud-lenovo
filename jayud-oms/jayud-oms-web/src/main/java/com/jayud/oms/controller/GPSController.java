package com.jayud.oms.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayud.common.CommonResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.GPSUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.bo.GetOrderDetailForm;
import com.jayud.oms.model.bo.QueryGPSRecord;
import com.jayud.oms.model.po.GpsPositioning;
import com.jayud.oms.model.po.LogisticsTrack;
import com.jayud.oms.model.vo.*;
import com.jayud.oms.service.IGpsPositioningService;
import com.jayud.oms.service.ILogisticsTrackService;
import com.jayud.oms.service.IOrderInfoService;
import com.jayud.oms.service.IRegionCityService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GPS?????? ???????????????
 */
@RestController
@RequestMapping("/GPS")
@Api(tags = "GPS??????")
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
    @Autowired
    private IRegionCityService regionCityService;

//    @ApiOperation(value = "????????????????????????")
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
//        log.warn("???????????????"+orderDetail);
//        log.warn("???????????????"+url);
//        log.warn("???????????????"+params);
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
////        //??????????????????
////        List<InputOrderTakeAdrVO> orderTakeAdrForms1 = orderTransportForm.getOrderTakeAdrForms1();
////        StringBuffer stringBuffer = new StringBuffer();
////        for (InputOrderTakeAdrVO inputOrderTakeAdrVO : orderTakeAdrForms1) {
////            stringBuffer.append(inputOrderTakeAdrVO.getGoodsDesc()).append(" ")
////                    .append(inputOrderTakeAdrVO.getPlateAmount() == null ? 0+"???" : inputOrderTakeAdrVO.getPlateAmount()+"???").append("/")
////                    .append(inputOrderTakeAdrVO.getPieceAmount()+"???").append("/")
////                    .append(inputOrderTakeAdrVO.getWeight()+"KG").append(";");
////        }
////        positionVO.setGoodInfo(stringBuffer.toString().substring(0,stringBuffer.length()-1));
//
//        PositionVO positionVO = this.getPositionResult(urlParam, orderDetail, jsonObject);
//
//        return CommonResult.success(positionVO);
//    }

    @ApiOperation(value = "?????????????????????????????????")
    @PostMapping("/getGaudeMapLoAndLaByAddr")
    public CommonResult getGaudeMapLoAndLaByAddr(@RequestBody Map<String, Object> map) {
        String addr = MapUtil.getStr(map, "addr");
        Long province = MapUtil.getLong(map, "province");
        Long city = MapUtil.getLong(map, "city");
        Long area = MapUtil.getLong(map, "area");
        if (StringUtils.isEmpty(addr)) {
            return CommonResult.error(400, "???????????????");
        }
        if (province != null) {
            Map<Long, String> regionCityMap = regionCityService.list().stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getName()));
            addr = regionCityMap.get(province) + regionCityMap.get(city) + MapUtil.getStr(regionCityMap, area) + addr;
        }

        String response = httpURLConectionGET(addr);
        if (StringUtils.isEmpty(response)) {
            return CommonResult.error(400, "?????????????????????");
        }
        String[] split = response.split(",");
        Map<String, Object> tmp = new HashMap<>();

        tmp.put("la", split[1]);
        tmp.put("lo", split[0]);
        return CommonResult.success(tmp);
    }

    /**
     * ?????????????????????????????????????????????
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

            //??????????????????
            List<InputOrderTakeAdrVO> orderTakeAdrForms1 = orderTransportForm.getOrderTakeAdrForms1();
            StringBuffer stringBuffer = new StringBuffer();
            for (InputOrderTakeAdrVO inputOrderTakeAdrVO : orderTakeAdrForms1) {
                stringBuffer.append(inputOrderTakeAdrVO.getGoodsDesc()).append(" ")
                        .append(inputOrderTakeAdrVO.getPlateAmount() == null ? 0 + "???" : inputOrderTakeAdrVO.getPlateAmount() + "???").append("/")
                        .append(inputOrderTakeAdrVO.getPieceAmount() + "???").append("/")
                        .append(inputOrderTakeAdrVO.getWeight() + "KG").append(";");
            }
            positionVO.setGoodInfo(stringBuffer.toString().substring(0, stringBuffer.length() - 1));
        }

        return positionVO;
    }

    /**
     * ?????????????????????????????????????????????
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
     * ???????????????????????????????????????
     */
    public static String httpURLConvertGET(String location) {
        //"http://restapi.amap.com/v3/geocode/geo?address=?????????????????????&output=JSON&key=xxxxxxxxx";
//        https://restapi.amap.com/v3/assistant/coordinate/convert?locations=116.481499,39.990475&coordsys=gps&output=xml&key=<?????????key>
        String geturl = "http://restapi.amap.com/v3/assistant/coordinate/convert?key=026b5609481d2fb3e1ef6790a69e961b&coordsys=gps&locations=" + location;
        String location1 = "";
        try {
            URL url = new URL(geturl);    // ?????????????????????URL????????????
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();// ????????????
            connection.connect();// ????????????
            // ???????????????
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {// ???????????????
                sb.append(line);
            }
            br.close();// ?????????
            connection.disconnect();// ????????????
//            System.out.println("Get=="+sb.toString());
            JSONObject a = JSON.parseObject(sb.toString());
//            System.out.println(a);
            location1 = a.get("locations").toString();
//            System.out.println(location);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("??????!");
        }
        return location1;
    }


    /**
     * ???????????????????????????????????????
     */
    @SneakyThrows
    public static String httpURLConectionGET(String address) {
        //"http://restapi.amap.com/v3/geocode/geo?address=?????????????????????&output=JSON&key=xxxxxxxxx";
//        5daef797f2a3134241fd7dee3ba06566
        String geturl = "http://restapi.amap.com/v3/geocode/geo?key=026b5609481d2fb3e1ef6790a69e961b&address=" + URLEncoder.encode(address, "utf-8");
        String location = "";
        try {
            URL url = new URL(geturl);    // ?????????????????????URL????????????
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();// ????????????
            connection.connect();// ????????????
            // ???????????????
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {// ???????????????
                sb.append(line);
            }
            br.close();// ?????????
            connection.disconnect();// ????????????
            System.out.println("Get==" + sb.toString());
            JSONObject a = JSON.parseObject(sb.toString());
//            System.out.println(a.get("geocodes"));
            JSONArray sddressArr = JSON.parseArray(a.get("geocodes").toString());
//            System.out.println(sddressArr.get(0));
            if (sddressArr != null && sddressArr.get(0) != null) {
                JSONObject c = JSON.parseObject(sddressArr.get(0).toString());
                location = c.get("location").toString();
            }
//            System.out.println(location);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("??????!");
        }
        return location;
    }

    public static void main(String[] args) {


//        // ??????Http?????????(???????????????:???????????????????????????;??????:?????????HttpClient???????????????????????????)
//        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//
//        // ??????Post??????
//        HttpPost httpPost = new HttpPost("http://vdppservice.v-infonet.com/GetPosition");
//
//        // ??????ContentType(???:?????????????????????????????????,ContentType??????????????????application/json)
//        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
//        // ??????json???????????????
//        JSONObject params = new JSONObject();
////        params.put("apply_no", "531620201160108051");
////        String st  = URLDecoder.decode("???ZYM59???", "UTF-8");
//        StringEntity s = null;
//        try {
//            String name="???ZYM17???";
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
//        // ????????????
//        CloseableHttpResponse response = null;
//        try {
//            // ??????????????????(??????)Post??????
//            response = httpClient.execute(httpPost);
//            // ????????????????????????????????????
//            HttpEntity responseEntity = response.getEntity();
//
//            System.err.println("???????????????:" + response.getStatusLine());
//            if (responseEntity != null) {
//                System.err.println("?????????????????????:" + responseEntity.getContentLength());
//                System.err.println("???????????????:" + EntityUtils.toString(responseEntity));
//            }
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                // ????????????
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
        String location = httpURLConectionGET("?????????????????????18?????????????????????307???,?????? : 852-54226755");
        System.out.println(location);
//        String location1 = httpURLConvertGET("114.22229,22.33366");
//        System.out.println(location1);
//        double[] doubles = GPSUtil.gps84_To_Gcj02(22.33366,114.22229);
//        System.out.println(doubles[0]+","+doubles[1]);
    }

    @ApiOperation(value = "????????????????????????")
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
        //??????????????????
        List<InputOrderTakeAdrVO> orderTakeAdrForms1 = orderTransportForm.getOrderTakeAdrForms1();
        StringBuffer stringBuffer = new StringBuffer();
        for (InputOrderTakeAdrVO inputOrderTakeAdrVO : orderTakeAdrForms1) {
            stringBuffer.append(inputOrderTakeAdrVO.getGoodsDesc()).append(" ")
                    .append(inputOrderTakeAdrVO.getPlateAmount() == null ? 0 + "???" : inputOrderTakeAdrVO.getPlateAmount() + "???").append("/")
                    .append(inputOrderTakeAdrVO.getPieceAmount() + "???").append("/")
                    .append(inputOrderTakeAdrVO.getWeight() + "KG").append(";");
        }
        convert.setCustomerName(orderDetail.getOrderForm().getCustomerName());
        convert.setGoodInfo(stringBuffer.toString().substring(0, stringBuffer.length() - 1));
        return CommonResult.success(convert);
    }

    /**
     * ????????????????????????????????????GPS????????????
     */
    @ApiOperation(value = "????????????????????????????????????GPS????????????")
    @PostMapping("/getPositionByPlateNumber")
    public CommonResult getPositionByPlateNumber(@RequestBody Map<String, Object> map) {
        String plateNumber = MapUtil.getStr(map, "plateNumber");
        List<GpsPositioning> gpsPositionings = gpsPositioningService.getByPlateNumber(plateNumber, 1);
        PositionVO convert = new PositionVO();
        if (!CollectionUtils.isEmpty(gpsPositionings)) {
            //convert = ConvertUtil.convert(orderTransportForm, PositionVO.class);  ???????????????????????????????????????????????????
            GpsPositioning positioning = gpsPositionings.get(0);
            convert = ConvertUtil.convert(positioning, PositionVO.class);
            double[] doubles = GPSUtil.gps84_To_Gcj02(Double.parseDouble(positioning.getLatitude()), Double.parseDouble(positioning.getLongitude()));
            //?????????????????????????????????????????????????????????????????????????????????????????????????????????
            convert.setLatitude(doubles[0]);//??????
            convert.setLongitude(doubles[1]);//??????
            convert.setReportTime(DateUtils.LocalDateTime2Str(positioning.getGpsTime(), DateUtils.DATE_TIME_PATTERN));//???????????????????????????
            convert.setSpeed(Double.valueOf(positioning.getSpeed()));//??????

            convert.setLicensePlate(positioning.getPlateNumber());//????????? -> ????????????
            convert.setAddr(positioning.getAddr());//????????????
            convert.setMile(positioning.getMile());//????????????
            convert.setVehicleStatus(positioning.getVehicleStatus());//????????????
        } else {
            return CommonResult.error(-1, "???" + plateNumber + "????????????GPS????????????");
        }
        return CommonResult.success(convert);
    }

    /**
     * ???????????????id????????????????????????????????????GPS????????????
     */
    @ApiOperation(value = "???????????????id????????????????????????????????????GPS????????????")
    @PostMapping("/getPositionBySupplierId")
    public CommonResult getPositionBySupplierId(@RequestBody Map<String, Object> map) {
        Integer supplierId = MapUtil.getInt(map, "supplierId");
        List<GpsPositioning> gpsPositionings = gpsPositioningService.getPositionBySupplierId(supplierId);
        List<PositionVO> positionVOS = new ArrayList<>();
        gpsPositionings.forEach(positioning -> {
            PositionVO convert = ConvertUtil.convert(positioning, PositionVO.class);
            double[] doubles = GPSUtil.gps84_To_Gcj02(Double.parseDouble(positioning.getLatitude()), Double.parseDouble(positioning.getLongitude()));
            //?????????????????????????????????????????????????????????????????????????????????????????????????????????
            convert.setLatitude(doubles[0]);//??????
            convert.setLongitude(doubles[1]);//??????
            convert.setReportTime(DateUtils.LocalDateTime2Str(positioning.getGpsTime(), DateUtils.DATE_TIME_PATTERN));//???????????????????????????
            convert.setSpeed(Double.valueOf(positioning.getSpeed()));//??????
            convert.setLicensePlate(positioning.getPlateNumber());//????????? -> ????????????
            convert.setAddr(positioning.getAddr());//????????????
            convert.setMile(positioning.getMile());//????????????
            convert.setVehicleStatus(positioning.getVehicleStatus());//????????????
            positionVOS.add(convert);
        });

        if(CollUtil.isEmpty(positionVOS)){
            return CommonResult.error(-1, "?????????????????????????????????GPS????????????");
        }
        return CommonResult.success(positionVOS);
    }

    @ApiOperation(value = "????????????????????????")
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
            return CommonResult.error(444, "??????????????????????????????");
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
            if (StringUtils.isEmpty(s)) {
                continue;
            }
            String[] split1 = s.split(",");
            orderTakeAdrVO.setLatitude(Double.parseDouble(split1[1]));
            orderTakeAdrVO.setLongitude(Double.parseDouble(split1[0]));
        }
        for (OrderTakeAdrVO orderTakeAdrVO : orderTakeAdrVOS1) {
            String s = httpURLConectionGET(orderTakeAdrVO.getAddress());
            if (!StringUtils.isEmpty(s)) {
                String[] split1 = s.split(",");
                orderTakeAdrVO.setLatitude(Double.parseDouble(split1[1]));
                orderTakeAdrVO.setLongitude(Double.parseDouble(split1[0]));
            }
        }

        historyPositionVO.setLists(lists);
        historyPositionVO.setOrderTakeAdrForms1(orderTakeAdrVOS);
        historyPositionVO.setOrderTakeAdrForms2(orderTakeAdrVOS1);
        return CommonResult.success(historyPositionVO);
    }

    @ApiOperation(value = "???????????????????????????")
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


    @ApiOperation(value = "??????????????????????????????")
    @PostMapping(value = "getVehicleHistoryTrackInfo")
    public CommonResult<TrackPlaybackVO> getVehicleHistoryTrackInfo(@RequestBody QueryGPSRecord form) {
        form.checkParam();
        return CommonResult.success(this.gpsPositioningService.getVehicleHistoryTrackInfo(form));
    }

    public String Post(String url, JSONObject params) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // ??????Post??????
        HttpPost httpPost = new HttpPost(url);

        // ??????ContentType(???:?????????????????????????????????,ContentType??????????????????application/json)
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");

        StringEntity s = null;
        try {
            s = new StringEntity(params.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPost.setEntity(s);

        // ????????????
        CloseableHttpResponse response = null;
        try {
            // ??????????????????(??????)Post??????
            response = httpClient.execute(httpPost);
            // ????????????????????????????????????
            HttpEntity responseEntity = response.getEntity();

            System.err.println("???????????????:" + response.getStatusLine());
            if (responseEntity != null) {
//                System.err.println("?????????????????????:" + responseEntity.getContentLength());
//                System.err.println("???????????????:" + EntityUtils.toString(responseEntity));
                return EntityUtils.toString(responseEntity);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // ????????????
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
     * ?????????????????????????????????
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
