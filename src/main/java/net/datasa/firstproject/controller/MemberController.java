package net.datasa.firstproject.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import net.datasa.firstproject.dto.MemberDTO;
import net.datasa.firstproject.exception.ValidationException;
import net.datasa.firstproject.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    /**
     * 생성자 주입
     * - 스프링 컨테이너가 MemberService 빈을 주입합니다.
     *
     * @param service 회원 관련 비즈니스 로직을 담당하는 서비스 빈
     */
    public MemberController(MemberService service) {
        memberService = service;
    }

    /**
     * 회원가입 화면 진입(GET)
     * - 사용자가 회원가입 폼을 입력할 수 있는 화면으로 이동합니다.
     * - 서버 사이드에서 전달할 데이터가 현재는 없으므로 단순히 템플릿만 반환합니다.
     *
     * @return 회원가입 템플릿 경로(view/member/register)
     */
    @GetMapping("/register")
    public String registerView() {
        log.debug("[GET - MemberController.registerView] 호출 완료.");
        return "view/member/register";
    }

    /**
     * 회원가입 처리(POST)
     * - 화면에서 전달된 폼 데이터를 MemberDTO로 바인딩하여 서비스 계층으로 전달합니다.
     * - 유효성 검증 실패 시 에러 메시지를 전달하고 회원가입 폼으로 돌아갑니다.
     * - 서비스에서 저장이 완료되면 루트("/")로 리디렉션합니다.
     *
     * @param dto   회원가입 폼 데이터 DTO
     * @param model 에러 메시지 전달을 위한 Model
     * @return 루트로 리디렉션 또는 회원가입 폼
     */
    @PostMapping("/register")
    public String register(@ModelAttribute MemberDTO dto, Model model) {
        log.debug("[POST - MemberController.register] 호출 완료.");

        try {
            // Service에서 유효성 검증 및 저장 수행
            memberService.saveUser(dto);
            log.debug("[POST - MemberController.register] 회원가입 성공 - userId: {}", dto.getUserId());
            return "redirect:/"; // Root 라우팅("/")으로 리디렉트
        } catch (ValidationException e) {
            // 유효성 검증 실패 시 에러 메시지 전달
            log.warn("[POST - MemberController.register] 회원가입 실패 - {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("memberDTO", dto); // 입력값 유지
            return "view/member/register"; // 회원가입 폼으로 복귀
        }
    }

    /**
     * 로그인 화면 진입(GET)
     * - 사용자가 로그인 폼을 입력할 수 있는 화면으로 이동합니다.
     *
     * @return 로그인 템플릿 경로(view/member/login)
     */
    @GetMapping({"/login"})
    public String loginView() {
        log.debug("[GET - MemberController.loginView] 호출 완료.");
        return "view/member/login";
    }

    /**
     * 로그인 처리(POST) - Session 방식
     * - 서비스 계층에서 DB 기반 인증을 수행합니다.
     * - 인증 성공 시 세션에 userId, userName을 저장하고 루트로 리디렉션합니다.
     * - 인증 실패 시 에러 메시지를 전달하고 로그인 화면으로 돌아갑니다.
     *
     * @param session  HttpSession 객체(로그인 상태 유지를 위한 사용자 정보 저장)
     * @param userId   사용자 아이디(폼 입력)
     * @param password 사용자 비밀번호(폼 입력)
     * @param model    에러 메시지 전달을 위한 Model
     * @return 리디렉션 경로 또는 로그인 폼
     */
    @PostMapping("/login")
    public String login(
            HttpSession session,
            @RequestParam("userId") String userId,
            @RequestParam("password") String password,
            Model model
    ) {
        log.debug("[POST - MemberController.login] 호출 완료 - userId: {}", userId);

        try {
            // Service를 통한 DB 기반 인증
            MemberDTO authenticatedUser = memberService.login(userId, password);

            // 로그인 성공 - 세션에 사용자 정보 저장
            session.setAttribute("userId", authenticatedUser.getUserId());
            session.setAttribute("userName", authenticatedUser.getUserName());

            log.debug("[POST - MemberController.login] 로그인 성공 - userName: {}", authenticatedUser.getUserName());
            return "redirect:/"; // Root로 리디렉트
        } catch (IllegalArgumentException e) {
            // 인증 실패 시 에러 메시지 전달
            log.warn("[POST - MemberController.login] 로그인 실패 - {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("userId", userId); // 아이디 입력값 유지
            return "view/member/login"; // 로그인 폼으로 복귀
        }
    }

    /**
     * 로그아웃 처리(GET)
     * - 세션에서 userId 속성을 제거하여 로그인 상태를 해제하고 루트로 리디렉션합니다.
     *
     * @param session 현재 사용자의 HttpSession
     * @return 루트로 리디렉션
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        log.debug("[POST - MemberService.logout] 호출 완료.");
        session.removeAttribute("userId");

        // > userId: null
        log.debug("userId: {}", session.getAttribute("userId"));
        return "redirect:/";
    }

    /**
     * 사용자 목록 화면(GET)
     * - 모든 사용자 정보를 조회하여 모델에 담고, 사용자 목록 화면을 렌더링합니다.
     * - 접근 제어(로그인 여부 체크)는 추후 공통 인터셉터/필터로 이관 가능하며, 샘플로 test 엔드포인트에서 확인합니다.
     *
     * @param model 뷰 템플릿으로 데이터 전달을 위한 Model 객체
     * @return 사용자 목록 템플릿 경로(view/member/user-list)
     */
    @GetMapping({"/list"})
    public String userListView(Model model) {
        log.debug("[GET - MemberController.userListView] 호출 완료.");
        model.addAttribute("users", memberService.findAllUser());
        return "view/member/user-list";
    }

    /**
     * 보호 리소스 접근 테스트 메서드
     * - 세션에 저장된 사용자 아이디(userId)가 존재하지 않으면 로그인 페이지로 리디렉션하여 비로그인 사용자의 접근을 차단합니다.
     * - 세션이 존재하면 사용자 목록을 조회하여 사용자 목록 템플릿을 반환합니다.
     *
     * @param model   뷰 템플릿으로 데이터 전달을 위한 Model
     * @param session 현재 사용자 세션
     * @return 로그인 페이지로 리디렉션 또는 사용자 목록 템플릿
     */
    @GetMapping("/test")
    public String test(
            Model model,
            HttpSession session
    ) {
        log.debug("[GET - MemberService.test] 호출 완료.");

        // Session에 userId가 없으면 로그인 페이지로 리디렉트
        if (session.getAttribute("userId") == null) {
            log.debug("### 로그인되지 않은 사용자의 접근 시도");
            return "redirect:/member/login";
        }

        model.addAttribute("users", memberService.findAllUser());

        return "view/member/user-list";
    }
}
