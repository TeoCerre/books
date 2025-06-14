
INSERT INTO author (name, surname, birth_date, nationality) VALUES
('Elena', 'Ferrante', '1943-10-05', 'Italiana'),
('Don', 'DeLillo', '1936-11-20', 'Americana'),
('Ian', 'McEwan', '1948-06-21', 'Inglese'),
('Niccolò', 'Ammaniti', '1966-09-25', 'Italiana'),
('Jonathan', 'Franzen', '1959-08-17', 'Americana');

-- Libri corrispondenti
INSERT INTO book (title, year, cover_image) VALUES
('L amica geniale', 2011, NULL),
('White Noise', 1985, NULL),
('Atonement', 2001, NULL),
('Io non ho paura', 2001, NULL),
('The Corrections', 2001, NULL);


INSERT INTO users (username, email, password, role) VALUES
('admin', 'admin@example.com', '$2a$10$7qeqeXv5EIjQnG6Fq1uQyOnv1TTQ4W7PYB4Pz9/h8eC2Xplz06KCa', 'ADMIN');

INSERT INTO review (title, text, rating, book_id, author_id) VALUES
('Bellissimo', 'Un romanzo davvero coinvolgente.', 5, 1, 1),
('Molto interessante', 'Stile unico e trama avvincente.', 4, 1, 1),
('Consigliato', 'Una lettura che lascia il segno.', 5, 2, 1),
('Noioso in alcuni punti', 'Non mi ha convinto del tutto.', 3, 3, 1),
('Emozionante', 'Mi ha fatto riflettere molto.', 5, 4, 1);
