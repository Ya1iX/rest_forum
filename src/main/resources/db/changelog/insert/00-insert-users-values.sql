INSERT INTO users
        (id, username, email, first_name, last_name, role, created_at, signed_in, is_deleted, is_verified, is_locked, lock_expiration, password, avatarurl)
    VALUES
        (gen_random_uuid(), 'JohnWick', 'john.wick@mail.com', 'John', 'Wick', 'USER', now(), null, false, false, false, null, '$2a$10$YMeoqyCpmMNHFjc7T5TxDOi7D0D3fhV1raqfOqlffAk8PPAjkKOTi', null)

GO

INSERT INTO users
        (id, username, email, first_name, last_name, role, created_at, signed_in, is_deleted, is_verified, is_locked, lock_expiration, password, avatarurl)
    VALUES
        (gen_random_uuid(), 'Norris', 'chuck@norris.com', 'Chuck', 'Norris', 'ADMIN', now(), null, false, false, false, null, '$2a$10$YMeoqyCpmMNHFjc7T5TxDOi7D0D3fhV1raqfOqlffAk8PPAjkKOTi', null)

GO

INSERT INTO users
        (id, username, email, first_name, last_name, role, created_at, signed_in, is_deleted, is_verified, is_locked, lock_expiration, password, avatarurl)
    VALUES
        (gen_random_uuid(), 'User', 'user@mail.com', null, null, 'USER', now(), null, false, false, false, null, '$2a$10$YMeoqyCpmMNHFjc7T5TxDOi7D0D3fhV1raqfOqlffAk8PPAjkKOTi', null)

GO

INSERT INTO users
        (id, username, email, first_name, last_name, role, created_at, signed_in, is_deleted, is_verified, is_locked, lock_expiration, password, avatarurl)
    VALUES
        (gen_random_uuid(), 'Admin', 'admin@mail.com', null, null, 'ADMIN', now(), null, false, false, false, null, '$2a$10$YMeoqyCpmMNHFjc7T5TxDOi7D0D3fhV1raqfOqlffAk8PPAjkKOTi', null)

GO

INSERT INTO users
        (id, username, email, first_name, last_name, role, created_at, signed_in, is_deleted, is_verified, is_locked, lock_expiration, password, avatarurl)
    VALUES
        (gen_random_uuid(), 'Moder', 'moder@mail.com', null, null, 'MODER', now(), null, false, false, false, null, '$2a$10$YMeoqyCpmMNHFjc7T5TxDOi7D0D3fhV1raqfOqlffAk8PPAjkKOTi', null)

GO