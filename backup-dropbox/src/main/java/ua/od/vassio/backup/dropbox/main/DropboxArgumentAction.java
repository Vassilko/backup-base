package ua.od.vassio.backup.dropbox.main;

import ua.od.vassio.common.args.EnumInterface;

/**
 * Created with IntelliJ IDEA.
 * User: vassio
 * Date: 02.08.14
 * Time: 15:41
 */
public enum DropboxArgumentAction implements EnumInterface {
    CONNECTION_STRING("connection","JDBC Connection String",true),
    ACCESS_TOKEN("accessToken","access token can be used to access dropbox accoun via the API",true),
    APP_NAME("appName","dropbox application name",true),
    ACTION("action","action must be \n"+DropBoxAction.getDescriptions(),true),
    FILENAME("fileName","uses with UPDATE",true);
    private String name;
    private String description;
    private boolean hasArgs;

    DropboxArgumentAction(String name, String description, boolean hasArgs) {
        this.name = name;
        this.description = description;
        this.hasArgs = hasArgs;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean hasArgs() {
        return hasArgs;
    }


}
