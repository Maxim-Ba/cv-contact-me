ALTER TABLE feedback
    ADD COLUMN ip_address      VARCHAR(45),
    ADD COLUMN user_agent      TEXT,
    ADD COLUMN referer         TEXT,
    ADD COLUMN accept_language VARCHAR(255),
    ADD COLUMN country         VARCHAR(100);
