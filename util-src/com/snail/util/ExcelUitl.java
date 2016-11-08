package com.snail.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.snail.common.SPException;

/**
 * 
 * <p>
 * Title: com.snail.util.ExcelUitl
 * </p>
 * 
 * <p>
 * Description: Excel文件处理类
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年8月12日
 * 
 * @version 1.0
 * 
 */
@SuppressWarnings("deprecation")
public class ExcelUitl {
	private static Logger LOG = Logger.getLogger(ExcelUitl.class);

	/**
	 * Excel 97-2003
	 */
	public static final String WORK_BOOK_TYPE_XLS = "xls";
	/**
	 * Excel 2007
	 */
	public static final String WORK_BOOK_TYPE_XLSX = "xlsx";

	/**
	 * 创建workbook
	 * 
	 * @param type
	 *            当type为xlsx时创建07版的Workbook,否则创建03版的Workbook
	 * @return HSSFWorkbook / XSSFWorkbook
	 * @throws Exception
	 */
	public static Workbook createWorkbook(String type) throws Exception {
		Workbook wb = new HSSFWorkbook();
		if (StringUtils.isNotEmpty(type) && type.equals(WORK_BOOK_TYPE_XLSX)) {
			wb = new XSSFWorkbook();
		}
		return wb;
	}

	/**
	 * 创建sheet
	 * 
	 * @param type
	 * @param sheetName
	 * @return
	 * @throws Exception
	 */
	public static Workbook createWorkbookWithASheet(String type,
			String sheetName) throws Exception {
		Workbook wb = createWorkbook(type);
		if (StringUtils.isEmpty(sheetName)) {
			wb.createSheet();
		} else {
			wb.createSheet(sheetName);
		}
		return wb;
	}

	/**
	 * 插入一行数据
	 * 
	 * @param sheet
	 * @param datas
	 * @param rowIndex
	 * @param startCellIndex
	 * @return
	 * @throws Exception
	 */
	public static boolean insertRowValues(Sheet sheet, List<String> datas,
			int rowIndex, int startCellIndex) throws Exception {
		if (sheet == null || datas == null || datas.isEmpty()) {
			return true;
		}
		if (rowIndex < 0) {
			rowIndex = 0;
		}
		if (startCellIndex < 0) {
			startCellIndex = 0;
		}
		Row row = sheet.getRow(rowIndex);
		if (row == null) {
			row = sheet.createRow(rowIndex);
		}
		for (String data : datas) {
			Cell cell = row.getCell(startCellIndex);
			if (cell == null) {
				cell = row.createCell(startCellIndex);
			}
			startCellIndex++;
			cell.setCellValue(data);
		}
		return true;
	}

	/**
	 * 插入一行数据
	 * 
	 * @param sheet
	 * @param datas
	 * @return
	 * @throws Exception
	 */
	public static boolean insertRowValues(Sheet sheet, List<String> datas)
			throws Exception {
		return insertRowValues(sheet, datas, 0, 0);
	}

	/**
	 * 插入一行数据
	 * 
	 * @param sheet
	 * @param datas
	 * @param rowIndex
	 * @return
	 * @throws Exception
	 */
	public static boolean insertRowValues(Sheet sheet, List<String> datas,
			int rowIndex) throws Exception {
		return insertRowValues(sheet, datas, rowIndex, 0);
	}

	/**
	 * 插入数据 包括Header
	 * 
	 * @param sheet
	 * @param headerNames
	 * @param cellNames
	 * @param datas
	 * @param startRowIndex
	 * @param startCellIndex
	 * @return
	 * @throws Exception
	 */
	public static boolean insertValuesWithHeader(Sheet sheet,
			List<String> headerNames, List<String> cellNames,
			List<Map<String, Object>> datas, int startRowIndex,
			int startCellIndex) throws Exception {
		if (sheet == null || headerNames == null || headerNames.isEmpty()
				|| datas == null || datas.isEmpty()) {
			return true;
		}
		if (startRowIndex < 0) {
			startRowIndex = 0;
		}
		if (startCellIndex < 0) {
			startCellIndex = 0;
		}
		if (cellNames == null) {
			cellNames = new ArrayList<String>();
		}
		if (cellNames.isEmpty()) {
			cellNames.addAll(headerNames);
		}
		// Header
		insertRowValues(sheet, headerNames, startRowIndex++, startCellIndex);
		// value
		insertValues(sheet, cellNames, datas, startRowIndex, startCellIndex);
		return true;
	}

