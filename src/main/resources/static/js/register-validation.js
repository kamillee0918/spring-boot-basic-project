/**
 * 회원가입 페이지 유효성 검증 스크립트
 * SweetAlert2를 사용하여 에러 메시지 표시
 */
document.addEventListener('DOMContentLoaded', function () {
    // 서버 측 에러 메시지가 있을 경우 SweetAlert2로 표시
    const errorElement = document.querySelector('.alert-danger span');
    if (errorElement) {
        const errorMessage = errorElement.textContent.trim();
        Swal.fire({
            icon: 'error',
            title: '회원가입 실패',
            text: errorMessage,
            confirmButtonText: '확인',
            confirmButtonColor: '#0d6efd'
        }).then(() => {
            // 에러 발생 시 비밀번호 필드 초기화 및 포커스
            document.getElementById('floatingPassword').value = '';
            document.getElementById('floatingConfirmPassword').value = '';
            document.getElementById('floatingPassword').focus();
        });
    }

    // 클라이언트 사이드 유효성 검증
    const registerForm = document.getElementById('registerForm');
    registerForm.addEventListener('submit', function (e) {
        e.preventDefault();

        const userId = document.getElementById('floatingUserId').value.trim();
        const password = document.getElementById('floatingPassword').value.trim();
        const confirmPassword = document.getElementById('floatingConfirmPassword').value.trim();
        const userName = document.getElementById('floatingUserName').value.trim();
        const phoneNumber = document.getElementById('floatingPhoneNumber').value.trim();

        // userId 검증: 3~14자, 영문/숫자/특수문자
        const userIdPattern = /^[A-Za-z0-9!@#$%^&*()_+=\-]{3,14}$/;
        if (!userId) {
            Swal.fire({
                icon: 'warning',
                title: '아이디를 입력해주세요',
                confirmButtonText: '확인',
                confirmButtonColor: '#0d6efd'
            }).then(() => document.getElementById('floatingInput').focus());
            return;
        }
        if (!userIdPattern.test(userId)) {
            Swal.fire({
                icon: 'warning',
                title: '아이디 형식 오류',
                text: '아이디는 3~14자, 영문/숫자/특수문자만 가능합니다.',
                confirmButtonText: '확인',
                confirmButtonColor: '#0d6efd'
            }).then(() => document.getElementById('floatingInput').focus());
            return;
        }

        // password 검증: 8~14자, 영문/숫자/특수문자
        const passwordPattern = /^[A-Za-z0-9!@#$%^&*()_+=\-]{8,14}$/;
        if (!password) {
            Swal.fire({
                icon: 'warning',
                title: '비밀번호를 입력해주세요',
                confirmButtonText: '확인',
                confirmButtonColor: '#0d6efd'
            }).then(() => document.getElementById('floatingPassword').focus());
            return;
        }
        if (!passwordPattern.test(password)) {
            Swal.fire({
                icon: 'warning',
                title: '비밀번호 형식 오류',
                text: '비밀번호는 8~14자, 영문/숫자/특수문자만 가능합니다.',
                confirmButtonText: '확인',
                confirmButtonColor: '#0d6efd'
            }).then(() => document.getElementById('floatingPassword').focus());
            return;
        }

        // confirmPassword 검증
        if (!confirmPassword) {
            Swal.fire({
                icon: 'warning',
                title: '비밀번호 확인을 입력해주세요',
                confirmButtonText: '확인',
                confirmButtonColor: '#0d6efd'
            }).then(() => document.getElementById('floatingConfirmPassword').focus());
            return;
        }
        if (password !== confirmPassword) {
            Swal.fire({
                icon: 'warning',
                title: '비밀번호 불일치',
                text: '비밀번호가 일치하지 않습니다.',
                confirmButtonText: '확인',
                confirmButtonColor: '#0d6efd'
            }).then(() => {
                document.getElementById('floatingConfirmPassword').value = '';
                document.getElementById('floatingConfirmPassword').focus();
            });
            return;
        }

        // userName 검증: 2~20자, 한글만
        const userNamePattern = /^[가-힣]{2,20}$/;
        if (!userName) {
            Swal.fire({
                icon: 'warning',
                title: '이름을 입력해주세요',
                confirmButtonText: '확인',
                confirmButtonColor: '#0d6efd'
            }).then(() => document.getElementById('floatingUserName').focus());
            return;
        }
        if (!userNamePattern.test(userName)) {
            Swal.fire({
                icon: 'warning',
                title: '이름 형식 오류',
                text: '이름은 2~20자, 한글만 가능합니다.',
                confirmButtonText: '확인',
                confirmButtonColor: '#0d6efd'
            }).then(() => document.getElementById('floatingUserName').focus());
            return;
        }

        // phoneNumber 검증: 10~11자, 숫자만
        const phonePattern = /^[0-9]{10,11}$/;
        if (!phoneNumber) {
            Swal.fire({
                icon: 'warning',
                title: '휴대전화번호를 입력해주세요',
                confirmButtonText: '확인',
                confirmButtonColor: '#0d6efd'
            }).then(() => document.getElementById('floatingPhoneNumber').focus());
            return;
        }
        if (!phonePattern.test(phoneNumber)) {
            Swal.fire({
                icon: 'warning',
                title: '휴대전화번호 형식 오류',
                text: '휴대전화번호는 10~11자, 숫자만 가능합니다.',
                confirmButtonText: '확인',
                confirmButtonColor: '#0d6efd'
            }).then(() => document.getElementById('floatingPhoneNumber').focus());
            return;
        }

        // 모든 검증 통과 시 폼 제출
        registerForm.submit();
    });
});
