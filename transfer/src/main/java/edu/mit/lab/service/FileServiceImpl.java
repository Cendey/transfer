package edu.mit.lab.service;

import edu.mit.lab.interfs.IFileService;
import edu.mit.lab.utils.Toolkit;
import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Path("/file")
public class FileServiceImpl implements IFileService {

    private static final String UPLOAD_FILE_LOCATION = "I:/Kewill/Store";

    @GET
    @Path("/download/{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(@PathParam("fileName") String fileName) {
        String category = FilenameUtils.getExtension(fileName);
        File file = new File(Toolkit.destDir(UPLOAD_FILE_LOCATION, category), fileName);
        if (!file.exists() || !file.isFile() || !file.canRead()) {
            throw new WebApplicationException(404);
        }
        try {
            StreamingOutput streamingOutput = output -> {
                try (FileInputStream inputStream = new FileInputStream(file)) {
                    try (OutputStream outputStream = output) {
                        int read;
                        byte[] buff = new byte[1024];
                        while ((read = inputStream.read(buff)) != -1) {
                            if (read > 0) {
                                outputStream.write(buff, 0, read);
                            }
                        }
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
            };
            String contentType = new MimetypesFileTypeMap().getContentType(file);
            return Response
                .ok(streamingOutput, contentType)
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .header("Cache-Control", "no-cache").build();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return Response.serverError().entity("Exception occurred when loading file").build();
        }
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
        @FormDataParam("uploadFile") InputStream fileInputStream,
        @FormDataParam("uploadFile") FormDataContentDisposition fileFormDataContentDisposition) {
        String fileFullName = fileFormDataContentDisposition.getFileName();
        String category = FilenameUtils.getExtension(fileFullName);
        Toolkit.initDir(UPLOAD_FILE_LOCATION, category);
        String destDir = Toolkit.destDir(UPLOAD_FILE_LOCATION, category);
        try (OutputStream outputStream = new FileOutputStream(
            new File(destDir, fileFullName))) {
            int read;
            byte[] buff = new byte[1024];
            while ((read = fileInputStream.read(buff)) != -1) {
                outputStream.write(buff, 0, read);
            }
            outputStream.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return Response.ok("File uploaded successfully at " + destDir + fileFullName).build();
    }

}