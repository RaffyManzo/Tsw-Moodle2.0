package model.dao;

import model.DBManager;
import model.DBManagerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class  AbstractDataAccessObject <T> {
    private DBManager dbManager;


    public AbstractDataAccessObject() {
        try {
            // try to get instance of db manager
            dbManager = DBManager.getInstance();

            // DBmanagerException is generated on IOException
        } catch (DBManagerException e) {
            System.err.println("AbstractDAO:" + e.getMessage());
        }
    }

    /**
     * Return the connection to database.
     *
     * @return Connection
     * @throws SQLException
     */
    protected Connection getConnection() throws SQLException {
        if (dbManager != null) {
            return dbManager.getConnection();
        }
        return null;
    }


    /**
     * Prepare a statement for a param query
     *
     * @param conn Connection to database
     * @param queryId a String to identify the query
     * @return PreparedStatement
     * @throws SQLException
     */
    protected PreparedStatement prepareStatement(Connection conn, String queryId) throws SQLException {
        return conn.prepareStatement(dbManager.getQuery(queryId));
    }


    /**
     * Return a List of <T> element.
     *
     * @param rs Result set, the result of an executed query
     * @return List<T>
     * @throws SQLException
     */
    protected List<T> getResultAsList(ResultSet rs) throws SQLException {
        ArrayList<T> resultList = new ArrayList<>();
        while (rs.next())
            resultList.add(extractFromResultSet(rs));
        return resultList;
    }

    protected T getResultAsObject(ResultSet rs, int index) throws SQLException {
        int current = 0;
        while (rs.next()) {
            if (current == index)
                return (extractFromResultSet(rs));
            current++;
        }
        return null;
    }

    /**
     * Return the query with queryId
     *
     * @param queryId
     * @return
     */
    protected String getQuery(String queryId) {
        return dbManager.getQuery(queryId);
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
}