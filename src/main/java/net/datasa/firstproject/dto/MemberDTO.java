package net.datasa.firstproject.dto;

import lombok.Data;

/**
 * MemberDTO
 * - 화면(폼)과 컨트롤러/서비스 계층 간에 사용자 데이터를 전달하기 위한 데이터 전송 객체입니다.
 * - Entity와 달리 영속성 컨텍스트와 무관하며, 요청/응답 모델에 적합한 형태로 사용합니다.
 */
@Data
public class MemberDTO {
    private String userId;
    private String userName;
    private String password;
    private String confirmPassword;
    private String phoneNumber;
}
