package ua.od.vassio.backup.spring;

import liquibase.resource.ResourceAccessor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.vassio.backup.common.exception.ClearException;
import ua.od.vassio.backup.common.exception.DBException;
import ua.od.vassio.backup.common.exception.UploadException;
import ua.od.vassio.backup.liquibase.LiquibaseStructureSaver;
import ua.od.vassio.backup.liquibase.database.LiquibaseWorkDatabaseImpl;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: vassio
 * Date: 12.04.15
 * Time: 22:35
 */
public class SpringStructureSaver extends LiquibaseStructureSaver {
    private static Logger logger = LoggerFactory.getLogger(SpringStructureSaver.class);

    private String pathToChangeSets;
    private String pathToSaveChangeSets=".";
    private static final String DEFAULT_PREFIX = "unknown_";
    private static SpringResourceOpener springResourceOpener= new SpringResourceOpener();
    private String prefix;

    @Override
    protected String pathToChangeSets() {
        return pathToChangeSets;
    }

    @Override
    protected String getIdPrefix() {
        return StringUtils.isNotEmpty(prefix)?prefix:DEFAULT_PREFIX;
    }

    @Override
    protected ResourceAccessor getResourceAccessor() {
        return springResourceOpener;
    }

    @Override
    public String upload(ByteArrayOutputStream outputStream) throws UploadException {
        try {
            String fileName = generateFileName();
            File file =new File(pathToSaveChangeSets,fileName);
            IOUtils.write(outputStream.toByteArray(),new FileOutputStream(file));
            logger.info("upload " + fileName + " Success");
            return fileName;
        } catch (IOException e) {
            throw new UploadException(e);
        }
    }

    private String generateFileName() {
        try {
            return getIdPrefix() + workDatabase.getCatalog() + ".xml";
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() throws ClearException {
        throw new UnsupportedOperationException("clear is not supported");
    }

    public void setPathToChangeSets(String pathToChangeSets) {
        this.pathToChangeSets = pathToChangeSets;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setPathToSaveChangeSets(String pathToSaveChangeSets) {
        File file=new File(pathToSaveChangeSets)  ;
        file.mkdirs();
        this.pathToSaveChangeSets = pathToSaveChangeSets;
    }

    public void setDataSource(DataSource dataSource) {
        try {
            setWorkDatabase(new LiquibaseWorkDatabaseImpl(dataSource.getConnection()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
