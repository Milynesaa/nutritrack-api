-- Insertar usuarios admin predefinidos
INSERT INTO users (name, email, password, role, status, created_at) VALUES
('Maria Iglesias', 'mariaiglesias', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'ROLE_ADMIN', 'ACTIVE', CURRENT_TIMESTAMP),
('Maria Paz Gutierrez', 'mariapazgutierrez', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'ROLE_ADMIN', 'ACTIVE', CURRENT_TIMESTAMP);
