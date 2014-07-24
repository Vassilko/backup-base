package ua.od.vassio.backup.liquibase.database;

import liquibase.database.DatabaseConnection;
import liquibase.database.jvm.JdbcConnection;

import java.sql.Connection;

/**
 * Created by vzakharchenko on 24.07.14.
 */
public class LiquibaseWorkDatabaseImpl extends LiquibaseWorkDatabase {

    JdbcConnection connection;

    public LiquibaseWorkDatabaseImpl(Connection connection) {
        this.connection = new JdbcConnection(connection);
    }

    @Override
    protected DatabaseConnection getDatabaseConnection() {
        return connection;
    }

    @Override
    public int getMaxConstraintLength() {
        return 31;
    }
}
