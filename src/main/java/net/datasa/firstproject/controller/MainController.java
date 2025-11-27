package net.datasa.firstproject.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class MainController {
    /**
     * 메인 페이지 라우팅 메서드
     * - 세션에 저장된 사용자 아이디(userId)가 존재하면 화면에서 환영 메시지 등에 활용할 수 있도록 모델에 추가합니다.
     * - 로그인 여부와 무관하게 index 템플릿을 반환합니다.
     *
     * @param model   뷰 템플릿으로 데이터 전달을 위한 Model 객체
     * @param session 현재 사용자의 HttpSession (로그인 사용자 식별용 userId를 저장/조회)
     * @return index 템플릿 이름
     */
    @GetMapping({"", "/"})
    public String index(
            Model model,
            HttpSession session
    ) {
        log.debug("[MainController.index] 호출 완료.");
        model.addAttribute("userId", session);
        return "index";
    }
}
