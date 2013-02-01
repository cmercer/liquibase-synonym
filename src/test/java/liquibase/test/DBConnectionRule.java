package liquibase.test;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import org.junit.rules.ExternalResource;


public class DbConnectionRule extends ExternalResource {

    private String url;

    private String username;

    private String password;

    private Properties properties;

    private Connection connection;


    public DbConnectionRule(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public DbConnectionRule(String url, Map<String, String> properties) {
        this.url = url;
        this.properties = new Properties();
        for(Map.Entry<String, String> entry: properties.entrySet()) {
            this.properties.setProperty(entry.getKey(), entry.getValue());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        Enumeration e = DriverManager.getDrivers();
        while(e.hasMoreElements()) {
            Driver d = (Driver)e.nextElement();
            d.acceptsURL("");
        }
        if(properties == null) {
            connection = DriverManager.getConnection(url, username, password);
        } else {
            connection = DriverManager.getConnection(url, properties);
        }
        connection.setAutoCommit(false);
    }

    @Override
    protected void after(){
        super.after();
        try {
            connection.rollback();
            connection.close();

        } catch(Throwable t) {

        }
        connection = null;
    }
}
