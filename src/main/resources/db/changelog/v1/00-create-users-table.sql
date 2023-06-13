CREATE TABLE users (
        is_deleted boolean not null,
        is_locked boolean not null,
        is_verified boolean not null,
        created_at timestamp(6) not null,
        lock_expiration timestamp(6),
        signed_in timestamp(6),
        id uuid not null,
        first_name varchar(20),
        last_name varchar(20),
        username varchar(20) not null unique,
        avatarurl varchar(255),
        email varchar(255) not null unique,
        password varchar(255) not null,
        role varchar(255) check (role in ('USER','MODER','ADMIN')),
        primary key (id)
)

GO