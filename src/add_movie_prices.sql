ALTER TABLE movies
ADD price INTEGER;

UPDATE movies
SET    price = RAND() * 20
WHERE  price IS NULL;