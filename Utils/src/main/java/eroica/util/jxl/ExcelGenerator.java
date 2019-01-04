package eroica.util.jxl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import eroica.util.enumeration.EnumUtil;
import jxl.Cell;
import jxl.Range;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.CellFormat;
import jxl.format.Font;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * Excel generator using jxl.
 * 
 * @TODO Currently only string data are supported, other kind of data may be
 *       wrongly disposed.
 * 
 * @author Minhua HUANG
 *
 */
public class ExcelGenerator {

	/**
	 * Get the recommended format of a cell, choosing border widths according to its
	 * position.
	 * 
	 * @param font
	 * @param alignment column alignment
	 * @param template  Border thickness template
	 * @throws WriteException thrown by jxl package
	 * @return the recommended format of a cell
	 */
	public static WritableCellFormat getCellFormat(WritableFont font, Alignment alignment,
			BorderThicknessTemplate template) throws WriteException {
		WritableCellFormat format = new WritableCellFormat(font);
		format.setAlignment(alignment);
		setBorder(format, template.getTopThickness(), Border.TOP);
		setBorder(format, template.getBottomThickness(), Border.BOTTOM);
		setBorder(format, template.getLeftThickness(), Border.LEFT);
		setBorder(format, template.getRightThickness(), Border.RIGHT);
		return format;
	}

	private static void setBorder(WritableCellFormat format, int thickness, Border border) throws WriteException {
		if (thickness == 2)
			format.setBorder(border, BorderLineStyle.MEDIUM);
		else if (thickness == 1)
			format.setBorder(border, BorderLineStyle.THIN);
		else
			format.setBorder(border, BorderLineStyle.NONE);
	}

