CREATE TABLE passports (
    id varchar(255) NOT NULL,
    passport_number varchar(255) NOT NULL,
    given_date DATE NOT NULL,
    department_code varchar(255) NOT NULL,
    passport_type varchar(255) NOT NULL,
    status varchar(255) NOT NULL,
  	person_id varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE passports
ADD CONSTRAINT passports_person_id_fk
FOREIGN KEY (person_id) REFERENCES persons(id);
