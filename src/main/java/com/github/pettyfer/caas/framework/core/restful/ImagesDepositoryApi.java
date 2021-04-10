package com.github.pettyfer.caas.framework.core.restful;

import com.github.pettyfer.caas.framework.biz.service.IBizImagesDepositoryService;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import com.github.pettyfer.caas.framework.core.model.ImagesDepositorySelect;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Pettyfer
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/core/images-depository")
public class ImagesDepositoryApi {

    private final IBizImagesDepositoryService bizImagesDepositoryService;

    public ImagesDepositoryApi(IBizImagesDepositoryService bizImagesDepositoryService) {
        this.bizImagesDepositoryService = bizImagesDepositoryService;
    }

    @GetMapping("/select")
    public R<List<ImagesDepositorySelect>> select() {
        return new R<>(bizImagesDepositoryService.select());
    }

}
