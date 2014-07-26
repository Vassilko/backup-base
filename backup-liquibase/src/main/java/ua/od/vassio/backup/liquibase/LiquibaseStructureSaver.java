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
import ua.od.vassio.backup.common.StructureSaver;
import ua.od.vassio.backup.common.WorkDatabase;
import ua.od.vassio.backup.common.exception.DBException;
import ua.od.vassio.backup.liquibase.database.LiquibaseWorkDatabaseImpl;

import java.io.OutputStream;
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
    protected final static String CONTEXT_NAME = null;
    protected final static String AUTHOR_NAME = "eclm (generate)";

    private WorkDatabase<Database> workDatabase;
    private Liquibase liquibase;


    public LiquibaseStructureSaver(Connection connection) throws DBException {
        workDatabase=new LiquibaseWorkDatabaseImpl(connection);
        liquibase = new Liquibase(getDatabaseChangeLog(), getResourceAccessor(), workDatabase.getCurrentDatabase());
    }

    protected Set<Class<? extends DatabaseObject>> getCompareTypes() {
        Set<Class<? extends DatabaseObject>> compareTypes=new HashSet<Class<? extends DatabaseObject>>();
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

    protected DiffToChangeLog getFullDiff() throws  DBException {
        Database database=workDatabase.getCurrentDatabase();
        try {
            DatabaseSnapshot referenceSnapshot = SnapshotGeneratorFactory.getInstance().createSnapshot(database.getDefaultSchema(), database, new SnapshotControl(database, getCompareTypes().toArray(new Class[]{})));
            DiffResult diffResult = DiffGeneratorFactory.getInstance().compare(referenceSnapshot, null, getCompareControl());
            DiffToChangeLog diffToChangeLog = new DiffToChangeLog(diffResult, getDiffOutputControl());
            diffToChangeLog.setChangeSetAuthor(AUTHOR_NAME);
            diffToChangeLog.setChangeSetContext(CONTEXT_NAME);
            diffToChangeLog.setIdRoot(getIdPrefix());
            return diffToChangeLog;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    protected void updateChangeSets(Collection<ChangeSet> changeSets) {
        if (CollectionUtils.isNotEmpty(changeSets)){
            for (ChangeSet changeSet:changeSets){
                updateChangeSet(changeSet);
            }
        }
    }

    protected void  updateChangeSet(ChangeSet changeSet){
        changeSet.setFilePath(StringUtils.substringAfterLast(changeSet.getFilePath(),"/"));
    }

    protected abstract String getIdPrefix();


    protected abstract DatabaseChangeLog getDatabaseChangeLog();

    protected abstract ResourceAccessor getResourceAccessor();

    protected abstract OutputStream getOutputStream(String changelogId);

    @Override
    public void save() throws DBException{
        DiffToChangeLog diffToChangeLog=getFullDiff();
        try {
            List<ChangeSet>  changeSets=diffToChangeLog.generateChangeSets();
            updateChangeSets(changeSets);
            diffToChangeLog.print(new PrintStream(getOutputStream(diffToChangeLog.getChangeSetPath())),
                    new XMLChangeLogSerializer(),
                    changeSets);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update() throws DBException {
        try {
            liquibase.update(CONTEXT_NAME);
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public void deploy() {

    }
}
