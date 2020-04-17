CREATE DATABASE ${cfg.get("db/name")}
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.UTF-8'
    LC_CTYPE = 'en_US.UTF-8'
    CONNECTION LIMIT = -1;

CREATE USER ${cfg.get("db/user")} WITH PASSWORD '${cfg.get("service1/password")}_${cfg.get("release")}';
