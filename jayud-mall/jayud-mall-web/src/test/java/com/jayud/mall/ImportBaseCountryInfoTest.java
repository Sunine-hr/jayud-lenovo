package com.jayud.mall;

import cn.hutool.core.io.resource.ClassPathResource;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;

/**
 * 临时功能：将QQ本地数据库的国家-省-市名称对应表的xml文件导入数据库
 * D:\app\QQ\I18N\2052\LocList.xml
 *
 * @author william
 * @description
 * @Date: 2020-05-15 11:13
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ImportBaseCountryInfoTest {
//    @Autowired
//    CountryRepository countryRepository;
//    @Autowired
//    ProvinceRepository provinceRepository;
//    @Autowired
//    CityRepository cityRepository;

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void inport() {
        //清空数据表
//        countryRepository.deleteAll();
//        provinceRepository.deleteAll();
//        cityRepository.deleteAll();
        log.info("数据库清理完毕");

        InputStream inputStream = this.getClass().getResourceAsStream("/xml/LocList.xml");

        //使用Dom4j获取xml文件 src\main\resources\xml\LocList.xml
        ClassPathResource classPathResource = new ClassPathResource("/xml/LocList.xml");
        Document xml;
        SAXReader reader = new SAXReader();
        try {
            //开始读取
            xml = reader.read(classPathResource.getStream());
            Element root = xml.getRootElement();

            //获取所有的国家级节点
            List<Element> countryNodes = root.elements("CountryRegion");
            //存放国家级数据实体类的列表
//            List<BaseCountry> baseCountryList = new ArrayList<>();
            for (Element countryNode : countryNodes) {
                log.info("正在处理{}",
                        countryNode.attributeValue("Name"));
//                BaseCountry baseCountry = new BaseCountry();
//                baseCountry.setName(countryNode.attributeValue("Name"));
//                baseCountry.setCode(countryNode.attributeValue("Code"));
//                Integer countryId = countryRepository.save(baseCountry).getId();
                //遍历此节点下的省
                List<Element> provinceNodes = countryNode.elements("State");
                for (Element provinceNode : provinceNodes) {
                    log.info("正在处理{}-{}",
                            countryNode.attributeValue("Name"),
                            provinceNode.attributeValue("Name"));
//                    BaseProvince baseProvince = new BaseProvince();
//                    baseProvince.setName(provinceNode.attributeValue("Name"));
//                    baseProvince.setCode(provinceNode.attributeValue("Code"));
//                    baseProvince.setCountryId(countryId);
//                    Integer provinceId = provinceRepository.save(baseProvince).getId();
                    //遍历市级节点
                    List<Element> cityNodes = provinceNode.elements("City");
//                    List<BaseCity> baseCityList = new ArrayList<>();
                    for (Element cityNode : cityNodes) {
                        log.info("正在处理{}-{}-{}",
                                countryNode.attributeValue("Name"),
                                provinceNode.attributeValue("Name"),
                                cityNode.attributeValue("Name"));
//                        BaseCity baseCity = new BaseCity();
//                        baseCity.setName(cityNode.attributeValue("Name"));
//                        baseCity.setCode(cityNode.attributeValue("Code"));
//                        baseCity.setCountryId(countryId);
//                        baseCity.setProvinceId(provinceId);
//                        baseCityList.add(baseCity);

                        //遍历区县(地区)节点
                        List<Element> regionNodes = cityNode.elements("Region");
                        for (int r=0; r<regionNodes.size(); r++){
                            Element regionNode = regionNodes.get(r);
                            log.info("正在处理{}-{}-{}-{}",
                                    countryNode.attributeValue("Name"),//国家
                                    provinceNode.attributeValue("Name"),//省州
                                    cityNode.attributeValue("Name"),//城市
                                    regionNode.attributeValue("Name")//区县(地区)
                            );
                        }

                    }
//                    cityRepository.saveAll(baseCityList);
                }
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }


    }
}
