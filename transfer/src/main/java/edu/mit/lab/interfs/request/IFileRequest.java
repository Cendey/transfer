package edu.mit.lab.interfs.request;

import java.io.File;
import java.io.IOException;

/**
 * <p>Title: Study Center Project</p>
 * <p>Description: edu.mit.lab.interfs.request.IFileRequest</p>
 * <p>Copyright: Copyright  © 2003, 2016, MIT CO., LTD. and/or its affiliates. All Rights Reserved.</p>
 * <p>Company: MIT CO., LTD.</p>
 *
 * @author <chao.deng@mit.edu>
 * @version 1.0
 * @since 2016-12-17
 */
public interface IFileRequest {

    String uploadService(File entity, String baseURL, String specifiedPath) throws Exception;

    String downloadFileService(String baseURL, String specifiedPath, String fileName) throws IOException;
}
