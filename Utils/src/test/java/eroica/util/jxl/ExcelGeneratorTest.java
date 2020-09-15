package eroica.util.jxl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eroica.util.doc.jxl.MergenceType;
import eroica.util.doc.jxl.WorkbookGenerator;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelGeneratorTest {
	public static void main(String[] args) throws IOException, WriteException {
		testGenerateListSheet("/home/huangminhua/tmp/test.xls");
		testGenerateListSheet("/home/huangminhua/tmp/test1.xls");
		testGenerateListSheet("/home/huangminhua/tmp/test2.xls");
	}

	private static void testGenerateListSheet(String filename) throws WriteException, IOException {
		WritableWorkbook workbook = Workbook.createWorkbook(new File(filename));
		WritableSheet sheet = workbook.createSheet("第一页", 0);
		String title = null;
		String[][] heads = new String[][] {
				new String[] { "序号", "部门信息", MergenceType.MERGE_TO_LEFT.getTypeId(),
						MergenceType.MERGE_TO_LEFT.getTypeId(), "人员信息", MergenceType.MERGE_TO_LEFT.getTypeId(),
						MergenceType.MERGE_TO_LEFT.getTypeId() },
				new String[] { MergenceType.MERGE_TO_UP.getTypeId(), "大部门", "中部门", "小部门", "基本信息",
						MergenceType.MERGE_TO_LEFT.getTypeId(), "其他信息" },
				new String[] { MergenceType.MERGE_TO_UP.getTypeId(), MergenceType.MERGE_TO_UP.getTypeId(),
						MergenceType.MERGE_TO_UP.getTypeId(), MergenceType.MERGE_TO_UP.getTypeId(), "姓名", "编号",
						"健康状况等等乱七八糟的东西" } };
		String[] keys = new String[] { "index", "dept0", "dept1", "dept2", "name", "code", "health" };
		List<Map<String, String>> data = new ArrayList<>();
		Map<String, String> m = new HashMap<>();
		m.put("index", "1");
		m.put("dept0", "dept0_0");
		m.put("dept1", "dept1_0");
		m.put("dept2", "dept2_0");
		m.put("name", "name_0");
		m.put("code", "code_0");
		m.put("health", "health_0");
		data.add(m);
		m = new HashMap<>();
		m.put("index", "2");
		m.put("dept0", MergenceType.MERGE_TO_UP.getTypeId());
		m.put("dept1", MergenceType.MERGE_TO_UP.getTypeId());
		m.put("dept2", MergenceType.MERGE_TO_UP.getTypeId());
		m.put("name", "name_1");
		m.put("code", "code_1");
		m.put("health", "health_1");
		data.add(m);
		m = new HashMap<>();
		m.put("index", "3");
		m.put("dept0", MergenceType.MERGE_TO_UP.getTypeId());
		m.put("dept1", MergenceType.MERGE_TO_UP.getTypeId());
		m.put("dept2", "dept2_1");
		m.put("name", "name_2");
		m.put("code", "code_2");
		m.put("health", "health_2");
		data.add(m);
		m = new HashMap<>();
		m.put("index", "4");
		m.put("dept0", "dept0_1");
		m.put("dept1", MergenceType.MERGE_TO_DOWN_RIGHT.getTypeId());
		m.put("dept2", "dept2_2");
		m.put("code", "code_3");
		m.put("health", "health_3");
		data.add(m);
		m = new HashMap<>();
		m.put("name", MergenceType.MERGE_TO_LEFT.getTypeId());
//		m.put("health", ExcelGenerationUtil.MERGED_TYPE_TO_RIGHT);
		data.add(m);
		WorkbookGenerator.generateListSheet(sheet, title, heads, keys, new int[] { 50, 50, 50, 50, 50, 50, 50 },
				data);
		WorkbookGenerator.applyAutoWidth(sheet);
		workbook.write();
		workbook.close();
	}
}
