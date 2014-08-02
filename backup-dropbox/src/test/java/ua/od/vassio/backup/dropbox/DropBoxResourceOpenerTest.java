package ua.od.vassio.backup.dropbox;

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

    public static final String APP_KEY = "kuebk1pz38bbg75";
    public static final String APP_SECRET = "dp3u216fym0wsgz";
    public static final String ACCESS_TOKEN ="EtOGckaNVbQAAAAAAAAAvAA7cf8x-3aWvhIJO-IHeIA9Xu49pR4tliFrwxBnAJue";
    public static final String APP_NAME="backup-base-test2";

    private DropBoxResourceOpener dropBoxResourceOpener = new DropBoxResourceOpener(APP_NAME,APP_KEY,APP_SECRET,ACCESS_TOKEN);

    @Test
    public void testReadFile() throws IOException {
        Set<String> strings = dropBoxResourceOpener.list("", "", false, false, false);
        assertNotNull(strings);
        assertEquals(strings.size(), 1);
        assertEquals(strings.iterator().next(), "/test.xml");
    }
}
