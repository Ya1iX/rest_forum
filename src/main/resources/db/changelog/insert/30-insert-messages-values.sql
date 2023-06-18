INSERT INTO messages
        (id,text,topic_id,user_id,created_at,changed_at,is_deleted,is_hidden,is_pinned)
    VALUES
        (gen_random_uuid(), 'Очень красивая и удобная машина, жаль что не самая надёжная)', 1, (SELECT id FROM users WHERE username = 'User'), now(), null, false, false, false)

GO

INSERT INTO messages
        (id,text,topic_id,user_id,created_at,changed_at,is_deleted,is_hidden,is_pinned)
    VALUES
        (gen_random_uuid(), 'Когда продам почку, тогда и попробую её в деле', 2, (SELECT id FROM users WHERE username = 'JohnWick'), now(), null, false, false, false)

GO

INSERT INTO messages
        (id,text,topic_id,user_id,created_at,changed_at,is_deleted,is_hidden,is_pinned)
    VALUES
        (gen_random_uuid(), 'Как и всегда, мощнее, больше и дороже, но свои задачи выполняет, как в гейминге, так и при работе с видео и 3D', 2, (SELECT id FROM users WHERE username = 'Norris'), now(), null, false, false, false)

GO

INSERT INTO messages
        (id,text,topic_id,user_id,created_at,changed_at,is_deleted,is_hidden,is_pinned)
    VALUES
        (gen_random_uuid(), 'Думаю я не стал исключением из тех, кто попробовал один раз и больше не хочет отказываться, очень удобная штука, правда иногда всё же её возможностей недостаточно', 3, (SELECT id FROM users WHERE username = 'User'), now(), null, false, false, false)

GO