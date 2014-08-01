package ua.od.vassio.backup.dropbox;

import liquibase.changelog.DatabaseChangeLog;
import liquibase.resource.ResourceAccessor;
import ua.od.vassio.backup.common.exception.DBException;
import ua.od.vassio.backup.liquibase.LiquibaseStructureSaver;

import java.io.OutputStream;
import java.sql.Connection;

/**
 * Created by vzakharchenko on 01.08.14.
 */
public class DropboxLiquibaseStructureSaver extends LiquibaseStructureSaver {

    public DropboxLiquibaseStructureSaver(Connection connection) throws DBException {
        super(connection);
    }

    @Override
    protected String pathToChangeSets() {
        return "/";
    }

    @Override
    protected String getIdPrefix() {
        return "DropBox_";
    }


    @Override
    protected ResourceAccessor getResourceAccessor() {
        return new DropBoxResourceOpener("backup-base-test2");
    }

    @Override
    protected OutputStream getOutputStream(String changelogId) {
        return null;
    }
}
