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
    private static IFileRequest instance;

    private static IFileRequest getIFileRequestInstance() {
        if (instance == null) {
            instance = new FileRequestImpl();
        }
        return instance;
    }

    @DataProvider(name = "file-walk-download")
    public static Object[][] downloadDataProvider() {
        String actionPath = "file/download";
        String fileName = "linux.png";
        return new Object[][]{{getIFileRequestInstance(), baseURL, actionPath, fileName}};
    }

    @DataProvider(name = "file-walk-upload")
    public static Object[][] uploadDataProvider() {
        String directory = "I:/";
        String uploadFileName = "Beauty.jpg";
        File entity = new File(directory, uploadFileName);
        if (entity.exists() && entity.isFile() && entity.canRead()) {
            String actionPath = "file/upload";
            return new Object[][]{{getIFileRequestInstance(), entity, baseURL, actionPath}};
        } else {
            return new Object[][]{};
        }
    }

    @DataProvider(name = "file-walk-all")
    public static Object[][] fileWalk(){
        return new Object[][]{};
    }
}
