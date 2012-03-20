package thumbnailapi;

import org.apache.tika.io.TikaInputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * An engine in charge of generating thumbnails for files
 */
public interface ThumbnailGeneratorEngine {

    /**
     * Generate thumbnails from input stream
     * @param fileNamePrefix the prefix for the generated thumbnails
     * @param inputStream	the stream to generate thumbnails for
     * @param contentType	the content type of this input stream for example image/jpeg
     */
    void generateThumbnails(String fileNamePrefix, InputStream inputStream, String contentType);


    void generateThumbnails(String fileNamePrefix, String generatedContentType, InputStream inputStream);
}
