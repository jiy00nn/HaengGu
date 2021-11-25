DROP TABLE IF EXISTS chat_message;
DROP TABLE IF EXISTS chat_session;
DROP TABLE IF EXISTS board_favorite;
DROP TABLE IF EXISTS board_info;
DROP TABLE IF EXISTS event_favorite;
DROP TABLE IF EXISTS event_info;
DROP TABLE IF EXISTS user_profile_photo;
DROP TABLE IF EXISTS user_info;
DROP TABLE IF EXISTS department;

CREATE TABLE department (
	dept_id uuid NOT NULL,
	university VARCHAR(20) NOT NULL,
	department VARCHAR(30) NOT NULL,
	PRIMARY KEY (dept_id)
);

CREATE TABLE user_info (
	user_id uuid NOT NULL,
	username CHAR(10) NOT NULL,
	password VARCHAR(30) NOT NULL,
	email VARCHAR(50) NOT NULL,
	certification VARCHAR(50),
	socialtype VARCHAR(10),
	gender VARCHAR(5) NOT NULL,
	birthday DATE NOT NULL,
	dept_id uuid REFERENCES department,
	grade INT,
	event_tag VARCHAR(50)[],
	region_tag VARCHAR(50)[],
	created_at DATE NOT NULL DEFAULT CURRENT_DATE,
	updated_at DATE NOT NULL DEFAULT CURRENT_DATE,
	PRIMARY KEY (user_id)
);

CREATE TABLE user_profile_photo (
	user_img_id uuid NOT NULL,
	user_id uuid NOT NULL REFERENCES user_info,
	file_name VARCHAR(30),
	file_size INT NOT NULL,
	file_type VARCHAR(4),
	file_data BYTEA,
	PRIMARY KEY (user_img_id)
);

CREATE TABLE event_info (
	event_id uuid NOT NULL,
	title VARCHAR(50) NOT NULL,
	description VARCHAR(2000),
	started_at DATE NOT NULL DEFAULT CURRENT_DATE,
	ended_at DATE NOT NULL DEFAULT CURRENT_DATE,
	reservation_ended_at DATE NOT NULL DEFAULT CURRENT_DATE,
	event_location VARCHAR(50),
	category VARCHAR(20),
	region VARCHAR(50),
	event_tag VARCHAR(50)[],
	PRIMARY KEY (event_id)
);

CREATE TABLE event_favorite(
	favorite_id uuid NOT NULL,
	event_id uuid NOT NULL REFERENCES event_info,
	user_id uuid NOT NULL REFERENCES user_info,
	PRIMARY KEY (favorite_id)
);

CREATE TABLE board_info (
	board_id uuid NOT NULL,
	title VARCHAR(50) NOT NULL,
	contents VARCHAR(2000),
	board_tag VARCHAR(50)[],
	event_id uuid NOT NULL REFERENCES event_info,
	user_id uuid NOT NULL REFERENCES user_info,
	created_at DATE NOT NULL DEFAULT CURRENT_DATE,
	updated_at DATE NOT NULL DEFAULT CURRENT_DATE,
	PRIMARY KEY (board_id)
);

CREATE TABLE board_favorite (
	favorite_id uuid NOT NULL,
	board_id uuid NOT NULL REFERENCES board_info,
	user_id uuid NOT NULL REFERENCES user_info,
	PRIMARY KEY (favorite_id)
);

CREATE TABLE chat_session (
	session_id uuid NOT NULL,
	user_id uuid NOT NULL REFERENCES user_info,
	board_id uuid NOT NULL REFERENCES board_info,
	PRIMARY KEY (session_id)
);

CREATE TABLE chat_message (
	user_id uuid NOT NULL REFERENCES user_info,
	chat_session uuid NOT NULL REFERENCES chat_session,
	created_at DATE NOT NULL DEFAULT CURRENT_DATE,
	message jsonb NOT NULL,
	PRIMARY KEY (user_id, chat_session)
);

COMMIT;