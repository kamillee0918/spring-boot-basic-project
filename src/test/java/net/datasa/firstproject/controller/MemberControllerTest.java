package net.datasa.firstproject.controller;

import net.datasa.firstproject.dto.MemberDTO;
import net.datasa.firstproject.exception.InvalidUserIdException;
import net.datasa.firstproject.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MemberController.class)
@TestMethodOrder(value = org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    // ===== 회원가입 테스트 =====

    @Order(1)
    @Test
    @DisplayName("회원가입 GET - 회원가입 화면 반환")
    void registerView_returnsRegisterPage() throws Exception {
        mockMvc.perform(get("/member/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("view/member/register"));
    }

    @Order(2)
    @Test
    @DisplayName("회원가입 POST - 실패 시 에러 메시지와 함께 회원가입 폼 반환")
    void register_fail_returnsRegisterFormWithError() throws Exception {
        // given
        doThrow(new InvalidUserIdException()).when(memberService).saveUser(any(MemberDTO.class));

        // when & then
        mockMvc.perform(post("/member/register")
                        .param("userId", "ab")
                        .param("password", "password123")
                        .param("confirmPassword", "password123")
                        .param("userName", "홍길동")
                        .param("phoneNumber", "01012345678"))
                .andExpect(status().isOk())
                .andExpect(view().name("view/member/register"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", "아이디는 3~14자, 영문/숫자/특수문자만 가능합니다."));
    }

    @Order(3)
    @Test
    @DisplayName("회원가입 POST - 성공 시 / 로 리디렉션")
    void register_success_redirectsHome() throws Exception {
        // given
        doNothing().when(memberService).saveUser(any(MemberDTO.class));

        // when & then
        mockMvc.perform(post("/member/register")
                        .param("userId", "user123")
                        .param("password", "password123")
                        .param("confirmPassword", "password123")
                        .param("userName", "홍길동")
                        .param("phoneNumber", "01012345678"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    // ===== 로그인 테스트 =====

    @Order(4)
    @Test
    @DisplayName("로그인 GET - 로그인 화면 반환")
    void loginView_returnsLoginPage() throws Exception {
        mockMvc.perform(get("/member/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("view/member/login"));
    }

    @Order(5)
    @Test
    @DisplayName("로그인 POST - 실패 시 에러 메시지와 함께 로그인 폼 반환")
    void login_fail_returnsLoginFormWithError() throws Exception {
        // given
        when(memberService.login("wrongUser", "wrongPass"))
                .thenThrow(new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다"));

        // when & then
        mockMvc.perform(post("/member/login")
                        .param("userId", "wrongUser")
                        .param("password", "wrongPass"))
                .andExpect(status().isOk())
                .andExpect(view().name("view/member/login"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", "아이디 또는 비밀번호가 일치하지 않습니다"));
    }

    @Order(6)
    @Test
    @DisplayName("로그인 POST - 성공 시 세션에 userId, userName 저장 및 / 로 리디렉션")
    void login_success_setsSessionAndRedirectsHome() throws Exception {
        // given
        MemberDTO authenticatedUser = new MemberDTO();
        authenticatedUser.setUserId("testUser1");
        authenticatedUser.setUserName("홍길동");
        when(memberService.login("testUser1", "password123")).thenReturn(authenticatedUser);

        // when
        MvcResult result = mockMvc.perform(post("/member/login")
                        .param("userId", "testUser1")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andReturn();

        // then
        Object userIdInSession = Objects.requireNonNull(result.getRequest().getSession(false)).getAttribute("userId");
        Object userNameInSession = Objects.requireNonNull(result.getRequest().getSession(false)).getAttribute("userName");
        assertThat(userIdInSession).isEqualTo("testUser1");
        assertThat(userNameInSession).isEqualTo("홍길동");
    }

    // ===== 사용자 목록 테스트 =====

    @Order(7)
    @Test
    @DisplayName("사용자 목록 GET - 모델에 users 포함 및 뷰명 반환")
    void userListView_returnsUserListWithModel() throws Exception {
        // given
        when(memberService.findAllUser()).thenReturn(Collections.<MemberDTO>emptyList());

        // when & then
        mockMvc.perform(get("/member/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("view/member/user-list"));
    }
}
