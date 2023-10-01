package com.iamalexvybornyi.passportmanagementsystem.mapper;

import com.iamalexvybornyi.passportmanagementsystem.model.passport.Passport;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.PassportType;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Status;
import lombok.NonNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class PassportRowMapper implements RowMapper<Passport> {

    @Override
    @NonNull
    public Passport mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        final Passport passport = new Passport();

        passport.setId(rs.getString("id"));
        passport.setStatus(Status.getPassportStatusFromString(rs.getString("status")));
        passport.setPassportType(PassportType.getPassportTypeFromString(rs.getString("passport_type")));
        passport.setPassportNumber(rs.getString("passport_number"));
        passport.setPersonId(rs.getString("person_id"));
        passport.setDepartmentCode(rs.getString("department_code"));
        passport.setGivenDate(LocalDate.parse(rs.getString("given_date")));

        return passport;
    }
}
