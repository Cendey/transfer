package edu.mit.lab.data.factory;

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

    @DataProvider(name = "file-walk-download")
    public static Object[][] downloadDataProvider() {
        String actionPath = "file/download";
        String fileName = "linux.png";
        return new Object[][]{{baseURL, actionPath, fileName}};
    }

    @DataProvider(name = "file-walk-upload")
    public static Object[][] uploadDataProvider() {
        String directory = "I:/";
        String uploadFileName = "Beauty.jpg";
        File entity = new File(directory, uploadFileName);
        if (entity.exists() && entity.isFile() && entity.canRead()) {
            String actionPath = "file/upload";
            return new Object[][]{{entity, baseURL, actionPath}};
        } else {
            return new Object[][]{};
        }
    }
}
