package ua.od.vassio.backup.dropbox;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ua.od.vassio.backup.common.exception.*;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.testng.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vassio
 * Date: 02.08.14
 * Time: 10:45
 */
public class DropboxLiquibaseStructureSaverTest {
    public static final String ACCESS_TOKEN = "EtOGckaNVbQAAAAAAAAAvZ-z4-GnYOCrAuvN2pcugHNv4tATxHGJnaCYbq92egn_";
    public static final String APP_NAME = "backup-test3";
    public static String CONNECTION_STRING = "jdbc:mysql://localhost:3306/messager?useUnicode=true&characterEncoding=utf8&user=test&password=test";
    DropboxLiquibaseStructureSaver dropboxLiquibaseStructureSaver;

    @BeforeMethod
    public void clearDirectory() throws SQLException, DBException, ClearException, DropAllException {
        dropboxLiquibaseStructureSaver = new DropboxLiquibaseStructureSaver(getConnection(), APP_NAME, ACCESS_TOKEN);
        dropboxLiquibaseStructureSaver.init();
        dropboxLiquibaseStructureSaver.clear();
        dropboxLiquibaseStructureSaver.dropAll();
    }

    @Test(enabled = false)
    public void testSaveData() throws DBException, SQLException {
        dropboxLiquibaseStructureSaver.save();
    }

    @Test(enabled = false)
    public void testLoadData() throws DBException, SQLException, IOException, UploadException, UpdateException {
        DropBoxResourceOpener dropBoxResourceOpener = dropboxLiquibaseStructureSaver.getDropBoxResourceOpener();
        String fileName = "DropBox_messager_02082014_132603.xml";
        dropBoxResourceOpener.upload("/" + fileName, FileUtils.openInputStream(new File("src/test/java/ua/od/vassio/backup/dropbox/" + fileName)));
        dropboxLiquibaseStructureSaver.update(fileName);
        assertNotNull(fileName);

    }

    private Connection getConnection() throws SQLException {
        Connection connection = DriverManager
                .getConnection(CONNECTION_STRING);
        return connection;
    }

    ;
}
