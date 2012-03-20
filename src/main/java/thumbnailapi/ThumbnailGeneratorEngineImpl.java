package thumbnailapi;

import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.xmlbeans.impl.common.ReaderInputStream;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.InputStreamEditor;

import javax.swing.text.html.parser.Parser;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Default impl for the Thumbnail generator engine
 */
public class ThumbnailGeneratorEngineImpl implements ThumbnailGeneratorEngine {

    private final static org.slf4j.Logger log = LoggerFactory.getLogger(ThumbnailGeneratorEngineImpl.class);

    private String generatedExtension;

    /**
     * @param generatedExtension The extension for the generated thumbnails
     */
    public void setGeneratedExtension(String generatedExtension) {
        this.generatedExtension = generatedExtension;
    }

    public String getGeneratedExtension() {
        return generatedExtension;
    }

    private Map<String, ThumbnailGenerator> thumbnailGenerators;

    /**
     * @param thumbnailGenerators The thumbnail generators known by this engine mapped to a content type
     */
    public void setThumbnailGenerators(Map<String, ThumbnailGenerator> thumbnailGenerators) {
        this.thumbnailGenerators = thumbnailGenerators;
    }

    private List<Integer> supportedSizes;

    /**
     * @param supportedSizes The suported sizes for the batch of generated thumbs
     */
    public void setSupportedSizes(List<Integer> supportedSizes) {
        this.supportedSizes = supportedSizes;
    }

    private ThumbnailGenerator defaultThumbnailGenerator;

    /**
     * @param defaultThumbnailGenerator the default thumbnail generator to be used for unregistered mime types
     */
    public void setDefaultThumbnailGenerator(ThumbnailGenerator defaultThumbnailGenerator) {
        this.defaultThumbnailGenerator = defaultThumbnailGenerator;
    }

    private String thumbnailsLocation;

    /**
     * @param thumbnailsLocation location for the generated thumbnails
     */
    public void setThumbnailsLocation(String thumbnailsLocation) {
        this.thumbnailsLocation = thumbnailsLocation;
    }

    private Map<String,String> generatedExtensions;

    public void setGeneratedExtensions(Map<String, String> generatedExtensions) {
        this.generatedExtensions = generatedExtensions;
    }

    /**
     * @param fileNamePrefix the prefix for the generated thumbnails
     * @param inputStream	the stream to generate thumbnails for
     * @param contentType	the content type of this input stream for example image/jpeg
     */
    public void generateThumbnails(String fileNamePrefix, InputStream inputStream, String contentType) {
        ThumbnailGenerator thumbnailGenerator = thumbnailGenerators.get(contentType);
        //thumbnailGenerator = thumbnailGenerator != null ? thumbnailGenerator : defaultThumbnailGenerator;

        if (thumbnailGenerator != null) {
            Object hint = null;
            for (int dimension : supportedSizes) {
                File fileOut = new File(thumbnailsLocation, fileNamePrefix + "_" + dimension + generatedExtension);
                try {
                    hint = thumbnailGenerator.createThumbnail(inputStream, fileOut, dimension, hint);
                    log.debug("Generated thumbnail for: " + inputStream + " in " + fileOut + " for type " + contentType);
                }
                catch (Exception e) {
                    log.error("Error generating thumbnail for: " + inputStream + " in " + fileOut + " for type " + contentType, e);
                }

            }
        } else {
            log.warn("Thumbnail generator not found for content type: " + contentType + " and no default generator was provided");
        }
    }

    @Override
    public void generateThumbnails(String fileNamePrefix, String generatedContentType, InputStream inputStream){

        Tika tika = new Tika();
        //Metadata metadata = new Metadata();

        String contentType=null;

        try {

            byte[] bytes=IOUtils.toByteArray(inputStream);

            contentType=tika.detect(bytes);
            inputStream= new ByteArrayInputStream(bytes);
        } catch (IOException e) {
            log.error("IO EX",e);
        }

        String extension=generatedExtensions.get(generatedContentType);

        log.info("Content type:{}",contentType);
        log.info("Generated extension:{}",extension);

        if(extension== null){
            log.warn("Not extension output format found for: {}", generatedContentType);
            return;
        }

        ThumbnailGenerator thumbnailGenerator = thumbnailGenerators.get(contentType);

        if(thumbnailGenerator == null){
            log.warn("Not found for content type for: {} and no default generator was provided", contentType);
            return;
        }
        else{

            Object hint = null;

            for (int dimension : supportedSizes) {

                File fileOut = new File(thumbnailsLocation, fileNamePrefix + "_" + dimension + "."+extension);
                try {
                    hint = thumbnailGenerator.createThumbnail(inputStream, fileOut, extension, dimension, hint);
                    log.debug("Generated thumbnail for: {} in {}",inputStream,fileOut);

                }
                catch (Exception e) {
                    log.warn("Error generating thumbnail for: {} in {}",inputStream ,fileOut);
                    log.error("Error ", e);
                }

            }
        }

    }




}
