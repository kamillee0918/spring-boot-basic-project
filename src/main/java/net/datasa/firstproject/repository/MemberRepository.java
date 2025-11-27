package net.datasa.firstproject.repository;

import net.datasa.firstproject.entity.MemberEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * MemberRepository
 * - 회원 엔티티(MemberEntity)에 대한 기본 CRUD 기능을 제공하는 Spring Data JPA 리포지토리입니다.
 * - userId를 기준으로 조회 및 중복 검사를 위한 커스텀 메서드를 제공합니다.
 */
@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String> {

    /**
     * 사용자 아이디로 회원 엔티티 조회
     * @param userId 사용자 아이디
     * @return 회원 엔티티 Optional
     */
    Optional<MemberEntity> findByUserId(String userId);

    /**
     * 사용자 아이디 중복 검사
     * @param userId 사용자 아이디
     * @return 존재 여부
     */
    boolean existsByUserId(String userId);
}
