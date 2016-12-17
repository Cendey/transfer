package edu.mit.lab.data.factory;

import edu.mit.lab.interfs.request.IFileRequest;
import edu.mit.lab.request.FileRequestImpl;
import org.testng.annotations.DataProvider;

import java.io.File;

/**
 * <p>Title: MIT Lib Project</p>
 * <p>Description: edu.mit.lab.data.factory.DataProviderFactory</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Lib Co., Ltd</p>
 *
 * @author <chao.deng@mit.edu>
 * @version 1.0
 * @since 12/16/2016
 */
public class DataProviderFactory {

    private static final String baseURL = "http://localhost:9080/rest";
    private static final String UPLOAD_DIR = "I:/";
    private static final String UPLOAD_FILE_NAME = "Beauty.jpg";
    private static final String UPLOAD_ACTION_PATH = "file/upload";
    private static final String DOWNLOAD_FILE_NAME = "linux.png";
    private static final String DOWNLOAD_ACTION_PATH = "file/download";
    private static IFileRequest instance;

    private static IFileRequest getIFileRequestInstance() {
        if (instance == null) {
            instance = new FileRequestImpl();
        }
        return instance;
    }

    @DataProvider(name = "file-walk-download")
    public static Object[][] downloadDataProvider() {
        return new Object[][]{{getIFileRequestInstance(), baseURL, DOWNLOAD_ACTION_PATH, DOWNLOAD_FILE_NAME}};
    }

    @DataProvider(name = "file-walk-upload")
    public static Object[][] uploadDataProvider() {

        File entity = new File(UPLOAD_DIR, UPLOAD_FILE_NAME);
        if (entity.exists() && entity.isFile() && entity.canRead()) {
            return new Object[][]{{getIFileRequestInstance(), entity, baseURL, UPLOAD_ACTION_PATH}};
        } else {
            return new Object[][]{};
        }
    }

    @DataProvider(name = "file-walk-all")
    public static Object[][] fileWalkDataProvider() {
        return new Object[][]{
            {
                getIFileRequestInstance(), baseURL, UPLOAD_DIR, UPLOAD_ACTION_PATH, UPLOAD_FILE_NAME,
                DOWNLOAD_ACTION_PATH, DOWNLOAD_FILE_NAME}};
    }
}
