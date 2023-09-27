document.addEventListener('DOMContentLoaded', () => {
    const registerForm = document.getElementById('registerForm');
    const loginForm = document.getElementById('loginForm');


    registerForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const formData = new FormData(registerForm);
        fetch('http://localhost:8080/api/v1/auth/register', {
            method: 'POST',
            body: JSON.stringify(Object.fromEntries(formData)),
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                // Добавьте обработчик ответа с сервера, например, сообщение об успешной регистрации
                console.log(data);
            })
            .catch(error => console.error('Ошибка:', error));
    });

    loginForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const formData = new FormData(loginForm);
        fetch('http://localhost:8080/api/v1/auth/authentication', {
            method: 'POST',
            body: JSON.stringify(Object.fromEntries(formData)),
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                // Добавьте обработчик ответа с сервера, например, сохраните токен и перенаправьте на кабинет пользователя
                console.log(data);
            })
            .catch(error => console.error('Ошибка:', error));
    });
});