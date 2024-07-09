create table if not exists Cuscuz_Order (
  id identity,
  delivery_name varchar(50) not null,
  delivery_street varchar(50) not null,
  delivery_city varchar(50) not null,
  delivery_state varchar(2) not null,
  delivery_zip varchar(10) not null,
  cc_number varchar(16) not null,
  cc_expiration varchar(5) not null,
  cc_cvv varchar(3) not null,
  placed_at timestamp not null
);

create table if not exists Cuscuz (
  id identity,
  name varchar(50) not null,
  cuscuz_order bigint not null,
  cuscuz_order_key bigint not null,
  created_at timestamp not null
);

create table if not exists Ingredient_Ref (
  ingredient varchar(4) not null,
  cuscuz bigint not null,
  cuscuz_key bigint not null
);


create table if not exists Ingredient (
  id varchar(4) not null,
  name varchar(25) not null,
  type varchar(10) not null
);

ALTER TABLE Ingredient
    ADD CONSTRAINT pk_ingredient PRIMARY KEY (id);
alter table Cuscuz
    add foreign key (cuscuz_order) references Cuscuz_Order(id);
alter table Ingredient_Ref
    add foreign key (ingredient) references Ingredient(id);
