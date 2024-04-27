package com.dku.council.debug.controller;

import com.dku.council.debug.service.ErrorLogService;
import com.dku.council.domain.ticket.model.dto.request.RequestNewTicketEventDto;
import com.dku.council.domain.ticket.model.entity.TicketEvent;
import com.dku.council.domain.ticket.repository.TicketEventRepository;
import com.dku.council.domain.ticket.repository.TicketMemoryRepository;
import com.dku.council.domain.user.exception.AlreadyNicknameException;
import com.dku.council.domain.user.exception.AlreadyPhoneException;
import com.dku.council.domain.user.exception.AlreadyStudentIdException;
import com.dku.council.domain.user.exception.MajorNotFoundException;
import com.dku.council.domain.user.model.UserStatus;
import com.dku.council.domain.user.model.entity.Major;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.MajorRepository;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.global.auth.role.UserRole;
import com.dku.council.global.error.exception.UserNotFoundException;
import com.dku.council.global.util.DateUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Tag(name = "테스트", description = "개발용 테스트 api")
@RestController
@ConditionalOnExpression("${app.enable-test-controller:false}")
@RequiredArgsConstructor
@RequestMapping("/test")
@Slf4j
public class TestController {

    private final UserRepository userRepository;
    private final MajorRepository majorRepository;
    private final PasswordEncoder passwordEncoder;
    private final ErrorLogService errorLogService;

    private final TicketEventRepository ticketEventRepository;
    private final TicketMemoryRepository ticketMemoryRepository;
    private final Clock clock;

    /**
     * 학번으로 유저 삭제.
     * <p>이 유저로 작성한 게시글, 댓글 등이 있는 경우에는 삭제되지 않습니다. 모두 삭제하고 유저를 삭제하세요.</p>
     *
     * @param studentId 학번
     */
    @DeleteMapping("/user")
    public void deleteUser(@RequestParam String studentId) {
        User user = userRepository.findByStudentId(studentId).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }

    /**
     * 유저 강제 회원가입
     * <p>인증없이 강제로 회원가입합니다. 단, 학번, 닉네임, 전화번호는 중복되면 안됩니다.</p>
     *
     * @param studentId 학번
     */
    @PostMapping("/user")
    public void addUser(@RequestParam String studentId,
                        @RequestParam String password,
                        @RequestParam String name,
                        @RequestParam String nickname,
                        @RequestParam Long majorId,
                        @RequestParam Integer yearOfAdmission,
                        @RequestParam String phone,
                        @RequestParam String age,
                        @RequestParam String gender) {
        if (userRepository.findByStudentId(studentId).isPresent()) {
            throw new AlreadyStudentIdException();
        }

        if (userRepository.findByPhone(phone).isPresent()) {
            throw new AlreadyPhoneException();
        }

        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new AlreadyNicknameException();
        }

        Major major = majorRepository.findById(majorId)
                .orElseThrow(MajorNotFoundException::new);

        String encryptedPassword = passwordEncoder.encode(password);

        User user = User.builder()
                .studentId(studentId)
                .password(encryptedPassword)
                .name(name)
                .nickname(nickname)
                .age(age)
                .gender(gender)
                .major(major)
                .yearOfAdmission(yearOfAdmission)
                .academicStatus("재학")
                .phone(phone)
                .status(UserStatus.ACTIVE)
                .role(UserRole.USER)
                .build();

        userRepository.save(user);
    }

    /**
     * 에러 로그 조회
     * <p>trackingId를 통해 에러 로그를 조회합니다.</p>
     *
     * @param trackingId 에러 로그의 trackingId
     * @return 에러 로그
     */
    @GetMapping(value = "/error/{trackingId}", produces = "text/plain;charset=UTF-8")
    public String findError(@PathVariable String trackingId) {
        return errorLogService.findErrorLog(trackingId);
    }

    @PostMapping("/create-event")
    public void createEvent() {
        RequestNewTicketEventDto dto = new RequestNewTicketEventDto(
                "이벤트",
                LocalDateTime.of(2024, 4, 28, 0, 0, 0),
                LocalDateTime.of(2024,5,28, 0, 0, 0),
                1200
        );
        ticketEventRepository.save(dto.createEntity());
        Major major = new Major("소프트웨어학과", "SW융합학부");
        majorRepository.save(major);
    }

    @PostMapping("/ticketing")
    public void ticketing() {
        Major major = majorRepository.findByName("소프트웨어학과", "SW융합학부").orElseThrow(MajorNotFoundException::new);
        User user = User.builder()
                .studentId(UUID.randomUUID().toString().substring(0, 8))
                .name(UUID.randomUUID().toString().substring(0, 5))
                .password(passwordEncoder.encode("1234"))
                .major(major)
                .phone(UUID.randomUUID().toString().substring(0, 11))
                .nickname(UUID.randomUUID().toString().substring(0, 8))
                .academicStatus("재학")
                .age("20")
                .gender("남자")
                .profileImage(null)
                .yearOfAdmission(2020)
                .status(UserStatus.ACTIVE)
                .role(UserRole.USER)
                .build();
        user = userRepository.save(user);
        TicketEvent event = ticketEventRepository.findByName("이벤트").orElseThrow();

        Instant now = Instant.now(clock);
        Instant eventTo = DateUtil.toInstant(event.getEndAt());
        Duration expiresNextKeyAfter = Duration.between(now, eventTo).plusMinutes(30);
        ticketMemoryRepository.enroll(user.getId(), event.getId(), expiresNextKeyAfter);
    }
}
