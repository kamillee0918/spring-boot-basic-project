package net.datasa.firstproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * MemberEntity
 * - project_user 테이블과 매핑되는 JPA 엔티티입니다.
 * - 샘플 단계로 password/confirm_password 컬럼을 모두 보유하고 있으며,
 *   추후 confirm_password 컬럼은 제거/리팩토링될 수 있습니다.
 */
@Data
@Entity
@Table(name = "project_user")
public class MemberEntity {
    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private String userId; // 사용자 아이디

    @Column(name = "user_name", nullable = false)
    private String userName; // 사용자 이름(본명)

    @Column(name = "password", nullable = false)
    private String password; // 사용자 비밀번호

    @Column(name = "confirm_password", nullable = false)
    private String confirmPassword; // 사용자 비밀번호 확인

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber; // 사용자 휴대전화번호
}
