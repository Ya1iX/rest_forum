CREATE TABLE messages (
        is_deleted boolean not null,
        is_hidden boolean not null,
        is_pinned boolean not null,
        changed_at timestamp(6),
        created_at timestamp(6) not null,
        topic_id bigint not null,
        id uuid not null,
        user_id uuid not null,
        text varchar(3000) not null,
        primary key (id)
)

GO

ALTER TABLE IF EXISTS messages
       ADD CONSTRAINT FK_messages_1
       FOREIGN KEY (topic_id)
       REFERENCES topics

GO

ALTER TABLE IF EXISTS messages
       ADD CONSTRAINT FK_messages_2
       FOREIGN KEY (user_id)
       REFERENCES users

GO