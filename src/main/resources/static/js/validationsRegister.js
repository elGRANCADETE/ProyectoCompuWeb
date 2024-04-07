document.getElementById('registrationForm').addEventListener('submit', function (e) {
  e.preventDefault();

  // Limpiar clases de error previas
  document.querySelectorAll('.input-box.error').forEach(el => {
    el.classList.remove('error');
  });

  // Ocultar mensajes de error previos
  document.querySelectorAll('.error-message').forEach(el => {
    el.style.display = 'none';
  });

  var isValid = true;
  var formData = new FormData(this);

  var csrfToken = formData.get('_csrf');

  // Validar correo electrónico
  var email = formData.get('email');
  if (!/^\S+@\S+\.\S+$/.test(email)) {
    setError('email', 'Please include an "@" in the email address.');
    isValid = false;
  }

  // Validar número de teléfono
  var phoneNumber = formData.get('phoneNumber');
  if (!/^\d{9}$/.test(phoneNumber)) {
    setError('phoneNumber', 'Phone number must be exactly 9 digits.');
    isValid = false;
  }

  // Validar coincidencia de contraseñas
  var password = formData.get('password');
  var confirmPassword = formData.get('confirmPassword');
  if (password !== confirmPassword) {
    setError('confirmPassword', 'Passwords do not match.');
    isValid = false;
  }

  // Si el formulario es válido hasta ahora, verificar disponibilidad
  if (isValid) {
    const username = formData.get('username');
    const email = formData.get('email');
    // Note: No need to pass csrfToken and setError as they are accessible from within the scope
    checkAvailability(username, email);
  }
});

function setError(inputName, message) {
  const formControl = document.querySelector(`input[name="${inputName}"]`).parentNode;
  const errorDisplay = formControl.querySelector('.error-message');
  errorDisplay.innerText = message;
  formControl.classList.add('error');
  errorDisplay.style.display = 'block';
}

function checkAvailability(username, email) {
  const csrfToken = document.querySelector('input[name="_csrf"]').value;
  fetch('/users/check-availability', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'X-CSRF-TOKEN': csrfToken
    },
    body: JSON.stringify({ username, email })
  })
    .then(response => response.json())
    .then(data => {
      if (!data.available) {
        if (data.usernameUnavailable) {
          setError('username', 'El nombre de usuario ya está en uso.');
        }
        if (data.emailUnavailable) {
          setError('email', 'El correo electrónico ya está registrado.');
        }
        // Do not proceed with form submission if there are errors
      } else {
        // If all is good, proceed with form submission
        submitForm();
      }
    })
    .catch(error => {
      console.error('Error en la verificación:', error);
      setError('server', 'Error al verificar la disponibilidad. Por favor, inténtelo de nuevo más tarde.');
      // Display server error message
      displayServerError('Error al verificar la disponibilidad. Por favor, inténtelo de nuevo más tarde.');
    });
}

function handleErrors(errors) {
  for (let field in errors) {
    const errorElement = document.getElementById(`error-${field}`);
    if (errorElement) {
      errorElement.textContent = errors[field];
      errorElement.style.display = 'block';
      document.querySelector(`input[name="${field}"]`).parentNode.classList.add('error');
    }
  }
}

// Función para mostrar el modal de confirmación
function showModal() {
  var modal = document.getElementById('registerModal');
  if (!modal) {
    console.error('No se encontró el modal con ID "registerModal".');
    return;
  }
  var span = modal.querySelector(".close");

  modal.style.display = "block";

  // Close the modal when the user clicks on 'x'
  span.onclick = function () {
    modal.style.display = "none";
  }

  // Close the modal if the user clicks outside of it
  window.onclick = function (event) {
    if (event.target == modal) {
      modal.style.display = "none";
    }
  }
}

// Log to confirm the script is loaded correctly
console.log("Script de validación cargado.");


// Event listeners para eliminar la clase de error cuando el usuario corrige la entrada
document.querySelector('input[name="phoneNumber"]').addEventListener('input', function () {
  this.parentNode.classList.remove('error');
  document.getElementById('error-phoneNumber').style.display = 'none';
});

document.querySelector('input[name="confirmPassword"]').addEventListener('input', function () {
  this.parentNode.classList.remove('error');
  document.getElementById('error-confirmPassword').style.display = 'none';
});

function submitForm() {
  var formData = new FormData(document.getElementById('registrationForm'));
  var userData = {
    username: formData.get('username'),
    email: formData.get('email'),
    phoneNumber: formData.get('phoneNumber'),
    password: formData.get('password'),
    confirmPassword: formData.get('confirmPassword')
  };

  fetch('/users/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'X-CSRF-TOKEN': formData.get('_csrf')
    },
    body: JSON.stringify(userData)
  })
    .then(response => {
      if (response.ok) {
        window.location.href = '/users/registerConfirm';
      } else {
        throw new Error('Something went wrong');
      }
    })
    .then(data => {
      // Manejar los errores mostrados al usuario aquí
      handleErrors(data);
    })
    .catch(error => {
      // Manejar errores de envío aquí
      console.error('Error al enviar el formulario:', error);
    });
}

// Añadir una nueva función para manejar la respuesta de disponibilidad
function handleAvailabilityResponse(response, setError) {
  if (!response.available) {
    if (response.usernameUnavailable) {
      setError('username', 'El nombre de usuario ya está en uso.');
    }
    if (response.emailUnavailable) {
      setError('email', 'El correo electrónico ya está en uso.');
    }
    // No proceder al envío del formulario si hay errores
  } else {
    // Si todo está bien, proceder con el envío del formulario
    submitForm();
  }
}

function displayServerError(message) {
  const serverErrorDiv = document.getElementById('server-error');
  serverErrorDiv.innerText = message;
  serverErrorDiv.style.display = 'block';
}
