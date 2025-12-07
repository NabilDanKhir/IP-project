const API_BASE = '/api';

function showMessage(elementId, message, isError = false) {
    const messageEl = document.getElementById(elementId);
    if (!messageEl) return;
    messageEl.textContent = message;
    messageEl.style.color = isError ? 'red' : 'green';
    messageEl.style.display = 'block';
}

document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');

    // Form will submit normally to /login POST endpoint
    // which creates a server-side session
    
    // Commented out API-based login - using session-based instead
    /*
    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
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
                        window.location.href = '/dashboard';
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
    */

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
