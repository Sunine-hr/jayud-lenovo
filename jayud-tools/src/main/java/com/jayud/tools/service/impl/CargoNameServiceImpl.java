package com.jayud.tools.service.impl;

import cn.hutool.poi.excel.sax.Excel03SaxReader;
import cn.hutool.poi.excel.sax.Excel07SaxReader;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.tools.mapper.CargoNameMapper;
import com.jayud.tools.model.po.CargoName;
import com.jayud.tools.model.vo.CargoNameSmallVO;
import com.jayud.tools.model.vo.CargoNameVO;
import com.jayud.tools.service.ICargoNameService;
import com.jayud.tools.thread.CargoNameThread;
import com.jayud.tools.utils.MathUtils;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 货物名称表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-27
 */
@Service
public class CargoNameServiceImpl extends ServiceImpl<CargoNameMapper, CargoName> implements ICargoNameService {
    //数值计算
    private static final BigDecimal b0 = new BigDecimal("0");
    private static final BigDecimal b1 = new BigDecimal("1");
    private static final BigDecimal b2 = new BigDecimal("2");
    private static final BigDecimal b3 = new BigDecimal("3");
    private static final BigDecimal b4 = new BigDecimal("4");
    private static final BigDecimal b5 = new BigDecimal("5");
    private static final BigDecimal b6 = new BigDecimal("6");
    private static final BigDecimal b7 = new BigDecimal("7");
    private static final BigDecimal b8 = new BigDecimal("8");
    private static final BigDecimal b9 = new BigDecimal("9");
    private static final BigDecimal b10 = new BigDecimal("10");

    @Autowired
    ICargoNameService cargoNameService;
    @Autowired
    CargoNameMapper cargoNameMapper;

    /**
     * excelList
     */
    private static List<List<Object>> excelList = Collections.synchronizedList(new ArrayList<List<Object>>());

    @Override
    public void importExcel(List<List<Object>> list, Long userId) {
        //当前登录用户Id
        userId = (userId != null) ? userId : 1;

        //导入前先删除数据,根据登录用户的用户id做删除
//        cargoNameMapper.truncateCargoName();
        cargoNameMapper.deleteCargoNameByUserId(userId);

        //构造插入的数据
        List<CargoName> cargoNameList = new ArrayList<>();
        //int i = 2，从第三行记录开是计算，跳过表头列
        for(int i=2; i<list.size(); i++){
            List<Object> o = list.get(i);
            CargoName cargoName = new CargoName();
            if(!"".equals(String.valueOf(o.get(0))) && !"null".equals(String.valueOf(o.get(0)))){
                cargoName.setXh(Long.valueOf(String.valueOf(o.get(0))));
            }
            cargoName.setDh(String.valueOf(o.get(1)));
            cargoName.setDz(MathUtils.getBigDecimal(o.get(2)));
            cargoName.setYtdh(String.valueOf(o.get(3)));
            cargoName.setTdh(String.valueOf(o.get(4)));
            cargoName.setSl(Integer.valueOf(String.valueOf(o.get(5))));
            cargoName.setZl(MathUtils.getBigDecimal(o.get(6)));
            cargoName.setRemark(String.valueOf(o.get(7)));
            cargoName.setHpmc(String.valueOf(o.get(8)));
            cargoName.setJs(Integer.valueOf(String.valueOf(o.get(9))));
            cargoName.setPce(String.valueOf(o.get(10)));
            //o.get(11) 空列
            cargoName.setJz(MathUtils.getBigDecimal(o.get(12)));
            cargoName.setXm1(String.valueOf(o.get(13)));
            //o.get(14) 空列
            cargoName.setXm2(String.valueOf(o.get(15)));
            cargoName.setAddress(String.valueOf(o.get(16)));
            cargoName.setHm1(String.valueOf(o.get(17)));
            //o.get(18) 空列
            cargoName.setXm3(String.valueOf(o.get(19)));
            cargoName.setHm2(String.valueOf(o.get(20)));
            cargoName.setBjdh(String.valueOf(o.get(21)));

            //登录用户的用户Id
            cargoName.setUserId(userId);

            //add cargoName
            cargoNameList.add(cargoName);
        }
        this.saveBatch(cargoNameList);
    }

    @Override
    public List<CargoNameVO> findCargoNameListByA() {
        return cargoNameMapper.findCargoNameListByA();
    }

    @Override
    public List<CargoNameVO> findCargoNameListByB() {
        return cargoNameMapper.findCargoNameListByB();
    }

