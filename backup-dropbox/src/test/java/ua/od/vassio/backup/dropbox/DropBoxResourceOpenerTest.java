package ua.od.vassio.backup.dropbox;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vassio
 * Date: 27.07.14
 * Time: 12:31
 */
public class DropBoxResourceOpenerTest {
    DropBoxResourceOpener dropBoxResourceOpener=new DropBoxResourceOpener("backup-base-test2");
           final String APP_KEY = "kuebk1pz38bbg75";
        final String APP_SECRET = "dp3u216fym0wsgz";

    @BeforeMethod
    public void initDropBoxResourceOpener(){
        dropBoxResourceOpener.setApp_key(APP_KEY);
        dropBoxResourceOpener.setApp_secret(APP_SECRET);
        dropBoxResourceOpener.setAccessToken("EtOGckaNVbQAAAAAAAAAvAA7cf8x-3aWvhIJO-IHeIA9Xu49pR4tliFrwxBnAJue");
        dropBoxResourceOpener.init();
    }
    @Test
    public void testReadFile() throws IOException {
        Set<String> strings= dropBoxResourceOpener.list("", "", false, false, false);
         assertNotNull(strings);
        assertEquals(strings.size(),1);
        assertEquals(strings.iterator().next(),"/test.xml");
    }
}
