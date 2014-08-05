package y2javaee.xmal2.common;

import java.util.*;
import java.sql.*;
import javax.servlet.jsp.jstl.sql.*;

/*
 * ͨ�õ�JDBC���ݿ������
 * Y2JavaEE��ch06�Ĺ�����
 */
public class SQLCommandBean {
    private Connection conn;
    private String sql;
    private List paramList;

    /**
     * �趨������
     */
    public void setConnection(Connection conn) {
        this.conn = conn;
    }

    /**
     * �趨SQL���.
     */
    public void setSql(String sql) {
        this.sql = sql;
    }

    /**
     * �趨SQL���Ĳ���
     */
    public void setParamList(List paramList) {
        this.paramList = paramList;
    }

    /**
     * ִ�в�ѯ
     *
     * @return a javax.servlet.jsp.jstl.sql.Result object
     * @exception SQLException
     */
    public Result executeQuery() throws SQLException {
        Result result = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Statement stm = null;
        try {
            if (paramList != null && paramList.size() > 0) {
                // Use a PreparedStatement and set all paramList
                ps = conn.prepareStatement(sql);
                setParamList(ps, paramList);
                rs = ps.executeQuery();
            }
            else {
                // Use a regular Statement
                stm = conn.createStatement();
                rs = stm.executeQuery(sql);
            }
            result = ResultSupport.toResult(rs);
        }
        finally {
            if (rs != null) {
                try {rs.close();} catch (SQLException e) {}
            }
            if (stm != null) {
                try {stm.close();} catch (SQLException e) {}
            }
            if (ps != null) {
                try {ps.close();} catch (SQLException e) {}
            }
        }
        return result;
    }

    /**
     * ִ��Update���
     *
     * @return the number of rows affected
     * @exception SQLException
     */
    public int executeUpdate() throws SQLException {
        int noOfRows = 0;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        try {
            if (paramList != null && paramList.size() > 0) {
                // Use a PreparedStatement and set all paramList
                pstmt = conn.prepareStatement(sql);
                setParamList(pstmt, paramList);
                noOfRows = pstmt.executeUpdate();
            }
            else {
                // Use a regular Statement
                stmt = conn.createStatement();
                noOfRows = stmt.executeUpdate(sql);
            }
        }
        finally {
            if (rs != null) {
                try {rs.close();} catch (SQLException e) {}
            }
            if (stmt != null) {
                try {stmt.close();} catch (SQLException e) {}
            }
            if (pstmt != null) {
                try {pstmt.close();} catch (SQLException e) {}
            }
        }
        return noOfRows;
    }

    /**
     * �趨���Ĳ���.
     *
     * @param pstmt the PreparedStatement
     * @param paramList a List with objects
     * @exception SQLException
     */
    private void setParamList(PreparedStatement ps, List paramList)
        throws SQLException {
        for (int i = 0; i < paramList.size(); i++) {
            Object v = paramList.get(i);
            // Set the value using the method corresponding to the type.
            // Note! Set methods are indexed from 1, so we add 1 to i
            ps.setObject(i + 1, v);
        }
    }
}

