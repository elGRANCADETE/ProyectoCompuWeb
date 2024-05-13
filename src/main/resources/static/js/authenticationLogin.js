document.querySelector('form').addEventListener('submit', function(e) {
    e.preventDefault();
  
    const formData = new FormData(this);
    const username = formData.get('username');
    const password = formData.get('password');
  
    // Envía una solicitud al servidor para validar las credenciales
    fetch('/validate-login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-CSRF-TOKEN': formData.get('_csrf')
      },
      body: JSON.stringify({ username, password })
    })
    .then(response => response.json())
    .then(data => {
      if (data.userExists && data.passwordCorrect) {
        // Si las credenciales son correctas, redirigir o continuar con la lógica de inicio de sesión
        window.location.href = '/dashboard'; // Ajusta según tu flujo de aplicación
      } else {
        // Mostrar mensaje de error adecuado
        displayError(data.errorMessage);
      }
    })
    .catch(error => {
      console.error('Error:', error);
      displayError('Error de servidor. Inténtelo de nuevo más tarde.');
    });
  });
  
  function displayError(message) {
    const errorDiv = document.getElementById('error-message');
    errorDiv.textContent = message;
    errorDiv.style.display = 'block';
  }
  