create table if not exists stat_log (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
	application VARCHAR(255) not null,
	ip VARCHAR(15) not null,
	uri text not null,
	hit_timestamp TIMESTAMP WITHOUT TIME zone not null
);