package com.dku.council.domain.with_dankook.service;

import com.dku.council.domain.post.exception.PostNotFoundException;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.domain.with_dankook.model.dto.list.SummarizedWithDankookDto;
import com.dku.council.domain.with_dankook.model.entity.WithDankook;
import com.dku.council.domain.with_dankook.repository.WithDankookRepository;
import com.dku.council.domain.with_dankook.repository.spec.WithDankookSpec;
import com.dku.council.global.error.exception.NotGrantedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WithDankookService<E extends WithDankook> {

    protected final UserRepository userRepository;

    /**
     * With-Dankook 게시판 글 목록 조회
     */
    @Transactional(readOnly = true)
    public Page<SummarizedWithDankookDto> list(WithDankookRepository<E> repository, Specification<E> spec,
                                               Pageable pageable, int bodySize) {
        Page<E> result = list(repository, spec, pageable);
        return result.map((withDankook) -> makeListDto(bodySize, withDankook));
    }

    @Transactional(readOnly = true)
    public <T> Page<T> list(WithDankookRepository<E> repository, Specification<E> spec, Pageable pageable, int bodySize,
                            PostResultMapper<T, SummarizedWithDankookDto, E> mapper) {
        Page<E> result = list(repository, spec, pageable);
        return result.map((withDankook) -> {
            SummarizedWithDankookDto dto = makeListDto(bodySize, withDankook);
            return mapper.map(dto, withDankook);
        });
    }

    private Page<E> list(WithDankookRepository<E> repository, Specification<E> spec, Pageable pageable) {
        if (spec == null) {
            spec = Specification.where(null);
        }

        spec = spec.and(WithDankookSpec.withActive());

        return repository.findAll(spec, pageable);
    }

    public SummarizedWithDankookDto makeListDto(int bodySize, E withDankook) {
        return new SummarizedWithDankookDto(bodySize, withDankook);
    }

    /**
     * With-Dankook 게시판 글 삭제. 실제 DB에서 삭제는 하지 않는다.
     *
     * @param repository     삭제할 게시글 repository
     * @param withDankookId  삭제할 게시글 id
     * @param userId         삭제 요청한 사용자 id
     * @param isAdmin        삭제 요청한 사용자가 관리자인지
     */
    @Transactional
    public void delete(WithDankookRepository<E> repository, Long withDankookId, Long userId, boolean isAdmin) {
        E withDankook = repository.findById(withDankookId).orElseThrow(PostNotFoundException::new);
        if(isAdmin) {
            withDankook.markAsDeleted(true);
        } else if(withDankook.getMasterUser().getId().equals(userId)) {
            withDankook.markAsDeleted(false);
        } else {
            throw new NotGrantedException();
        }
    }

    @FunctionalInterface
    public interface PostResultMapper<T, D, E extends WithDankook> {
        T map(D dto, E withDankook);
    }
}
