package ua.od.vassio.backup.dropbox;

import com.dropbox.core.*;
import liquibase.resource.ResourceAccessor;

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
public class DropBoxResourceOpener implements ResourceAccessor {

    private String app_key ;
    private String app_secret;
    private String app_name;

    public DropBoxResourceOpener(String app_name) {
        this.app_name = app_name;
    }

    public DropBoxResourceOpener(String app_key, String app_secret, String app_name) {
        this.app_key = app_key;
        this.app_secret = app_secret;
        this.app_name = app_name;
        init();
    }

    DbxClient client;

    public void init() {
        DbxAppInfo appInfo = new DbxAppInfo(app_key, app_secret);
        DbxRequestConfig config = new DbxRequestConfig(
                "DropBoxResourceOpener/1.0", Locale.getDefault().toString());
        DbxWebAuth webAuth = new DbxWebAuth(config, appInfo,);
        webAuth.start();
            String authorizeUrl = webAuth.;
        try{
            String authorizationCode;
            DbxAuthFinish authFinish = webAuth.finish(authorizationCode);
            String accessToken = authFinish.accessToken;
            client = new DbxClient(config, accessToken);
            System.out.println("Linked account: " + client.getAccountInfo().displayName);
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }

    }




    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public void setApp_secret(String app_secret) {
        this.app_secret = app_secret;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    @Override
    public Set<InputStream> getResourcesAsStream(String path) throws IOException {
        if (client==null){
            throw new IllegalAccessError("DropBoxResourceOpener must be init first");
        }

        Set<InputStream> inputStreams=new HashSet<InputStream>();

        try{
            DbxEntry.WithChildren listing = client.getMetadataWithChildren(path);
            for (DbxEntry child : listing.children) {
                  if (child.isFile()){
                      inputStreams.add(getInputStreamFromFile(child));
                  }
             }
        } catch (DbxException ex){
            throw new RuntimeException(ex);
        }

        return inputStreams;
    }

    private InputStream getInputStreamFromFile(DbxEntry file) throws IOException, DbxException {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
         try{
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
        if (client==null){
            throw new IllegalAccessError("DropBoxResourceOpener must be init first");
        }
        Set<String> list=new HashSet<>() ;
        try{
            DbxEntry.WithChildren listing = client.getMetadataWithChildren(path);
            for (DbxEntry child : listing.children) {
                if (child.isFile()){
                    list.add(child.path);
                }
            }
        } catch (DbxException ex){
            throw new RuntimeException(ex);
        }
        return list;
    }

    @Override
    public ClassLoader toClassLoader() {
        return this.getClass().getClassLoader();
    }


}
