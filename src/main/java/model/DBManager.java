package model;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.TimeZone;


public class DBManager {

    private static DataSource ds;

    /**
     * With a query ID, search for the requested query
     *
     * @param queryId
     * @return String query
     */
    public static String requestToGetQueryString(String queryId){
        return QueryPool.getQueryString(queryId);
    }

    /**
     * Create the connection to databasase
     *
     * @return Connection the connection to database
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        if (ds == null) {
            PoolProperties properties = new PoolProperties();
            properties.setDriverClassName("com.mysql.cj.jdbc.Driver");
            properties.setUrl("jdbc:mysql://localhost:3306/learn_hub?serverTimezone=" + TimeZone.getDefault().getID());
            properties.setUsername("root");
            properties.setPassword("abcde");
            properties.setMaxActive(100);
            properties.setInitialSize(10);
            properties.setMinIdle(10);
            properties.setRemoveAbandonedTimeout(60);
            properties.setRemoveAbandoned(true);

            ds = new DataSource();
            ds.setPoolProperties(properties);
        }
        return ds.getConnection();
    }
}
