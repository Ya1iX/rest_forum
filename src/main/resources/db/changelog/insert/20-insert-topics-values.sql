INSERT INTO topics
        (id, name, description, tags, text, section_id, user_id, created_at, changed_at, is_secured, password, is_closed, is_deleted, is_hidden, is_pinned, is_verified)
    VALUES
        (nextval('topics_sequence'), 'BMW X7', 'Интересует ваше мнение', 'cars,bmw,x7', 'Как вам последняя модель BMW?', 1, (SELECT id FROM users WHERE username = 'User'), now(), null, false, null, false, false, false, false, false)

GO

INSERT INTO topics
        (id, name, description, tags, text, section_id, user_id, created_at, changed_at, is_secured, password, is_closed, is_deleted, is_hidden, is_pinned, is_verified)
    VALUES
        (nextval('topics_sequence'), 'RTX 4090', 'Новая видеокарта', 'pc,rtx,nvidia,4090,gaming', 'Как вам производительность 4090, а также её цена?', 2, (SELECT id FROM users WHERE username = 'JohnWick'), now(), null, false, null, false, false, false, false, false)

GO

INSERT INTO topics
        (id, name, description, tags, text, section_id, user_id, created_at, changed_at, is_secured, password, is_closed, is_deleted, is_hidden, is_pinned, is_verified)
    VALUES
        (nextval('topics_sequence'), 'Lombok', 'Упрощение работы благодаря Lombok', 'programming,java,design,patterns', 'Ваши ощущения после первой работы с этой библиотекой?', 3, (SELECT id FROM users WHERE username = 'Norris'), now(), null, false, null, false, false, false, false, false)

GO