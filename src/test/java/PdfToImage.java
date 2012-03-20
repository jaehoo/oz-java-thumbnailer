import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Date: 18/03/12
 * Time: 03:55 PM
 *
 * @web www.orbitalzero.com , www.orbitalzero.org
 * @autor <a href="mailto:jaehoo@gmail.com">Lic. José Alberto Sánchez</a>
 */
public class PdfToImage {


    public static void main(String[] args) throws IOException {

        File pdfFile = new File("/home/alberto/spring-data-neo4j-tutorial.pdf");
        RandomAccessFile raf = new RandomAccessFile(pdfFile, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        PDFFile pdf = new PDFFile(buf);
        PDFPage page = pdf.getPage(0);

// create the image
        Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(),
                (int) page.getBBox().getHeight());
        BufferedImage bufferedImage = new BufferedImage(rect.width, rect.height,
                BufferedImage.TYPE_INT_RGB);

        Image image = page.getImage(rect.width, rect.height,    // width & height
                rect,                       // clip rect
                null,                       // null for the ImageObserver
                true,                       // fill background with white
                true                        // block until drawing is done
        );
        Graphics2D bufImageGraphics = bufferedImage.createGraphics();
        bufImageGraphics.drawImage(image, 0, 0, null);

//generate the image
        Image img = page.getImage(
                        rect.width, rect.height, //width & height
                        rect, // clip rect
                        null, // null for the ImageObserver
                        true, // fill background with white
                        true  // block until drawing is done
                        );


        ImageIO.write(bufferedImage,"jpg" , new File("/home/alberto/image.jpg"));

    }
}
