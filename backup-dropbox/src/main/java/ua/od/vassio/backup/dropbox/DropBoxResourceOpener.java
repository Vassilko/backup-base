package ua.od.vassio.backup.dropbox;

import com.dropbox.core.*;
import liquibase.resource.ResourceAccessor;
import org.apache.commons.lang3.StringUtils;
import ua.od.vassio.backup.common.ResourceOpenerClear;
import ua.od.vassio.backup.common.ResourceOpenerUpload;
import ua.od.vassio.backup.common.exception.ClearException;
import ua.od.vassio.backup.common.exception.UploadException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: vassio
 * Date: 27.07.14
 * Time: 11:37
 */
public class DropBoxResourceOpener implements ResourceAccessor, ResourceOpenerUpload, ResourceOpenerClear {

//    private String app_key;
//    private String app_secret;
    private String app_name;
    private String accessToken;

    public DropBoxResourceOpener(DbxClient client) {
        this.client = client;
    }

//    public DropBoxResourceOpener(String app_name, String app_key, String app_secret, String accessToken) {
//        this.app_name = app_name;
//        this.app_key = app_key;
//        this.app_secret = app_secret;
//        this.accessToken = accessToken;
//        init();
//    }

    public DropBoxResourceOpener(String app_name, String accessToken) {
        this.app_name = app_name;
        this.accessToken = accessToken;
        init();
    }

    DbxClient client;

    public void init() {
        DbxRequestConfig config = new DbxRequestConfig(
                app_name, Locale.getDefault().toString());
        try {
            client = new DbxClient(config, accessToken);
            System.out.println("Linked account: " + client.getAccountInfo().displayName);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }


    @Override
    public Set<InputStream> getResourcesAsStream(String path) throws IOException {
        if (client == null) {
            throw new IllegalAccessError("DropBoxResourceOpener must be init first");
        }

        Set<InputStream> inputStreams = new HashSet<InputStream>();

        try {
            DbxEntry dbxEntry = client.getMetadata(path);
            if (client.getMetadata(path).isFolder()) {
                DbxEntry.WithChildren listing = client.getMetadataWithChildren(path);
                for (DbxEntry child : listing.children) {
                    if (child.isFile()) {
                        inputStreams.add(getInputStreamFromFile(child));
                    }
                }
            } else {
                inputStreams.add(getInputStreamFromFile(dbxEntry));
            }

        } catch (DbxException ex) {
            throw new RuntimeException(ex);
        }

        return inputStreams;
    }

    private InputStream getInputStreamFromFile(DbxEntry file) throws IOException, DbxException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            DbxEntry.File downloadedFile = client.getFile(file.path, null,
                    byteArrayOutputStream);
            System.out.println("Metadata: " + downloadedFile.toString());
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } finally {
            byteArrayOutputStream.close();
        }

    }

    @Override
    public Set<String> list(String relativeTo, String path, boolean includeFiles, boolean includeDirectories, boolean recursive) throws IOException {
        if (!StringUtils.startsWith(path, "/")) {
            path = "/" + path;
        }
        if (client == null) {
            throw new IllegalAccessError("DropBoxResourceOpener must be init first");
        }
        Set<String> list = new HashSet<>();
        try {
            DbxEntry dbxEntry = client.getMetadata(path);
            if (client.getMetadata(path).isFolder()) {
                DbxEntry.WithChildren listing = client.getMetadataWithChildren(path);
                for (DbxEntry child : listing.children) {
                    if (child.isFile()) {
                        list.add(child.path);
                    }
                }
            } else {
                list.add(dbxEntry.path);
            }

        } catch (DbxException ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }

    @Override
    public ClassLoader toClassLoader() {
        return this.getClass().getClassLoader();
    }


    @Override
    public void upload(String fileName, InputStream inputStream) throws UploadException {
        try {
            DbxEntry.File uploadedFile = client.uploadFile(fileName,
                    DbxWriteMode.add(), -1, inputStream);
            System.out.println("Uploaded: " + uploadedFile.toString());
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception ex) {
            throw new UploadException(ex);
        }

    }

    @Override
    public void clearAll(String path) throws ClearException {
        if (!StringUtils.startsWith(path, "/")) {
            path = "/" + path;
        }
        try {
            DbxEntry.WithChildren listing = client.getMetadataWithChildren(path);
            for (DbxEntry child : listing.children) {
                if (child.isFile()) {
                    client.delete(child.path);
                }
            }
        } catch (Exception ex) {
            throw new ClearException(ex);
        }


    }
}
