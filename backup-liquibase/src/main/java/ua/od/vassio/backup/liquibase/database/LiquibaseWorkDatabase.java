package ua.od.vassio.backup.liquibase.database;

import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.exception.DatabaseException;
import ua.od.vassio.backup.common.WorkDatabase;
import ua.od.vassio.backup.common.exception.DBException;

/**
 * Created by vzakharchenko on 24.07.14.
 */
public abstract class LiquibaseWorkDatabase implements WorkDatabase<Database> {

    protected abstract DatabaseConnection getDatabaseConnection();

    @Override
    public synchronized Database getCurrentDatabase() throws DBException {
        try {
            return DatabaseFactory.getInstance().findCorrectDatabaseImplementation(getDatabaseConnection());
        } catch (DatabaseException e) {
            throw new DBException(e);
        }
    }

    @Override
    public String getSchema() throws DBException {
        return getCurrentDatabase().getDefaultSchemaName();
    }

    @Override
    public String getCatalog() throws DBException {
        return getCurrentDatabase().getDefaultCatalogName();
    }
}
