package ua.od.vassio.backup.dropbox;

import liquibase.resource.ResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.vassio.backup.common.exception.ClearException;
import ua.od.vassio.backup.common.exception.DBException;
import ua.od.vassio.backup.common.exception.UploadException;
import ua.od.vassio.backup.liquibase.LiquibaseStructureSaver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vzakharchenko on 01.08.14.
 */
public class DropboxLiquibaseStructureSaver extends LiquibaseStructureSaver {
    private static Logger logger= LoggerFactory.getLogger(DropboxLiquibaseStructureSaver.class);
    private String defaultPath="/";

    private String prefix="DropBox_";

    private String app_key ;
    private String app_secret;
    private String app_name;
    private String accessToken;
    private DropBoxResourceOpener dropBoxResourceOpener;
    private static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("ddMMYYYY_HHmmss");

    public DropboxLiquibaseStructureSaver(Connection connection, String app_key, String app_secret, String app_name, String accessToken) throws DBException {
        super(connection);
        this.app_key = app_key;
        this.app_secret = app_secret;
        this.app_name = app_name;
        this.accessToken = accessToken;
        this.dropBoxResourceOpener=new DropBoxResourceOpener(app_name,app_key,app_secret,accessToken);
    }

    public DropboxLiquibaseStructureSaver(Connection connection, String app_name, String accessToken) throws DBException {
        super(connection);
        this.app_name = app_name;
        this.accessToken = accessToken;
        this.dropBoxResourceOpener=new DropBoxResourceOpener(app_name,accessToken);
    }

    @Override
    protected String pathToChangeSets() {
        return defaultPath;
    }

    @Override
    protected String getIdPrefix() {
        return prefix;
    }


    @Override
    protected ResourceAccessor getResourceAccessor() {
        return dropBoxResourceOpener;
    }

    @Override
    public String upload(ByteArrayOutputStream outputStream) throws UploadException {
      InputStream inputStream=new ByteArrayInputStream(outputStream.toByteArray());
        String fileName=generateFileName();
        dropBoxResourceOpener.upload(pathToChangeSets()+fileName,inputStream);
        logger.info("upload "+fileName+ " Success");
        return fileName;
    }

    private String generateFileName(){
        try {
            return  getIdPrefix()+workDatabase.getCatalog()+"_"+simpleDateFormat.format(new Date())+".xml";
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() throws ClearException {
        dropBoxResourceOpener.clearAll(pathToChangeSets());
    }

    public DropBoxResourceOpener getDropBoxResourceOpener() {
        return dropBoxResourceOpener;
    }
}
