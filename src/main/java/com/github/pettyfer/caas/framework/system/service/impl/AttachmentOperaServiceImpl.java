package com.github.pettyfer.caas.framework.system.service.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.github.pettyfer.caas.framework.system.entity.SystemAttachmentInfo;
import com.github.pettyfer.caas.framework.system.model.FileProcessResult;
import com.github.pettyfer.caas.framework.system.model.Upload;
import com.github.pettyfer.caas.framework.system.model.UploadResult;
import com.github.pettyfer.caas.framework.system.service.IAttachmentOperaService;
import com.github.pettyfer.caas.framework.system.service.IAttachmentService;
import com.github.pettyfer.caas.framework.system.service.ISystemAttachmentInfoService;
import com.github.pettyfer.caas.global.constants.FileType;
import com.github.pettyfer.caas.global.constants.StorageType;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.global.factory.IAttachmentServiceFactory;
import com.github.pettyfer.caas.utils.ConverterUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;

/**
 * @author Pettyfer
 */
@Slf4j
@Service
public class AttachmentOperaServiceImpl implements IAttachmentOperaService {

    private final ISystemAttachmentInfoService attachmentInfoService;

    private final IAttachmentServiceFactory factory;

    public AttachmentOperaServiceImpl(ISystemAttachmentInfoService systemAttachmentInfoService, IAttachmentServiceFactory factory) {
        this.attachmentInfoService = systemAttachmentInfoService;
        this.factory = factory;
    }

    @Override
    public UploadResult upload(String userId, Upload upload, MultipartFile file) {
        IAttachmentService attachmentService = factory.create(upload.getStorage());
        FileProcessResult uploadResult = null;
        String fileId = UUID.fastUUID().toString();
        String fileName = file.getOriginalFilename();
        FileType fileType = FileType.parse(file.getContentType());
        String tempPath = "temp" + File.separator + fileId + fileType.getExpansionName();
        File tempFile = null;
        long length;
        try {
            tempFile = new File(tempPath);
            if (!tempFile.exists()) {
                FileUtils.copyInputStreamToFile(file.getInputStream(), tempFile);
            }
            length = tempFile.length();
            uploadResult = attachmentService.upload(fileId, upload, tempFile, fileType);
        } catch (IOException e) {
            throw new BaseRuntimeException("file pretreatment process error");
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
        attachmentInfoService.save(userId, uploadResult.getFileId(), fileName, uploadResult.getMd5(), uploadResult.getPath(), upload, fileType.getContentType(), length);
        UploadResult result = Optional.ofNullable(ConverterUtil.convert(uploadResult, new UploadResult())).orElseGet(UploadResult::new);
        result.setAdditionalData(new JSONObject().toJSONString());
        result.setFileName(fileName);
        return result;

    }

    @Override
    public UploadResult buildUpload(String userId, String fileId, Upload upload, MultipartFile file) {
        IAttachmentService attachmentService = factory.create(upload.getStorage());
        FileProcessResult uploadResult = null;
        String fileName = file.getOriginalFilename();
        FileType fileType = FileType.parse(file.getContentType());
        String tempPath = "temp" + File.separator + fileId + fileType.getExpansionName();
        File tempFile = null;
        long length;
        try {
            tempFile = new File(tempPath);
            if (!tempFile.exists()) {
                FileUtils.copyInputStreamToFile(file.getInputStream(), tempFile);
            }
            length = tempFile.length();
            uploadResult = attachmentService.upload(fileId, upload, tempFile, fileType);
        } catch (IOException e) {
            throw new BaseRuntimeException("file pretreatment process error");
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
        attachmentInfoService.save(userId, uploadResult.getFileId(), fileName, uploadResult.getMd5(), uploadResult.getPath(), upload, fileType.getContentType(), length);
        UploadResult result = Optional.ofNullable(ConverterUtil.convert(uploadResult, new UploadResult())).orElseGet(UploadResult::new);
        result.setAdditionalData(new JSONObject().toJSONString());
        result.setFileName(fileName);
        return result;
    }

    @Override
    public void download(String id, HttpServletResponse response) throws Exception {
        Optional<SystemAttachmentInfo> attachmentInfoOptional = Optional.ofNullable(attachmentInfoService.get(id));
        if (attachmentInfoOptional.isPresent()) {
            SystemAttachmentInfo attachmentInfo = attachmentInfoOptional.get();
            IAttachmentService attachmentService = factory.create(StorageType.parse(attachmentInfo.getStorageType()));
            response.setCharacterEncoding("utf-8");
            response.setContentType(attachmentInfo.getFileType());
            String fileName = null;
            try {
                fileName = URLEncoder.encode(attachmentInfo.getFileName(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setHeader("Content-Disposition", "inline;filename=" + fileName);
            attachmentService.download(attachmentInfo.getPath(), attachmentInfo.getMd5(), response.getOutputStream());
        } else {
            throw new BaseRuntimeException("not found file", HttpStatus.NOT_FOUND);
        }
    }

}
