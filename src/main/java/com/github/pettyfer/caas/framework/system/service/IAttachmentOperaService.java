package com.github.pettyfer.caas.framework.system.service;

import com.github.pettyfer.caas.framework.system.model.Upload;
import com.github.pettyfer.caas.framework.system.model.UploadResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface IAttachmentOperaService {

    UploadResult upload(String userId, Upload upload, MultipartFile file);

    UploadResult buildUpload(String userId, String fileId, Upload upload, MultipartFile file);

    void download(String id, HttpServletResponse response) throws Exception;
}
