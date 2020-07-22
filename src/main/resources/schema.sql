CREATE TABLE contacts_details
(
  resource_name varchar(100),
  display_name varchar(100) DEFAULT NULL ,
  phone_numbers varchar(100) DEFAULT NULL,
  photo_urls  varchar(100) DEFAULT NULL,
  PRIMARY KEY (resource_name)
);