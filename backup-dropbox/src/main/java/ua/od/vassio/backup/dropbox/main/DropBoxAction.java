package ua.od.vassio.backup.dropbox.main;

import java.util.Objects;

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

    private String internalName;
    private String description;

    DropBoxAction(String internalName, String description) {
        this.internalName = internalName;
        this.description = description;
    }

    public String getInternalName() {
        return internalName;
    }

    public String getDescription() {
        return description;
    }

    public static  String getDescriptions(){
        String ret="";
        for (DropBoxAction dropBoxAction:DropBoxAction.values()){
           ret+=dropBoxAction.getInternalName()+":  "+dropBoxAction.getDescription()+"\n";
        }
        return ret;
    }

    public static DropBoxAction getValue(String text){
        for (DropBoxAction dropBoxAction : values()) {
            if (Objects.equals(dropBoxAction.name(),text)){
                return dropBoxAction;
            }
            if (Objects.equals(dropBoxAction.getInternalName(),text)){
                return dropBoxAction;
            }
        }
        return null;
    }
}
