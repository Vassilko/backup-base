package ua.od.vassio.backup.common;

import ua.od.vassio.backup.common.exception.UploadException;

import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: vassio
 * Date: 02.08.14
 * Time: 12:46
 */
public interface ResourceOpenerUpload {
    public void upload(String fileName, InputStream inputStream) throws UploadException;
}
