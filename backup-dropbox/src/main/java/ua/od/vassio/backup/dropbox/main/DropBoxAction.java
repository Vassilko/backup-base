package ua.od.vassio.backup.dropbox.main;

/**
 * Created with IntelliJ IDEA.
 * User: vassio
 * Date: 02.08.14
 * Time: 15:58
 */
public enum DropBoxAction {
    SAVE("save","save data to dropbox"),
    UPDATE("update","update database"),
    DROP("drop","dropAll objects in database"),
    CLEAR("clear","clear all ChangeSets");

    private String name;
    private String description;

    DropBoxAction(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static  String getDescriptions(){
        String ret="";
        for (DropBoxAction dropBoxAction:DropBoxAction.values()){
           ret+=dropBoxAction.getName()+":  "+dropBoxAction.getDescription()+"\n";
        }
        return ret;
    }
}
