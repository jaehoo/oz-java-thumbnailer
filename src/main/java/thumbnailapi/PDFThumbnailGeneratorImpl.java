package thumbnailapi;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import org.apache.pdfbox.PrintPDF;
import org.apache.pdfbox.io.RandomAccessFileInputStream;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * Generate thumbnails for pdfs
 */
public class PDFThumbnailGeneratorImpl extends AbstractThumbnailGenerator implements ThumbnailGenerator {

    private static final Logger logger= LoggerFactory.getLogger(PDFThumbnailGeneratorImpl.class);

    /**
     * Create the thumbnail.  The thumbnail should always save as a JPEG file.
     *
     * @param inputStream	  The source data.
     * @param fileOut		  The output file.
     * @param largestDimension The max width and height.  The generator should size the thumbnail so
     *                         that the width and height both stay within this limit.
     * @param hint			 Optional hint that was returned from the prior thumbnail generation
     *                         on this same file, null if none was returned or if this is the first
     *                         thumbnail in this context.
     * @return an optional hint object that will be passed to subsequent thumbnail generation calls
     *         for this same source data.  Return null if you don't use hints, otherwise return some
     *         object which allows you to communicate extra information to the next round, such as
     *         the scaled image already loaded.
     */
    public Object createThumbnail(InputStream inputStream
            , File fileOut
            , int largestDimension
            , Object hint) throws IOException {

        byte[] fileBytes =IOUtils.toByteArray(inputStream);

        /*RandomAccessFile raf = new RandomAccessFile(pdfFile, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());*/
        ByteBuffer buf=ByteBuffer.allocate(fileBytes.length);
        buf.put(fileBytes);

        PDFFile pdf = new PDFFile(buf);
        PDFPage page = pdf.getPage(0);

        logger.debug("Page size [w,h] {}x{}", page.getWidth(), page.getHeight());
        logger.debug("Page size [w,h] {}x{}", page.getBBox().getWidth(), page.getBBox().getHeight());

        double percent=( largestDimension/page.getHeight() );
        int height= ( int )  page.getHeight();//largestDimension;
        int width= ( int )  page.getWidth() ;//int) (page.getWidth()*percent);

        int sHeight=(int) ( height * percent );
        int sWidth=(int) ( width * percent );

        logger.debug("Scale Percent:{}", percent);
        logger.debug("Scale Page to {}x{}, ", sWidth, sHeight);

        // create the image
        Rectangle rect = new Rectangle(0, 0, width, height);

        BufferedImage bufferedImage = new BufferedImage(sWidth, sHeight, // Image canvas width & height
                BufferedImage.TYPE_INT_RGB);

        Image image = page.getImage(rect.width, rect.height,    // image layer width & height
                rect,                       // clip rect
                null,                       // null for the ImageObserver
                true,                       // fill background with white
                true                        // block until drawing is done
        );
        Graphics2D bufImageGraphics = bufferedImage.createGraphics();
        bufImageGraphics.scale(percent,percent);
        bufImageGraphics.drawImage(image, 0, 0, null);
        bufImageGraphics.dispose();

        //generate the image
        Image img = page.getImage(
                rect.width, rect.height, //width & height
                rect, // clip rect
                null, // null for the ImageObserver
                true, // fill background with white
                true  // block until drawing is done
        );


        if(defaultSave){
            saveImageToFormatName(bufferedImage, "png", fileOut);
        }

        return img;

    }

    @Override
    public Object createThumbnail(InputStream inputStream
            , File fileOut
            , String generatedType
            , int largestDimension
            , Object hint) throws IOException {

        byte[] fileBytes = null;

        Image imageIn;

        double percent;
        int height;
        int width;

        Image image = null;
        Rectangle rect = null;

        if (hint instanceof Image) {
            imageIn = (Image) hint;


            imageIn.getHeight(null);
            imageIn.getWidth(null);

            height= imageIn.getHeight(null);//largestDimension;
            width= imageIn.getWidth(null) ;//int) (page.getWidth()*percent);

           percent= (double)largestDimension/(double)height;

            rect = new Rectangle(0, 0, width, height);

            image=imageIn;

        }else{
            fileBytes =IOUtils.toByteArray(inputStream);


            /*RandomAccessFile raf = new RandomAccessFile(pdfFile, "r");
                     FileChannel channel = raf.getChannel();
                    ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());*/
            ByteBuffer buf=ByteBuffer.allocateDirect(fileBytes.length);
            buf.put(fileBytes);

            PDFFile pdf = new PDFFile(buf);
            PDFPage page = pdf.getPage(0);


            logger.debug("Page size [w,h] {}x{}", page.getWidth(), page.getHeight());
            //logger.debug("Page size [w,h] {}x{}", page.getBBox().getWidth(), page.getBBox().getHeight());

            height= ( int )  page.getHeight();//largestDimension;
            width= ( int )  page.getWidth() ;//int) (page.getWidth()*percent);

            percent= (double)largestDimension/(double)height;

            rect = new Rectangle(0, 0, width, height);

            image = page.getImage(rect.width, rect.height,    // image layer width & height
                rect,                       // clip rect
                null,                       // null for the ImageObserver
                true,                       // fill background with white
                true                        // block until drawing is done
            );

        }

        int sHeight=(int) ( height * percent );
        int sWidth=(int) ( width * percent );

        logger.debug("Scale Percent:{}", percent);
        logger.debug("Scale Page to {}x{}, ", sWidth, sHeight);

        // create the image

        AffineTransform tx;

        BufferedImage bufferedImage = new BufferedImage(sWidth, sHeight, // Image canvas width & height
                BufferedImage.TYPE_INT_RGB);

        Graphics2D bufImageGraphics = bufferedImage.createGraphics();

        tx = new AffineTransform();
        tx.scale(percent, percent);

        bufImageGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        //bufImageGraphics.drawImage(image, 0, 0, null);
        bufImageGraphics.drawImage(image, tx, null);
        bufImageGraphics.dispose();

        if(defaultSave){
            saveImageToFormatName(bufferedImage, generatedType, fileOut);
        }

        return image;
    }
}