	/**
	 * 插入数据 包括Header
	 * 
	 * @param sheet
	 * @param headerNames
	 * @param datas
	 * @return
	 * @throws Exception
	 */
	public static boolean insertValuesWithHeader(Sheet sheet,
			List<String> headerNames, List<Map<String, Object>> datas)
			throws Exception {
		return insertValuesWithHeader(sheet, headerNames, headerNames, datas,
				0, 0);
	}

	/**
	 * 插入多行数据
	 * 
	 * @param sheet
	 * @param cellNames
	 * @param datas
	 * @param startRowIndex
	 * @param startCellIndex
	 * @return
	 * @throws Exception
	 */
	public static boolean insertValues(Sheet sheet, List<String> cellNames,
			List<Map<String, Object>> datas, int startRowIndex,
			int startCellIndex) throws Exception {
		if (sheet == null || cellNames == null || cellNames.isEmpty()
				|| datas == null || datas.isEmpty()) {
			return true;
		}
		if (startRowIndex < 0) {
			startRowIndex = 0;
		}
		if (startCellIndex < 0) {
			startCellIndex = 0;
		}

		List<String> data = null;
		Object value = null;
		for (Map<String, Object> map : datas) {
			data = new ArrayList<String>();
			for (String key : cellNames) {
				value = map.get(key);
				if (isNullValue(value)) {
					data.add("");
				} else if (isDateValue(value)) {
					data.add(DateUtil.getFormatDate((java.util.Date) value));
				} else {
					data.add(String.valueOf(value));
				}
			}
			insertRowValues(sheet, data, startRowIndex++, startCellIndex);
		}
		return true;
	}

	private static boolean isNullValue(Object value) {
		return value == null
				|| (value instanceof String && StringUtils
						.isBlank((String) value));
	}

	private static boolean isDateValue(Object value) {
		return value instanceof java.util.Date
				|| value instanceof java.sql.Date
				|| value instanceof java.sql.Timestamp;
	}

	/**
	 * 插入多行数据
	 * 
	 * @param sheet
	 * @param cellNames
	 * @param datas
	 * @return
	 * @throws Exception
	 */
	public static boolean insertValues(Sheet sheet, List<String> cellNames,
			List<Map<String, Object>> datas) throws Exception {
		return insertValues(sheet, cellNames, datas, 0, 0);
	}

	/**
	 * 将数据保存到excel(03版本)文件中.
	 * @param fileDir 输出文件目录
	 * @param fileName 输出文件名
	 * @param datas 数据
	 * @param headers excel文件表头
	 * @param keys 数据data中keys(必须与headers中的顺序一致.)
	 * @return
	 */
	public static boolean saveDataToExcel(String fileDir, String fileName,
			List<Map<String, Object>> datas, String headers, String keys) {
		String[] headerArray = headers.split(",");
		String[] keyArray = keys.split(",");
		List<String> headerList = new ArrayList<String>();
		Map<String, String> headerMap = new HashMap<String, String>();
		for (int index = 0; index < keyArray.length; index++) {
			headerList.add(headerArray[index]);
			headerMap.put(headerArray[index], keyArray[index]);
		}
		return saveDataToExcel(fileDir, fileName, datas, headerMap, headerList);
	}

