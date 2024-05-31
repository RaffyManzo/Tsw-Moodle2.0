package model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DBManager {

    private static DBManager dbManager;
    private Properties queryProperties; // all query list
    private Properties dbProperties; // all database properties

    private final String mysqlUrl;
    private final String user;
    private final String password;

    public DBManager() throws DBManagerException {
        try {
            queryProperties = new Properties();
            dbProperties = new Properties();

            // Load .properties resources
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            dbProperties.load(loader.getResourceAsStream("${catalina.home}/upload/database-file/db.properties"));
            queryProperties.load(loader.getResourceAsStream("${catalina.home}/upload/database-file/query.properties"));

            // Assign the jdbc connection instance variables
            this.mysqlUrl = "jdbc:mysql://" + dbProperties.getProperty("host") + ":"
                    + dbProperties.getProperty("port") + "/" + dbProperties.getProperty("dbName");
            this.user = dbProperties.getProperty("user");
            this.password = dbProperties.getProperty("password");
        } catch (IOException e) {
            throw new DBManagerException(e);
        }
    }


    /**
     * Create or return the instance of database manager. Create it if not exists
     *
     * @return DBManager
     * @throws DBManagerException
     */
    public static DBManager getInstance() throws DBManagerException {
        if (dbManager == null)
            dbManager = new DBManager();
        return dbManager;
    }

    /**
     *  Create the connection to databasase
     *
     * @return Connection the connection to database
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(mysqlUrl, user, password);
    }


    /**
     * With a query ID, search for the requested query
     *
     * @param queryId
     * @return String query
     */
    public String getQuery(String queryId) {
        return queryProperties.getProperty(queryId);
    }
}