	/**
	 * Generate a .xls file with recommended format.<br>
	 * 
	 * @TODO If the total amount of data is larger than Constants.MAX_BODY_ROWS, the
	 *       mergence types may result in incorrect result.
	 * @param title     sheet name and title
	 * @param heads     table heads, mergence types are supported
	 * @param keys      the keys of columns in data
	 * @param data      table data, mergence types are supported
	 * @param showTitle whether the title is shown at the top of sheets
	 * @return a byte array of the excel workbook
	 * @throws WriteException thrown by jxl package
	 * @throws IOException    thrown by jxl package
	 */
	public static <T> byte[] generateWorkbook(String title, String[] heads, String[] keys, List<Map<String, T>> data,
			boolean showTitle) throws WriteException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024 * 1024);
		WritableWorkbook workbook = null;
		try {
			workbook = Workbook.createWorkbook(baos);
			int totalSize = data.size();
			// the count of sheets
			int pages = totalSize % Constants.MAX_BODY_ROWS == 0 ? totalSize / Constants.MAX_BODY_ROWS
					: totalSize / Constants.MAX_BODY_ROWS + 1;
			// the data count of the last sheet
			int lastPageSize = totalSize % Constants.MAX_BODY_ROWS == 0 ? Constants.MAX_BODY_ROWS
					: totalSize % Constants.MAX_BODY_ROWS;
			for (int i = 0; i < pages; i++) {
				// sheet name/title of the current sheet
				String currentTitle = title == null ? String.valueOf(i + 1)
						: pages == 1 ? title : title + "-" + (i + 1);
				// sub list of the current sheet
				List<Map<String, T>> subList = data.subList(i * Constants.MAX_BODY_ROWS,
						i < pages - 1 ? ((i + 1) * Constants.MAX_BODY_ROWS)
								: i * Constants.MAX_BODY_ROWS + lastPageSize);
				generateListSheet(workbook.createSheet(currentTitle, i), showTitle && title != null ? title : null,
						heads, keys, null, subList);
			}
			workbook.write();
			return baos.toByteArray();
		} finally {
			if (baos != null)
				try {
					baos.close();
				} catch (IOException e) {
				}
			if (workbook != null)
				try {
					workbook.close();
				} catch (WriteException e) {
				} catch (IOException e) {
				}
		}
	}

	/**
	 * Generate a sheet of query-list kind with recommended format. The sheet object
	 * should be created and input. If more than one sheet are of query-list kind,
	 * the invoker may invoke this function on every sheet.
	 * 
	 * @param sheet  a sheet of excel workbook
	 * @param title  the title shown at top of the sheet(if it is null, the sheet
	 *               will start with table heads)
	 * @param heads  table heads, mergence types are supported
	 * @param keys   the keys of columns in data
	 * @param widths widths of the columns(if it is null or its length is less than
	 *               the column length or the length of a column is less than 0, the
	 *               widths of the columns are automatically calculated)
	 * @param data   table data, mergence types are supported
	 * @throws WriteException thrown by jxl package
	 */
	public static <T> void generateListSheet(WritableSheet sheet, String title, String[] heads, String[] keys,
			int[] widths, List<Map<String, T>> data) throws WriteException {
		generateListSheet(sheet, title, new String[][] { heads }, keys, widths, data);
	}

	/**
	 * Get column count according to heads and keys.
	 * 
	 * @param heads multi-line heads
	 * @param keys  the keys of columns in data
	 * @return column count
	 */
	public static int getColumnCount(String[][] heads, String[] keys) {
		int max = keys.length;
		for (int i = 0; i < heads.length; i++)
			max = Math.max(max, heads[i].length);
		return max;
	}

	/**
	 * Generate the title of a sheet.
	 * 
	 * @param sheet       a sheet of excel workbook
	 * @param title       the title
	 * @param font        the font of the title
	 * @param column      the left column of the title
	 * @param columnCount the span column count of the title
	 * @param row         the row number of the title
	 * @throws WriteException thrown by jxl package
	 */
	public static void generateTitle(WritableSheet sheet, String title, WritableFont font, int column, int columnCount,
			int row) throws WriteException {
		if (columnCount > 1)
			sheet.mergeCells(column, row, column + columnCount - 1, row);
		Label titleLabel = new Label(column, row, title,
				getCellFormat(font, Alignment.CENTRE, BorderThicknessTemplate.NONE));
		sheet.addCell(titleLabel);
	}

	/**
	 * Generate a sheet of query-list kind with recommended format. The sheet object
	 * should be created and input. If more than one sheet are of query-list kind,
	 * the invoker may invoke this function on every sheet. Multi-line heads are
	 * supported.
	 * 
	 * @param sheet  a sheet of excel workbook
	 * @param title  the title shown at top of the sheet(if it is null, the sheet
	 *               will start with table heads)
	 * @param heads  table heads, mergence types are supported, multi-line heads are
	 *               supported
	 * @param keys   the keys of columns in data
	 * @param widths widths of the columns(if it is null or its length is less than
	 *               the column length or the length of a column is less than 0, the
	 *               widths of the columns are automatically calculated)
	 * @param data   table data, mergence types are supported
	 * @throws WriteException thrown by jxl package
	 */
	public static <T> void generateListSheet(WritableSheet sheet, String title, String[][] heads, String[] keys,
			int[] widths, List<Map<String, T>> data) throws WriteException {
		int columnCount = getColumnCount(heads, keys);
		if (title != null)
			generateTitle(sheet, title, RecommendedFonts.defaultTitle(), 0, columnCount, 0);
		generateListSheet(sheet, 0, title == null ? 0 : 1, heads, keys, widths, data);
	}

	/**
	 * Generate a sheet of query-list kind with recommended format. The sheet object
	 * should be created and input. If more than one sheet are of query-list kind,
	 * the invoker may invoke this function on every sheet. Multi-line heads are
	 * supported. Where the table starts is according to leftMargin and topMargin,
	 * so that the table may not start at top-left.
	 * 
	 * @param sheet      a sheet of excel workbook
	 * @param leftMargin blank columns spared out of the left of the table
	 * @param topMargin  blank rows spared out of the top of the table
	 * @param heads      table heads, mergence types are supported, multi-line heads
	 *                   are supported
	 * @param keys       the keys of columns in data
	 * @param widths     widths of the columns(if it is null or its length is less
	 *                   than the column length or the length of a column is less
	 *                   than 0, the widths of the columns are automatically
	 *                   calculated)
	 * @param data       table data, mergence types are supported
	 * @throws WriteException thrown by jxl package
	 */
	public static <T> void generateListSheet(WritableSheet sheet, int leftMargin, int topMargin, String[][] heads,
			String[] keys, int[] widths, List<Map<String, T>> data) throws WriteException {
		int columnCount = getColumnCount(heads, keys);
		int rowCount = heads.length + data.size();
		int[] resultWidths = initResultWidths(widths, columnCount);
		WritableFont headFont = RecommendedFonts.defaultHead();
		WritableFont dataFont = RecommendedFonts.defaultBody();

		/* merge the cells and fill data into the table */
		List<MergedUnit> mergedUnits = getMergedUnits(heads, keys, data);// collect the merged units from heads and data
		for (MergedUnit mu : mergedUnits) {
			sheet.mergeCells(mu.getLeft() + leftMargin, mu.getTop() + topMargin, mu.getRight() + leftMargin,
					mu.getBottom() + topMargin);
			String valueOfTheMergedCell = null;// the value of the merged cell
			boolean isValueAtHead = false;// whether the cell is at head or at body
			/* determine valueOfTheMergedCell and isValueAtHead */
			for (int i = mu.getTop(); i <= mu.getBottom(); i++) {
				boolean isLineAtHead = false;
				if (i < heads.length)
					isLineAtHead = true;
				// collect the value of the merged cell
				for (int j = mu.getLeft(); j <= mu.getRight(); j++) {
					String value = null;
					if (isLineAtHead && heads[i].length > j)
						value = heads[i][j];
					else if (!isLineAtHead) {
						T valueT = data.get(i - heads.length).get(keys[j]);
						if (valueT != null)
							value = valueT.toString();
					}
					if (StringUtils.isNotEmpty(value) && !EnumUtil.existById(MergenceType.class, value)) {
						if (valueOfTheMergedCell == null) {
							valueOfTheMergedCell = value;
							isValueAtHead = isLineAtHead;
						} else
							throw new IllegalArgumentException("The merged cell of top:" + mu.getTop() + " bottom:"
									+ mu.getBottom() + " left:" + mu.getLeft() + " right:" + mu.getRight()
									+ " has more than one value(" + valueOfTheMergedCell + " and " + value + ").");
					}
				}
			}
			/* first, write the merged cells to the table */
			WritableCellFormat wcf = getCellFormat(isValueAtHead ? headFont : dataFont,
					isValueAtHead ? Alignment.CENTRE : Alignment.GENERAL,
					BorderThicknessTemplate.getRecommendedTemplate(mu, columnCount, rowCount));
			wcf.setVerticalAlignment(VerticalAlignment.CENTRE);// 行对齐-居中
			Label mergedLabel = new Label(mu.getLeft() + leftMargin, mu.getTop() + topMargin, valueOfTheMergedCell,
					wcf);
			sheet.addCell(mergedLabel);

			/* update auto width */
			if (mu.getLeft() == mu.getRight() && valueOfTheMergedCell != null && isAutoWidth(widths, mu.getLeft()))
				initAutoWidth(resultWidths, mu.getLeft(), mergedLabel);
		}

		/* second, write the head cells to the table */
		for (int i = 0; i < heads.length; i++) {
			for (int j = 0; j < columnCount; j++) {
				if (isCellMerged(j, i, mergedUnits))
					continue;
				BorderThicknessTemplate btt = BorderThicknessTemplate.getRecommendedTemplate(j, i, columnCount,
						rowCount);
				Label headLabel = new Label(j + leftMargin, i + topMargin, heads[i].length > j ? heads[i][j] : null,
						getCellFormat(headFont, Alignment.CENTRE, btt));
				sheet.addCell(headLabel);

				/* update auto width */
				if (heads[i][j] != null && isAutoWidth(widths, j))
					initAutoWidth(resultWidths, j, headLabel);
			}
		}

		/* third, write the datum cells */
		for (int i = 0; i < data.size(); i++) {
			Map<String, T> datumMap = data.get(i);
			for (int j = 0; j < columnCount; j++) {
				if (isCellMerged(j, i + heads.length, mergedUnits))
					continue;
				T datum = keys.length > j ? datumMap.get(keys[j]) : null;
				BorderThicknessTemplate btt = BorderThicknessTemplate.getRecommendedTemplate(j, i + heads.length,
						columnCount, rowCount);
				String datumStr = datum == null ? null : datum.toString();
				Label datumLabel = new Label(j + leftMargin, i + topMargin + heads.length, datumStr,
						getCellFormat(dataFont, Alignment.GENERAL, btt));
				sheet.addCell(datumLabel);

				/* update auto width */
				if (datum != null && isAutoWidth(widths, j))
					initAutoWidth(resultWidths, j, datumLabel);
			}
		}

		/* 设置列宽 */
		for (int i = 0; i < columnCount; i++)
			sheet.setColumnView(i + leftMargin, resultWidths[i]);
	}

	/**
	 * Apply auto width.
	 * 
	 * @param sheet       a sheet of excel workbook
	 * @param leftColumn  the most left column
	 * @param rightColumn the most right column
	 */
	public static void applyAutoWidth(WritableSheet sheet, int leftColumn, int rightColumn) {
		Range[] mergedCells = sheet.getMergedCells();
		for (int i = leftColumn; i < sheet.getColumns() && i <= rightColumn; i++) {
			Cell[] columnCells = sheet.getColumn(i);
			int autoWidth = 0;
			for (int j = 0; j < columnCells.length; j++) {
				MergedUnit mergedCell = getRelativeMergedUnit(i, j, mergedCells);
				if (mergedCell == null)
					autoWidth = Math.max(autoWidth, getAutoWidth(columnCells[j]));
				// TODO merged cells may not show correctly
				else if (mergedCell.getLeft() == mergedCell.getRight() && j == mergedCell.getTop())
					autoWidth = Math.max(autoWidth, getAutoWidth(sheet, mergedCell));
			}
			sheet.setColumnView(i, autoWidth);
		}
	}

	/**
	 * Apply auto width.
	 * 
	 * @param sheet a sheet of excel workbook
	 */
	public static void applyAutoWidth(WritableSheet sheet) {
		applyAutoWidth(sheet, 0, sheet.getColumns() - 1);
	}

	private static boolean isAutoWidth(int[] widths, int column) {
		return widths == null || widths.length <= column || widths[column] < 0;
	}

	// The width will be (standardWidthRatio*pointSize*charWidth+1).round.
	private final static BigDecimal STANDARD_WIDTH_RATIO = new BigDecimal(0.115f);

	/**
	 * Update the widths array with new width calculated by label content.
	 * 
	 * @param widths widths array
	 * @param column the column of the sheet(not the table, so that it supports the
	 *               left blank columns)
	 * @param label  label
	 */
	private static void initAutoWidth(int[] widths, int column, Label label) {
		widths[column] = Math.max(widths[column], getAutoWidth(label));
	}

	/**
	 * Get the recommended width of a column, according to the cell content. It does
	 * not support merged cells.
	 * 
	 * @param cell cell
	 * @return recommended width
	 */
	private static int getAutoWidth(Cell cell) {
		if (cell == null)
			return 0;
		return getAutoWidth(cell.getContents(), cell.getCellFormat());
	}

	/**
	 * Get the recommended width of a column, according to the cell content. It
	 * supports merged cells.
	 * 
	 * @param sheet      a sheet of excel workbook
	 * @param mergedCell a merged cell
	 * @return recommended width
	 */
	private static int getAutoWidth(WritableSheet sheet, MergedUnit mergedCell) {
		if (mergedCell.getLeft() < mergedCell.getRight())
			throw new IllegalArgumentException("The cell spans " + (mergedCell.getRight() - mergedCell.getLeft() + 1)
					+ " columns. I cannot decide its width.");
		String contents = null;
		CellFormat cellFormat = null;
		for (int i = mergedCell.getTop(); i <= mergedCell.getBottom(); i++) {
			Cell cell = sheet.getCell(mergedCell.getLeft(), i);
			if (contents == null && cell.getContents() != null)
				contents = cell.getContents();
			if (cellFormat == null && cell.getCellFormat() != null)
				cellFormat = cell.getCellFormat();
		}
		return getAutoWidth(contents, cellFormat);
	}

	private static int getAutoWidth(String contents, CellFormat cellFormat) {
		if (contents == null)
			return 0;
		char[] chars = contents.toCharArray();
		Font font = cellFormat.getFont();
		int pointSize = font.getPointSize();
		int charWidth = 0;
		for (int i = 0; i < chars.length; i++)
			if (chars[i] <= 0xFF)// in the range of ASCII and expanded ASCII
				charWidth++;
			else // not in the range of ASCII and expanded ASCII
				charWidth += 2;
		return new BigDecimal(pointSize * charWidth).multiply(STANDARD_WIDTH_RATIO)
				.setScale(0, BigDecimal.ROUND_HALF_UP).intValue() + 1;
	}

	private static int[] initResultWidths(int[] widths, int columnCount) {
		int[] resultWidth = widths == null ? new int[columnCount] : Arrays.copyOf(widths, columnCount);
		for (int i = widths == null ? 0 : widths.length; i < columnCount; i++)
			resultWidth[i] = -1;
		return resultWidth;
	}

	/**
	 * To determine whether the current cell is in the list of merged units.
	 * 
	 * @param column      the column of current cell
	 * @param row         the row of current cell
	 * @param mergedUnits the list of merged units
	 * @return in or not in
	 */
	private static boolean isCellMerged(int column, int row, List<MergedUnit> mergedUnits) {
		return getRelativeMergedUnit(column, row, mergedUnits) != null;
	}

	/**
	 * Find the related merged unit of the current cell.
	 * 
	 * @param column      the column of current cell
	 * @param row         the row of current cell
	 * @param mergedUnits the list of merged units
	 * @return the related merged unit
	 */
	private static MergedUnit getRelativeMergedUnit(int column, int row, List<MergedUnit> mergedUnits) {
		for (MergedUnit mu : mergedUnits)
			if (mu.getTop() <= row && mu.getBottom() >= row && mu.getLeft() <= column && mu.getRight() >= column)
				return mu;
		return null;
	}

	private static MergedUnit getRelativeMergedUnit(int column, int row, Range[] mergedCells) {
		for (Range r : mergedCells) {
			int top = r.getTopLeft().getRow();
			int bottom = r.getBottomRight().getRow();
			int left = r.getTopLeft().getColumn();
			int right = r.getBottomRight().getColumn();
			if (top <= row && bottom >= row && left <= column && right >= column)
				return new MergedUnit(top, bottom, left, right);
		}
		return null;
	}

	private static <T> List<MergedUnit> getMergedUnits(String[][] heads, String[] keys, List<Map<String, T>> data) {
		int columnCount = getColumnCount(heads, keys);
		int rowCount = heads.length + data.size();
		List<MergedUnit> mergedUnits = new ArrayList<>();
		/* the units in heads */
		for (int i = 0; i < heads.length; i++)
			for (int j = 0; j < heads[i].length; j++)
				appendCellToMergedUnits(j, i, columnCount, rowCount, heads[i][j], mergedUnits);
		/* the units in data */
		for (int i = 0; i < data.size(); i++) {
			Map<String, T> datumMap = data.get(i);
			for (int j = 0; j < keys.length; j++)
				appendCellToMergedUnits(j, i + heads.length, columnCount, rowCount, datumMap.get(keys[j]), mergedUnits);
		}
		return mergedUnits;
	}

	private static void appendCellToMergedUnits(int column, int row, int columnCount, int rowCount, Object datum,
			List<MergedUnit> mergedUnits) {
		int left = column, right = column;
		int top = row, bottom = row;
		if (datum == null)
			return;
		String datumStr = datum.toString();
		if (MergenceType.MERGE_TO_UP.getTypeId().equals(datumStr))
			top--;
		else if (MergenceType.MERGE_TO_DOWN.getTypeId().equals(datumStr))
			bottom++;
		else if (MergenceType.MERGE_TO_LEFT.getTypeId().equals(datumStr))
			left--;
		else if (MergenceType.MERGE_TO_RIGHT.getTypeId().equals(datumStr))
			right++;
		else if (MergenceType.MERGE_TO_UP_LEFT.getTypeId().equals(datumStr)) {
			top--;
			left--;
		} else if (MergenceType.MERGE_TO_UP_RIGHT.getTypeId().equals(datumStr)) {
			top--;
			right++;
		} else if (MergenceType.MERGE_TO_DOWN_LEFT.getTypeId().equals(datumStr)) {
			bottom++;
			left--;
		} else if (MergenceType.MERGE_TO_DOWN_RIGHT.getTypeId().equals(datumStr)) {
			bottom++;
			right++;
		} else
			return;

		if (top < 0 || left < 0 || right >= columnCount || bottom >= rowCount)
			throw new IllegalArgumentException("Merging out of bound.");

		/* merge with existing merged unit */
		for (Iterator<MergedUnit> iterator = mergedUnits.iterator(); iterator.hasNext();) {
			MergedUnit mu = iterator.next();
			if (mu.getLeft() <= right && mu.getRight() >= left && mu.getTop() <= bottom && mu.getBottom() >= top) {
				left = Math.min(mu.getLeft(), left);
				right = Math.max(mu.getRight(), right);
				top = Math.min(mu.getTop(), top);
				bottom = Math.max(mu.getBottom(), bottom);
				iterator.remove();
			}
		}
		mergedUnits.add(new MergedUnit(top, bottom, left, right));
	}
}