	/**
	 * 将数据保存到Excel(03版本)中
	 * @param fileDir 输出文件目录
	 * @param fileName 输出文件名
	 * @param datas 数据
	 * @param headerMaps excel文件表头与data中key的对照map
	 * @param headers excel文件表头
	 * @return
	 */
	public static boolean saveDataToExcel(String fileDir, String fileName,
			List<Map<String, Object>> datas, Map<String, String> headerMaps,
			List<String> headers) {
		if (datas == null || datas.isEmpty()) {
			return false;
		}
		if (headers == null) {
			headers = new ArrayList<String>();
		}

		if (headers.isEmpty()) {
			headers.addAll(datas.get(0).keySet());
		}
		List<String> keyList = new ArrayList<String>();
		if (headerMaps != null && !headerMaps.isEmpty()) {
			for (String header : headers) {
				keyList.add(headerMaps.get(header));
			}
		} else {
			keyList.addAll(headers);
		}
		try {
			Workbook book = null;
			Sheet sheet = null;
			File dir = new File(fileDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			if (!fileDir.endsWith(File.separator)) {
				fileDir += File.separator;
			}
			File file = new File(fileDir + fileName);
			if (!file.exists()) {
				file.createNewFile();
				book = createWorkbook(WORK_BOOK_TYPE_XLS);
				sheet = book.createSheet();
				// 写表头
				insertRowValues(sheet, headers);
				// 写正文数据.
				insertValues(sheet, keyList, datas, 1, 0);
			} else {
				book = new HSSFWorkbook(new FileInputStream(file));
				sheet = book.getSheetAt(0);
				insertValues(sheet, keyList, datas, sheet.getLastRowNum() + 1,
						0);
			}
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			book.write(fileOutputStream);
			fileOutputStream.close();
		} catch (Exception e) {
			LogUtil.errorLog(LOG, "向excel文件中写入数据失败.",e);
			return false;
		}
		return true;
	}

	/**
	 * 获取sheet中指定区域的内容
	 * @param sheet
	 * @param startRowIndex
	 * @param endRowIndex
	 * @param startCellIndex
	 * @param endCellIndex
	 * @return
	 * @throws Exception
	 */
	public static List<List<String>> getValues(Sheet sheet, int startRowIndex,
			int endRowIndex, int startCellIndex, int endCellIndex)
			throws Exception {
		List<List<String>> returnDatas = new ArrayList<List<String>>();
		if (sheet == null) {
			return returnDatas;
		}

		if (startRowIndex < 0) {
			startRowIndex = 0;
		}
		if (startCellIndex < 0) {
			startCellIndex = 0;
		}

		int lastRowNum = sheet.getLastRowNum();
		if (endRowIndex >= 0 && lastRowNum > endRowIndex) {
			lastRowNum = endRowIndex;
		}
		if (startRowIndex > lastRowNum) {
			return returnDatas;
		}

		Row row = sheet.getRow(startRowIndex);
		int lastCellNum = row.getLastCellNum();
		if (endCellIndex >= 0 && lastCellNum > endCellIndex) {
			lastCellNum = endCellIndex;
		}
		if (startCellIndex > lastCellNum) {
			return returnDatas;
		}

		List<String> data = null;
		for (int rowIndex = startRowIndex; rowIndex <= lastRowNum; rowIndex++) {
			data = getRowValue(sheet, rowIndex, startCellIndex, lastCellNum);
			if (ReturnUtil.listDataValidness(data)) {
				returnDatas.add(data);
			}
		}

		return returnDatas;
	}

	/**
	 * 获取一行数据
	 * @param sheet
	 * @param rowIndex
	 * @param startCellIndex
	 * @param endCellIndex
	 * @return
	 * @throws Exception
	 */
	private static List<String> getRowValue(Sheet sheet, int rowIndex,
			int startCellIndex, int endCellIndex) throws Exception {
		List<String> data = new ArrayList<String>();
		Row row = sheet.getRow(rowIndex);
		if (row == null) {
			return data;
		}
		for (int cellIndex = startCellIndex; cellIndex <= endCellIndex; cellIndex++) {
			Cell cell = row.getCell(cellIndex);
			if (cell == null) {
				data.add("");
			} else {
				data.add(cell.getStringCellValue());
			}
		}
		return data;
	}

	/**
	 * 获取一个单元格的内容
	 * @param sheet
	 * @param startRowIndex
	 * @param startCellIndex
	 * @return
	 * @throws Exception
	 */
	public static List<List<String>> getValues(Sheet sheet, int startRowIndex,
			int startCellIndex) throws Exception {
		return getValues(sheet, startRowIndex, -1, startCellIndex, -1);
	}

	/**
	 * 获取指定行的内容,从第一个单元格的开始
	 * @param sheet
	 * @param startRowIndex
	 * @return
	 * @throws Exception
	 */
	public static List<List<String>> getValues(Sheet sheet, int startRowIndex)
			throws Exception {
		return getValues(sheet, startRowIndex, 0);
	}

	/**
	 * 获取sheet中的数据,从第一行第一列开始
	 * 
	 * @param sheet
	 * @return
	 * @throws Exception
	 */
	public static List<List<String>> getValues(Sheet sheet) throws Exception {
		return getValues(sheet, 0);
	}

	/**
	 * 获取指定Excel指定区域的内容并保存到Map中
	 * 
	 * @param sheet
	 * @param startRowIndex
	 * @param endRowIndex
	 * @param startCellIndex
	 * @param endCellIndex
	 * @param mapKeys
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Map<String, String>> getValuesToMap(Sheet sheet,
			int startRowIndex, int endRowIndex, int startCellIndex,
			int endCellIndex, List<String> mapKeys) throws Exception {
		List<Map<String, String>> returnDatas = new ArrayList<Map<String, String>>();
		if (sheet == null || mapKeys == null || mapKeys.isEmpty()) {
			return returnDatas;
		}

		if (startRowIndex < 0) {
			startRowIndex = 0;
		}
		if (startCellIndex < 0) {
			startCellIndex = 0;
		}

		int lastRowNum = sheet.getLastRowNum();
		if (endRowIndex >= 0 && lastRowNum > endRowIndex) {
			lastRowNum = endRowIndex;
		}
		if (startRowIndex > lastRowNum) {
			return returnDatas;
		}

		Row row = sheet.getRow(startRowIndex);
		int lastCellNum = row.getLastCellNum();
		if (endCellIndex >= 0 && lastCellNum > endCellIndex) {
			lastCellNum = endCellIndex;
		}
		if (startCellIndex > lastCellNum) {
			return returnDatas;
		}

		Map<String, String> data = null;
		for (int rowIndex = startRowIndex; rowIndex <= lastRowNum; rowIndex++) {
			data = getRowValueToMap(sheet, rowIndex, startCellIndex,
					lastCellNum, mapKeys);
			if (ReturnUtil.listDataValidness(new ArrayList(data.values()))) {
				returnDatas.add(data);
			}
		}

		return returnDatas;
	}

	/**
	 * 
	 * 获取指定行的数据 并保存到Map中
	 * 
	 * @param sheet
	 * @param rowIndex
	 * @param startCellIndex
	 * @param endCellIndex
	 * @param mapKeys
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getRowValueToMap(Sheet sheet,
			int rowIndex, int startCellIndex, int endCellIndex,
			List<String> mapKeys) throws Exception {
		Map<String, String> data = new HashMap<String, String>();

		if (mapKeys == null || mapKeys.isEmpty()) {
			return data;
		}

		Row row = sheet.getRow(rowIndex);
		if (row == null) {
			return data;
		}

		int i = 0;
		int size = mapKeys.size();
		endCellIndex = Math.min(endCellIndex - 1, startCellIndex + size - 1);
		for (int cellIndex = startCellIndex; cellIndex <= endCellIndex; cellIndex++) {
			Cell cell = row.getCell(cellIndex);
			if (cell == null) {
				data.put(mapKeys.get(i++), "");
			} else {
				data.put(mapKeys.get(i++), getCellValueToString(cell));
			}
		}

		return data;
	}

	/**
	 * 获取Excel中的内容并保存到Map中
	 * 
	 * @param sheet
	 * @param startRowIndex
	 * @param startCellIndex
	 * @param mapKeys
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, String>> getValuesToMap(Sheet sheet,
			int startRowIndex, int startCellIndex, List<String> mapKeys)
			throws Exception {
		return getValuesToMap(sheet, startRowIndex, -1, startCellIndex, -1,
				mapKeys);
	}

	/**
	 * 获取Excel中的内容并保存到Map中
	 * 
	 * @param sheet
	 * @param startRowIndex
	 * @param mapKeys
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, String>> getValuesToMap(Sheet sheet,
			int startRowIndex, List<String> mapKeys) throws Exception {
		return getValuesToMap(sheet, startRowIndex, 0, mapKeys);
	}

	/**
	 * 获取Excel(03版本)中的内容并保存到Map中
	 * 
	 * @param excelFile
	 * @param mapKeys
	 *            保存到Map中的Key,按Excel内容中的顺序
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, String>> getDatasFromExcelFile(
			File excelFile, String mapKeys) throws Exception {
		String[] keys = mapKeys.split(",");
		List<String> mapKeyList = new ArrayList<String>();
		for (String key : keys) {
			mapKeyList.add(key);
		}
		return getDatasFromExcelFile(excelFile, mapKeyList);
	}

	/**
	 * 获取Excel(03版本)中的内容并保存到Map中
	 * 
	 * @param excelFile
	 * @param mapKeys
	 *            保存到Map中的Key,按Excel内容中的顺序
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, String>> getDatasFromExcelFile(
			File excelFile, List<String> mapKeys) throws Exception {
		List<Map<String, String>> returnData = new ArrayList<Map<String, String>>();
		if (excelFile == null || !excelFile.exists() || !excelFile.isFile()) {
			throw new SPException("文件不存在或不是文件!");
		}

		String fileName = excelFile.getName();
		if (!fileName.endsWith(".xls")) {
			throw new SPException("只能处理xls文件!");
		}

		if (!excelFile.canRead()) {
			throw new SPException("文件不可读!");
		}

		Workbook wb = new HSSFWorkbook(new FileInputStream(excelFile));
		Sheet sheet = wb.getSheetAt(0);
		try {
			returnData = getValuesToMap(sheet, 1, mapKeys);
		} catch (Exception e) {
			LogUtil.errorLog(LOG, "读取文件出错!", e);
			throw new Exception("读取文件出错,详细请参见日志!");
		}
		if (returnData == null) {
			throw new Exception("读取文件出错!");
		}
		return returnData;
	}

	/**
	 * 获取Excel中的内容并保存到Map中
	 * 
	 * @param sheet
	 * @param mapKeys
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, String>> getValuesToMap(Sheet sheet,
			List<String> mapKeys) throws Exception {
		return getValuesToMap(sheet, 0, mapKeys);
	}

	/**
	 * 获取Excel中的内容并保存到转换成bean
	 * 
	 * @param sheet
	 * @param startRowIndex
	 * @param endRowIndex
	 * @param startCellIndex
	 * @param endCellIndex
	 * @param bean
	 * @param attrs
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getValuesToBean(Sheet sheet, int startRowIndex,
			int endRowIndex, int startCellIndex, int endCellIndex, T bean,
			List<String> attrs) throws Exception {
		List<T> returnDatas = new ArrayList<T>();
		if (sheet == null) {
			return returnDatas;
		}

		if (startRowIndex < 0) {
			startRowIndex = 0;
		}
		if (startCellIndex < 0) {
			startCellIndex = 0;
		}

		int lastRowNum = sheet.getLastRowNum();
		if (endRowIndex >= 0 && lastRowNum > endRowIndex) {
			lastRowNum = endRowIndex;
		}
		if (startRowIndex > lastRowNum) {
			return returnDatas;
		}

		Row row = sheet.getRow(startRowIndex);
		int lastCellNum = row.getLastCellNum();
		if (endCellIndex >= 0 && lastCellNum > endCellIndex) {
			lastCellNum = endCellIndex;
		}
		if (startCellIndex > lastCellNum) {
			return returnDatas;
		}

		List<String> data = null;
		for (int rowIndex = startRowIndex; rowIndex <= lastRowNum; rowIndex++) {
			data = getRowValue(sheet, rowIndex, startCellIndex, lastCellNum);
			if (ReturnUtil.listDataValidness(data)) {
				bean = (T) bean.getClass().newInstance();
				BeanUtils.populate(bean, convertFromListToMap(data, attrs));
				returnDatas.add(bean);
			}
		}

		return returnDatas;
	}

	/**
	 * 获取Excel中的内容并保存到转换成bean
	 * 
	 * @param sheet
	 * @param startRowIndex
	 * @param startCellIndex
	 * @param bean
	 * @param attrs
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> getValuesToBean(Sheet sheet, int startRowIndex,
			int startCellIndex, T bean, List<String> attrs) throws Exception {
		return getValuesToBean(sheet, startRowIndex, -1, startCellIndex, -1,
				bean, attrs);
	}

	/**
	 * 获取Excel中的内容并保存到转换成bean
	 * 
	 * @param sheet
	 * @param startRowIndex
	 * @param bean
	 * @param attrs
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> getValuesToBean(Sheet sheet, int startRowIndex,
			T bean, List<String> attrs) throws Exception {
		return getValuesToBean(sheet, startRowIndex, 0, bean, attrs);
	}

	/**
	 * 获取Excel中的内容并保存到转换成bean
	 * 
	 * @param sheet
	 * @param bean
	 * @param attrs
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> getValuesToBean(Sheet sheet, T bean,
			List<String> attrs) throws Exception {
		return getValuesToBean(sheet, 0, bean, attrs);
	}

	/**
	 * List 转换成 Map
	 * @param data 数据
	 * @param attrs 属性(必须与数据的顺序一致)
	 * @return
	 * @throws Exception
	 */
	private static Map<String, Object> convertFromListToMap(List<String> data,
			List<String> attrs) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if (data == null || data.isEmpty()) {
			return map;
		}
		int dataListSize = data.size();
		int attrsListSize = attrs.size();
		if (dataListSize > attrsListSize) {
			dataListSize = attrsListSize;
		}

		for (int i = 0; i < dataListSize; i++) {
			map.put(attrs.get(i), data.get(i));
		}
		return map;
	}

	/**
	 * 取单元格中的数据返回字符串.
	 * @param cell
	 * @return
	 */
	private static String getCellValueToString(Cell cell) {
		String cellValue = "";
		if (cell == null) {
			return cellValue;
		}
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			cellValue = cell.getRichStringCellValue().getString();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
				cellValue = DateUtil.getFormatDateTime(cell.getDateCellValue());
			} else {
				cellValue = new DecimalFormat("###").format(cell
						.getNumericCellValue());
			}
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			if (cell.getBooleanCellValue()) {
				cellValue = "1";
			} else {
				cellValue = "0";
			}
			break;
		case Cell.CELL_TYPE_FORMULA:
			cellValue = cell.getCellFormula();
			break;
		default:
			cellValue = "";
		}
		return cellValue;
	}
}
