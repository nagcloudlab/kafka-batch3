docker-compose up -d

curl -i -X POST localhost:8083/connectors \
 -H 'Content-Type: application/json' \
 -d @connectors/debezium-mysql-moviesdb.json

curl -i -X POST http://localhost:8083/connectors \
 -H 'Content-Type: application/json' \
 -d @connectors/elasticsearch-sink-movies.json

curl localhost:8083/connectors/debezium-mysql-moviesdb/status
curl localhost:8083/connectors/elasticsearch-sink-movies/status

curl -X PUT localhost:9200/movies \
 -H "Content-Type: application/json" \
 -d @elasticsearch/movies-mapping.json

docker exec -it -e MYSQL_PWD=secret mysql mysql -uroot --database moviesdb

SELECT \* FROM movies;

curl -i "localhost:9200/movies/\_search?pretty"

INSERT INTO `movies` (`imdb_id`, `year`, `title`, `actors`, `poster`)
VALUES (
'tt7286456',
1019,
'Joker',
'Joaquin Phoenix, Robert De Niro, Zazie Beetz',
'https://m.media-amazon.com/images/M/MV5BNGVjNWI4ZGUtNzE0MS00YTJmLWE0ZDctN2ZiYTk2YmI3NTYyXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_SX300.jpg'
);

INSERT INTO `movies` (`imdb_id`, `year`, `title`, `actors`, `poster`)
VALUES (
'tt7286454',
2024,
'Movie-1',
'Joaquin Phoenix, Robert De Niro, Zazie Beetz',
'https://m.media-amazon.com/images/M/MV5BNGVjNWI4ZGUtNzE0MS00YTJmLWE0ZDctN2ZiYTk2YmI3NTYyXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_SX300.jpg'
);
INSERT INTO `movies` (`imdb_id`, `year`, `title`, `actors`, `poster`)
VALUES (
'tt7286456',
2024,
'Movie-2',
'Joaquin Phoenix, Robert De Niro, Zazie Beetz',
'https://m.media-amazon.com/images/M/MV5BNGVjNWI4ZGUtNzE0MS00YTJmLWE0ZDctN2ZiYTk2YmI3NTYyXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_SX300.jpg'
);

curl -i "localhost:9200/movies/\_search?pretty"

UPDATE movies SET year = 2019 WHERE imdb_id = 'tt7286456';

curl -i "localhost:9200/movies/\_search?pretty"

DELETE FROM movies WHERE imdb_id = 'tt7286456';

docker compose down -v
