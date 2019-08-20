package eroica.util.data;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections4.MapUtils;

/**
 * A simple data structure for plain table operations.
 * 
 * @author Minhua HUANG
 *
 */
public class DataTable {
	private final Map<String, List<Object>> data; // all data
	private int size;// row count
	private final Map<String, String> keysAndNames = new HashMap<>(); // names of columns, which are shown as titles and
																		// comments

	/**
	 * Generate DataTable from ResultSet.
	 * 
	 * @param rs           ResultSet
	 * @param keysAndNames names of columns, which are shown as titles or comments
	 * @throws SQLException thrown by ResultSet operations
	 */
	public DataTable(ResultSet rs, Map<String, String> keysAndNames) throws SQLException {
		this(rs);
		correlateColumnKeysAndNames(keysAndNames);
	}

	/**
	 * Generate DataTable from ResultSet.
	 * 
	 * @param rs ResultSet
	 * @throws SQLException thrown by ResultSet operations
	 */
	public DataTable(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();// get all column names(keys) from ResultSetMetaData
		int columnCount = rsmd.getColumnCount();
		data = new LinkedHashMap<String, List<Object>>(columnCount);
		for (int i = 0; i < columnCount; i++)
			data.put(rsmd.getColumnName(i + 1), new ArrayList<>());
		for (size = 0; rs.next(); size++)// save data to data object
			for (int j = 0; j < columnCount; j++)
				setListValue(data.get(rsmd.getColumnName(j + 1)), size, rs.getObject(j + 1));
	}

	/**
	 * Generate DataTable from Map(String, ?) as a row.
	 * 
	 * @param m            Map(String, ?) as a row of List(Map(String, ?))
	 * @param keysAndNames names of columns, which are shown as titles or comments
	 */
	public DataTable(Map<String, ?> m, Map<String, String> keysAndNames) {
		this(m);
		correlateColumnKeysAndNames(keysAndNames);
	}

	/**
	 * Generate DataTable from Map(String, ?) as a row.
	 * 
	 * @param m Map(String, ?) as a row of List(Map(String, ?))
	 */
	public DataTable(Map<String, ?> m) {
		data = new LinkedHashMap<String, List<Object>>();
		prepareDataByRow(m);
	}

	/**
	 * Generate DataTable from List(Map(String, ?)).
	 * 
	 * @param rs           List(Map(String, ?)) as data set
	 * @param keysAndNames names of columns, which are shown as titles or comments
	 */
	public DataTable(List<Map<String, ?>> rs, Map<String, String> keysAndNames) {
		data = new LinkedHashMap<String, List<Object>>();
		size = 0;
		for (Map<String, ?> m : rs) {
			prepareDataByRow(m);
			size++;
		}
		correlateColumnKeysAndNames(keysAndNames);
	}

	/**
	 * Generate DataTable from List(Map(String, ?)).
	 * 
	 * @param rs List(Map(String, ?)) as data set
	 */
	public DataTable(List<Map<String, ?>> rs) {
		this(rs, null);
	}

	/**
	 * Correlate keys and names, which are shown as titles and comments, of columns.
	 * 
	 * @param keysAndNames names of columns, which are shown as titles or comments
	 */
	public void correlateColumnKeysAndNames(Map<String, String> keysAndNames) {
		this.keysAndNames.clear();
		if (MapUtils.isNotEmpty(keysAndNames))
			for (Entry<String, String> e : keysAndNames.entrySet())
				this.keysAndNames.put(e.getKey().toUpperCase(), e.getValue());
	}

	private void prepareDataByRow(Map<String, ?> m) {
		for (Entry<String, ?> e : m.entrySet()) {
			String key = e.getKey().toUpperCase();// transfer column keys to upper case
			if (data.containsKey(key)) // need not to add a column to data object
				setListValue(data.get(key), size, e.getValue());
			else // need to add a column to data object
			{
				List<Object> col = new ArrayList<>();
				setListValue(col, size, e.getValue());
				data.put(key, col);
			}
		}
	}

	private void setListValue(List<Object> list, int row, Object value) {
		for (int i = 0; i < row - list.size() + 1; i++)
			list.add(null);
		list.set(row, value);
	}

	/**
	 * Get all column keys, which represent column names of database.
	 * 
	 * @return column keys
	 */
	public Set<String> columnKeys() {
		return new LinkedHashSet<>(data.keySet());
	}

	/**
	 * Get pairs of key and name of columns.
	 * 
	 * @return pairs of key and name of columns
	 */
	public Map<String, String> columnKeysAndNames() {
		Map<String, String> res = new HashMap<>();
		for (String columnkey : this.columnKeys())
			res.put(columnkey, keysAndNames.get(columnkey));
		return res;
	}

