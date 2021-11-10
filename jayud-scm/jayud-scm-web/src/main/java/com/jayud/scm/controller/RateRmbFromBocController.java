package com.jayud.scm.controller;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.ApiResult;
import com.jayud.scm.model.po.RateRmbFromBoc;
import com.jayud.scm.model.vo.BDataDicEntryVO;
import com.jayud.scm.service.IBDataDicEntryService;
import com.jayud.scm.service.IRateRmbFromBocService;
import com.jayud.scm.utils.DateUtil;
import com.jayud.scm.utils.StringUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 中行汇率表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-09-09
 */
@RestController
@RequestMapping("/rateRmbFromBoc")
@Slf4j
public class RateRmbFromBocController {

    @Autowired
    private IBDataDicEntryService ibDataDicEntryService;

    @Autowired
    private IRateRmbFromBocService rateRmbFromBocService;

    private final String RATE_ADDRESS = "http://www.boc.cn/sourcedb/whpj/index.html;" +
            "http://www.boc.cn/sourcedb/whpj/index_1.html;" +
            "http://www.boc.cn/sourcedb/whpj/index_2.html;" +
            "http://www.boc.cn/sourcedb/whpj/index_3.html;" +
            "http://www.boc.cn/sourcedb/whpj/index_4.html;" +
            "http://www.boc.cn/sourcedb/whpj/index_5.html;" +
            "http://www.boc.cn/sourcedb/whpj/index_6.html;" +
            "http://www.boc.cn/sourcedb/whpj/index_7.html;" +
            "http://www.boc.cn/sourcedb/whpj/index_8.html;" +
            "http://www.boc.cn/sourcedb/whpj/index_9.html";

    @ApiOperation(value = "定时任务抓取中行汇率")
    @PostMapping(value = "/grabExchangeRate")
//    @Scheduled(cron = "0 0/5 * * * ?")
    public ApiResult grabExchangeRate() {
        String[] split = RATE_ADDRESS.split(";");
        List<RateRmbFromBoc> rateRmbFromBocs = new ArrayList<>();
        for (String s : split) {
            try {
                List<RateRmbFromBoc> rateRmbFromBocs1 = this.listChinaBnakRate(s);
//                System.out.println("rateRmbFromBocs1"+ rateRmbFromBocs1);
                rateRmbFromBocs.addAll(rateRmbFromBocs1);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        log.warn("抓取数据成功");
        for (int i = 0; i < rateRmbFromBocs.size(); i++) {
            List<RateRmbFromBoc> rateRmbFromBocs1 = rateRmbFromBocService.getRateRmbFromBocByTimeAndCurrencyName(rateRmbFromBocs.get(i).getPublishTime(),rateRmbFromBocs.get(i).getCurrencyName());
            if(CollectionUtils.isNotEmpty(rateRmbFromBocs1)){
                rateRmbFromBocs.remove(i);
            }
        }

        boolean result = rateRmbFromBocService.saveBatch(rateRmbFromBocs);
        if(result){
            log.warn("添加数据成功");
            return ApiResult.ok();
        }
        return ApiResult.error(444,"数据添加失败"+rateRmbFromBocs);
    }

    public List<RateRmbFromBoc> listChinaBnakRate(String url) throws Exception{
        //Document doc = Jsoup.connect(url).get();
        Document doc=new Document("");
        try{
            doc = Jsoup.connect(url).get();
        }
        catch (Exception e) {//可以精确处理timeoutException
            try{
                doc = Jsoup.connect(url).get();
            }
            catch (Exception e2) {//可以精确处理timeoutException
                //logger.error("后台任务自动获取银行汇率job异常："+e.getMessage());
                e2.printStackTrace();
            }
        }
        Elements tds = doc.select("table[align]").select("tr").select("td"); // 带有align属性的tr元素
        List<RateRmbFromBoc> data = new ArrayList();
        int k = 1, cols = 8;
        RateRmbFromBoc entity = new RateRmbFromBoc();

        Map<String, String> currMap = new HashMap<String, String>();
        List<BDataDicEntryVO> currList = ibDataDicEntryService.getDropDownList("1003");
        for(BDataDicEntryVO currency : currList) {
            currMap.put(currency.getDataText(), currency.getDataText());
        }
		/*currMap.put("港币", "港币");
		currMap.put("美元", "美元");
		currMap.put("欧元", "欧元");
		currMap.put("英镑", "英镑");
		currMap.put("日元", "日元");
		currMap.put("人民币", "人民币");*/
        for ( int i=0;i<tds.size();i++) {
            Element td=tds.get(i);
            String text = td.html();
            if (k % cols == 1) {
                entity.setCurrencyName(text);// 货币名称
                if (!StringUtils.isEmptyNotTrim(text)
                        && !currMap.containsKey(text.trim())) {
                    i=i+cols-1;
                    continue;
                }
            }
            if (k % cols == 2 && !StringUtils.isEmptyNotTrim(text)) {
                entity.setBuyingRate(new BigDecimal(text.trim()));// 现汇买入价
            }
            if (k % cols == 3 && !StringUtils.isEmptyNotTrim(text)) {
                entity.setCashBuyingRate(new BigDecimal(text.trim()));// 现钞买入价
            }
            if (k % cols == 4 && !StringUtils.isEmptyNotTrim(text)) {
                entity.setSellingRate(new BigDecimal(text.trim()));// 现汇卖出价
            }
            if (k % cols == 5 && !StringUtils.isEmptyNotTrim(text)) {
                entity.setCashSellingRate(new BigDecimal(text.trim()));// 现钞卖出价
            }
            if (k % cols == 6 && !StringUtils.isEmptyNotTrim(text)) {
                entity.setBocRate(new BigDecimal(text.trim()));// 中行折算价
                entity.setMiddleRate(new BigDecimal(text.trim()));  //基准价
            }
            if (k % cols == 7 && !StringUtils.isEmptyNotTrim(text)) {
                entity.setPublishTime(DateUtil.stringToLocalDateTime(text.trim().replace('.','-'),"yyyy-MM-dd HH:mm:ss"));// 发布日期
            }
            if (k % cols == 0) {
                //给日期加时分秒
//                entity.setPublishTime(DateUtil.stringToLocalDateTime(entity.getPublishTime()+" "+text.trim(),"yyyy-MM-dd HH:mm:ss"));
                data.add(entity);
                entity = new RateRmbFromBoc();
            }
            k++;
        }
        return data;
    }

}

