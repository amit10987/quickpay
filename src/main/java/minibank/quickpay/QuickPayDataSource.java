package minibank.quickpay;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.sql2o.Sql2o;

import java.sql.Connection;
import java.sql.SQLException;

public final class QuickPayDataSource {

    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource dataSource;
    private static final Sql2o sql2o;

    static {
        config.setJdbcUrl("jdbc:hsqldb:mem:quickpay");
        config.setUsername("SA");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMinimumIdle(2);
        config.setMaximumPoolSize(20);
        dataSource = new HikariDataSource(config);
        sql2o = new Sql2o(dataSource);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static Sql2o getSql2o() {
        return sql2o;
    }

    private QuickPayDataSource() {
    }
}
