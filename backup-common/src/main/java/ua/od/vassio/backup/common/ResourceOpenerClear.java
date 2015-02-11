package ua.od.vassio.backup.common;

import ua.od.vassio.backup.common.exception.ClearException;

/**
 * Created with IntelliJ IDEA.
 * User: vassio
 * Date: 02.08.14
 * Time: 13:15
 */
public interface ResourceOpenerClear { //NOPMD
    public void clearAll(String path) throws ClearException; //NOPMD
}
