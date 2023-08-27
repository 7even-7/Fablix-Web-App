USE moviedb;

CREATE TABLE IF NOT EXISTS ft (
        entryID INT AUTO_INCREMENT,
        entry text,
        PRIMARY KEY (entryID),
        FULLTEXT (entry)
    );

INSERT INTO ft (entry)
SELECT title
FROM movies;