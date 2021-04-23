package com.github.pettyfer.caas.framework.system.restful;

import com.github.pettyfer.caas.framework.system.model.Upload;
import com.github.pettyfer.caas.framework.system.service.IAttachmentOperaService;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Pettyfer
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/build/file")
public class BuildFileApi {

    private final IAttachmentOperaService attachmentOperaService;

    public BuildFileApi(IAttachmentOperaService attachmentOperaService) {
        this.attachmentOperaService = attachmentOperaService;
    }

    @PostMapping("upload")
    public void upload(@RequestParam String userId, @RequestParam String fileId, @Validated Upload upload, MultipartFile file) {
        attachmentOperaService.buildUpload(userId, fileId, upload, file);
    }

    @GetMapping("download/{id}")
    public void download(@PathVariable String id, HttpServletResponse response) throws Exception {
        attachmentOperaService.download(id, response);
    }

}
