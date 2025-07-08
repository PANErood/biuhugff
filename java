// --- Lluvia de corazones (corazones que caen) ---
const heartRainContainer = document.getElementById('heart-rain-container');

function createFallingHeart() {
    const heart = document.createElement('div');
    heart.textContent = '❤️'; // El emoji de corazón
    heart.classList.add('heart');

    // Asigna colores alternos
    if (Math.random() > 0.5) {
        heart.classList.add('red');
    } else {
        heart.classList.add('white');
    }

    // Posición inicial aleatoria en el ancho de la pantalla
    heart.style.left = Math.random() * 100 + 'vw';
    // Duración de la animación de caída (aleatoria para variación)
    heart.style.animationDuration = Math.random() * 5 + 5 + 's'; // Entre 5 y 10 segundos
    // Retraso de la animación (para que no caigan todos a la vez)
    heart.style.animationDelay = Math.random() * 2 + 's'; // Hasta 2 segundos de retraso
    // Tamaño aleatorio
    heart.style.fontSize = (Math.random() * 2 + 1) + 'em'; // Entre 1em y 3em

    heartRainContainer.appendChild(heart);

    // Elimina el corazón una vez que termina su animación para no sobrecargar el DOM
    heart.addEventListener('animationend', () => {
        heart.remove();
    });
}

// Crea un nuevo corazón cayendo cada 300 milisegundos
setInterval(createFallingHeart, 300);


// --- Corazones que brotan al hacer clic ---
const canvas = document.getElementById('heart-canvas');
const ctx = canvas.getContext('2d');

let hearts = [];

// Ajusta el tamaño del canvas al tamaño de la ventana
function resizeCanvas() {
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;
}
window.addEventListener('resize', resizeCanvas);
resizeCanvas(); // Llama una vez al inicio

// Clase para un corazón individual que brota
class BurstHeart {
    constructor(x, y) {
        this.x = x;
        this.y = y;
        this.size = Math.random() * 10 + 10; // Tamaño inicial entre 10 y 20
        this.color = Math.random() > 0.5 ? 'red' : 'white'; // Color rojo o blanco
        this.opacity = 1;
        this.speedX = (Math.random() - 0.5) * 5; // Velocidad horizontal aleatoria
        this.speedY = (Math.random() - 1) * 5; // Velocidad vertical (hacia arriba)
        this.gravity = 0.1; // Gravedad para que caigan después de subir
    }

    update() {
        this.speedY += this.gravity; // Aplica gravedad
        this.x += this.speedX;
        this.y += this.speedY;
        this.opacity -= 0.02; // Se desvanece
        if (this.opacity < 0) this.opacity = 0; // Evita valores negativos
        this.size *= 0.98; // Se encoge ligeramente
    }

    draw() {
        ctx.save(); // Guarda el estado actual del contexto
        ctx.globalAlpha = this.opacity; // Aplica la opacidad
        ctx.fillStyle = this.color;

        // Dibuja un corazón (forma simplificada)
        ctx.beginPath();
        ctx.moveTo(this.x, this.y + this.size / 4);
        ctx.bezierCurveTo(this.x + this.size / 2, this.y - this.size / 2,
                          this.x + this.size, this.y + this.size / 4,
                          this.x, this.y + this.size);
        ctx.bezierCurveTo(this.x - this.size, this.y + this.size / 4,
                          this.x - this.size / 2, this.y - this.size / 2,
                          this.x, this.y + this.size / 4);
        ctx.closePath();
        ctx.fill();
        ctx.restore(); // Restaura el estado
    }
}

// Función para animar los corazones que brotan
function animateHearts() {
    ctx.clearRect(0, 0, canvas.width, canvas.height); // Limpia el canvas

    for (let i = hearts.length - 1; i >= 0; i--) {
        hearts[i].update();
        hearts[i].draw();

        // Elimina los corazones que ya no son visibles
        if (hearts[i].opacity <= 0 || hearts[i].size <= 1) {
            hearts.splice(i, 1);
        }
    }
    requestAnimationFrame(animateHearts); // Llama a la función de nuevo en el siguiente frame
}

// Escucha los clics/toques en el canvas
canvas.addEventListener('mousedown', (e) => { // Para clic de ratón
    const numHearts = Math.random() * 5 + 5; // Entre 5 y 10 corazones por clic
    for (let i = 0; i < numHearts; i++) {
        hearts.push(new BurstHeart(e.clientX, e.clientY));
    }
});

canvas.addEventListener('touchstart', (e) => { // Para toques en pantallas táctiles
    // Previene el zoom o scroll por defecto
    e.preventDefault();
    const touch = e.touches[0];
    const numHearts = Math.random() * 5 + 5;
    for (let i = 0; i < numHearts; i++) {
        hearts.push(new BurstHeart(touch.clientX, touch.clientY));
    }
}, { passive: false }); // Usamos { passive: false } para que preventDefault funcione en touchstart

// Inicia la animación de los corazones que brotan
animateHearts();
