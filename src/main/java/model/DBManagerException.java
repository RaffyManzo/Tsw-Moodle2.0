package model;
import java.io.IOException;

public class DBManagerException extends Exception {
    public DBManagerException(IOException errorMessage) {
        super(errorMessage);
    }
}