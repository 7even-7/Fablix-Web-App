USE moviedb;

DELIMITER $$

CREATE PROCEDURE add_movie (
    IN p_id varchar(10),
    IN p_title varchar(100),
    IN p_year int,
    IN p_director varchar(100),
    IN p_star varchar(100),
    IN p_genre varchar(32)
)
BEGIN
    DECLARE random_id varchar(10);
    DECLARE random_genre_id INT;

INSERT INTO movies VALUES(p_id ,p_title,p_year,p_director,FLOOR(RAND() * 20));

IF EXISTS(SELECT name FROM stars WHERE name=p_star) THEN
    INSERT INTO stars_in_movies (starId, movieId) VALUES((SELECT id FROM stars WHERE name=p_star),p_id);
ELSE
    SET random_id = CONCAT('nm', FLOOR(RAND() * 10000000)) ;
    INSERT INTO stars (id, name) VALUES(random_id, p_star);
    INSERT INTO stars_in_movies (starId, movieId) VALUES(random_id,p_id);
END IF;

    IF EXISTS(SELECT name FROM genres WHERE name=p_genre) THEN
        INSERT INTO genres_in_movies (genreId, movieId) VALUES((SELECT id FROM genres WHERE name=p_genre), p_id);
ELSE
        SET random_genre_id = FLOOR(RAND() * 1000);
        INSERT INTO genres (id, name) VALUES(random_genre_id, p_genre);
        INSERT INTO genres_in_movies (genreId, movieId) VALUES(random_genre_id, p_id);
END IF;

END
$$

DELIMITER ;