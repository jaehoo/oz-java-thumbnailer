import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.WriteOutContentHandler;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.ContentHandler;

/**
 * Date: 18/03/12
 * Time: 01:46 PM
 *
 * @web www.orbitalzero.com , www.orbitalzero.org
 * @autor <a href="mailto:jaehoo@gmail.com">Lic. José Alberto Sánchez</a>
 */

public class TestTika{

    private static final Logger logger= LoggerFactory.getLogger(TestTika.class);

    @Test
    public void tikeHelloWorld() throws IOException, TikaException, SAXException {


        Tika tika = new Tika();
        Metadata metadata = new Metadata();

        File file = new File("/home/alberto/oz.png");

        FileInputStream fis= new FileInputStream(file);

        String type= tika.detect(file);
        String text = tika.parseToString(fis,metadata);

        logger.info("File exists: {}",file.exists());
        logger.info("File type:{}", type);
        logger.info("Metadata:{}", metadata);
        logger.info("Parse to String:\n{}", text);

        Parser parser = new AutoDetectParser();

        StringWriter writer = new StringWriter();

        parser.parse(new FileInputStream(file)
                , new WriteOutContentHandler(writer)
                , metadata
                , new ParseContext());
        String content = writer.toString();
        //System.out.println(metadata);

//        InputStream input = new FileInputStream(file);
//               BodyContentHandler handler = new BodyContentHandler();
//
//               Metadata metadata = new Metadata();
//               new HtmlParser().parse(input, handler, metadata, new ParseContext());
//               String plainText = handler.toString();
//
//        System.out.println(plainText);


    }

}
