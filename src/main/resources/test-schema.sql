DROP TABLE ITEMS CASCADE CONSTRAINTS;

DROP TABLE COMMENTS CASCADE CONSTRAINTS;

DROP TABLE USERS CASCADE CONSTRAINTS;

DROP TABLE REQUESTS CASCADE CONSTRAINTS;

DROP TABLE BOOKINGS CASCADE CONSTRAINTS;

CREATE TABLE IF NOT EXISTS USERS (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  email varchar(255) NOT NULL,
  name varchar(255) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT unique_user_email UNIQUE (email)
);

create table if not exists REQUESTS (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  creation_date timestamp WITHOUT TIME ZONE not null,
  description varchar(255) not null,
  owner int not null,
  constraint requests_owner_fk foreign key (owner) references USERS,
  CONSTRAINT pk_requests PRIMARY KEY (id)
);

create table if not exists BOOKINGS (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  stop timestamp WITHOUT TIME ZONE not null,
  start timestamp WITHOUT TIME ZONE not null,
  status varchar(255) not null,
  booker int,
  item int,
  CONSTRAINT pk_bookings PRIMARY KEY (id),
  constraint bookings_owner_id_fk foreign key (booker) references USERS
);

create table if not exists ITEMS (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  is_available boolean not null,
  description varchar(255) not null,
  name varchar(255) not null,
  owner int not null,
  last_booking int,
  next_booking int,
  request int,
  CONSTRAINT pk_items PRIMARY KEY (id),
  constraint items_owner_id_fk foreign key (owner) references USERS,
  constraint last_booking_id_fk foreign key (last_booking) references BOOKINGS,
  constraint next_booking_id_fk foreign key (next_booking) references BOOKINGS
);

create table if not exists COMMENTS (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  author int not null,
  creation timestamp WITHOUT TIME ZONE not null,
  item int not null,
  text varchar(255) not null,
  CONSTRAINT pk_comments PRIMARY KEY (id),
  constraint comments_item_id_fk foreign key (item) references ITEMS,
  constraint comments_author_id_fk foreign key (author) references USERS
);

--ALTER TABLE BOOKINGS ADD CONSTRAINT bookings_item_id_fk FOREIGN KEY (item) REFERENCES ITEMS(id);
