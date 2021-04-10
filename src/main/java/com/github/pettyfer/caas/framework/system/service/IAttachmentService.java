package com.github.pettyfer.caas.framework.system.service;



import com.github.pettyfer.caas.framework.system.model.FileProcessResult;
import com.github.pettyfer.caas.framework.system.model.Upload;
import com.github.pettyfer.caas.global.constants.FileType;

import java.io.File;
import java.io.OutputStream;

/**
 * 附件核心服务，对接具体的存储方式，比如本地文件操作 阿里云附件操作
 *
 * @author Petty
 */
public interface IAttachmentService {

    /**
     * 文件上传
     *
     * @param fileId    文件ID
     * @param file     文件对象
     * @param upload   上传文件基本信息
     * @param fileType 文件类型
     * @return FileProcessResult
     */
    FileProcessResult upload(String fileId, Upload upload, File file, FileType fileType);

    /**
     * 下载
     *
     * @param path         储存路径
     * @param md5          文件MD5
     * @param outputStream 输出流
     */
    void download(String path, String md5, OutputStream outputStream);


    /**
     * 删除文件（同时删除数据记录）
     *
     * @param path 储存路径
     * @return Boolean
     */
    Boolean delete(String path);

}
