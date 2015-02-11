package ua.od.vassio.backup.common;

import ua.od.vassio.backup.common.exception.DBException;

/**
 * Created by vzakharchenko on 24.07.14.
 */
public interface WorkDatabase<DATABASE> {
    /**
     * get Current Database from DataSource. Use cache for resolve
     */
    public DATABASE getCurrentDatabase() throws DBException;   //NOPMD

    /**
     * get Default Schema Name
     *
     * @return
     */
    public String getSchema() throws DBException;   //NOPMD

    /**
     * get Default Catalog Name
     *
     * @return
     */
    public String getCatalog() throws DBException;  //NOPMD

    public int getMaxConstraintLength();  //NOPMD

}