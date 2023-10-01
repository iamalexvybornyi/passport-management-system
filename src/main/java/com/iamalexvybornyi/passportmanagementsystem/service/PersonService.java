package com.iamalexvybornyi.passportmanagementsystem.service;

import com.iamalexvybornyi.passportmanagementsystem.converter.PassportConverter;
import com.iamalexvybornyi.passportmanagementsystem.converter.PersonConverter;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.CreatePassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.PassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.person.CreatePersonDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.person.PersonDto;
import com.iamalexvybornyi.passportmanagementsystem.exception.RecordNotFoundException;
import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Passport;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Status;
import com.iamalexvybornyi.passportmanagementsystem.repository.PassportRepository;
import com.iamalexvybornyi.passportmanagementsystem.repository.PersonRepository;
import com.iamalexvybornyi.passportmanagementsystem.util.IdGeneratorUtil;
import com.iamalexvybornyi.passportmanagementsystem.validation.CommonFieldValidationUtil;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonService {

    @NonNull
    private final PersonRepository personRepository;
    @NonNull
    private final PassportRepository passportRepository;
    @NonNull
    private final PersonConverter personConverter;
    @NonNull
    private final PassportConverter passportConverter;
    @NonNull
    private final CommonFieldValidationUtil commonFieldValidationUtil;
    @NonNull
    private final IdGeneratorUtil idGeneratorUtil;

    @NonNull
    public PersonDto addPerson(@NonNull CreatePersonDto createPersonDto) {
        commonFieldValidationUtil.verifyCreatePersonDtoForBusinessRequirements(createPersonDto);
        final Person person = personConverter.createPersonDtoToPerson(createPersonDto);
        person.setId(idGeneratorUtil.generatePersonId());
        personRepository.save(person);
        return personConverter.personToPersonDto(person);
    }

    @NonNull
    public List<PersonDto> getPersons(@Nullable String passportNumber) {
        final List<PersonDto> personDtoList = new ArrayList<>();
        if (passportNumber != null && !passportNumber.isEmpty()) {
            final PersonDto personDto = personConverter.personToPersonDto(personRepository.findByPassportNumber(passportNumber));
            if (personDto != null) {
                personDtoList.add(personDto);
            }
        } else {
            personRepository.findAll().forEach(person -> personDtoList.add(personConverter.personToPersonDto(person)));
        }
        return personDtoList;
    }

    public PersonDto getPerson(@NonNull String id) {
        final Person personFromRepo = this.getPersonById(id);
        return personConverter.personToPersonDto(personFromRepo);
    }

    @NonNull
    public PersonDto updatePerson(@NonNull String id, @NonNull CreatePersonDto createPersonDto) {
        commonFieldValidationUtil.verifyCreatePersonDtoForBusinessRequirements(createPersonDto);
        final Person personFromRepo = this.getPersonById(id);
        personFromRepo.setName(createPersonDto.getName());
        personFromRepo.setBirthCountry(createPersonDto.getBirthCountry());
        personFromRepo.setBirthDate(LocalDate.parse(createPersonDto.getBirthDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        personRepository.save(personFromRepo);
        return personConverter.personToPersonDto(personFromRepo);
    }

    @NonNull
    public PassportDto addPersonPassport(@NonNull String id, @NonNull CreatePassportDto createPassportDto) {
        commonFieldValidationUtil.verifyCreatePassportDtoForBusinessRequirements(null, createPassportDto);

        final Passport passport = passportConverter.createPassportDtoToPassport(createPassportDto, id);
        passport.setId(idGeneratorUtil.generatePassportId());
        passportRepository.save(passport);

        return passportConverter.passportToPassportDto(passport);
    }

    @NonNull
    public List<PassportDto> getPersonPassports(@NonNull String id, @Nullable Status status) {
        final Person personFromRepo = this.getPersonById(id);
        return passportRepository.findByPersonId(personFromRepo.getId())
                .stream()
                .filter(passport -> passport.getStatus().equals(Optional.ofNullable(status).orElse(Status.ACTIVE)))
                .map(passportConverter::passportToPassportDto)
                .toList();
    }

    private Person getPersonById(@NonNull String id) {
        final Person personFromRepo = personRepository.findById(id);
        if (personFromRepo == null) {
            throw new RecordNotFoundException("Person is not found!");
        }
        return personFromRepo;
    }
}
