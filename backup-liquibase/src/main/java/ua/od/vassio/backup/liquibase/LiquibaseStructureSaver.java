package ua.od.vassio.backup.liquibase;

import liquibase.Liquibase;
import ua.od.vassio.backup.common.StructureSaver;
import ua.od.vassio.backup.common.exception.DBException;
import ua.od.vassio.backup.liquibase.database.LiquibaseWorkDatabaseImpl;

import java.sql.Connection;

/**
 * Created by vzakharchenko on 24.07.14.
 */
public class LiquibaseStructureSaver implements StructureSaver {

    private Liquibase liquibase;

    public LiquibaseStructureSaver(Liquibase liquibase) {
        this.liquibase = liquibase;
    }

    public LiquibaseStructureSaver(Connection connection) throws DBException {
      //  liquibase=new Liquibase(new LiquibaseWorkDatabaseImpl(connection).getCurrentDatabase())
    }

    @Override
    public void save() {

    }

    @Override
    public void update() {

    }

    @Override
    public void deploy() {

    }
}
