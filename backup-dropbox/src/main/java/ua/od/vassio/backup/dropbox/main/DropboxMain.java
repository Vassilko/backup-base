package ua.od.vassio.backup.dropbox.main;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import ua.od.vassio.backup.dropbox.DropboxLiquibaseStructureSaver;
import ua.od.vassio.common.args.ArgumentsHelper;

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
    private static String APP_NAME=DropboxMain.class.getSimpleName();
    public static void main(String args[]) throws Exception {
        CommandLine commandLine=ArgumentsHelper.getCommandLine(args,DropboxArgumentAction.class);
        //read command line parameters
        String connectionString= ArgumentsHelper.getArgument(args,DropboxArgumentAction.CONNECTION_STRING,commandLine,String.class);
        String accessToken= ArgumentsHelper.getArgument(args,DropboxArgumentAction.ACCESS_TOKEN,commandLine,String.class);
        String appName=ArgumentsHelper.getArgument(args,DropboxArgumentAction.APP_NAME,commandLine,String.class);
        String dropBoxActionString=ArgumentsHelper.getArgument(args,DropboxArgumentAction.ACTION,commandLine,String.class);
        DropBoxAction dropBoxAction=EnumUtils.getEnum(DropBoxAction.class,dropBoxActionString);
        String fileName=ArgumentsHelper.getArgument(args,DropboxArgumentAction.FILENAME,commandLine,String.class);
        //validate parameters
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
            appName= APP_NAME;
        }

        //getConnection
        Connection connection=getConnection(connectionString);
        //init logic
        DropboxLiquibaseStructureSaver dropboxLiquibaseStructureSaver=new DropboxLiquibaseStructureSaver(connection,appName,accessToken);
        dropboxLiquibaseStructureSaver.init();
        //start logic
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
    }

    private static Connection getConnection(String connectionString) throws SQLException {
        Connection connection= DriverManager
                .getConnection(connectionString);
        return connection;
    };
}
