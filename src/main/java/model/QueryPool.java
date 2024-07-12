package model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class QueryPool {
    private static final Properties queries = new Properties();

    protected static String getQueryString(String queryId) {
        if (queries.isEmpty()) {
            try (InputStream inputStream = readResourceFile()) {
                if (inputStream == null) {
                    throw new IllegalArgumentException("xml/query.xml is not found");
                }
                queries.loadFromXML(inputStream);
                if (queries.isEmpty()) {
                    System.out.println("Properties queries not loaded");
                } else {
                    //queries.forEach((key, value) -> System.out.println(key + ": " + value));
                    System.out.println("Properties queries loaded successfully");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return queries.getProperty(queryId);
    }

    private static InputStream readResourceFile() {
        String xmlFilePath = "xml/query.xml";
        InputStream inputStream = QueryPool.class.getClassLoader().getResourceAsStream(xmlFilePath);

        if (inputStream == null) {
            System.out.println("File not found in classpath: " + xmlFilePath);
            throw new IllegalArgumentException(xmlFilePath + " is not found");
        }

        System.out.println("File found: " + xmlFilePath);
        return inputStream;
    }
}
