package thumbnailapi;

import org.junit.Test;
import util.AbstractJUnitTest;

import java.io.File;
import java.io.FileInputStream;

/**
 * Date: 18/03/12
 * Time: 08:44 PM
 *
 * @web www.orbitalzero.com , www.orbitalzero.org
 * @autor <a href="mailto:jaehoo@gmail.com">Lic. José Alberto Sánchez</a>
 */
public class PDFThumbnailGeneratorImplTest extends AbstractJUnitTest {


    @Test
    public void testCreateThumbnail() throws Exception {


        ThumbnailGenerator gen= (ThumbnailGenerator) applicationContext.getBean("pdfThumbnailGenerator");


        File file= new File("/home/alberto/tmp/pdfdoc.pdf");
        File fileOut= new File("/home/alberto/tmp/pdfout.png");
        gen.createThumbnail(new FileInputStream(file),fileOut,"png",400, null);

    }
}
