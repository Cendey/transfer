package edu.mit.lab.service;

import edu.mit.lab.data.factory.DataProviderFactory;
import edu.mit.lab.interfs.request.IFileRequest;
import edu.mit.lab.request.FileRequestImpl;
import org.testng.annotations.Test;

import java.io.File;

/**
 * <p>Title: MIT Lib Project</p>
 * <p>Description: edu.mit.lab.service.FileRequestImplTest</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Lib Co., Ltd</p>
 *
 * @author <chao.deng@mit.edu>
 * @version 1.0
 * @since 12/16/2016
 */
public class FileRequestImplTest {

    @Test(timeOut = 50, dataProviderClass = DataProviderFactory.class, dataProvider = "file-walk-upload")
    public void testUploadService(
        IFileRequest instance, File entity, String baseURL, String specifiedPath) throws Exception {
        instance.uploadService(entity, baseURL, specifiedPath);
    }

    @Test(timeOut = 50, dataProviderClass = DataProviderFactory.class, dataProvider = "file-walk-download")
    public void testDownloadFileService(
        IFileRequest instance, String baseURL, String specifiedPath, String fileName) throws Exception {
        instance.downloadFileService(baseURL, specifiedPath, fileName);
    }

    @Test(timeOut = 80, dataProviderClass = DataProviderFactory.class, dataProvider = "file-walk-all")
    public void testAllFileService(
        IFileRequest instance, String baseURL, String directory, String uploadPath, String uploadFileName,
        String downloadPath, String downloadFileName) throws Exception {
        File entity = new File(directory, uploadFileName);
        if (entity.exists() && entity.isFile() && entity.canRead()) {
            System.out
                .println(
                    String.format("Response message : %s", instance.uploadService(entity, baseURL, uploadPath)));
        }
        System.out.println(instance.downloadFileService(baseURL, downloadPath, downloadFileName));
    }

}