package edu.mit.lab.interfs.service;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

public interface IFileService {

    @GET
    @Path("/download/{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    Response downloadFile(String fileName);

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    Response uploadFile(
        @FormDataParam("uploadFile") InputStream fileInputStream,
        @FormDataParam("uploadFile") FormDataContentDisposition formContentDisposition);
}