# MovieLibrary
Aplikacja desktopowa do oceniania obejrzanych filmów

## Baza danych

### Tworzenie bazy danych
'''
CREATE DATABASE IF NOT EXISTS moviedb
'''

### Tworzenie tabel

#### Reżyserzy
'''
CREATE TABLE IF NOT EXISTS directors
(
    director_id INT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(40) NOT NULL,
    last_name VARCHAR(40) NOT NULL,
    gender enum('Male', 'Female') NOT NULL,
    nationality VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    date_of_death DATE NULL,
    PRIMARY KEY(director_id)
);
'''

#### Filmy
'''
CREATE TABLE IF NOT EXISTS movies
(
    movie_id INT NOT NULL AUTO_INCREMENT,
    director_id INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    release_date INT NOT NULL,
    rate FLOAT NOT NULL,
    genre VARCHAR(200) NOT NULL,
    country VARCHAR(100) NOT NULL,
    PRIMARY KEY(movie_id),
    FOREIGN KEY(director_id) REFERENCES directors(director_id)
);
'''

#### Aktorzy
'''
CREATE TABLE IF NOT EXISTS actors
(
    actor_id INT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(40) NOT NULL,
    last_name VARCHAR(40) NOT NULL,
    gender enum('Male', 'Female') NOT NULL,
    nationality VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    date_of_death DATE NULL,
    PRIMARY KEY(actor_id)
);
'''

#### Relacja Film-Aktor (rola)
'''
CREATE TABLE IF NOT EXISTS movie_actor
(
    movie_id INT NOT NULL,
    actor_id INT NOT NULL,
	rate FLOAT NOT NULL,
    FOREIGN KEY(movie_id) REFERENCES movies(movie_id),
   	FOREIGN KEY(actor_id) REFERENCES actors(actor_id)
);
'''

### Ustawienie własnej nazwy użytkownika i hasła przy logowaniu do bazy danych w pliku hibernate.cfg.xml
'''
<property name="hibernate.connection.username">username</property>
<property name="hibernate.connection.password">password</property>
'''

## Widok aplikacji

### Przegląd filmów
![Widok główny](/screenshots/view_main.png?raw=true)

### Dodawanie nowego filmu
![Dodawanie filmu](/screenshots/view_add_movie.png?raw=true)

### Edycja filmu
![Edycja filmu](/screenshots/view_edit_movie.png?raw=true)

### Dodawanie nowego rezysera
![Dodawanie reżysera](/screenshots/view_add_director.png?raw=true)

### Dodawanie nowego aktora
![Dodawanie aktora](/screenshots/view_add_actor.png?raw=true)

### Ocenianie roli aktora w filmie
![Ocenianie roli](/screenshots/view_rate_actor.png?raw=true)

### Statystyki filmów
![Statystyki filmów](/screenshots/view_movies.png?raw=true)

### Statystyki reżyserów
![Statystyki reżyserów](/screenshots/view_directors.png?raw=true)

### Statystyki aktorów
![Statystyki aktorów](/screenshots/view_actors.png?raw=true)


## Do zrobienia
- Optymalizacja!
- Film może mieć kilku reżyserów