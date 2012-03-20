package util;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Date: 18/03/12
 * Time: 07:06 PM
 *
 * @web www.orbitalzero.com , www.orbitalzero.org
 * @autor <a href="mailto:jaehoo@gmail.com">Lic. José Alberto Sánchez</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public abstract class AbstractJUnitTest extends AbstractJUnit4SpringContextTests {

}
