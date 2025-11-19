CREATE DATABASE IF NOT EXISTS librequiz;
USE librequiz;

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
    nis VARCHAR(100),
    no_absen INT,
    UNIQUE (id),
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
    image_answer VARCHAR(255),

    FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE
);

CREATE TABLE auth_key (
    id INT AUTO_INCREMENT PRIMARY KEY,
    auth_key VARCHAR(255) NOT NULL
);


-- =========================
-- TABLE: question_image
-- =========================
CREATE TABLE question_image (
    id INT AUTO_INCREMENT PRIMARY KEY,
    image_path VARCHAR(255) NOT NULL,
    question_id INT NOT NULL,
    UNIQUE (id),

    FOREIGN KEY (question_id)
        REFERENCES question(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
CREATE TABLE final_score (
    id INT AUTO_INCREMENT PRIMARY KEY,
    siswa_id INT NOT NULL,
    correct_answer INT NOT NULL DEFAULT 0,
    wrong_answer INT NOT NULL DEFAULT 0,
    total_question INT NOT NULL DEFAULT 0,
    final_score INT NOT NULL DEFAULT 0,
    
    FOREIGN KEY (siswa_id) REFERENCES siswa(id)
);



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

    -- hubungan ke jawaban (opsi)
    question_answer_id INT,  -- sesuai struktur yang kamu kirim, pakai 'enum' tapi lebih tepat INT
                             -- karena referensi ke options_answer.id

    siswa_id INT NOT NULL,

    UNIQUE (id),

    FOREIGN KEY (question_answer_id)
        REFERENCES options_answer(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,

    FOREIGN KEY (siswa_id)
        REFERENCES siswa(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);


INSERT INTO admin (user, password)
VALUES ('admin', 'admin123');
INSERT INTO auth_key (auth_key)
VALUES ('FOGOH');

INSERT INTO kelas (kelas, jurusan) VALUES
('XII RPL 1', 'REKAYASA PERANGKAT LUNAK'),
('XII RPL 2', 'REKAYASA PERANGKAT LUNAK'),
('XI RPL 1',  'REKAYASA PERANGKAT LUNAK'),
('XI RPL 2',  'REKAYASA PERANGKAT LUNAK');

INSERT INTO question (question_text, answer_type, level, topic)
VALUES
    ('Sebuah meja berbentuk persegi panjang memiliki panjang 150 cm dan lebar 80 cm. Berapakah luas permukaan meja tersebut dalam cm^2?', 'SINGLE_CHOICES', 'mudah', 'Persegi Panjang'),    -- question_id = 1
    ('Jika keliling sebuah lapangan berbentuk persegi adalah 60 meter. Berapakah panjang satu sisi lapangan tersebut?', 'SINGLE_CHOICES', 'mudah', 'Persegi'),                                         -- question_id = 2
    ('Sebuah kotak mainan berbentuk kubus memiliki panjang rusuk 5 cm. Berapakah volume kotak mainan tersebut dalam cm^3?', 'SINGLE_CHOICES', 'mudah', 'Kubus'),                                   -- question_id = 3
    ('Sebuah segitiga siku-siku memiliki alas 10 cm dan tinggi 6 cm. Berapakah luas segitiga tersebut?', 'SINGLE_CHOICES', 'mudah', 'Segitiga'),                                                    -- question_id = 4
    ('Sebuah roda sepeda memiliki diameter 70 cm. Berapakah keliling roda tersebut? (Gunakan π = 22/7)', 'SINGLE_CHOICES', 'sedang', 'Lingkaran'),                                                 -- question_id = 5
    ('Sebuah trapesium memiliki panjang sisi sejajar 8 cm dan 12 cm, serta tinggi 5 cm. Berapakah luas trapesium tersebut?', 'SINGLE_CHOICES', 'sedang', 'Trapesium'),                           -- question_id = 6
    ('Sebuah lapangan voli berukuran panjang 18 m dan lebar 9 m. Berapakah keliling lapangan tersebut?', 'SINGLE_CHOICES', 'mudah', 'Persegi Panjang'),                                           -- question_id = 7
    ('Sebuah akuarium berbentuk balok memiliki panjang 10 dm, lebar 5 dm, dan tinggi 6 dm. Berapakah volume akuarium tersebut?', 'SINGLE_CHOICES', 'sedang', 'Balok'),                             -- question_id = 8
    ('Diagonal-diagonal pada belah ketupat berturut-turut adalah 16 cm dan 12 cm. Berapakah luas belah ketupat tersebut?', 'SINGLE_CHOICES', 'sedang', 'Belah Ketupat'),                          -- question_id = 9
    ('Jika sebuah piring memiliki jari-jari 10 cm, berapakah luas piring tersebut? (Gunakan π = 3,14)', 'SINGLE_CHOICES', 'sedang', 'Lingkaran');                                                  -- question_id = 10

-- Asumsi: question_id yang baru dibuat adalah 1
INSERT INTO options_answer (answer, question_id, score, correct, label, image_answer)
VALUES
-- Jawaban untuk Soal 1 (Luas P. Panjang = 12.000)
('230 cm^2', 1, 0, FALSE, 'A', NULL),
('460 cm^2', 1, 0, FALSE, 'B', NULL),
('12.000 cm^2', 1, 1, TRUE, 'C', NULL),
('15.000 cm^2', 1, 0, FALSE, 'D', NULL),

-- Jawaban untuk Soal 2 (Sisi Persegi = 15)
('10 meter', 2, 0, FALSE, 'A', NULL),
('15 meter', 2, 1, TRUE, 'B', NULL),
('20 meter', 2, 0, FALSE, 'C', NULL),
('30 meter', 2, 0, FALSE, 'D', NULL),

-- Jawaban untuk Soal 3 (Volume Kubus = 125)
('25 cm^3', 3, 0, FALSE, 'A', NULL),
('50 cm^3', 3, 0, FALSE, 'B', NULL),
('100 cm^3', 3, 0, FALSE, 'C', NULL),
('125 cm^3', 3, 1, TRUE, 'D', NULL),

-- Jawaban untuk Soal 4 (Luas Segitiga = 30)
('30 cm^2', 4, 1, TRUE, 'A', NULL),
('40 cm^2', 4, 0, FALSE, 'B', NULL),
('60 cm^2', 4, 0, FALSE, 'C', NULL),
('120 cm^2', 4, 0, FALSE, 'D', NULL),

-- Jawaban untuk Soal 5 (Keliling Lingkaran = 220)
('110 cm', 5, 0, FALSE, 'A', NULL),
('220 cm', 5, 1, TRUE, 'B', NULL),
('350 cm', 5, 0, FALSE, 'C', NULL),
('440 cm', 5, 0, FALSE, 'D', NULL),

-- Jawaban untuk Soal 6 (Luas Trapesium = 50)
('25 cm^2', 6, 0, FALSE, 'A', NULL),
('40 cm^2', 6, 0, FALSE, 'B', NULL),
('50 cm^2', 6, 1, TRUE, 'C', NULL),
('60 cm^2', 6, 0, FALSE, 'D', NULL),

-- Jawaban untuk Soal 7 (Keliling P. Panjang = 54)
('27 m', 7, 0, FALSE, 'A', NULL),
('54 m', 7, 1, TRUE, 'B', NULL),
('81 m', 7, 0, FALSE, 'C', NULL),
('162 m', 7, 0, FALSE, 'D', NULL),

-- Jawaban untuk Soal 8 (Volume Balok = 300)
('30 dm^3', 8, 0, FALSE, 'A', NULL),
('60 dm^3', 8, 0, FALSE, 'B', NULL),
('110 dm^3', 8, 0, FALSE, 'C', NULL),
('300 dm^3', 8, 1, TRUE, 'D', NULL),

-- Jawaban untuk Soal 9 (Luas Belah Ketupat = 96)
('48 cm^2', 9, 0, FALSE, 'A', NULL),
('80 cm^2', 9, 0, FALSE, 'B', NULL),
('96 cm^2', 9, 1, TRUE, 'C', NULL),
('192 cm^2', 9, 0, FALSE, 'D', NULL),

-- Jawaban untuk Soal 10 (Luas Lingkaran = 314)
('31,4 cm^2', 10, 0, FALSE, 'A', NULL),
('62,8 cm^2', 10, 0, FALSE, 'B', NULL),
('100 cm^2', 10, 0, FALSE, 'C', NULL),
('314 cm^2', 10, 1, TRUE, 'D', NULL);