document.getElementById('registrationForm').addEventListener('submit', function(e) {
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

  var csrfToken = formData.get('_csrf'); // Obtiene el token CSRF del formulario


    // Log form data for debugging
    console.log("Enviando formulario con datos:", Object.fromEntries(formData));

   // Validar correo electrónico
   var email = formData.get('email');
  if (!/^\S+@\S+\.\S+$/.test(email)) {
    const errorEmail = document.getElementById('error-email');
    errorEmail.textContent = 'Please include an "@" in the email address.';
    document.querySelector('input[name="email"]').parentNode.classList.add('error');
    errorEmail.style.display = 'block';
    isValid = false;
  }

  // Validar número de teléfono
  var phoneNumber = formData.get('phoneNumber');
  if (!/^\d{9}$/.test(phoneNumber)) {
    const errorPhoneNumber = document.getElementById('error-phoneNumber');
    errorPhoneNumber.textContent = 'Phone number must be exactly 9 digits.';
    document.querySelector('input[name="phoneNumber"]').parentNode.classList.add('error');
    errorPhoneNumber.style.display = 'block';
    isValid = false;
  }

  // Validar coincidencia de contraseñas
  var password = formData.get('password');
  var confirmPassword = formData.get('confirmPassword');
  if (password !== confirmPassword) {
    const errorConfirmPassword = document.getElementById('error-confirmPassword');
    errorConfirmPassword.textContent = 'Passwords do not match.';
    document.querySelector('input[name="confirmPassword"]').parentNode.classList.add('error');
    errorConfirmPassword.style.display = 'block';
    isValid = false;
  }

  // Si el formulario no es válido, detener el procesamiento
  if (!isValid) {
    return;
  }

  // Crear un objeto con los datos del formulario
  var userData = {
    username: formData.get('username'), // Asegúrate de que los nombres coincidan con los campos del formulario
    email: formData.get('email'),
    phoneNumber: formData.get('phoneNumber'),
    password: formData.get('password'),
    confirmPassword: formData.get('confirmPassword')
  };

  // Continuar con la solicitud fetch si el formulario es válido
  fetch(this.action, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'X-CSRF-TOKEN': csrfToken
    },
    body: JSON.stringify(userData)
  })
  .then(response => {
    if (!response.ok) {
      return response.json().then(errors => {
        throw errors;
      });
    }
    return response.json();
  })
  .then(data => {
    // Muestra el modal aquí
    showModal();
  })
  .catch(errorResponse => {
    if (errorResponse.json) {
      errorResponse.json().then(errors => {
        handleErrors(errors);
      });
    } else {
      console.error("Error fetching:", errorResponse);
    }
  });
});

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
  span.onclick = function() {
    modal.style.display = "none";
  }

  // Close the modal if the user clicks outside of it
  window.onclick = function(event) {
    if (event.target == modal) {
      modal.style.display = "none";
    }
  }
}

// Log to confirm the script is loaded correctly
console.log("Script de validación cargado.");


// Event listeners para eliminar la clase de error cuando el usuario corrige la entrada
document.querySelector('input[name="phoneNumber"]').addEventListener('input', function() {
  this.parentNode.classList.remove('error');
  document.getElementById('error-phoneNumber').style.display = 'none';
});

document.querySelector('input[name="confirmPassword"]').addEventListener('input', function() {
  this.parentNode.classList.remove('error');
  document.getElementById('error-confirmPassword').style.display = 'none';
});
