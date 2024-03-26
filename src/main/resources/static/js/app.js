function guardarComoFavorito(eventoId) {
    fetch('/favorites', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ eventId: eventoId })
    })
    .then(response => response.json())
    .then(data => {
        console.log('Success:', data);
        // Actualizar la interfaz de usuario según sea necesario
    })
    .catch((error) => {
        console.error('Error:', error);
    });
}