	/**
	 * Get column count.
	 * 
	 * @return column count
	 */
	public int columnCount() {
		return data.size();
	}

	/**
	 * Get row count.
	 * 
	 * @return row count
	 */
	public int rowCount() {
		return size;
	}

	/**
	 * Get a List(Map(String, ?)) object as a row.
	 * 
	 * @param row row number, 0 as the first row
	 * @return a List(Map(String, ?)) object as a row
	 */
	public Map<String, Object> getRow(int row) {
		Map<String, Object> result = new LinkedHashMap<>(columnCount());
		for (Entry<String, List<Object>> e : data.entrySet())
			result.put(e.getKey(), e.getValue().get(row));
		return result;
	}

	/**
	 * Get a List(Map(String, ?)) object as a column.
	 * 
	 * @param columnKey key of a column, which represents a column name of database
	 * @return a List(Map(String, ?)) object as a column
	 */
	public List<Object> getColumn(String columnKey) {
		return data.get(columnKey.toUpperCase());
	}

	/**
	 * Get an object from DataTable by given column key and row number. The return
	 * type can be assigned.
	 * 
	 * @param            <C> return type
	 * @param row        row number, 0 as the first row
	 * @param columnKey  key of a column, which represents a column name of database
	 * @param returnType return type
	 * @return the object requested
	 * @throws UnsupportedOperationException thrown when the returned object cannot
	 *                                       be transformed to the assigned return
	 *                                       type
	 */
	public <C> C get(int row, String columnKey, Class<C> returnType) {
		List<Object> column = getColumn(columnKey);
		if (column == null)
			return null;
		Object o = column.get(row);
		return transformDataType(o, returnType);
	}

	/**
	 * When DataTable has only one element(one row with one column), return its
	 * value. The return type can be assigned.
	 * 
	 * @param            <C> return type
	 * @param returnType return type
	 * @return the object requested
	 * @throws UnsupportedOperationException thrown when the returned object cannot
	 *                                       be transformed to the assigned return
	 *                                       type or the table has more than one
	 *                                       element
	 */
	public <C> C getSingleItem(Class<C> returnType) {
		if (size == 1 && data.size() == 1) {
			Object o = data.entrySet().iterator().next().getValue().get(0);
			return transformDataType(o, returnType);
		} else
			throw new UnsupportedOperationException(
					"GetSingleItem is not supported as the table has more than one element.");
	}

	private <C> C transformDataType(Object o, Class<C> returnType) {
		if (o == null)
			return null;
		else if (returnType.isAssignableFrom(o.getClass())) {
			@SuppressWarnings("unchecked")
			C cc = (C) o;
			return cc;
		} else if (String.class.equals(returnType))// transformed to a string
		{
			@SuppressWarnings("unchecked")
			C cc = (C) o.toString();
			return cc;
		} else // TODO a lot of other transformation probabilities should be supported
			throw new UnsupportedOperationException(
					"Unsupported data type transformation: " + o.getClass() + "--->" + returnType);
	}

	/**
	 * Append another DataTable to right of this.
	 * 
	 * @param another another DataTable
	 */
	public void appendRight(DataTable another) {
		if (this.size != another.size)
			throw new IllegalArgumentException("The other DataTable should has the same row count as this.");
		Set<String> thisColumnKeys = this.columnKeys();
		Set<String> anotherColumnKeys = another.columnKeys();
		for (String ack : anotherColumnKeys)
			if (thisColumnKeys.contains(ack))
				throw new IllegalArgumentException("The other DataTable should not has any same column key as this.");
		this.data.putAll(another.data);
		this.keysAndNames.putAll(another.keysAndNames);
	}

	/**
	 * Set a value to this DataTable.
	 * 
	 * @param row       row number, 0 as the first row
	 * @param columnKey key of a column, which represents a column name of database
	 * @param value     the object to be set
	 */
	public void set(int row, String columnKey, Object value) {
		columnKey = columnKey.toUpperCase();
		List<Object> l = getColumn(columnKey);
		if (l == null) {
			l = new ArrayList<Object>();
			data.put(columnKey, l);
		}
		setListValue(l, row, value);
	}

	/**
	 * To a string which represents the table.
	 * 
	 * @return a string which represents the table
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String[] colKeys = columnKeys().toArray(new String[0]);
		for (int i = 0; i < colKeys.length; i++) {
			if (i > 0)
				sb.append("\t");
			sb.append(colKeys[i]);
			if (keysAndNames.containsKey(colKeys[i]))
				sb.append("(" + keysAndNames.get(colKeys[i]) + ")");
		}
		sb.append("\n");
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < colKeys.length; j++) {
				if (i > 0)
					sb.append("\t");
				sb.append(get(i, colKeys[j], Object.class));
			}
			sb.append("\n");
		}
		return sb.toString();
	}

}
