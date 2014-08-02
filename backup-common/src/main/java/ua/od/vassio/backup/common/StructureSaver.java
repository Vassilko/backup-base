package ua.od.vassio.backup.common;

import ua.od.vassio.backup.common.exception.ClearException;
import ua.od.vassio.backup.common.exception.DBException;
import ua.od.vassio.backup.common.exception.DropAllException;
import ua.od.vassio.backup.common.exception.UpdateException;

/**
 * Created by vzakharchenko on 24.07.14.
 */
public interface StructureSaver {
    /**
     *  Save changeSet
     * @return filename
     * @throws DBException
     */
    public String save() throws DBException;

    /**
     * Clear all changeSets
     * @throws ClearException
     */
    public void clear() throws ClearException;

    /**
     * update using file or path
     * @param fileName
     * @throws UpdateException
     */
    public void update(String fileName) throws UpdateException;

    /**
     * drop all object in database
     */
    public void dropAll() throws DropAllException;
}
