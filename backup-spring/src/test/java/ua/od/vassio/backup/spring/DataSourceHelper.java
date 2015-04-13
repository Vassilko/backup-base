package ua.od.vassio.backup.spring;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.hsqldb.jdbc.JDBCDriver;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

/**
 * Created with IntelliJ IDEA.
 * User: vassio
 * Date: 13.04.15
 * Time: 8:54
 */
public class DataSourceHelper {

    public static DataSource getDataSourceMySQL(String url,String userName, String password){
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(url);
        dataSource.setUser(userName);
        dataSource.setPassword(password);
        dataSource.setCharacterEncoding("utf8");
        return   dataSource;
    }

    public static DataSource getDataSourceHsqldbCreateSchema(String dbUrl) throws Exception {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(JDBCDriver.class);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }
}
