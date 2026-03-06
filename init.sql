CREATE DATABASE IF NOT EXISTS quizmath;
USE quizmath;

-- =========================
-- TABLE: kelas
-- =========================
CREATE TABLE kelas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    kelas VARCHAR(100) NOT NULL,
    jurusan VARCHAR(100) NOT NULL,
    UNIQUE (kelas)
);

-- =========================
-- TABLE: siswa
-- =========================
CREATE TABLE siswa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(255) NOT NULL,
    kelas_id INT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nis VARCHAR(100),
    no_absen INT,
    FOREIGN KEY (kelas_id)
        REFERENCES kelas(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

-- =========================
-- TABLE: question
-- =========================
CREATE TABLE question (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question_text TEXT NOT NULL,
    answer_type ENUM('SINGLE_CHOICES', 'MULTIPLE_CHOICES') NOT NULL DEFAULT 'SINGLE_CHOICES',
    level VARCHAR(50) NOT NULL,
    topic VARCHAR(100) NOT NULL
);

-- =========================
-- TABLE: options_answer
-- =========================
CREATE TABLE options_answer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question_id INT NOT NULL,
    answer TEXT,
    label VARCHAR(10),
    score INT NOT NULL DEFAULT 0,
    correct BOOLEAN NOT NULL DEFAULT FALSE,

    FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE
);

