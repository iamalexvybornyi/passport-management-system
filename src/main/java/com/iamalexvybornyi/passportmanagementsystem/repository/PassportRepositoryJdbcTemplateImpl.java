package com.iamalexvybornyi.passportmanagementsystem.repository;

import com.iamalexvybornyi.passportmanagementsystem.mapper.PassportRowMapper;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Passport;
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

import java.sql.Types;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PassportRepositoryJdbcTemplateImpl implements PassportRepository {

    private static final String INSERT_PASSPORT = "INSERT INTO passports VALUES (:id, :passportNumber, :givenDate, " +
            ":departmentCode, :passportType, :status, :personId)";
    private static final String UPDATE_PASSPORT = "UPDATE passports " +
            "SET id = :id, passport_number = :passportNumber, given_date = :givenDate, " +
            "department_code = :departmentCode, passport_type = :passportType, " +
            "status = :status, person_id = :personId " +
            "WHERE id = :id";
    private static final String FIND_PASSPORT_BY_ID = "SELECT * FROM passports WHERE id = :id";
    private static final String FIND_PASSPORT_BY_PASSPORT_NUMBER = "SELECT * FROM passports " +
            "WHERE passport_number = :passportNumber";
    private static final String FIND_PASSPORT_BY_PERSON_ID = "SELECT * FROM passports WHERE person_id = :personId";
    private static final String FIND_PASSPORTS = "SELECT * FROM passports";
    private static final String FIND_PASSPORTS_BY_GIVEN_DATE = "SELECT * FROM passports " +
            "WHERE (:startDate is null OR given_date >= :startDate) " +
            "AND (:endDate is null OR given_date <= :endDate)";
    private static final String DELETE_PASSPORT_BY_ID = "DELETE FROM passports WHERE id = :id";
    private static final String DELETE_ALL_PASSPORTS = "DELETE FROM passports";
    @NonNull
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @NonNull
    private final JdbcTemplate jdbcTemplate;
    @NonNull
    private final PassportRowMapper passportRowMapper;

    @Override
    @NonNull
    public Passport save(@NonNull Passport passport) {
        final SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", passport.getId())
                .addValue("passportNumber", passport.getPassportNumber())
                .addValue("givenDate", passport.getGivenDate())
                .addValue("departmentCode", passport.getDepartmentCode())
                .addValue("passportType", passport.getPassportType().name())
                .addValue("status", passport.getStatus().name())
                .addValue("personId", passport.getPersonId());
        if (findById(passport.getId()) == null) {
            namedParameterJdbcTemplate.update(INSERT_PASSPORT, namedParameters);
        } else {
            namedParameterJdbcTemplate.update(UPDATE_PASSPORT, namedParameters);
        }
        return passport;
    }

    @Override
    @Nullable
    public Passport findById(@NonNull String id) {
        final SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
        try {
            return namedParameterJdbcTemplate.queryForObject(FIND_PASSPORT_BY_ID, namedParameters, passportRowMapper);
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    @Nullable
    public Passport findByPassportNumber(@NonNull String passportNumber) {
        final SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("passportNumber", passportNumber);
        try {
            return namedParameterJdbcTemplate
                    .queryForObject(FIND_PASSPORT_BY_PASSPORT_NUMBER, namedParameters, passportRowMapper);
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    @NonNull
    public List<Passport> findByPersonId(@NonNull String personId) {
        final SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("personId", personId);
        return namedParameterJdbcTemplate.query(FIND_PASSPORT_BY_PERSON_ID, namedParameters, passportRowMapper);
    }

    @Override
    @NonNull
    public Iterable<Passport> findAll() {
        return jdbcTemplate.query(FIND_PASSPORTS, passportRowMapper);
    }

    @Override
    @NonNull
    public Iterable<Passport> findByGivenDateBetween(LocalDate startDate, LocalDate endDate) {
        final SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("startDate", startDate, Types.DATE)
                .addValue("endDate", endDate, Types.DATE);
        return namedParameterJdbcTemplate.query(FIND_PASSPORTS_BY_GIVEN_DATE, namedParameters, passportRowMapper);
    }

    @Override
    public void delete(@NonNull Passport passport) {
        final SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", passport.getId());
        namedParameterJdbcTemplate.update(DELETE_PASSPORT_BY_ID, namedParameters);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(DELETE_ALL_PASSPORTS);
    }
}
