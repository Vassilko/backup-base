package ua.od.vassio.backup.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ua.od.vassio.backup.common.exception.DBException;
import ua.od.vassio.backup.common.exception.UpdateException;

/**
 * Created with IntelliJ IDEA.
 * User: vassio
 * Date: 13.04.15
 * Time: 8:42
 */
@ContextConfiguration(
        locations = "/test-spring-dao-context.xml")
public class SpringStructureSaverTest extends AbstractTestNGSpringContextTests {
    @Autowired
    SpringStructureSaver springStructureSaver;

    @BeforeMethod
    public void before() throws UpdateException, DBException {
        springStructureSaver.init();
        springStructureSaver.update("*");
    }

    @Test
    public void saveTest() throws DBException {
        springStructureSaver.save();
    }
}
