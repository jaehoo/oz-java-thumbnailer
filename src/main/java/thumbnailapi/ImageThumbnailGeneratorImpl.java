package thumbnailapi;


import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;


/**
 * Generate thumbnails for images.
 */
public class ImageThumbnailGeneratorImpl extends AbstractThumbnailGenerator implements ThumbnailGenerator {

    private final static Logger log = LoggerFactory.getLogger(ImageThumbnailGeneratorImpl.class);

    private java.util.List<String> alphaTranslucentFormats;

    public void setAlphaTranslucentFormats(List<String> alphaTranslucentFormats) {
        this.alphaTranslucentFormats = alphaTranslucentFormats;
    }

    public Object createThumbnail(InputStream inputStream, File fileOut, int largestDimension, Object hint) throws IOException {

        // What's the base image that we are starting with?  If there's a hint, that's the scaled image
        // from the last time around, use that... (since we know we always iterate downwards in scale)
        Image imageIn;
        if (hint instanceof Image) {
            imageIn = (Image) hint;
            log.info("createThumbnail(" + fileOut + ") reusing prior result image...");
        }
        else {
            log.info("createThumbnail(" + fileOut + ") reading image from stream " + inputStream);
            imageIn = ImageIO.read(inputStream);
        }

        if (imageIn == null) {
            log.warn("Could not read image file: " + inputStream);
            return hint;
        }

        BufferedImage imageOut = createThumbnailImage(imageIn, fileOut, largestDimension,false);

        // Save image
        if(defaultSave){
            saveImageToFormatName(imageOut, "png", fileOut);
            //saveImageAsJPEG(imageOut, fileOut);
        }

        // Return this image now as the hint for the next scaling iteration
        if (imageOut != null)
            hint = imageOut;

        return hint;
    }


    /**
     * Create a thumbnail image and save it to disk.
     *
     * This algorithm is based on:
     *      http://www.philreeve.com/java_high_quality_thumbnails.php
     *
     * @param imageIn           The image you want to scale.
     * @param fileOut           The output file.
     * @param largestDimension  The largest dimension, so that neither the width nor height
     *                          will exceed this value.
     *
     * @return the image that was created, null if imageIn or fileOut is null.
	 * @throws java.io.IOException if something goes wrong when saving as jpeg
     */
    public BufferedImage createThumbnailImage(Image imageIn
            , File fileOut
            , int largestDimension
            , boolean supportTranslucent) throws IOException {
        if ((imageIn == null) || (fileOut == null))
            return null;

        //it seems to not return the right size until the methods get called for the first time
        imageIn.getWidth(null);
        imageIn.getHeight(null);

        // Find biggest dimension
        int     nImageWidth = imageIn.getWidth(null);
        int     nImageHeight = imageIn.getHeight(null);
        int     nImageLargestDim = Math.max(nImageWidth, nImageHeight);
        double  scale = (double) largestDimension / (double) nImageLargestDim;
        int     sizeDifference = nImageLargestDim - largestDimension;

        log.info("Image size [w,h] {}x{}", nImageWidth, nImageHeight);

        double percent= (double)largestDimension/(double)nImageHeight ;
        int scaledW= (int) (nImageWidth * percent);
        int scaledH= (int) (nImageHeight * percent);

        log.info("Scale Percent:{}",percent);
        log.info("Scale Image to {}x{}, ",scaledW,scaledH);

        //BufferedImage imageOut = new BufferedImage(scaledW, scaledH, BufferedImage.TYPE_INT_ARGB); // 8 bit RGB
        BufferedImage imageOut = null;
        Graphics2D g2d= null;
        AffineTransform tx;

        if(supportTranslucent){

            imageOut=new BufferedImage(scaledW, scaledH, BufferedImage.TYPE_INT_ARGB); // 8 bit RGB
            g2d = imageOut.createGraphics();
            g2d.setBackground(new Color(0,0,0,0.0f));

        }else {

            new BufferedImage(scaledW, scaledH, BufferedImage.TYPE_INT_RGB); // 8 bit RGB
            g2d = imageOut.createGraphics();
            g2d.setBackground(Color.WHITE);
        }

        g2d.clearRect(0, 0, imageOut.getWidth(), imageOut .getHeight());
        //g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        //g2d.scale(percent,percent);

        tx = new AffineTransform();
        tx.scale(scale, scale);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.drawImage(imageIn, tx, null);
        g2d.dispose();

        return imageOut;

    }


    public Object createThumbnail(InputStream inputStream, File fileOut,String generatedType, int largestDimension, Object hint) throws IOException{
       // What's the base image that we are starting with?  If there's a hint, that's the scaled image
        // from the last time around, use that... (since we know we always iterate downwards in scale)
        Image imageIn;
        if (hint instanceof Image) {
            imageIn = (Image) hint;
            log.info("createThumbnail(" + fileOut + ") reusing prior result image...");
        }
        else {
            log.info("createThumbnail(" + fileOut + ") reading image from stream " + inputStream);
            imageIn = ImageIO.read(inputStream);
        }

        if (imageIn == null) {
            log.warn("Could not read image file: " + inputStream);
            return hint;
        }

        boolean supportAlpha=alphaTranslucentFormats.contains(generatedType);

        BufferedImage imageOut = createThumbnailImage(imageIn, fileOut, largestDimension,supportAlpha);

        // Save image
        if(defaultSave){
            saveImageToFormatName(imageOut, generatedType, fileOut);
            //saveImageAsJPEG(imageOut, fileOut);
        }

        // Return this image now as the hint for the next scaling iteration
        if (imageOut != null)
            hint = imageOut;

        return hint;
    }
}
