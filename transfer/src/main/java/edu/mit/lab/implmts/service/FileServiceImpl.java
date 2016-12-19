package edu.mit.lab.implmts.service;

import edu.mit.lab.interfs.service.IFileService;
import edu.mit.lab.utils.Toolkit;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@Path("/file")
public class FileServiceImpl implements IFileService {

    private static final Logger logger = LogManager.getLogger(FileServiceImpl.class);
    private static final String FILES_STORE_LOCATION = "K:/Store/Upload";

    @GET
    @Path("/download/{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(@PathParam("fileName") String fileName) {
        logger.trace("File : {} , will be download from request", fileName);
        try {
            fileName = URLDecoder.decode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
        String category = FilenameUtils.getExtension(fileName);
        File file = new File(Toolkit.destDir(FILES_STORE_LOCATION, category), fileName);
        if (!file.exists() || !file.isFile() || !file.canRead()) {
            logger.error("File : {}, can't be found to download", fileName);
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
                        logger.error(e.getMessage());
                    }
                }
            };
            String contentType = new MimetypesFileTypeMap().getContentType(file);
            return Response
                .ok(streamingOutput, contentType)
                .header("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"")
                .header("Cache-Control", "no-cache").build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.serverError().entity("Exception occurred when loading file").build();
        }
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
        @FormDataParam("uploadFile") InputStream fileInputStream,
        @FormDataParam("uploadFile") FormDataContentDisposition formContentDisposition) {
        String fileFullName = null;
        try {
            fileFullName = URLDecoder.decode(formContentDisposition.getFileName(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
        logger.trace("File : {} is prepared to upload", fileFullName);
        String category = FilenameUtils.getExtension(fileFullName);
        Toolkit.initDir(FILES_STORE_LOCATION, category);
        String destDir = Toolkit.destDir(FILES_STORE_LOCATION, category);
        assert fileFullName != null;
        try (OutputStream outputStream = new FileOutputStream(new File(destDir, fileFullName))) {
            int read;
            byte[] buff = new byte[1024];
            while ((read = fileInputStream.read(buff)) != -1) {
                outputStream.write(buff, 0, read);
            }
            outputStream.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return Response.ok("File uploaded successfully at " + destDir + fileFullName).build();
    }

}