package edu.mit.lab.service;

import edu.mit.lab.data.factory.DataProviderFactory;
import edu.mit.lab.request.FileWalkClient;
import org.testng.annotations.Test;

import java.io.File;

/**
 * <p>Title: MIT Lib Project</p>
 * <p>Description: edu.mit.lab.service.FileWalkClientTest</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Lib Co., Ltd</p>
 *
 * @author <chao.deng@mit.edu>
 * @version 1.0
 * @since 12/16/2016
 */
public class FileWalkClientTest {

    @Test(timeOut = 10, dataProviderClass = DataProviderFactory.class, dataProvider = "file-walk-upload")
    public void testUploadService(File entity, String baseURL, String specifiedPath) throws Exception {
        FileWalkClient.uploadService(entity, baseURL, specifiedPath);
    }

    @Test(timeOut = 50, dataProviderClass = DataProviderFactory.class, dataProvider = "file-walk-download")
    public void testDownloadFileService(String baseURL, String specifiedPath, String fileName) throws Exception {
        FileWalkClient.downloadFileService(baseURL, specifiedPath, fileName);
    }

}