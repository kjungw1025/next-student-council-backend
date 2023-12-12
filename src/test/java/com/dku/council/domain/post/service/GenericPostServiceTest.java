package com.dku.council.domain.post.service;

import com.dku.council.domain.like.service.impl.CachedLikeServiceImpl;
import com.dku.council.domain.post.exception.PostNotFoundException;
import com.dku.council.domain.post.model.dto.list.SummarizedGenericPostDto;
import com.dku.council.domain.post.model.dto.request.RequestCreateNoticeDto;
import com.dku.council.domain.post.model.dto.response.ResponseSingleGenericPostDto;
import com.dku.council.domain.post.model.entity.posttype.Notice;
import com.dku.council.domain.post.repository.post.GenericPostRepository;
import com.dku.council.domain.post.service.post.GenericPostService;
import com.dku.council.domain.tag.service.TagService;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.global.auth.role.UserRole;
import com.dku.council.global.error.exception.NotGrantedException;
import com.dku.council.global.error.exception.UserNotFoundException;
import com.dku.council.infra.nhn.s3.service.FileUploadService;
import com.dku.council.infra.nhn.s3.service.ImageUploadService;
import com.dku.council.infra.nhn.s3.service.OriginalFileUploadService;
import com.dku.council.infra.nhn.s3.service.ObjectUploadContext;
import com.dku.council.mock.MultipartFileMock;
import com.dku.council.mock.NoticeMock;
import com.dku.council.mock.UserMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.dku.council.domain.like.model.LikeTarget.POST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenericPostServiceTest {

    @Mock
    private GenericPostRepository<Notice> noticeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TagService tagService;

    @Mock
    private ViewCountService viewCountService;

    @Mock
    private OriginalFileUploadService originalFileUploadService;

    @Mock
    private OriginalFileUploadService.Context originalFileUploadContext;

    @Mock
    private FileUploadService.Context fileUploadContext;

    @Mock
    private ImageUploadService.Context imageUploadContext;

    @Mock
    private ObjectUploadContext uploadContext;

    @Mock
    private ThumbnailService thumbnailService;

    @Mock
    private CachedLikeServiceImpl postLikeService;

    @InjectMocks
    private GenericPostService<Notice> noticeService;

    @Mock
    private FileUploadService fileUploadService;

    @Mock
    private ImageUploadService imageUploadService;


    @Test
    @DisplayName("list가 잘 동작하는지?")
    public void list() {
        // given
        List<Notice> allNoticeList = NoticeMock.createListDummy("generic-", 20);
        Page<Notice> allNotice = new DummyPage<>(allNoticeList, 20);

        when(noticeRepository.findAll((Specification<Notice>) any(), (Pageable) any())).thenReturn(allNotice);
        when(postLikeService.getCountOfLikes(any(), eq(POST))).thenReturn(15);

        // when
        Page<SummarizedGenericPostDto> allPage = noticeService.list(noticeRepository, null, Pageable.unpaged(),
                500);

        // then
        assertThat(allPage.getTotalElements()).isEqualTo(allNoticeList.size());
        for (int i = 0; i < allPage.getTotalElements(); i++) {
            SummarizedGenericPostDto dto = allPage.getContent().get(i);
            Notice notice = allNoticeList.get(i);
            assertThat(dto.getId()).isEqualTo(notice.getId());
            assertThat(dto.getTitle()).isEqualTo(notice.getTitle());
            assertThat(dto.getBody()).isEqualTo(notice.getBody());
            assertThat(dto.getLikes()).isEqualTo(15);
            assertThat(dto.getViews()).isEqualTo(notice.getViews());
        }
    }

    @Test
    @DisplayName("새롭게 잘 생성되는지?")
    public void create() {
        // given
        User user = UserMock.createDummyMajor(99L);
        Notice notice = NoticeMock.create(user, 3L);

        List<MultipartFile> images = MultipartFileMock.createList(10);
        List<MultipartFile> files = MultipartFileMock.createList(10);
        RequestCreateNoticeDto dto = new RequestCreateNoticeDto("title", "body", null, images, files);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(noticeRepository.save(any())).thenReturn(notice);
        when(imageUploadService.newContext()).thenReturn(imageUploadContext);
        when(fileUploadService.newContext()).thenReturn(fileUploadContext);


        // when
        Long noticeId = noticeService.create(noticeRepository, 2L, dto);

        // then
        assertThat(noticeId).isEqualTo(3L);

        verify(imageUploadContext).uploadImages(argThat(fileList -> {
            assertThat(fileList).hasSize(images.size());
            for (int i = 0; i < images.size(); i++) {
                assertThat(fileList.get(i).getOriginalFilename()).isEqualTo(images.get(i).getOriginalFilename());
            }
            return true;
        }), any());

        verify(fileUploadContext).uploadFiles(argThat(fileList -> {
            assertThat(fileList).hasSize(files.size());
            for (int i = 0; i < files.size(); i++) {
                assertThat(fileList.get(i).getOriginalFilename()).isEqualTo(files.get(i).getOriginalFilename());
            }
            return true;
        }), any());

        verify(noticeRepository).save(argThat(entity -> {
            assertThat(entity.getUser()).isEqualTo(user);
            return true;
        }));
    }

    @Test
    @DisplayName("태그를 명시하며 생성하기")
    public void createWithTag() {
        // given
        User user = UserMock.createDummyMajor(99L);
        Notice notice = NoticeMock.create(user, 3L);
        List<Long> tagIds = List.of(10L, 11L, 12L, 13L);

        RequestCreateNoticeDto dto = new RequestCreateNoticeDto("title", "body", tagIds, List.of(), List.of());
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(noticeRepository.save(any())).thenReturn(notice);
        when(imageUploadService.newContext()).thenReturn(imageUploadContext);
        when(fileUploadService.newContext()).thenReturn(fileUploadContext);

        // when
        Long noticeId = noticeService.create(noticeRepository, 2L, dto);

        // then
        assertThat(noticeId).isEqualTo(3L);

        verify(noticeRepository).save(argThat(entity -> {
            assertThat(entity.getUser()).isEqualTo(user);
            return true;
        }));
        verify(tagService).addTagsToPost(any(), eq(tagIds));
    }


    @Test
    @DisplayName("생성할 때 유저가 없으면 오류")
    public void failedCreateByNotFoundUser() {
        // given
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () ->
                noticeService.create(noticeRepository, 2L,
                        new RequestCreateNoticeDto("title", "body", List.of(), List.of(), List.of())));
    }

    @Test
    @DisplayName("단건 조회가 잘 동작하는지?")
    public void findOne() {
        // given
        Notice notice = NoticeMock.createDummy(4L);
        when(noticeRepository.findById(any())).thenReturn(Optional.of(notice));
        when(postLikeService.isLiked(any(), any(), eq(POST))).thenReturn(false);

        // when
        ResponseSingleGenericPostDto dto = noticeService.findOne(noticeRepository, 4L,
                notice.getUser().getId(), notice.getUser().getUserRole(), "Addr");

        // then
        verify(viewCountService).increasePostViews(argThat(post -> {
            assertThat(post.getId()).isEqualTo(notice.getId());
            return true;
        }), eq("Addr"));

        assertThat(dto.getId()).isEqualTo(4L);
        assertThat(dto.isLiked()).isEqualTo(false);
        assertThat(dto.isMine()).isEqualTo(true);
    }


    @Test
    @DisplayName("없는 게시글 단건 조회시 오류")
    public void failedFindOneByNotFound() {
        // given
        when(noticeRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(PostNotFoundException.class, () ->
                noticeService.findOne(noticeRepository, 0L, 4L, UserRole.USER, "Addr"));
    }

    @Test
    @DisplayName("없는 게시글 삭제시 오류")
    public void failedDeleteByNotFound() {
        // given
        when(noticeRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(PostNotFoundException.class, () ->
                noticeService.delete(noticeRepository, 0L, 0L, false));
    }

    @Test
    @DisplayName("권한 없는 게시글 삭제시 오류")
    public void failedDeleteByAccessDenied() {
        // given
        Notice notice = NoticeMock.createDummy(4L);
        when(noticeRepository.findById(any())).thenReturn(Optional.of(notice));

        // when & then
        assertThrows(NotGrantedException.class, () ->
                noticeService.delete(noticeRepository, 0L, 0L, false));
    }
}