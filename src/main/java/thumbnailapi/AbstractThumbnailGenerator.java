package thumbnailapi;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Class providing convenience method for abstract thumbnail generators
 */
public abstract class AbstractThumbnailGenerator {

    private final static Logger log = Logger.getLogger(AbstractThumbnailGenerator.class);

    protected boolean defaultSave;

    public void setDefaultSave(boolean defaultSave) {
        this.defaultSave = defaultSave;
    }

    /**
     * Save an image as a JPEG file on disk.
     *
     * @param image     The raw image to save.
     * @param fileOut   The location where you want to save the file.
     *
     * @return true if successful, false if unsuccessful.
     * @throws java.io.IOException if something goes wrong closing the stream
     */
    protected boolean saveImageAsJPEG(BufferedImage image, File fileOut) throws IOException {
        OutputStream streamOut = null;
        boolean      bSuccess = false;

        try {
            streamOut = new FileOutputStream(fileOut);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(streamOut);
            encoder.encode(image);
            bSuccess = true;
        }
        catch (Throwable t) {
            log.warn("Files.saveImageAsJPEG(" + fileOut + "): " + t, t);
        }
        finally {
            if (streamOut != null) {
                streamOut.close();
            }
        }

        return bSuccess;
    }

    /**
     * <p>Save image with indicated format into a file</p>
     *
     * @param image The raw image to save.
     * @param imageType Type of image to save (png, jpg, tiff, bmp, jpeg)
     * @param fileOut The location where you want to save the file.
     *
     * @return  <tt>true</tt> if successful, <tt>false</tt> if unsuccessful
     * @throws IOException something goes wrong closing the stream
     */
    protected boolean saveImageToFormatName(BufferedImage image,final String imageType, File fileOut) throws IOException{

        return ImageIO.write(image, imageType, fileOut);

    }


}
