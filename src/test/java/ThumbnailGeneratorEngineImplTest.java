import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.Resource;
import thumbnailapi.ThumbnailGeneratorEngine;
import util.AbstractJUnitTest;

import java.io.*;

/**
 * Date: 18/03/12
 * Time: 04:22 PM
 *
 * @web www.orbitalzero.com , www.orbitalzero.org
 * @author <a href="mailto:jaehoo@gmail.com">Lic. José Alberto Sánchez</a>
 */
public class ThumbnailGeneratorEngineImplTest extends AbstractJUnitTest {


//    @Test
//    public void testTumbnails() throws FileNotFoundException {
//
//        ThumbnailGeneratorEngine tge=(ThumbnailGeneratorEngine)
//                applicationContext.getBean("thumbnailGeneratorEngine");
//
//
//        File file= new File("/home/alberto/oz.png");
//
//        tge.generateThumbnails("tb_", new FileInputStream(file),"image/png");
//
//
//    }

    private ThumbnailGeneratorEngine tge;

    @Before
    public void setUp(){
       tge=(ThumbnailGeneratorEngine) applicationContext.getBean("thumbnailGeneratorEngine");
    }

    @Test
    public void testGenerateThumbnailsFromPdf() throws IOException {

        Resource pdf=applicationContext.getResource("file:src/test/resources/files/html5.pdf");

        tge.generateThumbnails("tb_","image/jpg", pdf.getInputStream());
    }

    @Test
    public void testGenerateThumbnailsFromImage() throws IOException {

        Resource png=applicationContext.getResource("file:src/test/resources/files/oz.png");

        tge.generateThumbnails("tb_","image/png", png.getInputStream());

    }

}
