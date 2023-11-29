create table "user"
(
    username varchar(63) not null
        primary key,
    password varchar(63),
    name     varchar(63),
    email    varchar(63)
);

create table "anime"
(
    anime_id     integer not null
        primary key,
    title        varchar(63),
    poster_image text,
    rating       double precision,
    episode      integer
);

create type status as enum ('WISHLIST', 'WATCHLIST', 'FINISHED');

create table "library"
(
    library_id     integer generated always as identity
        primary key,
    username       varchar(63) not null
        constraint library___fk
            references "user",
    anime_id       integer     not null
        constraint library___fk2
            references anime,
    current_status status
);

create table "review"
(
    review_id    integer generated always as identity
        primary key,
    username     varchar(63)
        references "user",
    anime_id     integer
        references anime,
    anime_review text,
    anime_score  double precision,
    review_date  timestamp
);