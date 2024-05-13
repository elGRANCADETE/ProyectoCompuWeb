function validateImage(input) {
    const maxFileSize = 30 * 1024 * 1024; // 30MB en bytes
    const file = input.files[0];

    if (file) {
        // Validar extensión
        if (file.type !== "image/jpeg") {
            alert('El archivo debe ser una imagen en formato JPG.');
            input.value = ''; // Limpia el campo
            return false;
        }

        // Validar tamaño
        if (file.size > maxFileSize) {
            alert('El archivo debe ser menor de 30MB.');
            input.value = ''; // Limpia el campo
            return false;
        }
    }
    return true;
}

document.addEventListener('DOMContentLoaded', function() {
    var form = document.querySelector('form'); // Asegúrate de que este selector obtenga el formulario correcto
    
    form.onsubmit = function(event) {
        event.preventDefault(); // Prevenir el envío normal del formulario

        // Validar aquí si lo necesitas
        if (validateForm()) {
            var formData = new FormData(form);

            // Configurar la solicitud
            fetch(form.action, {
                method: 'POST',
                body: formData,
            })
            .then(function(response) {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(function(result) {
                // Aquí manejas la respuesta de la creación exitosa del evento
                console.log(result);
                showSuccessMessage(); // Mostrar mensaje de éxito
            })
            .catch(function(error) {
                console.error('There has been a problem with your fetch operation:', error);
            });
        }
    };
});

function validateForm() {
    // Valida que todos los campos excepto la carátula del evento estén llenos
    if (!document.getElementById('title').value.trim() || 
        !document.getElementById('description').value.trim() ||
        !document.getElementById('startTime').value.trim() ||
        !document.getElementById('durationMinutes').value.trim()) {
        alert('Por favor, rellena todos los campos requeridos.');
        return false;
    }
    return true;
}

function previewImage() {
    if (validateForm()) {
        var fileInput = document.getElementById('eventCover');
        var title = document.getElementById('title').value;
        var description = document.getElementById('description').value;
        var startTime = document.getElementById('startTime').value;
        var durationMinutes = document.getElementById('durationMinutes').value;
        var previewModal = document.getElementById('previewModal');
        var previewContent = document.getElementById('previewContent');
        
        // Limpiar el contenido previo, incluyendo una imagen existente
        while (previewContent.firstChild) {
            previewContent.removeChild(previewContent.firstChild);
        }

        // Crear y añadir el contenido del evento (título, descripción, etc.)
        var titleElement = document.createElement('p');
        titleElement.innerHTML = '<strong>Título:</strong> ' + title;
        var descriptionElement = document.createElement('p');
        descriptionElement.innerHTML = '<strong>Descripción:</strong> ' + description;
        var startTimeElement = document.createElement('p');
        startTimeElement.innerHTML = '<strong>Hora de Inicio:</strong> ' + startTime;
        var durationElement = document.createElement('p');
        durationElement.innerHTML = '<strong>Duración (Minutos):</strong> ' + durationMinutes;
        
        // Añadir los elementos creados al DOM
        previewContent.appendChild(titleElement);
        previewContent.appendChild(descriptionElement);
        previewContent.appendChild(startTimeElement);
        previewContent.appendChild(durationElement);

        if (fileInput.files && fileInput.files[0]) {
            if (validateImage(fileInput)) {
                var reader = new FileReader();
                reader.onload = function(e) {
                    var img = new Image();
                    img.onload = function() {
                        var maxWidth = 500; // Ancho máximo para la previsualización
                        var maxHeight = 500; // Altura máxima para la previsualización
                        
                        // Procesamiento del canvas y redimensionado de la imagen
                        var canvas = document.getElementById('imageCanvas');
                        var ctx = canvas.getContext('2d');
                        var ratio = Math.min(maxWidth / img.width, maxHeight / img.height);
                        var newWidth = img.width * ratio;
                        var newHeight = img.height * ratio;
                        canvas.width = newWidth;
                        canvas.height = newHeight;
                        ctx.drawImage(img, 0, 0, newWidth, newHeight);
                        
                        // Convertir a dataURL
                        var dataUrl = canvas.toDataURL('image/jpeg');
                        
                        // Crear elemento de imagen para la previsualización
                        var imageElement = document.createElement('img');
                        imageElement.src = dataUrl;
                        imageElement.alt = 'Previsualización del Evento';
                        imageElement.style.maxWidth = '100%';
                        imageElement.style.display = 'block'; // Asegura que la imagen no tiene margen extra
                        
                        // Añadir la imagen redimensionada al contenido del modal
                        previewContent.appendChild(imageElement);
                        
                        // Mostrar el modal
                        previewModal.style.display = 'block';
                    };
                    img.src = e.target.result;
                };
                reader.readAsDataURL(fileInput.files[0]);
            }
        } else {
            // Mostrar el modal incluso si no hay una imagen seleccionada
            previewModal.style.display = 'block';
        }
    }
}


function closePreview() {
    var previewModal = document.getElementById('previewModal');
    previewModal.style.display = 'none';
}

function showSuccessMessage() {
    var successMessage = document.getElementById('successMessage');
    successMessage.style.display = 'block';
}

function closeSuccessMessage() {
    var successMessage = document.getElementById('successMessage');
    successMessage.style.display = 'none';
}

function closeSuccessModal() {
    var successModal = document.getElementById('successModal');
    successModal.style.display = 'none';
}

function showSuccessModal() {
    var successModal = document.getElementById('successModal');
    successModal.style.display = 'block';
}

// Función para guardar eventos
function validateImage(input) {
    const maxFileSize = 30 * 1024 * 1024; // 30MB en bytes
    const file = input.files[0];

    if (file) {
        // Validar extensión
        if (file.type !== "image/jpeg") {
            alert('El archivo debe ser una imagen en formato JPG.');
            input.value = ''; // Limpia el campo
            return false;
        }

        // Validar tamaño
        if (file.size > maxFileSize) {
            alert('El archivo debe ser menor de 30MB.');
            input.value = ''; // Limpia el campo
            return false;
        }
    }
    return true;
}

document.addEventListener('DOMContentLoaded', function() {
    var form = document.querySelector('form'); // Asegúrate de que este selector obtenga el formulario correcto
    
    form.onsubmit = function(event) {
        event.preventDefault(); // Prevenir el envío normal del formulario

        // Validar aquí si lo necesitas
        if (validateForm()) {
            var formData = new FormData(form);

            // Configurar la solicitud
            fetch(form.action, {
                method: 'POST',
                body: formData,
            })
            .then(function(response) {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(function(result) {
                // Aquí manejas la respuesta de la creación exitosa del evento
                console.log(result);
                showSuccessMessage(); // Mostrar mensaje de éxito
            })
            .catch(function(error) {
                console.error('There has been a problem with your fetch operation:', error);
            });
        }
    };
});

function validateForm() {
    // Valida que todos los campos excepto la carátula del evento estén llenos
    if (!document.getElementById('title').value.trim() || 
        !document.getElementById('description').value.trim() ||
        !document.getElementById('startTime').value.trim() ||
        !document.getElementById('durationMinutes').value.trim()) {
        alert('Por favor, rellena todos los campos requeridos.');
        return false;
    }
    return true;
}

function previewImage() {
    if (validateForm()) {
        var fileInput = document.getElementById('eventCover');
        var title = document.getElementById('title').value;
        var description = document.getElementById('description').value;
        var startTime = document.getElementById('startTime').value;
        var durationMinutes = document.getElementById('durationMinutes').value;
        var previewModal = document.getElementById('previewModal');
        var previewContent = document.getElementById('previewContent');
        
        // Limpiar el contenido previo, incluyendo una imagen existente
        while (previewContent.firstChild) {
            previewContent.removeChild(previewContent.firstChild);
        }

        // Crear y añadir el contenido del evento (título, descripción, etc.)
        var titleElement = document.createElement('p');
        titleElement.innerHTML = '<strong>Título:</strong> ' + title;
        var descriptionElement = document.createElement('p');
        descriptionElement.innerHTML = '<strong>Descripción:</strong> ' + description;
        var startTimeElement = document.createElement('p');
        startTimeElement.innerHTML = '<strong>Hora de Inicio:</strong> ' + startTime;
        var durationElement = document.createElement('p');
        durationElement.innerHTML = '<strong>Duración (Minutos):</strong> ' + durationMinutes;
        
        // Añadir los elementos creados al DOM
        previewContent.appendChild(titleElement);
        previewContent.appendChild(descriptionElement);
        previewContent.appendChild(startTimeElement);
        previewContent.appendChild(durationElement);

        if (fileInput.files && fileInput.files[0]) {
            if (validateImage(fileInput)) {
                var reader = new FileReader();
                reader.onload = function(e) {
                    var img = new Image();
                    img.onload = function() {
                        var maxWidth = 500; // Ancho máximo para la previsualización
                        var maxHeight = 500; // Altura máxima para la previsualización
                        
                        // Procesamiento del canvas y redimensionado de la imagen
                        var canvas = document.getElementById('imageCanvas');
                        var ctx = canvas.getContext('2d');
                        var ratio = Math.min(maxWidth / img.width, maxHeight / img.height);
                        var newWidth = img.width * ratio;
                        var newHeight = img.height * ratio;
                        canvas.width = newWidth;
                        canvas.height = newHeight;
                        ctx.drawImage(img, 0, 0, newWidth, newHeight);
                        
                        // Convertir a dataURL
                        var dataUrl = canvas.toDataURL('image/jpeg');
                        
                        // Crear elemento de imagen para la previsualización
                        var imageElement = document.createElement('img');
                        imageElement.src = dataUrl;
                        imageElement.alt = 'Previsualización del Evento';
                        imageElement.style.maxWidth = '100%';
                        imageElement.style.display = 'block'; // Asegura que la imagen no tiene margen extra
                        
                        // Añadir la imagen redimensionada al contenido del modal
                        previewContent.appendChild(imageElement);
                        
                        // Mostrar el modal
                        previewModal.style.display = 'block';
                    };
                    img.src = e.target.result;
                };
                reader.readAsDataURL(fileInput.files[0]);
            }
        } else {
            // Mostrar el modal incluso si no hay una imagen seleccionada
            previewModal.style.display = 'block';
        }
    }
}


function closePreview() {
    var previewModal = document.getElementById('previewModal');
    previewModal.style.display = 'none';
}

function showSuccessMessage() {
    var successMessage = document.getElementById('successMessage');
    successMessage.style.display = 'block';
}

function closeSuccessMessage() {
    var successMessage = document.getElementById('successMessage');
    successMessage.style.display = 'none';
}

function closeSuccessModal() {
    var successModal = document.getElementById('successModal');
    successModal.style.display = 'none';
}

function showSuccessModal() {
    var successModal = document.getElementById('successModal');
    successModal.style.display = 'block';
}

function saveEvent(button) {
    const eventId = button.getAttribute('data-event-id');
    const csrfToken = document.querySelector('meta[name="csrf-token"]').getAttribute('content');

    fetch(`/events/save/${eventId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        }
    }).then(response => {
        if (response.ok) {
            return response.text(); // Cambia a .json() si la respuesta es JSON
        } else {
            throw new Error('Error al guardar el evento: ' + response.statusText);
        }
    }).then(data => {
        alert('Evento guardado con éxito');
    }).catch(error => {
        console.error('Error:', error);
        alert('Error al guardar el evento: ' + error.message);
    });
}

function shareEvent(button) {
    var eventId = button.getAttribute('data-event-id');
    window.location.href = `/user/home/userForum/shareEvents?eventId=${eventId}`;
}

function removeEvent(eventId) {
    const csrfToken = document.querySelector('meta[name="csrf-token"]').getAttribute('content');
    fetch(`/user/events/remove/${eventId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        },
    }).then(response => {
        if (response.ok) {
            window.location.reload(); // Esto recargará la página automáticamente
        } else {
            response.text().then(text => { throw new Error(text); });
        }
    }).catch(error => {
        console.error('Error:', error);
        alert('Error al eliminar el evento: ' + error.message);
    });
}












