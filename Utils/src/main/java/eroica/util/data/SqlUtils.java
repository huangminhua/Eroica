package eroica.util.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Utilities for sql processes.
 * 
 * @author Minhua HUANG
 *
 */
public class SqlUtils {
	/**
	 * Execute a DQL process by PreparedStatement.
	 * 
	 * @param conn   a database connection
	 * @param sql    a DQL string for PreparedStatement
	 * @param params parameters for PreparedStatement, which can be null
	 * @return a DataTable object
	 * @throws SQLException thrown by jdbc processes
	 */
	public static DataTable executeQuery(Connection conn, String sql, Object... params) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			if (ArrayUtils.isNotEmpty(params))
				for (int i = 0; i < params.length; i++)
					pstmt.setObject(i + 1, params[i]);
			return new DataTable(pstmt.executeQuery());
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
		}
	}

	/**
	 * Execute a batch of DML processes by PreparedStatement.
	 * 
	 * @param conn   a database connection
	 * @param sql    a DML string for PreparedStatement
	 * @param params parameters for PreparedStatement, every element of this list
	 *               represents one command, only one DML process is executed when
	 *               it is set to null
	 * @return an array of update counts containing one element for each element of
	 *         params. The elements of the array are ordered according to the order
	 *         of params. A number greater than or equal to zero -- indicates that
	 *         the command was processed successfully and is an update count giving
	 *         the number of rows in the database that were affected by the
	 *         command's execution. A value of
	 *         <code>java.sql.Statement.SUCCESS_NO_INFO</code> -- indicates that the
	 *         command was processed successfully but that the number of rows
	 *         affected is unknown.
	 * @throws SQLException thrown by jdbc processes
	 */
	public static int[] executeUpdates(Connection conn, String sql, List<Object[]> params) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			if (CollectionUtils.isEmpty(params))
				return new int[] { pstmt.executeUpdate() };
			else {
				for (int i = 0; i < params.size(); i++) {
					Object[] parama = params.get(i);
					for (int j = 0; j < parama.length; j++)
						pstmt.setObject(j + 1, parama[j]);
					if (params.size() == 1)
						return new int[] { pstmt.executeUpdate() };
					else {
						pstmt.addBatch();
						pstmt.clearParameters();
					}
				}
				return pstmt.executeBatch();
			}
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
		}
	}

	/**
	 * Execute a DML process by PreparedStatement.
	 * 
	 * @param conn   a database connection
	 * @param sql    a DML string for PreparedStatement
	 * @param params parameters for PreparedStatement
	 * @return either (1) the row count for SQL Data Manipulation Language (DML)
	 *         statements or (2) 0 for SQL statements that return nothing
	 * @throws SQLException thrown by jdbc processes
	 */
	public static int executeUpdate(Connection conn, String sql, Object... params) throws SQLException {
		List<Object[]> a = new ArrayList<>();
		a.add(params);
		return executeUpdates(conn, sql, a)[0];
	}

	/**
	 * Verify a batch of update counts. Results of
	 * <code>java.sql.Statement.SUCCESS_NO_INFO</code> are skipped.
	 * 
	 * @param updateCounts   update counts of an executed batch
	 * @param expectedCounts expected update counts of the same executed batch
	 * @throws SQLException verification fails
	 */
	public static void verifyUpdateCounts(int[] updateCounts, int[] expectedCounts) throws SQLException {
		for (int i = 0; i < updateCounts.length; i++)
			verifyUpdateCount(updateCounts[i], expectedCounts[i]);
	}

	/**
	 * Verify an update count. Results of
	 * <code>java.sql.Statement.SUCCESS_NO_INFO</code> are skipped.
	 * 
	 * @param updateCount   update count of an executed Statement
	 * @param expectedCount expected update count of the same executed Statement
	 * @throws SQLException verification fails
	 */
	public static void verifyUpdateCount(int updateCount, int expectedCount) throws SQLException {
		if (updateCount == Statement.SUCCESS_NO_INFO) // java.sql.Statement.SUCCESS_NO_INFO are skipped.
			return;
		else if (updateCount != expectedCount)
			throw new SQLException("Update count verification failed. Update count is " + updateCount
					+ " and ecpected count is " + expectedCount + ".");
	}
}
