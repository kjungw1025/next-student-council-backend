package com.dku.council.domain.post.controller;

import com.dku.council.domain.post.model.CoalitionType;
import com.dku.council.domain.post.model.entity.posttype.Coalition;
import com.dku.council.domain.post.repository.post.GenericPostRepository;
import com.dku.council.domain.tag.repository.TagRepository;
import com.dku.council.domain.user.model.entity.Major;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.MajorRepository;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.mock.CoalitionMock;
import com.dku.council.mock.MajorMock;
import com.dku.council.mock.UserMock;
import com.dku.council.mock.user.UserAuth;
import com.dku.council.util.EntityUtil;
import com.dku.council.util.base.AbstractContainerRedisTest;
import com.dku.council.util.test.FullIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@FullIntegrationTest
class CoalitionControllerTest extends AbstractContainerRedisTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private GenericPostRepository<Coalition> coalitionRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    private Major major;
    private User user;

    private List<Coalition> coalition1;
    private List<Coalition> allCoalition;

    @BeforeEach
    void setupUser() {
        major = majorRepository.save(MajorMock.create());

        user = UserMock.create(0L, major);
        user = userRepository.save(user);
        UserAuth.withUser(user.getId());

        coalition1 = coalitionRepository.saveAll(CoalitionMock.createList("coalition1", user, 3, CoalitionType.FOOD));
        allCoalition.addAll(coalition1);

        List<Coalition> coalition2 = CoalitionMock.createList("test", user, 2, CoalitionType.FOOD);
        allCoalition.addAll(coalition2);

        coalitionRepository.saveAll(coalition2);
        coalitionRepository.saveAll(CoalitionMock.createList("coalition", user, 3, CoalitionType.FOOD));
    }

    @Test
    @DisplayName("Coalition 게시글 목록 조회")
    void list() throws Exception {
        // when
        ResultActions result = mvc.perform(get("/post/coalition"));

        // then
         Integer[] ids = EntityUtil.getIdArray(allCoalition);
         result.andExpect(status().isOk())
                 .andExpect(jsonPath("$.content[*].id", containsInAnyOrder(ids)));
    }

    @Test
    @DisplayName("Coalition 게시글 목록 조회 - 키워드 명시")
    void listWithKeyword() throws Exception {
        ResultActions result = mvc.perform(get("/post/coalition")
                .param("keyword", "coalition"));

        Integer[] ids = EntityUtil.getIdArray(coalition1);
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].id", containsInAnyOrder(ids)));
    }

    @Test
    @DisplayName("Coalition 게시글 삭제 - Admin")
    void deleteByAdmin() throws Exception {
        UserAuth.withAdmin(user.getId());
        Coalition coalition = allCoalition.get(0);

        ResultActions result = mvc.perform(delete("/post/coalition/" + coalition.getId()));

        result.andExpect(status().isOk());
    }
}