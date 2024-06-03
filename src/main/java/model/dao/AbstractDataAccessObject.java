package model.dao;

import model.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class  AbstractDataAccessObject <T> {


    public AbstractDataAccessObject() {
    }

    /**
     * Return the connection to database.
     *
     * @return Connection
     * @throws SQLException
     */
    protected Connection getConnection() throws SQLException {
        return DBManager.getConnection();
    }


    /**
     * Prepare a statement for a param query
     *
     * @param conn Connection to database
     * @param queryId a query id
     * @return PreparedStatement
     * @throws SQLException
     */
    protected PreparedStatement prepareStatement(Connection conn, String queryId) throws SQLException{
        String sql = DBManager.requestToGetQueryString(queryId);
        if (sql == null) {
            throw new SQLException("SQL query for query ID " + queryId + " is null");
        }
        return conn.prepareStatement(sql);
    }


    /**
     * Return a List of <T> element.
     *
     * @param rs Result set, the result of an executed query
     * @return List<T>
     * @throws SQLException
     */
    protected ArrayList<T> getResultAsList(ResultSet rs) throws SQLException {
        ArrayList<T> resultList = new ArrayList<>();
        while (rs.next())
            resultList.add(extractFromResultSet(rs));
        return resultList;
    }

    protected T getResultAsObject(ResultSet rs) throws SQLException {
        int current = 0;
        while (rs.next()) {
            if (current == 0)
                return (extractFromResultSet(rs));
            current++;
        }
        return null;
    }

    /**
     * Implemented by DAO classes. It's used to return the T object extracted from ResultSet
     *
     * @param rs A result set. Query's output
     * @return T object
     * @throws SQLException
     */
    protected abstract T extractFromResultSet(ResultSet rs) throws SQLException;
    protected abstract void insertInto (T t) throws SQLException;

    protected abstract <E> void update(E e);

    protected abstract void delete(int id);
}