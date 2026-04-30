
-- Crear índice en email para búsquedas más rápidas
CREATE INDEX idx_email ON users(email);

-- Insertar datos de ejemplo (opcional)
INSERT INTO users (email, firstname, lastname, password) VALUES
    ('juan@example.com', 'Juan', 'Pérez', 'hashed_password_1'),
    ('maria@example.com', 'María', 'López', 'hashed_password_2'),
    ('carlos@example.com', 'Carlos', 'García', 'hashed_password_3');

