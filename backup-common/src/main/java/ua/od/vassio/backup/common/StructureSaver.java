package ua.od.vassio.backup.common;

import ua.od.vassio.backup.common.exception.DBException;

/**
 * Created by vzakharchenko on 24.07.14.
 */
public interface StructureSaver {
    public void save() throws DBException;
    public void update() throws DBException;
    public void deploy();
}
