INSERT INTO users (username, password, enabled)
    values ("user", "pass", true);

INSERT INTO users (username, password, enabled)
    values ("admin", "pass", true);

INSERT INTO authorities (username, password, enabled)
    values ("user", "ROLE_USER");

INSERT INTO authorities (username, password, enabled)
    values ("admin", "ROLE_ADMIN");

