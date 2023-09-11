import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;

import static java.sql.Connection.*;

public class JDBCUtil {
    private static Properties settings = new Properties();
    private static String fileSeparator;
    private static String filePath = "src/resources/jdbc.properties";

    private static String url;
    private static String ip;
    private static String username;
    private static String password;
    private static String driver;

    private static String logFile;

    private static BasicDataSource ds = new BasicDataSource();

    private static int transactionIsolationLevel = TRANSACTION_READ_COMMITTED;

    static {
        fileSeparator = System.getProperty("file.separator");
        FileReader in = null;
        try {
            in = new FileReader(filePath);
        } catch (FileNotFoundException e1) {
            System.out.println("File is not found: " + filePath);
            e1.printStackTrace();
        }
        try {
            settings.load(in);
        } catch (IOException e) {
            System.out.println("Problem with IO: " + e.getMessage());
            e.printStackTrace();
        }

        // settings.list(System.out);
        // System.out.println();
        url = settings.getProperty("url");
        username = settings.getProperty("username");
        password = settings.getProperty("password");
        driver = settings.getProperty("driver");
        logFile = settings.getProperty("logFile");

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.out.println(driver + " is not found: " + e.getMessage());
        }
    }

    public static Connection getConnection() {

        if (logFile != null) {
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(new File(logFile));
            } catch (FileNotFoundException e) {
                System.out.println("In DriverManagerExample, problem with creating log file " + e.getMessage());
            }
            DriverManager.setLogWriter(writer);
        }

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            //connection.setTransactionIsolation(transactionIsolationLevel);
        } catch (SQLException e) {
            System.out.println("In JDBCUtil, problem with getting a connection: " + e.getMessage());
        }
        // Properties clientInfo = new Properties();
        // clientInfo.setProperty("verifyServerCertificate", "false") ;
        // clientInfo.setProperty("useSSL", "false") ;
        // try {
        // connection.setClientInfo(clientInfo);
        // } catch (SQLClientInfoException e) {
        // e.printStackTrace();
        // }
        return connection;
    }

    /*
     * https://gitbox.apache.org/repos/asf?p=commons-dbcp.git;a=blob_plain;f=doc/BasicDataSourceExample.java;hb=HEAD
     */
    public static Connection getConnectionFromDataSource() {

        ds.setDriverClassName(driver);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setUrl(url);

        Connection connection = null;
        try {
            connection = ds.getConnection();
        } catch (SQLException e) {
            System.out.println("In JDBCUtil, problem with getting a connection: " + e.getMessage());
        }
        return connection;
    }

    public static void setTransactionIsolationLevel(int transactionIsolationLevel) {
        JDBCUtil.transactionIsolationLevel = transactionIsolationLevel;
    }

    public static String getProperty(String propertyName) {
        String propertyValue = switch (propertyName) {
            case "url" -> url;
            case "username" -> username;
            case "password" -> password;
            case "driver" -> driver;
            default -> null;

//		case "database":
//			propertyValue = database; break;
        };

        return propertyValue;
    }

    public static void main(String[] args) {
        Connection conn = getConnection();
//		Connection conn = getConnectionFromDataSource();
        if (conn != null)
            System.out.println(conn);
        else
            System.out.println("No connection!");


    }

}
