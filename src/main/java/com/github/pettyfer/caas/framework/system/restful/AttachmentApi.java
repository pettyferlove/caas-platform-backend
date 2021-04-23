package com.github.pettyfer.caas.framework.system.restful;

import com.github.pettyfer.caas.framework.system.model.Upload;
import com.github.pettyfer.caas.framework.system.model.UploadResult;
import com.github.pettyfer.caas.framework.system.service.IAttachmentOperaService;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import com.github.pettyfer.caas.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Pettyfer
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/attachment")
@Api(value = "附件操作", tags = {"附件操作接口"})
public class AttachmentApi {

    private final IAttachmentOperaService attachmentOperaService;

    public AttachmentApi(IAttachmentOperaService attachmentOperaService) {
        this.attachmentOperaService = attachmentOperaService;
    }

    @ApiOperation(value = "上传附件", authorizations = @Authorization(value = "oauth2"))
    @PostMapping("upload")
    public R<UploadResult> upload(@ApiParam("附件信息") @Validated Upload upload, @ApiParam("文件") MultipartFile file) {
        return new R<>(attachmentOperaService.upload(SecurityUtil.getUser().getId(), upload, file));
    }

}
