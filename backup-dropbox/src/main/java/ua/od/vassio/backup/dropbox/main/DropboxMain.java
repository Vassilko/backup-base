package ua.od.vassio.backup.dropbox.main;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.vassio.backup.dropbox.DropboxLiquibaseStructureSaver;
import ua.od.vassio.common.args.ArgumentsHelper;
import ua.od.vassio.jaxrslog.client.LogBackInitializer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: vassio
 * Date: 02.08.14
 * Time: 15:39
 */
public class DropboxMain {
    private static Logger logger= LoggerFactory.getLogger(DropboxMain.class);

    private static String APP_NAME=DropboxMain.class.getSimpleName();

    public static void main(String args[]) throws Exception {
        LogBackInitializer.initialization(DropboxMain.class.getClassLoader().getResourceAsStream("logback.xml"));
        CommandLine commandLine=ArgumentsHelper.getCommandLine(args,DropboxArgumentAction.class);
        logger.info("read command line parameters");
        String connectionString= ArgumentsHelper.getArgument(args,DropboxArgumentAction.CONNECTION_STRING,commandLine,String.class);
        String accessToken= ArgumentsHelper.getArgument(args,DropboxArgumentAction.ACCESS_TOKEN,commandLine,String.class);
        String appName=ArgumentsHelper.getArgument(args,DropboxArgumentAction.APP_NAME,commandLine,String.class);
        String dropBoxActionString=ArgumentsHelper.getArgument(args,DropboxArgumentAction.ACTION,commandLine,String.class);
        DropBoxAction dropBoxAction=EnumUtils.getEnum(DropBoxAction.class,dropBoxActionString);
        String fileName=ArgumentsHelper.getArgument(args,DropboxArgumentAction.FILENAME,commandLine,String.class);

        logger.info("validate parameters");
        if (StringUtils.isEmpty(connectionString)){
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(DropboxMain.class.getSimpleName(), ArgumentsHelper.buildOptions(DropboxArgumentAction.class));
           throw new IllegalArgumentException("connectionString is empty");
        }
        if (StringUtils.isEmpty(accessToken)){
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(DropboxMain.class.getSimpleName(), ArgumentsHelper.buildOptions(DropboxArgumentAction.class));
            throw new IllegalArgumentException("accessToken is empty");
        }
        if (dropBoxAction==null){
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(DropboxMain.class.getSimpleName(), ArgumentsHelper.buildOptions(DropboxArgumentAction.class));
            throw new IllegalArgumentException("Action is empty");
        }
        if (dropBoxAction==DropBoxAction.UPDATE ){
            if (StringUtils.isEmpty(fileName)){
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp(DropboxMain.class.getSimpleName(), ArgumentsHelper.buildOptions(DropboxArgumentAction.class));
                throw new IllegalArgumentException("fileName is empty");
            }
        }
        if (StringUtils.isEmpty(appName)){
            logger.warn("Applicatioon Name is Empty use " + APP_NAME);
            appName= APP_NAME;
        }

        //getConnection
        logger.info("getConnection");
        Connection connection=getConnection(connectionString);
        //init logic
        logger.info("init logic");
        DropboxLiquibaseStructureSaver dropboxLiquibaseStructureSaver=new DropboxLiquibaseStructureSaver(connection,appName,accessToken);
        dropboxLiquibaseStructureSaver.init();
        //start logic
        logger.info("start logic "+dropBoxAction);
        if (dropBoxAction==DropBoxAction.SAVE){
            dropboxLiquibaseStructureSaver.save();
        }
        if (dropBoxAction==DropBoxAction.DROP){
            dropboxLiquibaseStructureSaver.dropAll();
        }
        if (dropBoxAction==DropBoxAction.CLEAR){
            dropboxLiquibaseStructureSaver.dropAll();
        }
        if (dropBoxAction==DropBoxAction.UPDATE){
            dropboxLiquibaseStructureSaver.update(fileName);
        }
        logger.info("finish Successfully");
    }

    private static Connection getConnection(String connectionString) throws SQLException {
        Connection connection= DriverManager
                .getConnection(connectionString);
        return connection;
    };
}
