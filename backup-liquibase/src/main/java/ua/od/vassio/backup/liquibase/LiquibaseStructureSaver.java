package ua.od.vassio.backup.liquibase;

import liquibase.Liquibase;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.database.Database;
import liquibase.diff.DiffGeneratorFactory;
import liquibase.diff.DiffResult;
import liquibase.diff.compare.CompareControl;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.DiffToChangeLog;
import liquibase.resource.ResourceAccessor;
import liquibase.serializer.core.xml.XMLChangeLogSerializer;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.SnapshotControl;
import liquibase.snapshot.SnapshotGeneratorFactory;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.vassio.backup.common.StructureSaver;
import ua.od.vassio.backup.common.WorkDatabase;
import ua.od.vassio.backup.common.exception.DBException;
import ua.od.vassio.backup.common.exception.DropAllException;
import ua.od.vassio.backup.common.exception.UpdateException;
import ua.od.vassio.backup.common.exception.UploadException;
import ua.od.vassio.backup.liquibase.database.LiquibaseWorkDatabaseImpl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by vzakharchenko on 24.07.14.
 */
public abstract class LiquibaseStructureSaver implements StructureSaver {
    private static Logger logger= LoggerFactory.getLogger(LiquibaseStructureSaver.class);
    protected final static String CONTEXT_NAME = null;
    protected final static String AUTHOR_NAME = System.getProperty("user.name")+"(generate)";

    protected WorkDatabase<Database> workDatabase;
    protected Liquibase liquibase;
    protected DatabaseChangeLogReader changeLogReader;

    public LiquibaseStructureSaver(Connection connection) throws DBException {
        workDatabase = new LiquibaseWorkDatabaseImpl(connection);
    }

    public void init() throws DBException {
        logger.info("init LiquibaseStructureSaver");
        changeLogReader = new DatabaseChangeLogReaderImpl(getResourceAccessor(), workDatabase.getCurrentDatabase());
        liquibase = new Liquibase(getDatabaseChangeLog(), getResourceAccessor(), workDatabase.getCurrentDatabase());
        logger.info("init LiquibaseStructureSaver Success");
    }

    protected void initDatabaseChangeLog(DatabaseChangeLog databaseChangeLog,String fileName) throws DBException {
        try {
            logger.info("read "+fileName);
            databaseChangeLog.getChangeSets().clear();
            databaseChangeLog.getPreconditions().getNestedPreconditions().clear();
            changeLogReader.read(databaseChangeLog,pathToChangeSets()+ fileName, new StringComparator());
            updateChangeSets(databaseChangeLog.getChangeSets());
        } catch (Exception e) {
            throw new DBException(e);
        }
    }



    protected DatabaseChangeLog getDatabaseChangeLog() throws DBException {
        DatabaseChangeLog databaseChangeLog = new DatabaseChangeLog();
        return databaseChangeLog;
    }

    ;

    protected abstract String pathToChangeSets();

    protected Set<Class<? extends DatabaseObject>> getCompareTypes() {
        Set<Class<? extends DatabaseObject>> compareTypes = new HashSet<Class<? extends DatabaseObject>>();
        compareTypes.add(Table.class);
        compareTypes.add(PrimaryKey.class);
        compareTypes.add(ForeignKey.class);
        compareTypes.add(Column.class);
        compareTypes.add(Sequence.class);
        compareTypes.add(StoredProcedure.class);
        compareTypes.add(UniqueConstraint.class);
        compareTypes.add(Index.class);
        compareTypes.add(Data.class);
        return compareTypes;
    }

    protected CompareControl getCompareControl() {
        return new CompareControl(getCompareTypes());
    }


    protected DiffOutputControl getDiffOutputControl() {
        return new DiffOutputControl(false, true, true);
    }

    protected DiffToChangeLog getFullDiff() throws DBException {
        logger.info("get Full diff");
        Database database = workDatabase.getCurrentDatabase();
        try {
            DatabaseSnapshot referenceSnapshot = SnapshotGeneratorFactory.getInstance().createSnapshot(database.getDefaultSchema(), database, new SnapshotControl(database, getCompareTypes().toArray(new Class[]{})));
            DiffResult diffResult = DiffGeneratorFactory.getInstance().compare(referenceSnapshot, null, getCompareControl());
            DiffToChangeLog diffToChangeLog = new DiffToChangeLog(diffResult, getDiffOutputControl());
            diffToChangeLog.setChangeSetAuthor(AUTHOR_NAME);
            diffToChangeLog.setChangeSetContext(CONTEXT_NAME);
            diffToChangeLog.setIdRoot(getIdPrefix());
            return diffToChangeLog;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    protected void updateChangeSets(Collection<ChangeSet> changeSets) {
        if (CollectionUtils.isNotEmpty(changeSets)) {
            for (ChangeSet changeSet : changeSets) {
                updateChangeSet(changeSet);
            }
        }
    }

    protected void updateChangeSet(ChangeSet changeSet) {
        changeSet.setFilePath(StringUtils.substringAfterLast(changeSet.getFilePath(), "/"));
    }

    protected abstract String getIdPrefix();


    protected abstract ResourceAccessor getResourceAccessor();

    protected ByteArrayOutputStream getOutputStream() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        return byteArrayOutputStream;
    }

    public abstract String upload(ByteArrayOutputStream outputStream) throws UploadException;

    @Override
    public String save() throws DBException {
        logger.info("Start Save file");
        DiffToChangeLog diffToChangeLog = getFullDiff();
        try {
            List<ChangeSet> changeSets = diffToChangeLog.generateChangeSets();
            updateChangeSets(changeSets);
            ByteArrayOutputStream outputStream = getOutputStream();
            diffToChangeLog.print(new PrintStream(outputStream),
                    new XMLChangeLogSerializer(),
                    changeSets);
            return upload(outputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(String fileName) throws UpdateException {
        try {
            logger.info("Start update file: "+fileName);
            initDatabaseChangeLog(liquibase.getDatabaseChangeLog(),fileName);
            liquibase.update(CONTEXT_NAME);
        } catch (Exception e) {
            throw new UpdateException(e);
        }
    }

    @Override
    public void dropAll() throws DropAllException {
        try {
            logger.info("Start dropAll");
            liquibase.dropAll();
        } catch (Exception e) {
            throw new DropAllException(e);
        }
    }
}
