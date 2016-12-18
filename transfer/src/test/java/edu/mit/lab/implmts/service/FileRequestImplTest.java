package edu.mit.lab.implmts.service;

import edu.mit.lab.data.factory.DataProviderFactory;
import edu.mit.lab.interfs.request.IFileRequest;
import org.testng.annotations.Test;

import java.io.File;

/**
 * <p>Title: MIT Lib Project</p>
 * <p>Description: edu.mit.lab.implmts.service.FileRequestImplTest</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Lib Co., Ltd</p>
 *
 * @author <chao.deng@mit.edu>
 * @version 1.0
 * @since 12/16/2016
 */
public class FileRequestImplTest {

    @Test(dataProviderClass = DataProviderFactory.class, dataProvider = "file-walk-upload")
    public void testUploadService(
        IFileRequest instance, File entity, String baseURL, String specifiedPath) throws Exception {
        instance.uploadFile(entity, baseURL, specifiedPath);
    }

    @Test(dataProviderClass = DataProviderFactory.class, dataProvider = "file-walk-download")
    public void testDownloadFileService(
        IFileRequest instance, String baseURL, String specifiedPath, String fileName) throws Exception {
        instance.downloadFile(baseURL, specifiedPath, fileName);
    }

    @Test(timeOut = 80, dataProviderClass = DataProviderFactory.class, dataProvider = "file-walk-all")
    public void testAllFileService(
        IFileRequest instance, String baseURL, String directory, String uploadPath, String uploadFileName,
        String downloadPath, String downloadFileName) throws Exception {
        File entity = new File(directory, uploadFileName);
        if (entity.exists() && entity.isFile() && entity.canRead()) {
            System.out
                .println(
                    String.format("Response message : %s", instance.uploadFile(entity, baseURL, uploadPath)));
        }
        System.out.println(instance.downloadFile(baseURL, downloadPath, downloadFileName));
    }

}