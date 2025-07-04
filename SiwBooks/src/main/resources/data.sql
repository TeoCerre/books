INSERT INTO author (id,name, surname, birth_date, nationality) VALUES
(1,'Elena', 'Ferrante', '1943-10-05', 'Italiana'),
(2,'Don', 'DeLillo', '1936-11-20', 'Americana'),
(3,'Ian', 'McEwan', '1948-06-21', 'Inglese'),
(4,'Niccolò', 'Ammaniti', '1966-09-25', 'Italiana'),
(5,'Jonathan', 'Franzen', '1959-08-17', 'Americana');

-- Libri corrispondenti
INSERT INTO book (id,title, year, cover_image) VALUES
(1,'L amica geniale', 2011, NULL),
(2,'White Noise', 1985, NULL),
(3,'Atonement', 2001, NULL),
(4,'Io non ho paura', 2001, NULL),
(5,'The Corrections', 2001, NULL),
(6,'Underworld', 1997, NULL),                        
(7,'Anna', 2015, NULL),                              
(8,'Freedom', 2010, NULL);

INSERT INTO book_authors (books_id, authors_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 2),
(7, 4),
(8, 5);

INSERT INTO users (username, email, password, role) VALUES
('admin', 'admin@example.com', '$2a$10$ucGnEQLK49WmoEEcJdLYt.CoktosKI7JeHD9iLdbu60Ix3ruIk7Sm', 'ADMIN'),
('mario', 'mario@example.com', '$2a$12$GGEc6IwZlIun69cdyswDV.zchU0BYJ3UZIDILn5Xq99GfOwuh8fXu', 'REGISTERED'),
('anna', 'anna@example.com', '$2a$12$GGEc6IwZlIun69cdyswDV.zchU0BYJ3UZIDILn5Xq99GfOwuh8fXu', 'REGISTERED');

INSERT INTO review (title, text, rating, book_id, author_id) VALUES
('Bellissimo', 'Un romanzo davvero coinvolgente.', 5, 1, 1),
('Molto interessante', 'Stile unico e trama avvincente.', 4, 1, 2),
('Consigliato', 'Una lettura che lascia il segno.', 5, 2, 1),
('Noioso in alcuni punti', 'Non mi ha convinto del tutto.', 3, 3, 1),
('Emozionante', 'Mi ha fatto riflettere molto.', 5, 4, 1),
('Non male', 'Un po’ lento all’inizio ma poi migliora.', 3, 3, 2),
('Molto attuale', 'Riflessione interessante sulla società.', 4, 5, 2),
('Troppo lungo', 'Ben scritto ma eccessivamente prolisso.', 2, 6, 2),
('Intenso e reale', 'Un racconto femminile molto autentico.', 5, 1, 3),
('Disturbante ma affascinante', 'Un libro profondo e complesso.', 4, 2, 3),
('Stile elegante', 'Un romanzo ben scritto e coinvolgente.', 5, 3, 3),
('Toccante', 'Una storia semplice ma potente.', 4, 4, 3),
('Satira potente', 'Un mix interessante di critica e umorismo.', 4, 5, 3),
('Epico', 'Un libro monumentale, difficile ma gratificante.', 5, 6, 3),
('Distopico e originale', 'Atmosfera coinvolgente e scrittura secca.', 4, 7, 3),
('Modernissimo', 'Un romanzo sulle fragilità moderne. Consigliato.', 5, 8, 3);
