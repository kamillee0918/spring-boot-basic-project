package net.datasa.firstproject.service;

import lombok.extern.slf4j.Slf4j;
import net.datasa.firstproject.dto.MemberDTO;
import net.datasa.firstproject.entity.MemberEntity;
import net.datasa.firstproject.exception.*;
import net.datasa.firstproject.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    // 정규식 패턴
    private static final Pattern USER_ID_PATTERN = Pattern.compile("^[A-Za-z0-9!@#$%^&*()_+=\\-]{3,14}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[A-Za-z0-9!@#$%^&*()_+=\\-]{8,14}$");
    private static final Pattern USER_NAME_PATTERN = Pattern.compile("^[가-힣]{2,20}$");
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^[0-9]{10,11}$");

    @Autowired
    /**
     * 생성자 주입
     * - 스프링이 MemberRepository 빈을 주입하여 데이터 액세스 기능을 제공합니다.
     *
     * @param repository 사용자 엔티티에 대한 CRUD를 담당하는 리포지토리 빈
     */
    public MemberService(MemberRepository repository) {
        memberRepository = repository;
    }

    /**
     * 모든 사용자 조회 메서드
     * - DB에서 전체 사용자(Entity)를 조회하여 화면/컨트롤러 계층에서 쓰기 쉽게 DTO 리스트로 변환합니다.
     * - 현재는 단순 매핑만 수행합니다.
     *
     * @return 전체 사용자 정보 DTO 리스트
     */
    public List<MemberDTO> findAllUser() {
        log.debug("[UserService.findAllUser] 호출 완료.");

        // 1. DTO를 담을 ArrayList 생성
        List<MemberDTO> dtoList = new ArrayList<>();

        // 2&3. 반복문으로 DTO 객체 생성해서 entity 값을 저장 및 ArrayList에 추가
        for (MemberEntity entity : memberRepository.findAll()) {
            MemberDTO dto = new MemberDTO();
            dto.setUserId(entity.getUserId());
            dto.setUserName(entity.getUserName());
            dto.setPassword(entity.getPassword());
            dto.setConfirmPassword(entity.getConfirmPassword());
            dto.setPhoneNumber(entity.getPhoneNumber());
            dtoList.add(dto);
        }

        // 4. ArrayList를 반환
        return dtoList;
    }

    /**
     * 사용자 저장(회원가입) 메서드
     * - 유효성 검증 규칙을 모두 적용합니다.
     * - 아이디 중복, 비밀번호 일치, 정규식 검증, 전화번호 포맷팅을 수행합니다.
     *
     * @param dto 회원가입 폼 데이터 DTO
     * @throws IllegalArgumentException 유효성 검증 실패 시
     */
    public void saveUser(MemberDTO dto) {
        log.debug("[MemberService.saveUser] 호출 - userId: {}", dto != null ? dto.getUserId() : "null");

        // 1) DTO null 체크
        if (dto == null) {
            throw new MissingFieldException("회원가입 정보가 전달되지 않았습니다.", true);
        }

        // 2) 필수 필드 null/공백 체크
        if (isNullOrEmpty(dto.getUserId())) {
            throw new MissingFieldException("아이디");
        }
        if (isNullOrEmpty(dto.getPassword())) {
            throw new MissingFieldException("비밀번호");
        }
        if (isNullOrEmpty(dto.getConfirmPassword())) {
            throw new MissingFieldException("비밀번호 확인");
        }
        if (isNullOrEmpty(dto.getUserName())) {
            throw new MissingFieldException("이름");
        }
        if (isNullOrEmpty(dto.getPhoneNumber())) {
            throw new MissingFieldException("휴대전화번호");
        }

        // 3) 정규식 검증
        if (!USER_ID_PATTERN.matcher(dto.getUserId()).matches()) {
            throw new InvalidUserIdException();
        }
        if (!PASSWORD_PATTERN.matcher(dto.getPassword()).matches()) {
            throw new InvalidPasswordException();
        }
        if (!PASSWORD_PATTERN.matcher(dto.getConfirmPassword()).matches()) {
            throw new InvalidPasswordException("비밀번호 확인은 8~14자, 영문/숫자/특수문자만 가능합니다.");
        }
        if (!USER_NAME_PATTERN.matcher(dto.getUserName()).matches()) {
            throw new InvalidUserNameException();
        }
        if (!PHONE_NUMBER_PATTERN.matcher(dto.getPhoneNumber()).matches()) {
            throw new InvalidPhoneNumberException();
        }

        // 4) 비밀번호 일치 검증
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordMismatchException();
        }

        // 5) 아이디 중복 검사
        if (memberRepository.existsByUserId(dto.getUserId())) {
            throw new DuplicateUserIdException();
        }

        // 6) 전화번호 포맷팅 (010-1234-5678)
        String formattedPhoneNumber = formatPhoneNumber(dto.getPhoneNumber());

        // 7) Entity 생성 및 매핑
        MemberEntity entity = new MemberEntity();
        entity.setUserId(dto.getUserId());
        entity.setUserName(dto.getUserName());
        entity.setPassword(dto.getPassword());
        entity.setConfirmPassword(dto.getConfirmPassword());
        entity.setPhoneNumber(formattedPhoneNumber);

        // 8) Repository를 통한 저장
        memberRepository.save(entity);

        log.debug("[MemberService.saveUser] 저장 완료. entity = {}", entity);
    }

    /**
     * 로그인 메서드
     * - userId와 password를 DB와 대조하여 인증합니다.
     * - 성공 시 사용자 정보를 담은 DTO를 반환합니다.
     *
     * @param userId   사용자 아이디
     * @param password 사용자 비밀번호
     * @return 인증된 사용자 정보 DTO
     * @throws IllegalArgumentException 인증 실패 시
     */
    public MemberDTO login(String userId, String password) {
        log.debug("[MemberService.login] 호출 - userId: {}", userId);

        // 1) 필수 파라미터 검증
        if (isNullOrEmpty(userId) || isNullOrEmpty(password)) {
            throw new IllegalArgumentException("아이디와 비밀번호를 모두 입력해 주세요.");
        }

        // 2) DB에서 사용자 조회
        Optional<MemberEntity> optionalEntity = memberRepository.findByUserId(userId);
        if (optionalEntity.isEmpty()) {
            throw new IllegalArgumentException("아이디와 비밀번호를 정확히 입력해 주세요.");
        }

        MemberEntity entity = optionalEntity.get();

        // 3) 비밀번호 대조
        if (!entity.getPassword().equals(password)) {
            throw new IllegalArgumentException("아이디와 비밀번호를 정확히 입력해 주세요.");
        }

        // 4) 로그인 성공 - DTO 반환
        MemberDTO dto = new MemberDTO();
        dto.setUserId(entity.getUserId());
        dto.setUserName(entity.getUserName());
        dto.setPhoneNumber(entity.getPhoneNumber());
        // 비밀번호는 노출하지 않음

        log.debug("[MemberService.login] 로그인 성공 - userName: {}", dto.getUserName());
        return dto;
    }

    /**
     * 전화번호 포맷팅 메서드
     * - 10~11자리 숫자 문자열을 010-1234-5678 형태로 변환합니다.
     *
     * @param phoneNumber 숫자만 포함된 전화번호 (10~11자리)
     * @return 포맷팅된 전화번호
     */
    private String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() == 10) {
            // 예: 0101234567 -> 010-123-4567
            return phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 6) + "-" + phoneNumber.substring(6);
        } else if (phoneNumber.length() == 11) {
            // 예: 01012345678 -> 010-1234-5678
            return phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 7) + "-" + phoneNumber.substring(7);
        }
        // 정규식 검증을 통과했다면 여기 도달하지 않지만, 안전장치
        return phoneNumber;
    }

    /**
     * 문자열 null 또는 공백 체크
     */
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
