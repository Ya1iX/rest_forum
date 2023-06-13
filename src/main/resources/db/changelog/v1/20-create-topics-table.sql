CREATE SEQUENCE topics_sequence START WITH 1 INCREMENT BY 1

GO

CREATE TABLE topics (
        is_closed boolean not null,
        is_deleted boolean not null,
        is_hidden boolean not null,
        is_pinned boolean not null,
        is_secured boolean not null,
        is_verified boolean not null,
        changed_at timestamp(6),
        created_at timestamp(6) not null,
        id bigint not null,
        section_id bigint not null,
        user_id uuid not null,
        name varchar(50) not null,
        description varchar(100),
        tags varchar(100),
        text varchar(10000),
        password varchar(255),
        primary key (id)
)

GO

ALTER TABLE IF EXISTS topics
       ADD CONSTRAINT FK_topics_1
       FOREIGN KEY (section_id)
       REFERENCES sections

GO

ALTER TABLE IF EXISTS topics
       ADD CONSTRAINT FK_topics_2
       FOREIGN KEY (user_id)
       REFERENCES users

GO