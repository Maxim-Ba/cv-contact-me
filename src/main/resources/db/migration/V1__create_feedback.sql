CREATE TABLE IF NOT EXISTS feedback (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    message    TEXT         NOT NULL,
    sent_via   VARCHAR(50),
    created_at TIMESTAMPTZ  DEFAULT NOW()
);
