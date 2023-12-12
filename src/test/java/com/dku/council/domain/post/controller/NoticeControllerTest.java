package com.dku.council.domain.post.controller;

import com.dku.council.domain.post.model.entity.posttype.Notice;
import com.dku.council.domain.post.repository.post.GenericPostRepository;
import com.dku.council.domain.tag.model.entity.Tag;
import com.dku.council.domain.tag.repository.TagRepository;
import com.dku.council.domain.tag.service.TagService;
import com.dku.council.domain.user.model.entity.Major;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.MajorRepository;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.global.model.dto.ResponseIdDto;
import com.dku.council.mock.MajorMock;
import com.dku.council.mock.NoticeMock;
import com.dku.council.mock.TagMock;
import com.dku.council.mock.UserMock;
import com.dku.council.mock.user.UserAuth;
import com.dku.council.util.EntityUtil;
import com.dku.council.util.MvcMockResponse;
import com.dku.council.util.base.AbstractContainerRedisTest;
import com.dku.council.util.test.FullIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@FullIntegrationTest
class NoticeControllerTest extends AbstractContainerRedisTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private TagService tagService;

    @Autowired
    private GenericPostRepository<Notice> postRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Major major;
    private User user;
    private List<Notice> allNotices;
    private List<Notice> notice1;

    @BeforeEach
    void setupUser() {
        major = majorRepository.save(MajorMock.create());

        user = UserMock.create(0L, major);
        user = userRepository.save(user);
        UserAuth.withUser(user.getId());

        allNotices = new ArrayList<>();

        notice1 = NoticeMock.createList("notice", user, 3);
        allNotices.addAll(notice1);

        List<Notice> notice2 = NoticeMock.createList("test", user, 2);
        allNotices.addAll(notice2);

        postRepository.saveAll(allNotices);
        postRepository.saveAll(NoticeMock.createList("ews", user, 3, false));
    }


    @Test
    @DisplayName("Notice 리스트 가져오기")
    void list() throws Exception {
        // when
        ResultActions result = mvc.perform(get("/post/notice"));

        // then
        Integer[] ids = EntityUtil.getIdArray(allNotices);
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].id", containsInAnyOrder(ids)));
    }

    @Test
    @DisplayName("Notice 리스트 가져오기 - 키워드 명시")
    void listWithKeyword() throws Exception {
        // when
        ResultActions result = mvc.perform(get("/post/notice")
                .param("keyword", "ews"));

        // then
        Integer[] ids = EntityUtil.getIdArray(notice1);
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].id", containsInAnyOrder(ids)));
    }

    @Test
    @DisplayName("Notice 리스트 가져오기 - 태그 명시")
    void listWithTags() throws Exception {
        // given
        Tag tag1 = TagMock.create();
        List<Notice> notice2 = createPostsWithTag(tag1, 6);

        Tag tag2 = TagMock.create();
        createPostsWithTag(tag2, 7);

        // when
        ResultActions result = mvc.perform(get("/post/notice")
                .param("tagIds", tag1.getId().toString()));

        // then
        Integer[] tag1Ids = EntityUtil.getIdArray(notice2);
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].id", containsInAnyOrder(tag1Ids)));
    }

    @Test
    @DisplayName("Notice 리스트 가져오기 - 여러 태그 명시")
    void listWithMultipleTags() throws Exception {
        // given
        Tag tag1 = TagMock.create();
        List<Notice> notice2 = createPostsWithTag(tag1, 6);

        Tag tag2 = TagMock.create();
        List<Notice> notice3 = createPostsWithTag(tag2, 7);

        // when
        ResultActions result = mvc.perform(get("/post/notice")
                .param("tagIds", tag1.getId().toString())
                .param("tagIds", tag2.getId().toString()));

        // then
        List<Notice> expected = Stream.concat(notice2.stream(), notice3.stream())
                .collect(Collectors.toList());
        Integer[] allIds = EntityUtil.getIdArray(expected);
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].id", containsInAnyOrder(allIds)));
    }

    private List<Notice> createPostsWithTag(Tag tag, int size) {
        List<Notice> noticeList = NoticeMock.createList("notice-with-tag", user, size);
        tag = tagRepository.save(tag);
        noticeList = postRepository.saveAll(noticeList);
        for (Notice notice : noticeList) {
            tagService.addTagsToPost(notice, List.of(tag.getId()));
        }
        return noticeList;
    }

    @Test
    @DisplayName("Notice 생성")
    void create() throws Exception {
        // when
        UserAuth.withAdmin(user.getId());

        ResultActions result = mvc.perform(multipart("/post/notice")
                .param("title", "제목")
                .param("body", "본문"));

        // then
        MvcResult response = result.andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andReturn();

        ResponseIdDto dto = MvcMockResponse.getResponse(objectMapper, response, ResponseIdDto.class);
        Notice actualNotice = postRepository.findById(dto.getId()).orElseThrow();

        assertThat(actualNotice.getTitle()).isEqualTo("제목");
        assertThat(actualNotice.getBody()).isEqualTo("본문");
    }

    @Test
    @DisplayName("Notice 생성 - 태그 명시")
    void createWithTag() throws Exception {
        // given
        UserAuth.withAdmin(user.getId());

        Tag tag = new Tag("tag");
        tagRepository.save(tag);
        Tag tag2 = new Tag("tag2");
        tagRepository.save(tag2);
        String[] tagIds = new String[]{tag.getId().toString(), tag2.getId().toString()};

        // when
        ResultActions result = mvc.perform(multipart("/post/notice")
                .param("title", "제목")
                .param("body", "본문")
                .param("tagIds", tagIds));

        // then
        MvcResult response = result.andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andReturn();

        ResponseIdDto dto = MvcMockResponse.getResponse(objectMapper, response, ResponseIdDto.class);
        Notice actualNotice = postRepository.findById(dto.getId()).orElseThrow();

        assertThat(actualNotice.getTitle()).isEqualTo("제목");
        assertThat(actualNotice.getBody()).isEqualTo("본문");
        assertThat(actualNotice.getPostTags().get(0).getTag().getId()).isEqualTo(tag.getId());
        assertThat(actualNotice.getPostTags().get(1).getTag().getId()).isEqualTo(tag2.getId());
    }

    @Test
    @DisplayName("Notice 단건조회")
    void findOne() throws Exception {
        // given
        Notice notice = allNotices.get(0);

        // when
        ResultActions result = mvc.perform(get("/post/notice/" + notice.getId()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("id", is(notice.getId().intValue())))
                .andExpect(jsonPath("title", is("notice0")))
                .andExpect(jsonPath("body", is("0")))
                .andExpect(jsonPath("author", is(notice.getDisplayingUsername())))
                .andExpect(jsonPath("mine", is(true)));
    }

    @Test
    @DisplayName("Notice 삭제")
    void delete() throws Exception {
        // given
        UserAuth.withAdmin(user.getId());
        Notice notice = allNotices.get(0);

        // when
        ResultActions result = mvc.perform(MockMvcRequestBuilders.delete("/post/notice/" + notice.getId()));

        // then
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("Notice 삭제 - Admin")
    void deleteByAdmin() throws Exception {
        // given
        UserAuth.withAdmin(user.getId());
        Notice notice = NoticeMock.create(user);
        notice = postRepository.save(notice);

        // when
        ResultActions result = mvc.perform(MockMvcRequestBuilders.delete("/post/notice/" + notice.getId()));

        // then
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("Notice 삭제 실패 - 권한 없음")
    void failedDeleteByAccessDenied() throws Exception {
        // given
        Notice notice = NoticeMock.create(userRepository.save(UserMock.create(0L, major)));
        notice = postRepository.save(notice);

        // when
        ResultActions result = mvc.perform(MockMvcRequestBuilders.delete("/post/notice/" + notice.getId()));

        // then
        result.andExpect(status().isForbidden());
    }
}