const container = document.querySelector('.container');
const LoginLink = document.querySelector('.SignInLink');
const RegisterLink = document.querySelector('.SignUpLink');

RegisterLink.addEventListener('click', () => {
    container.classList.add('active'); // Hiện form đăng ký
});

LoginLink.addEventListener('click', () => {
    container.classList.remove('active'); // Quay lại form đăng nhập
});
