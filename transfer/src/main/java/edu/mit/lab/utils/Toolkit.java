package edu.mit.lab.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * <p>Title: MIT Lib Project</p>
 * <p>Description: edu.mit.lab.utils.Toolkit</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Lib Co., Ltd</p>
 *
 * @author <chao.deng@mit.edu>
 * @version 1.0
 * @since 12/16/2016
 */
public class Toolkit {
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    public static void initDir(String baseDir, String category) {
        try {
            FileUtils.forceMkdir(new File(baseDir+ FILE_SEPARATOR + category + FILE_SEPARATOR));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static String destDir(String baseDir, String category) {
        return  baseDir+ Toolkit.FILE_SEPARATOR + category + Toolkit.FILE_SEPARATOR;
    }
}
