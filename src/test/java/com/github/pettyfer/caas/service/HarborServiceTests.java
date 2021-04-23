package com.github.pettyfer.caas.service;

import com.github.pettyfer.caas.framework.engine.docker.register.model.RepositoryView;
import com.github.pettyfer.caas.framework.engine.docker.register.service.IHarborService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("compay")
public class HarborServiceTests {

    @Autowired
    private IHarborService harborService;

    @Test
    public void checkProjectTest() {
        harborService.checkProject("demo");
    }

    @Test
    public void queryRepositoryTest() {
        List<RepositoryView> repositoryViews = harborService.queryRepository("14", null);
        for (RepositoryView repositoryView : repositoryViews) {
            System.out.println(repositoryView);
        }
    }

}
