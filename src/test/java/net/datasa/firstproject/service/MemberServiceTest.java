package net.datasa.firstproject.service;

import net.datasa.firstproject.dto.MemberDTO;
import net.datasa.firstproject.entity.MemberEntity;
import net.datasa.firstproject.exception.*;
import net.datasa.firstproject.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(value = org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private MemberDTO createDto(String userId, String userName, String pw, String confirmPw, String phone) {
        MemberDTO dto = new MemberDTO();
        dto.setUserId(userId);
        dto.setUserName(userName);
        dto.setPassword(pw);
        dto.setConfirmPassword(confirmPw);
        dto.setPhoneNumber(phone);
        return dto;
    }

    // ===== 보호 리소스 접근 테스트 =====
    @Order(1)
    @Test
    @DisplayName("세션 - 세션에 userId가 없으면 예외 발생")
    void validateAccess_noUserId_throwsException() {
        assertThatThrownBy(() -> memberService.validateAccess(null))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("로그인이 필요한 서비스입니다");
    }

    // ===== 유효성 검증 테스트 (공백) =====

    @Order(2)
    @Test
    @DisplayName("회원가입 - 아이디 공백 오류")
    void saveUser_userIdIsNull_throwsException() {
        MemberDTO dto = createDto("", "홍길동", "password123!", "password123!", "01012345678");

        assertThatThrownBy(() -> memberService.saveUser(dto))
                .isInstanceOf(MissingFieldException.class)
                .hasMessageContaining("아이디를 입력해 주세요");
    }

    @Order(3)
    @Test
    @DisplayName("회원가입 - 이름 공백 오류")
    void saveUser_userNameIsNull_throwsException() {
        MemberDTO dto = createDto("user123", "", "password123!", "password123!", "01012345678");

        assertThatThrownBy(() -> memberService.saveUser(dto))
                .isInstanceOf(MissingFieldException.class)
                .hasMessageContaining("이름을 입력해 주세요");
    }

    @Order(4)
    @Test
    @DisplayName("회원가입 - 비밀번호 공백 오류")
    void saveUser_passwordIsNull_throwsException() {
        MemberDTO dto = createDto("user123", "홍길동", "", "password123!", "01012345678");

        assertThatThrownBy(() -> memberService.saveUser(dto))
                .isInstanceOf(MissingFieldException.class)
                .hasMessageContaining("비밀번호를 입력해 주세요");
    }

    @Order(5)
    @Test
    @DisplayName("회원가입 - 비밀번호 확인 공백 오류")
    void saveUser_confirmPasswordIsNull_throwsException() {
        MemberDTO dto = createDto("user123", "홍길동", "password123!", "", "01012345678");

        assertThatThrownBy(() -> memberService.saveUser(dto))
                .isInstanceOf(MissingFieldException.class)
                .hasMessageContaining("비밀번호 확인을 입력해 주세요");
    }

    @Order(6)
    @Test
    @DisplayName("회원가입 - 휴대전화번호 공백 오류")
    void saveUser_phoneIsNull_throwsException() {
        MemberDTO dto = createDto("user123", "홍길동", "password123!", "password123!", "");

        assertThatThrownBy(() -> memberService.saveUser(dto))
                .isInstanceOf(MissingFieldException.class)
                .hasMessageContaining("휴대전화번호를 입력해 주세요");
    }

    // ===== 유효성 검증 테스트 (정규식) =====

    @Order(7)
    @Test
    @DisplayName("회원가입 - 아이디 형식 오류 (2자)")
    void saveUser_userIdTooShort_throwsException() {
        MemberDTO dto = createDto("us", "홍길동", "password123!", "password123!", "01012345678");

        assertThatThrownBy(() -> memberService.saveUser(dto))
                .isInstanceOf(InvalidUserIdException.class)
                .hasMessageContaining("아이디는 3~14자");
    }

    @Order(8)
    @Test
    @DisplayName("회원가입 - 아이디 형식 오류 (15자)")
    void saveUser_userIdTooLong_throwsException() {
        MemberDTO dto = createDto("user01234567890", "홍길동", "password123!", "password123!", "01012345678");

        assertThatThrownBy(() -> memberService.saveUser(dto))
                .isInstanceOf(InvalidUserIdException.class)
                .hasMessageContaining("아이디는 3~14자");
    }

    @Order(9)
    @Test
    @DisplayName("회원가입 - 아이디 형식 오류 (한글 포함)")
    void saveUser_userIdInvalidCharacter_throwsException() {
        MemberDTO dto = createDto("사용자123", "홍길동", "password123!", "password123!", "01012345678");

        assertThatThrownBy(() -> memberService.saveUser(dto))
                .isInstanceOf(InvalidUserIdException.class)
                .hasMessageContaining("아이디는 3~14자");
    }

    @Order(10)
    @Test
    @DisplayName("회원가입 - 비밀번호 형식 오류 (7자)")
    void saveUser_passwordTooShort_throwsException() {
        MemberDTO dto = createDto("user123", "홍길동", "pass12!", "pass12!", "01012345678");

        assertThatThrownBy(() -> memberService.saveUser(dto))
                .isInstanceOf(InvalidPasswordException.class)
                .hasMessageContaining("비밀번호는 8~14자");
    }

    @Order(11)
    @Test
    @DisplayName("회원가입 - 이름 형식 오류 (1자)")
    void saveUser_userNameTooShort_throwsException() {
        MemberDTO dto = createDto("user123", "홍", "password123!", "password123!", "01012345678");

        assertThatThrownBy(() -> memberService.saveUser(dto))
                .isInstanceOf(InvalidUserNameException.class)
                .hasMessageContaining("이름은 2~20자");
    }

    @Order(12)
    @Test
    @DisplayName("회원가입 - 이름 형식 오류 (영문 포함)")
    void saveUser_userNameInvalidCharacter_throwsException() {
        MemberDTO dto = createDto("user123", "Hong길동", "password123!", "password123!", "01012345678");

        assertThatThrownBy(() -> memberService.saveUser(dto))
                .isInstanceOf(InvalidUserNameException.class)
                .hasMessageContaining("이름은 2~20자, 한글만");
    }

    @Order(13)
    @Test
    @DisplayName("회원가입 - 전화번호 형식 오류 (9자)")
    void saveUser_phoneNumberTooShort_throwsException() {
        MemberDTO dto = createDto("user123", "홍길동", "password123!", "password123!", "010123456");

        assertThatThrownBy(() -> memberService.saveUser(dto))
                .isInstanceOf(InvalidPhoneNumberException.class)
                .hasMessageContaining("휴대전화번호는 10~11자");
    }

    @Order(14)
    @Test
    @DisplayName("회원가입 - 전화번호 형식 오류 (하이픈 포함)")
    void saveUser_phoneNumberInvalidCharacter_throwsException() {
        MemberDTO dto = createDto("user123", "홍길동", "password123!", "password123!", "010-1234-5678");

        assertThatThrownBy(() -> memberService.saveUser(dto))
                .isInstanceOf(InvalidPhoneNumberException.class)
                .hasMessageContaining("휴대전화번호는 10~11자, 숫자만");
    }

    // ===== 아이디 중복 검사 테스트 =====

    @Order(15)
    @Test
    @DisplayName("회원가입 - 아이디 중복 시 예외 발생")
    void saveUser_duplicateUserId_throwsException() {
        // given
        MemberDTO dto = createDto("user123", "홍길동", "password123!", "password123!", "01012345678");
        when(memberRepository.existsByUserId("user123")).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.saveUser(dto))
                .isInstanceOf(DuplicateUserIdException.class)
                .hasMessageContaining("이미 사용 중인 아이디입니다");

        verify(memberRepository, never()).save(any(MemberEntity.class));
    }

    // ===== 전화번호 포맷팅 테스트 =====

    @Order(16)
    @Test
    @DisplayName("회원가입 - 전화번호 포맷팅 (10자리)")
    void saveUser_phoneNumberFormatting_10digits() {
        // given
        MemberDTO dto = createDto("user123", "홍길동", "password123!", "password123!", "0101234567");
        when(memberRepository.existsByUserId("user123")).thenReturn(false);

        // when
        memberService.saveUser(dto);

        // then
        ArgumentCaptor<MemberEntity> captor = ArgumentCaptor.forClass(MemberEntity.class);
        verify(memberRepository, times(1)).save(captor.capture());

        MemberEntity saved = captor.getValue();
        assertThat(saved.getPhoneNumber()).isEqualTo("010-123-4567");
    }

    @Order(17)
    @Test
    @DisplayName("회원가입 - 전화번호 포맷팅 (11자리)")
    void saveUser_phoneNumberFormatting_11digits() {
        // given
        MemberDTO dto = createDto("user123", "홍길동", "password123!", "password123!", "01012345678");
        when(memberRepository.existsByUserId("user123")).thenReturn(false);

        // when
        memberService.saveUser(dto);

        // then
        ArgumentCaptor<MemberEntity> captor = ArgumentCaptor.forClass(MemberEntity.class);
        verify(memberRepository, times(1)).save(captor.capture());

        MemberEntity saved = captor.getValue();
        assertThat(saved.getPhoneNumber()).isEqualTo("010-1234-5678");
    }

    @Order(18)
    @Test
    @DisplayName("회원가입 - 모든 유효성 검증 통과 시 저장 성공")
    void saveUser_validDto_savesSuccessfully() {
        // given
        MemberDTO dto = createDto("user123", "홍길동", "password123!", "password123!", "01012345678");
        when(memberRepository.existsByUserId("user123")).thenReturn(false);

        // when
        memberService.saveUser(dto);

        // then
        ArgumentCaptor<MemberEntity> captor = ArgumentCaptor.forClass(MemberEntity.class);
        verify(memberRepository, times(1)).save(captor.capture());

        MemberEntity saved = captor.getValue();
        assertThat(saved.getUserId()).isEqualTo("user123");
        assertThat(saved.getUserName()).isEqualTo("홍길동");
        assertThat(saved.getPassword()).isEqualTo("password123!");
        assertThat(saved.getConfirmPassword()).isEqualTo("password123!");
        assertThat(saved.getPhoneNumber()).isEqualTo("010-1234-5678");
    }

    // ===== 기본 회원가입 테스트 =====

    @Order(19)
    @Test
    @DisplayName("회원가입 - DTO null 시 예외 발생")
    void saveUser_nullDto_throwsException() {
        assertThatThrownBy(() -> memberService.saveUser(null))
                .isInstanceOf(MissingFieldException.class)
                .hasMessageContaining("회원가입 정보가 전달되지 않았습니다");
    }

    @Order(20)
    @Test
    @DisplayName("회원가입 - 비밀번호 불일치 시 예외 발생")
    void saveUser_passwordMismatch_throwsException() {
        // given
        MemberDTO dto = createDto("user1", "홍길동", "password123!", "password123@", "01012345678");

        // when & then
        assertThatThrownBy(() -> memberService.saveUser(dto))
                .isInstanceOf(PasswordMismatchException.class)
                .hasMessageContaining("비밀번호가 일치하지 않습니다");

        verify(memberRepository, never()).save(any(MemberEntity.class));
    }


    // ===== 로그인 테스트 =====

    @Order(21)
    @Test
    @DisplayName("로그인 - 아이디 미입력 시 예외 발생")
    void login_emptyUserId_throwsException() {
        assertThatThrownBy(() -> memberService.login("", "password"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("아이디와 비밀번호를 모두 입력해 주세요");
    }

    @Order(22)
    @Test
    @DisplayName("로그인 - 비밀번호 미입력 시 예외 발생")
    void login_emptyPassword_throwsException() {
        assertThatThrownBy(() -> memberService.login("user123", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("아이디와 비밀번호를 모두 입력해 주세요");
    }

    @Order(23)
    @Test
    @DisplayName("로그인 - 존재하지 않는 아이디")
    void login_userNotFound_throwsException() {
        // given
        when(memberRepository.findByUserId("nonexistent")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.login("nonexistent", "password"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("아이디와 비밀번호를 정확히 입력해 주세요.");
    }

    @Order(24)
    @Test
    @DisplayName("로그인 - 비밀번호 불일치")
    void login_passwordMismatch_throwsException() {
        // given
        MemberEntity entity = new MemberEntity();
        entity.setUserId("user123");
        entity.setPassword("correctPassword");
        when(memberRepository.findByUserId("user123")).thenReturn(Optional.of(entity));

        // when & then
        assertThatThrownBy(() -> memberService.login("user123", "wrongPassword"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("아이디와 비밀번호를 정확히 입력해 주세요.");
    }

    @Order(25)
    @Test
    @DisplayName("로그인 - 성공 시 사용자 정보 DTO 반환")
    void login_success_returnsUserDto() {
        // given
        MemberEntity entity = new MemberEntity();
        entity.setUserId("user123");
        entity.setUserName("홍길동");
        entity.setPassword("password123");
        entity.setPhoneNumber("010-1234-5678");
        when(memberRepository.findByUserId("user123")).thenReturn(Optional.of(entity));

        // when
        MemberDTO result = memberService.login("user123", "password123");

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo("user123");
        assertThat(result.getUserName()).isEqualTo("홍길동");
        assertThat(result.getPhoneNumber()).isEqualTo("010-1234-5678");
        assertThat(result.getPassword()).isNull(); // 비밀번호는 반환하지 않음
    }
}
