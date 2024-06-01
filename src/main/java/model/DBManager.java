package model;

import jakarta.servlet.ServletContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DBManager {

    private static DBManager dbManager;
    private String mysqlUrl;
    private String user;
    private String password;
    private final Properties queryProperties; // all query list
    private Properties dbProperties; // all database properties

    public DBManager() throws DBManagerException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        String catalinaHome = System.getenv("CATALINA_HOME") + "\\upload";
        String queryPropertiesPath = catalinaHome + "\\app\\query.properties";

        System.out.println(queryPropertiesPath);

        try {
            queryProperties = new Properties();

            File f = new File(queryPropertiesPath);

            queryProperties.load(f.toURI().toURL().openStream());


            // Assign the jdbc connection instance variables
            this.mysqlUrl = "jdbc:mysql://localhost:3306/learn_hub";
            this.user = "root";
            this.password = "abcde";
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
        public static DBManager getInstance () throws DBManagerException, ClassNotFoundException {
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
        public Connection getConnection () throws SQLException {
            return DriverManager.getConnection(mysqlUrl, user, password);
        }


        /**
         * With a query ID, search for the requested query
         *
         * @param queryId
         * @return String query
         */
        public String getQuery (String queryId){
            return queryProperties.getProperty(queryId);
        }
    }
