package edu.mit.lab.implmts.request;


import edu.mit.lab.interfs.request.IFileRequest;
import edu.mit.lab.utils.Toolkit;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

public class FileRequestImpl implements IFileRequest {

    private static final Logger logger = LogManager.getLogger(FileRequestImpl.class);
    private static final String DOWNLOAD_FILE_LOCATION = "K:/Store/Download";

    public String uploadFile(File entity, String baseURL, String specifiedPath) throws Exception {
        Client client = null;
        Response response = null;
        FileDataBodyPart fileDataBodyPart = null;
        String message = null;

        try (FormDataMultiPart formDataMultiPart = new FormDataMultiPart()) {
            logger.trace("Request to upload file from client.");
            // invoke service after setting necessary parameters
            client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();

            // set file upload values
            fileDataBodyPart = new FileDataBodyPart("uploadFile", entity);
            formDataMultiPart.bodyPart(fileDataBodyPart);

            // invocationBuilder.header("Authorization", "Basic " + authorization);
            response = client.target(baseURL).path(specifiedPath).request(MediaType.APPLICATION_OCTET_STREAM_TYPE)
                .post(Entity.entity(formDataMultiPart, MediaType.MULTIPART_FORM_DATA));

            // get response code
            int status = response.getStatus();
            logger.info("Response code: {}", status);

            if (response.getStatus() != 200) {
                logger.error("Failed with HTTP error status : {}", status);
                throw new RuntimeException(String.format("Failed with HTTP error status : %s", status));
            }

            // get response message
            logger.trace("Response message from server: {}", response.getStatusInfo().getReasonPhrase());

            // get response string
            message = response.readEntity(String.class);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        } finally {
            clear(client, response, fileDataBodyPart);
        }
        return message;
    }

    public String downloadFile(String baseURL, String specifiedPath, String fileName) throws IOException {
        logger.trace("Request to download file from client.");
        // invoke service after setting necessary parameters
        Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
        client.property("accept", MediaType.APPLICATION_OCTET_STREAM);

        // invoke service
        Response response = client.target(baseURL).path(specifiedPath)
            .path(URLEncoder.encode(fileName,"UTF-8")).request().get();

        // get response code
        int status = response.getStatus();
        logger.info("Response status: {}", status);

        if (response.getStatus() != 200) {
            logger.error("Failed with HTTP error code : {}", status);
            throw new RuntimeException(String.format("Failed with HTTP error code : %s", status));
        }

        // get response message
        logger.info("Response Message From Server: {}", response.getStatusInfo().getReasonPhrase());

        // read response string
        InputStream inputStream = response.readEntity(InputStream.class);

        String message = null;
        String category = FilenameUtils.getExtension(fileName);
        Toolkit.initDir(DOWNLOAD_FILE_LOCATION, category);
        String fullFileName = Toolkit.destDir(DOWNLOAD_FILE_LOCATION, category) + fileName;
        try (OutputStream outputStream = new FileOutputStream(fullFileName)) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
            // set download SUCCESS message to return
            logger.info("Downloaded successfully at: {}", fullFileName);
            message = String.format("Downloaded successfully at: %s", fullFileName);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        } finally {
            clear(client, response);
        }
        return message;
    }

    private void clear(Client client, Response response) throws IOException {
        clear(client, response, null);
    }

    private void clear(Client client, Response response, FileDataBodyPart fileBodyPart) throws IOException {
        // release resources, if any
        if (fileBodyPart != null) {
            fileBodyPart.cleanup();
        }
        if (response != null) {
            response.close();
        }
        if (client != null) {
            client.close();
        }
    }
}