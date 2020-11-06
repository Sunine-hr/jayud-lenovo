package com.jayud.tools.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
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
