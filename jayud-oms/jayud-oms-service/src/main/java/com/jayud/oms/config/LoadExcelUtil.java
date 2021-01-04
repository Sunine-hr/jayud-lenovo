package com.jayud.oms.config;


import org.apache.poi.hssf.usermodel.*;

import java.io.OutputStream;
import java.util.ArrayList;


public class LoadExcelUtil {
	
	private final int SPLIT_COUNT = 100; //Excel每个工作簿的行数
 
//	private ArrayList<String> fieldName = null; //excel标题数据集
 
	private ArrayList<ArrayList<String>> fieldData = null; //excel数据内容	
 
	private HSSFWorkbook workBook = null;
 
	/**
	 * 构造器
	 * @param fieldData 结果集的字段名
	 */
	public LoadExcelUtil(ArrayList<ArrayList<String>> fieldData) {
		this.fieldData = fieldData;
	}
 
	/**
	 * 创建HSSFWorkbook对象
	 * @return HSSFWorkbook
	 */
	public HSSFWorkbook createWorkbook() {
 
		workBook = new HSSFWorkbook();//创建一个工作薄对象
		int rows = fieldData.size();//总的记录数
		int sheetNum = 0;           //指定sheet的页数
 
		if (rows % SPLIT_COUNT == 0) {
			sheetNum = rows / SPLIT_COUNT;
		} else {
			sheetNum = rows / SPLIT_COUNT + 1;
		}
 
		for (int i = 1; i <= sheetNum; i++) {//循环2个sheet的值
			HSSFSheet sheet = workBook.createSheet("Page " + i);//使用workbook对象创建sheet对象
			/**************对标题添加样式begin********************/
			HSSFRow headRow = sheet.createRow((short) 0); //创建行，0表示第一行（本例是excel的标题）

			headRow.createCell(0).setCellValue("*客户名称");
			headRow.createCell(1).setCellValue("客户代码");
			headRow.createCell(2).setCellValue("*客户类型");
			headRow.createCell(3).setCellValue("*联系人");
			headRow.createCell(4).setCellValue("*联系电话");
			headRow.createCell(5).setCellValue("*地址");
			headRow.createCell(6).setCellValue("邮箱");
			headRow.createCell(7).setCellValue("*法人主体");
			headRow.createCell(8).setCellValue("*纳税号");
			headRow.createCell(9).setCellValue("*是否签约合同");
			headRow.createCell(10).setCellValue("*结算类型");
			headRow.createCell(11).setCellValue("*账期");
			headRow.createCell(12).setCellValue("税票种类");
			headRow.createCell(13).setCellValue("税率");
			headRow.createCell(14).setCellValue("客户等级");
			headRow.createCell(15).setCellValue("*接单部门");
			headRow.createCell(16).setCellValue("*接单客服");
			headRow.createCell(17).setCellValue("*业务员");
			headRow.createCell(18).setCellValue("原因");
 
			//处理excel的数据，遍历所有的结果
			for (int k = 0; k < (rows < SPLIT_COUNT ? rows : SPLIT_COUNT); k++) {
				if (((i - 1) * SPLIT_COUNT + k) >= rows)//如果数据超出总的记录数的时候，就退出循环
					break;
				HSSFRow row = sheet.createRow((short) (k + 1));//创建1行
				//分页处理，获取每页的结果集，并将数据内容放入excel单元格
				ArrayList<String> rowList = (ArrayList<String>) fieldData.get((i - 1) * SPLIT_COUNT + k);
				for (int n = 0; n < rowList.size(); n++) {//遍历某一行的结果
					HSSFCell cell1 = row.createCell( n);//使用行创建列对象
					if(rowList.get(n) != null){
						cell1.setCellValue((String) rowList.get(n).toString());
					}else{
						cell1.setCellValue("");
					}
				}
			}
		}
		return workBook;
	}
 
	public void expordExcel(OutputStream os) throws Exception {
		workBook = createWorkbook();
		workBook.write(os);//将excel中的数据写到输出流中，用于文件的输出
		os.close();
	}

	/**
	 * 创建HSSFWorkbook对象
	 * @return HSSFWorkbook
	 */
	public HSSFWorkbook createWorkbook2() {

		workBook = new HSSFWorkbook();//创建一个工作薄对象
		int rows = fieldData.size();//总的记录数
		int sheetNum = 0;           //指定sheet的页数

		if (rows % SPLIT_COUNT == 0) {
			sheetNum = rows / SPLIT_COUNT;
		} else {
			sheetNum = rows / SPLIT_COUNT + 1;
		}

		for (int i = 1; i <= sheetNum; i++) {//循环2个sheet的值
			HSSFSheet sheet = workBook.createSheet("Page " + i);//使用workbook对象创建sheet对象
			/**************对标题添加样式begin********************/
			HSSFRow headRow = sheet.createRow((short) 0); //创建行，0表示第一行（本例是excel的标题）

			headRow.createCell(0).setCellValue("*供应商名称");
			headRow.createCell(1).setCellValue("供应商代码");
			headRow.createCell(2).setCellValue("*服务类型");
			headRow.createCell(3).setCellValue("*联系人");
			headRow.createCell(4).setCellValue("*联系电话");
			headRow.createCell(5).setCellValue("*地址");
			headRow.createCell(6).setCellValue("*结算类型");
			headRow.createCell(7).setCellValue("*账期");
			headRow.createCell(8).setCellValue("税票种类");
			headRow.createCell(9).setCellValue("税率");
			headRow.createCell(10).setCellValue("采购人员");
			headRow.createCell(11).setCellValue("原因");

			//处理excel的数据，遍历所有的结果
			for (int k = 0; k < (rows < SPLIT_COUNT ? rows : SPLIT_COUNT); k++) {
				if (((i - 1) * SPLIT_COUNT + k) >= rows)//如果数据超出总的记录数的时候，就退出循环
					break;
				HSSFRow row = sheet.createRow((short) (k + 1));//创建1行
				//分页处理，获取每页的结果集，并将数据内容放入excel单元格
				ArrayList<String> rowList = (ArrayList<String>) fieldData.get((i - 1) * SPLIT_COUNT + k);
				for (int n = 0; n < rowList.size(); n++) {//遍历某一行的结果
					HSSFCell cell1 = row.createCell( n);//使用行创建列对象
					if(rowList.get(n) != null){
						cell1.setCellValue((String) rowList.get(n).toString());
					}else{
						cell1.setCellValue("");
					}
				}
			}
		}
		return workBook;
	}

	public void expordExcel2(OutputStream os) throws Exception {
		workBook = createWorkbook2();
		workBook.write(os);//将excel中的数据写到输出流中，用于文件的输出
		os.close();
	}
 
}