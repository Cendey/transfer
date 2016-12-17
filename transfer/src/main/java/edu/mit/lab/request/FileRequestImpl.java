package edu.mit.lab.request;


import edu.mit.lab.interfs.request.IFileRequest;
import edu.mit.lab.utils.Toolkit;
import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileRequestImpl implements IFileRequest {

    private static final String DOWNLOAD_FILE_LOCATION = "I:/Download/Store";

    public String uploadService(File entity, String baseURL, String specifiedPath) throws Exception {
        Client client = null;
        WebTarget target;
        Response response = null;
        FileDataBodyPart fileDataBodyPart = null;
        FormDataMultiPart formDataMultiPart = null;
        String message = null;

        try {
            // invoke service after setting necessary parameters
            client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
            target = client.target(baseURL).path(specifiedPath);

            // set file upload values
            fileDataBodyPart = new FileDataBodyPart("uploadFile", entity);
            formDataMultiPart = new FormDataMultiPart();
            formDataMultiPart.bodyPart(fileDataBodyPart);

            // invocationBuilder.header("Authorization", "Basic " + authorization);
            response = target.request(MediaType.APPLICATION_OCTET_STREAM_TYPE)
                .post(Entity.entity(formDataMultiPart, MediaType.MULTIPART_FORM_DATA));

            // get response code
            int status = response.getStatus();
            System.out.println("Response code: " + status);

            if (response.getStatus() != 200) {
                throw new RuntimeException(String.format("Failed with HTTP error status : %s", status));
            }

            // get response message
            System.out
                .println(String.format("Response message from server: %s", response.getStatusInfo().getReasonPhrase()));

            // get response string
            message = response.readEntity(String.class);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        } finally {
            // release resources, if any
            if (fileDataBodyPart != null) {
                fileDataBodyPart.cleanup();
            }
            if (formDataMultiPart != null) {
                formDataMultiPart.cleanup();
                formDataMultiPart.close();
            }
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
        return message;
    }

    public String downloadFileService(String baseURL, String specifiedPath, String fileName)
        throws IOException {
        // invoke service after setting necessary parameters
        Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
        client.property("accept", MediaType.APPLICATION_OCTET_STREAM);
        WebTarget target = client.target(baseURL).path(specifiedPath).path(fileName);

        // invoke service
        Response response = target.request().get();

        // get response code
        int status = response.getStatus();
        System.out.println(String.format("Response status: %s", status));

        if (response.getStatus() != 200) {
            throw new RuntimeException(String.format("Failed with HTTP error code : %s", status));
        }

        // get response message
        System.out
            .println(String.format("Response Message From Server: %s", response.getStatusInfo().getReasonPhrase()));

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
            message = String.format("Downloaded successfully at: %s", fullFileName);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        } finally {
            // release resources, if any
            response.close();
            client.close();
        }
        return message;
    }
}