    @Override
    public void deleteAllCargoName() {
        cargoNameMapper.deleteAllCargoName();
    }

    @Override
    public void importExcelV2(List<CargoName> list) {
        //导入前，先清空表里面的数据
        cargoNameMapper.truncateCargoName();
        //批量插入数据
        this.saveBatch(list);
    }

    @Override
    public List<CargoNameSmallVO> findCargoNameListByAV2(Long userId) {
        //根据登录用户的Id，查询导出的数据
        userId = (userId != null) ? userId : 1;
        return cargoNameMapper.findCargoNameListByAV2(userId);
    }

    @Override
    public List<CargoNameSmallVO> findCargoNameListByBV2(Long userId) {
        //根据登录用户的Id，查询导出的数据
        userId = (userId != null) ? userId : 1;
        return cargoNameMapper.findCargoNameListByBV2(userId);
    }

    @Override
    public void truncateCargoName() {
        cargoNameMapper.truncateCargoName();
    }

    /**
     * 读取大数据Excel
     * @return
     */
    private RowHandler createRowHandler() {
        return new RowHandler() {
            @Override
            public void handle(int sheetIndex, long rowIndex, List<Object> rowlist) {
                excelList.add(rowlist);
            }
        };
    }

    @Override
    public void importBigExcel(InputStream inputStream, Long userId) {
        //每次进入createRowHandler之前先清空excelList
        excelList.clear();
        Excel07SaxReader reader = new Excel07SaxReader(createRowHandler());
        Excel07SaxReader read = reader.read(inputStream, 0);

        //当前登录用户Id
        userId = (userId != null) ? userId : 1;

        //导入前先删除数据,根据登录用户的用户id做删除
        cargoNameMapper.deleteCargoNameByUserId(userId);

        //构造插入的数据
        List<CargoName> cargoNameList = new ArrayList<>();
        //int i = 2，从第三行记录开是计算，跳过表头列
        for(int i=2; i<excelList.size(); i++){
            List<Object> o = excelList.get(i);
            CargoName cargoName = new CargoName();
            if(!"".equals(String.valueOf(o.get(0))) && !"null".equals(String.valueOf(o.get(0)))){
                cargoName.setXh(Long.valueOf(String.valueOf(o.get(0))));
            }
            cargoName.setDh(String.valueOf(o.get(1)));
            cargoName.setDz(MathUtils.getBigDecimal(o.get(2)));
            cargoName.setYtdh(String.valueOf(o.get(3)));
            cargoName.setTdh(String.valueOf(o.get(4)));
            cargoName.setSl(Integer.valueOf(String.valueOf(o.get(5))));
            cargoName.setZl(MathUtils.getBigDecimal(o.get(6)));
            cargoName.setRemark(String.valueOf(o.get(7)));
            cargoName.setHpmc(String.valueOf(o.get(8)));
            cargoName.setJs(Integer.valueOf(String.valueOf(o.get(9))));
            cargoName.setPce(String.valueOf(o.get(10)));
            //o.get(11) 空列
            cargoName.setJz(MathUtils.getBigDecimal(o.get(12)));
            cargoName.setXm1(String.valueOf(o.get(13)));
            //o.get(14) 空列
            cargoName.setXm2(String.valueOf(o.get(15)));
            cargoName.setAddress(String.valueOf(o.get(16)));
            cargoName.setHm1(String.valueOf(o.get(17)));
            //o.get(18) 空列
            cargoName.setXm3(String.valueOf(o.get(19)));
            cargoName.setHm2(String.valueOf(o.get(20)));
            cargoName.setBjdh(String.valueOf(o.get(21)));

            //登录用户的用户Id
            cargoName.setUserId(userId);

            //add cargoName
            cargoNameList.add(cargoName);
        }
//        excelList.clear();

//        long start = System.currentTimeMillis();
//        this.saveBatch(cargoNameList);
//        long end = System.currentTimeMillis();
//        System.out.println("插入耗时:--------------------------" + (start - end) + "--------------------------");
//        //插入耗时:---------------------------80356--------------------------

        try {
            long start = System.currentTimeMillis();
            this.exec(cargoNameList, 1000);
            long end = System.currentTimeMillis();
            System.out.println("插入耗时:--------------------------" + (start - end) + "--------------------------");
            //插入耗时:---------------------------17776--------------------------
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void importBigExcelV3(InputStream inputStream, Long userId) {
        //每次进入createRowHandler之前先清空excelList
        excelList.clear();

//        Excel03SaxReader reader = new Excel03SaxReader(createRowHandler());
//        Excel03SaxReader read = reader.read(inputStream, 0);

        //判断excel是03版本还是07版本
//        Workbook wb = WorkbookFactory.create(inputStream);
        InputStream is = FileMagic.prepareToCheckMagic(inputStream);
        try {
            FileMagic fm = FileMagic.valueOf(is);
            switch(fm) {
                case OLE2:
                    //excel是03版本
                    Excel03SaxReader reader03 = new Excel03SaxReader(createRowHandler());
                    reader03.read(is, 0);
                    break;
                case OOXML:
                    //excel是07版本
                    Excel07SaxReader reader07 = new Excel07SaxReader(createRowHandler());
                    reader07.read(is, 0);
                    break;
                default:
                    throw new IOException("Your InputStream was neither an OLE2 stream, nor an OOXML stream");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //当前登录用户Id
        userId = (userId != null) ? userId : 1;
        //导入前先删除数据,根据登录用户的用户id做删除
        cargoNameMapper.deleteCargoNameByUserId(userId);
        //构造插入的数据
        List<CargoName> cargoNameList = new ArrayList<>();
        //int i = 1，从第2行记录开是计算，跳过表头列
        for(int i=1; i<excelList.size(); i++){
            /**
             //配置别名
             Map<String,String> aliasMap=new HashMap<>();
             aliasMap.put("运单编号", "ytdh");
             aliasMap.put("商品名称", "hpmc");
             aliasMap.put("件数", "js");
             aliasMap.put("毛重KG", "zl");
             excelReader.setHeaderAlias(aliasMap);
             */
            List<Object> o = excelList.get(i);
            if(String.valueOf(o.get(0)) != null && String.valueOf(o.get(0)) != ""){
//                CargoName cargoName = new CargoName();
//                cargoName.setYtdh(String.valueOf(o.get(0)));//运单编号
//                cargoName.setHpmc(String.valueOf(o.get(1)));//商品名称
//                cargoName.setJs(Integer.valueOf(String.valueOf(o.get(2))));//件数
//                cargoName.setZl(MathUtils.getBigDecimal(o.get(3)));//毛重KG

                String ytdh = String.valueOf(o.get(0));//运单编号
                String hpmc = String.valueOf(o.get(1));//商品名称
                Integer js = Integer.valueOf(String.valueOf(o.get(2)));//件数
                BigDecimal grossWeight = MathUtils.getBigDecimal(String.valueOf(o.get(3)));//毛重

                String[] hpmcArr = hpmc.split("/");
                //商品数量
                int count = hpmcArr.length;
                //净重，(毛重 -> 净重)
                BigDecimal suttle = calcSuttle(grossWeight);

                //如果商品数量等于1，并且商品净重大于2，需要拆单
                if(count == 1 && suttle.compareTo(b2) == 1){
                    //拆单数量，按重量拆单的（最大重量为2） 净重/2
                    BigDecimal separateBillCount = suttle.divide(b2);
                    //拆单数量(向上取整)
                    BigDecimal up = separateBillCount.setScale( 0, BigDecimal.ROUND_UP ); // 向上取整
                    //拆单数量(向下取整)
                    BigDecimal down = separateBillCount.setScale( 0, BigDecimal.ROUND_DOWN ); // 向下取整

                    int sign = up.compareTo(down);//如果sign=0，说明数量相等，净重被整除为整数，数据没有小数位，sign!=0，说明没有被整除
                    for(int j = 0; j < up.intValue(); j++){
                        CargoName cargoName = new CargoName();
                        //按照向上取整，拆单
                        if(sign != 0 && down.intValue() == j){
                            //sign!=0 最后一单的净重 = 整单净重 - (前面累计的单数 * 2)
                            BigDecimal lastSuttle = suttle.subtract(down.multiply(b2));
                            cargoName.setZl(lastSuttle);//净重，这里直接是最后一单的净重
                        }else{
                            cargoName.setZl(b2);//净重，这里直接是净重为2
                        }
                        cargoName.setYtdh(ytdh+"_"+(j+1));//运单编号,j从0开始，所有要加1
                        cargoName.setHpmc(hpmc);//商品名称
                        cargoName.setJs(js);//件数

                        //登录用户的用户Id
                        cargoName.setUserId(userId);//userid
                        //add cargoName
                        cargoNameList.add(cargoName);
                    }
                }
                //如果商品数量等于1，并且商品净重小于或等于2，不需要拆单，直接保存
                if(count == 1 && (suttle.compareTo(b2) == -1 || suttle.compareTo(b2) == 0 )){
                    CargoName cargoName = new CargoName();

                    cargoName.setYtdh(ytdh);//运单编号
                    cargoName.setHpmc(hpmc);//商品名称
                    cargoName.setJs(js);//件数
                    cargoName.setZl(suttle);//净重

                    //登录用户的用户Id
                    cargoName.setUserId(userId);//userid
                    //add cargoName
                    cargoNameList.add(cargoName);

                }
                //如果商品数量大于1
                if(count > 1){
                    BigDecimal spsl = new BigDecimal(String.valueOf(count));
                    BigDecimal jsZl = spsl.multiply(b2);
                    //净重 >= (商品数量*2)，表示可以根据净重拆单，每单商品净重最大为2KG
                    if(suttle.compareTo(jsZl) > 0){
                        //商品净重大于2,需要根据净重，商品拆单

                        //拆单数量，按重量拆单的（最大重量为2） 净重/2
                        BigDecimal separateBillCount = suttle.divide(b2);
                        //拆单数量(向上取整)
                        BigDecimal up = separateBillCount.setScale( 0, BigDecimal.ROUND_UP ); // 向上取整
                        //拆单数量(向下取整)
                        BigDecimal down = separateBillCount.setScale( 0, BigDecimal.ROUND_DOWN ); // 向下取整

                        int sign = up.compareTo(down);//如果sign=0，说明数量相等，净重被整除为整数，数据没有小数位，sign!=0，说明没有被整除

                        for(int j = 0; j < up.intValue(); j++){
                            int a = j % hpmcArr.length;
                            hpmc = hpmcArr[a];
                            CargoName cargoName = new CargoName();

                            //按照向上取整，拆单
                            if(sign != 0 && down.intValue() == j){
                                //sign!=0 最后一单的净重 = 整单净重 - (前面累计的单数 * 2)
                                BigDecimal lastSuttle = suttle.subtract(down.multiply(b2));
                                cargoName.setZl(lastSuttle);//净重，这里直接是最后一单的净重
                            }else{
                                cargoName.setZl(b2);//净重，这里直接是净重为2
                            }
                            cargoName.setYtdh(ytdh+"_"+(j+1));//运单编号,j从0开始，所有要加1
                            cargoName.setHpmc(hpmc);//商品名称
                            cargoName.setJs(js);//件数

                            //登录用户的用户Id
                            cargoName.setUserId(userId);//userid
                            //add cargoName
                            cargoNameList.add(cargoName);
                        }


                    }else{
                        //(净重)<(商品数量*2),需要根据商品平均拆单

                        //拆单数量
                        int length = hpmcArr.length;
                        BigDecimal cdsl = new BigDecimal(String.valueOf(length));
                        //平均分配净重到每一件商品
//                        BigDecimal jz = suttle.divide(cdsl);
                        BigDecimal jz = suttle.divide(cdsl,2, BigDecimal.ROUND_HALF_UP);
                        for(int j=0; j<hpmcArr.length; j++){
                            hpmc = hpmcArr[j];
                            CargoName cargoName = new CargoName();

                            //最后一单
                            if(j == hpmcArr.length -1){
                                //前面的单数
                                BigDecimal danshu = new BigDecimal(String.valueOf(hpmcArr.length - 1));
                                //最后一单的净重 = 总净重 - 之前拆单的净重
                                BigDecimal multiply = jz.multiply(danshu);
                                jz = suttle.subtract(multiply);
                                cargoName.setZl(jz);//净重
                            }else{
                                cargoName.setZl(jz);//净重
                            }
                            cargoName.setYtdh(ytdh+"_"+(j+1));//运单编号,j从0开始，所有要加1
                            cargoName.setHpmc(hpmc);//商品名称
                            cargoName.setJs(js);//件数
                            //登录用户的用户Id
                            cargoName.setUserId(userId);//userid
                            //add cargoName
                            cargoNameList.add(cargoName);
                        }
                    }
                }

            }
        }
        try {
            long start = System.currentTimeMillis();
            this.exec(cargoNameList, 1000);
            long end = System.currentTimeMillis();
            System.out.println("插入耗时:--------------------------" + (start - end) + "--------------------------");
            //插入耗时:---------------------------17776--------------------------
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 通过毛重，计算净重
     * 数据表里的重量是**毛重**，要修改为**净重{
     * 毛重不能小于等于0
     * (0 < a < 1重量不变）
     * (1 <= a < 3重量减0.15），
     * (3 <= a < 10重量减1），
     * (10 <= a < 20减2)，
     * (20 <= a < 30减3)，
     * (30 <= a < 40减4)，
     * 以此类推下去)}
     * @param weight
     * @return
     */
    private BigDecimal calcSuttle(BigDecimal weight){
        if(weight.compareTo(b0) == -1 || weight.compareTo(b0) == 0){
            throw new RuntimeException("毛重，数值不能小于或等于0");
        }
        //(0 < a < 1重量不变）
        else if(weight.compareTo(b0) == 1 && weight.compareTo(b1) == -1){
            weight = weight;
        }
        //(1 <= a < 3重量减0.15）
        else if((weight.compareTo(b1) == 1 || weight.compareTo(b1) == 0 )
                && (weight.compareTo(b3) == -1)){
            BigDecimal bigDecimal = new BigDecimal("0.15");
            weight = weight.subtract(bigDecimal);
        }
        //(3 <= a < 10重量减1）
        else if ((weight.compareTo(b3) == 1 || weight.compareTo(b3) == 0 )
                && (weight.compareTo(b10) == -1) ){
            weight = weight.subtract(b1);
        }
        //(10 <= a < 20减2)
        else if ((weight.compareTo(b10) == 1 || weight.compareTo(b10) == 0 )
                && (weight.compareTo(b10.multiply(b2)) == -1 ) ){
            weight = weight.subtract(b2);
        }
        //(20 <= a < 30减3)
        else if ((weight.compareTo(b10.multiply(b2)) == 1 || weight.compareTo(b10.multiply(b2)) == 0 )
                && (weight.compareTo(b10.multiply(b3)) == -1 ) ){
            weight = weight.subtract(b3);
        }
        //(30 <= a < 40减4)
        else if ((weight.compareTo(b10.multiply(b3)) == 1 || weight.compareTo(b10.multiply(b3)) == 0 )
                && (weight.compareTo(b10.multiply(b4)) == -1 ) ){
            weight = weight.subtract(b4);
        }
        //(40 <= a < 50减5)
        else if ((weight.compareTo(b10.multiply(b4)) == 1 || weight.compareTo(b10.multiply(b4)) == 0 )
                && (weight.compareTo(b10.multiply(b5)) == -1 ) ){
            weight = weight.subtract(b5);
        }
        //(50 <= a < 60减6)
        else if ((weight.compareTo(b10.multiply(b5)) == 1 || weight.compareTo(b10.multiply(b5)) == 0 )
                && (weight.compareTo(b10.multiply(b6)) == -1 ) ){
            weight = weight.subtract(b6);
        }
        //(60 <= a < 70减7)
        else if ((weight.compareTo(b10.multiply(b6)) == 1 || weight.compareTo(b10.multiply(b6)) == 0 )
                && (weight.compareTo(b10.multiply(b7)) == -1 ) ){
            weight = weight.subtract(b7);
        }
        //(70 <= a < 80减8)
        else if ((weight.compareTo(b10.multiply(b7)) == 1 || weight.compareTo(b10.multiply(b7)) == 0 )
                && (weight.compareTo(b10.multiply(b8)) == -1 ) ){
            weight = weight.subtract(b8);
        }
        //(80 <= a < 90减9)
        else if ((weight.compareTo(b10.multiply(b8)) == 1 || weight.compareTo(b10.multiply(b8)) == 0 )
                && (weight.compareTo(b10.multiply(b9)) == -1 ) ){
            weight = weight.subtract(b9);
        }
        //(90 <= a < 100减10)
        else if ((weight.compareTo(b10.multiply(b9)) == 1 || weight.compareTo(b10.multiply(b9)) == 0 )
                && (weight.compareTo(b10.multiply(b10)) == -1 ) ){
            weight = weight.subtract(b10);
        }
        return weight;
    }


    /**
     * 执行线程
     * @param list
     * @param dealSize
     * @throws Exception
     */
    private void exec(List<CargoName> list, int dealSize) throws Exception {
        if (!CollectionUtils.isEmpty(list)) {
            //数据总的大小
            int count = list.size();
            //每个线程数据集
            List<CargoName>  threadList = null;
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
                CargoNameThread myThread = new CargoNameThread(cargoNameService, threadList, countDownLatch);
                executor.execute(myThread);
            }
            //计数
            countDownLatch.await();
            //关闭线程池
            executor.shutdown();
        }

    }



}
