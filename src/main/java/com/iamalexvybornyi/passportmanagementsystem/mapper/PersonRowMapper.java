package com.iamalexvybornyi.passportmanagementsystem.mapper;

import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import lombok.NonNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class PersonRowMapper implements RowMapper<Person> {

    @Override
    @NonNull
    public Person mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        final Person person = new Person();

        person.setId(rs.getString("id"));
        person.setName(rs.getString("name"));
        person.setBirthDate(LocalDate.parse(rs.getString("birth_date")));
        person.setBirthCountry(rs.getString("birth_country"));

        return person;
    }
}
