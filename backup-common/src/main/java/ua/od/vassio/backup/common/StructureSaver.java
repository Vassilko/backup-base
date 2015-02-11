package ua.od.vassio.backup.common;

import ua.od.vassio.backup.common.exception.ClearException;
import ua.od.vassio.backup.common.exception.DBException;
import ua.od.vassio.backup.common.exception.DropAllException;
import ua.od.vassio.backup.common.exception.UpdateException;

/**
 * Created by vzakharchenko on 24.07.14.
 */
public interface StructureSaver {  //NOPMD
    /**
     * Save changeSet
     *
     * @return filename
     * @throws DBException
     */
    public String save() throws DBException; //NOPMD

    /**
     * Clear all changeSets
     *
     * @throws ClearException
     */
    public void clear() throws ClearException;   //NOPMD

    /**
     * update using file or path
     *
     * @param fileName
     * @throws UpdateException
     */
    public void update(String fileName) throws UpdateException; //NOPMD

    /**
     * drop all object in database
     */
    public void dropAll() throws DropAllException;  //NOPMD
}
