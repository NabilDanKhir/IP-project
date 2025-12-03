const API_BASE = '/api';

// Show message
function showMessage(elementId, message, isError = false) {
    const messageEl = document.getElementById(elementId);
    if (!messageEl) return;
    messageEl.textContent = message;
    messageEl.style.color = isError ? 'red' : 'green';
    messageEl.style.display = 'block';
}

// If user prefers form-based login, the HTML form uses POST /login — no JS needed.
// But to keep the same JS-based login, we provide the fetch flow below:
document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');

    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            // If you want the JS flow (AJAX), prevent the form's default submit:
            e.preventDefault();

            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;

            try {
                const response = await fetch(`${API_BASE}/auth/login`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ username, password })
                });

                const data = await response.json();

                if (data.success) {
                    localStorage.setItem('token', data.token);
                    localStorage.setItem('user', JSON.stringify(data.user));
                    showMessage('message', 'Login successful! Redirecting...', false);

                    setTimeout(() => {
                        // Redirect to menu (server-side page)
                        window.location.href = '/menu';
                    }, 700);
                } else {
                    showMessage('message', data.message || 'Login failed', true);
                }
            } catch (error) {
                console.error('Login error:', error);
                showMessage('message', 'Login failed. Please try again.', true);
            }
        });
    }

    const token = localStorage.getItem('token');
    if (token) {
        fetch(`${API_BASE}/auth/verify`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
        .then(r => r.json())
        .then(data => {
            if (data.success) {
                window.location.href = '/menu';
            } else {
                localStorage.removeItem('token');
                localStorage.removeItem('user');
            }
        })
        .catch(() => {
            localStorage.removeItem('token');
            localStorage.removeItem('user');
        });
    }
});
