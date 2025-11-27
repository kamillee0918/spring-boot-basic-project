/**
 * 로그인 페이지 유효성 검증 스크립트
 * SweetAlert2를 사용하여 에러 메시지 표시
 */
document.addEventListener('DOMContentLoaded', function () {
    // 서버 측 에러 메시지가 있을 경우 SweetAlert2로 표시
    const errorElement = document.querySelector('.alert-danger span');
    if (errorElement) {
        const errorMessage = errorElement.textContent.trim();
        Swal.fire({
            icon: 'error',
            title: '로그인 실패',
            text: errorMessage,
            confirmButtonText: '확인',
            confirmButtonColor: '#0d6efd'
        }).then(() => {
            // 로그인 실패 시 비밀번호 필드 초기화 및 포커스
            const passwordField = document.getElementById('floatingPassword');
            passwordField.value = '';
            passwordField.focus();
        });
    }

    // 클라이언트 사이드 유효성 검증
    const loginForm = document.getElementById('loginForm');
    loginForm.addEventListener('submit', function (e) {
        const userId = document.getElementById('floatingUserId').value.trim();
        const password = document.getElementById('floatingPassword').value.trim();

        if (!userId) {
            e.preventDefault();
            Swal.fire({
                icon: 'warning',
                title: '아이디를 입력해주세요',
                confirmButtonText: '확인',
                confirmButtonColor: '#0d6efd'
            }).then(() => document.getElementById('floatingUserId').focus());
            return;
        }

        if (!password) {
            e.preventDefault();
            Swal.fire({
                icon: 'warning',
                title: '비밀번호를 입력해주세요',
                confirmButtonText: '확인',
                confirmButtonColor: '#0d6efd'
            }).then(() => document.getElementById('floatingPassword').focus());
            return;
        }
    });
});