-- =========================
-- TABLE: question_image
-- =========================
CREATE TABLE question_image (
    id INT AUTO_INCREMENT PRIMARY KEY,
    image_path VARCHAR(255) NOT NULL,
    question_id INT NOT NULL,
    FOREIGN KEY (question_id)
        REFERENCES question(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- =========================
-- TABLE: final_score
-- =========================
CREATE TABLE final_score (
    id INT AUTO_INCREMENT PRIMARY KEY,
    siswa_id INT NOT NULL,
    correct_answer INT NOT NULL DEFAULT 0,
    wrong_answer INT NOT NULL DEFAULT 0,
    total_question INT NOT NULL DEFAULT 0,
    final_score INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (siswa_id) REFERENCES siswa(id)
);

-- =========================
-- TABLE: admin
-- =========================
CREATE TABLE admin (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- TABLE: siswa_answer
-- =========================
CREATE TABLE siswa_answer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question_answer_id INT,
    siswa_id INT NOT NULL,
    final_score_id INT NOT NULL,
    FOREIGN KEY (question_answer_id)
        REFERENCES options_answer(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    FOREIGN KEY (siswa_id)
        REFERENCES siswa(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (final_score_id)
        REFERENCES final_score(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        REFERENCES siswa(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- =========================
-- INSERT DATA
-- =========================
INSERT INTO admin (user, password)
VALUES ('admin', 'admin123');

INSERT INTO kelas (kelas, jurusan) VALUES
('XII RPL 1', 'REKAYASA PERANGKAT LUNAK'),
('XII RPL 2', 'REKAYASA PERANGKAT LUNAK'),
('XI RPL 1',  'REKAYASA PERANGKAT LUNAK'),
('XI RPL 2',  'REKAYASA PERANGKAT LUNAK');

INSERT INTO siswa (nama, kelas_id, username, password, nis, no_absen) VALUES
('Afiq Pratama', 1, 'afiq01', 'pass123', 'NIS001', 1),
('Budi Santoso', 1, 'budi02', 'pass123', 'NIS002', 2),
('Citra Lestari', 2, 'citra03', 'pass123', 'NIS003', 3),
('Dewi Rahma', 2, 'dewi04', 'pass123', 'NIS004', 4),
('Eko Saputra', 3, 'eko05', 'pass123', 'NIS005', 5),
('Fajar Nugroho', 3, 'fajar06', 'pass123', 'NIS006', 6),
('Gita Amalia', 1, 'gita07', 'pass123', 'NIS007', 7),
('Hadi Wijaya', 2, 'hadi08', 'pass123', 'NIS008', 8),
('Intan Permata', 3, 'intan09', 'pass123', 'NIS009', 9),
('Joko Santika', 1, 'joko10', 'pass123', 'NIS010', 10);

-- =========================
-- INSERT DATA QUESTION
-- =========================
INSERT INTO question (question_text, answer_type, level, topic) VALUES
('Sebuah meja berbentuk persegi panjang memiliki panjang 150 cm dan lebar 80 cm. Berapakah luas permukaan meja tersebut dalam cm^2?', 'SINGLE_CHOICES', 'MUDAH', 'Persegi Panjang'), -- question_id = 1
('Jika keliling sebuah lapangan berbentuk persegi adalah 60 meter. Berapakah panjang satu sisi lapangan tersebut?', 'SINGLE_CHOICES', 'MUDAH', 'Persegi'), -- question_id = 2
('Sebuah kotak mainan berbentuk kubus memiliki panjang rusuk 5 cm. Berapakah volume kotak mainan tersebut dalam cm^3?', 'SINGLE_CHOICES', 'MUDAH', 'Kubus'), -- question_id = 3
('Sebuah segitiga siku-siku memiliki alas 10 cm dan tinggi 6 cm. Berapakah luas segitiga tersebut?', 'SINGLE_CHOICES', 'MUDAH', 'Segitiga'), -- question_id = 4
('Sebuah roda sepeda memiliki diameter 70 cm. Berapakah keliling roda tersebut? (Gunakan π = 22/7)', 'SINGLE_CHOICES', 'NORMAL', 'Lingkaran'), -- question_id = 5
('Sebuah trapesium memiliki panjang sisi sejajar 8 cm dan 12 cm, serta tinggi 5 cm. Berapakah luas trapesium tersebut?', 'SINGLE_CHOICES', 'NORMAL', 'Trapesium'), -- question_id = 6
('Sebuah lapangan voli berukuran panjang 18 m dan lebar 9 m. Berapakah keliling lapangan tersebut?', 'SINGLE_CHOICES', 'MUDAH', 'Persegi Panjang'), -- question_id = 7
('Sebuah akuarium berbentuk balok memiliki panjang 10 dm, lebar 5 dm, dan tinggi 6 dm. Berapakah volume akuarium tersebut?', 'SINGLE_CHOICES', 'NORMAL', 'Balok'), -- question_id = 8
('Diagonal-diagonal pada belah ketupat berturut-turut adalah 16 cm dan 12 cm. Berapakah luas belah ketupat tersebut?', 'SINGLE_CHOICES', 'NORMAL', 'Belah Ketupat'), -- question_id = 9
('Jika sebuah piring memiliki jari-jari 10 cm, berapakah luas piring tersebut? (Gunakan π = 3.14)', 'SINGLE_CHOICES', 'NORMAL', 'Lingkaran'); -- question_id = 10

-- =========================
-- INSERT DATA OPTIONS_ANSWER
-- =========================

-- Soal 1
INSERT INTO options_answer (answer, question_id, score, correct, label) VALUES
('230 cm^2', 1, 0, FALSE, 'A'),
('460 cm^2', 1, 0, FALSE, 'B'),
('12000 cm^2', 1, 1, TRUE, 'C'),
('15000 cm^2', 1, 0, FALSE, 'D');

-- Soal 2
INSERT INTO options_answer (answer, question_id, score, correct, label) VALUES
('10 meter', 2, 0, FALSE, 'A'),
('15 meter', 2, 1, TRUE, 'B'),
('20 meter', 2, 0, FALSE, 'C'),
('30 meter', 2, 0, FALSE, 'D');

-- Soal 3
INSERT INTO options_answer (answer, question_id, score, correct, label) VALUES
('25 cm^3', 3, 0, FALSE, 'A'),
('50 cm^3', 3, 0, FALSE, 'B'),
('100 cm^3', 3, 0, FALSE, 'C'),
('125 cm^3', 3, 1, TRUE, 'D');

-- Soal 4
INSERT INTO options_answer (answer, question_id, score, correct, label) VALUES
('30 cm^2', 4, 1, TRUE, 'A'),
('40 cm^2', 4, 0, FALSE, 'B'),
('60 cm^2', 4, 0, FALSE, 'C'),
('120 cm^2', 4, 0, FALSE, 'D');

-- Soal 5
INSERT INTO options_answer (answer, question_id, score, correct, label) VALUES
('110 cm', 5, 0, FALSE, 'A'),
('220 cm', 5, 1, TRUE, 'B'),
('350 cm', 5, 0, FALSE, 'C'),
('440 cm', 5, 0, FALSE, 'D');

-- Soal 6
INSERT INTO options_answer (answer, question_id, score, correct, label) VALUES
('25 cm^2', 6, 0, FALSE, 'A'),
('40 cm^2', 6, 0, FALSE, 'B'),
('50 cm^2', 6, 1, TRUE, 'C'),
('60 cm^2', 6, 0, FALSE, 'D');

-- Soal 7
INSERT INTO options_answer (answer, question_id, score, correct, label) VALUES
('27 m', 7, 0, FALSE, 'A'),
('54 m', 7, 1, TRUE, 'B'),
('81 m', 7, 0, FALSE, 'C'),
('162 m', 7, 0, FALSE, 'D');

-- Soal 8
INSERT INTO options_answer (answer, question_id, score, correct, label) VALUES
('30 dm^3', 8, 0, FALSE, 'A'),
('60 dm^3', 8, 0, FALSE, 'B'),
('110 dm^3', 8, 0, FALSE, 'C'),
('300 dm^3', 8, 1, TRUE, 'D');

-- Soal 9
INSERT INTO options_answer (answer, question_id, score, correct, label) VALUES
('48 cm^2', 9, 0, FALSE, 'A'),
('80 cm^2', 9, 0, FALSE, 'B'),
('96 cm^2', 9, 1, TRUE, 'C'),
('192 cm^2', 9, 0, FALSE, 'D');

-- Soal 10
INSERT INTO options_answer (answer, question_id, score, correct, label) VALUES
('31.4 cm^2', 10, 0, FALSE, 'A'),
('62.8 cm^2', 10, 0, FALSE, 'B'),
('100 cm^2', 10, 0, FALSE, 'C'),
('314 cm^2', 10, 1, TRUE, 'D');
