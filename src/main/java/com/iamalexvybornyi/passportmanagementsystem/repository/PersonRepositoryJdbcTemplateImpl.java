package com.iamalexvybornyi.passportmanagementsystem.repository;

import com.iamalexvybornyi.passportmanagementsystem.mapper.PersonRowMapper;
import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PersonRepositoryJdbcTemplateImpl implements PersonRepository {

    private static final String INSERT_PERSON = "INSERT INTO persons VALUES (:id, :name, :birthDate, :birthCountry)";
    private static final String FIND_PERSON_BY_ID = "SELECT * FROM persons WHERE id = :id";
    private static final String FIND_PERSON_BY_PASSPORT_NUMBER = "SELECT " +
            "persons.id, " +
            "persons.name, " +
            "persons.birth_date, " +
            "persons.birth_country " +
            "FROM persons " +
            "INNER JOIN passports " +
            "ON persons.id = passports.person_id " +
            "WHERE passports.passport_number = :passportNumber";
    private static final String FIND_PERSONS = "SELECT * FROM persons";
    private static final String DELETE_PERSON_BY_ID = "DELETE FROM persons WHERE id = :id";
    private static final String DELETE_ALL_PERSONS = "DELETE FROM persons";
    @NonNull
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @NonNull
    private final JdbcTemplate jdbcTemplate;
    @NonNull
    private final PersonRowMapper personRowMapper;


    @Override
    @NonNull
    public Person save(@NonNull Person person) {
        final SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(person);
        namedParameterJdbcTemplate.update(INSERT_PERSON, namedParameters);
        return person;
    }

    @Override
    @Nullable
    public Person findById(@NonNull String id) {
        final SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
        try {
            return namedParameterJdbcTemplate.queryForObject(FIND_PERSON_BY_ID, namedParameters, personRowMapper);
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    @NonNull
    public Iterable<Person> findAll() {
        return jdbcTemplate.query(FIND_PERSONS, personRowMapper);
    }

    @Override
    @Nullable
    public Person findByPassportNumber(@NonNull String passportNumber) {
        final SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("passportNumber", passportNumber);
        try {
            return namedParameterJdbcTemplate.queryForObject(FIND_PERSON_BY_PASSPORT_NUMBER, namedParameters, personRowMapper);
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public void delete(@NonNull Person person) {
        final SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", person.getId());
        namedParameterJdbcTemplate.queryForObject(DELETE_PERSON_BY_ID, namedParameters, personRowMapper);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(DELETE_ALL_PERSONS);
    }
}
