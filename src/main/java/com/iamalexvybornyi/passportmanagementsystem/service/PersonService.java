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
import com.iamalexvybornyi.passportmanagementsystem.repository.PassportRepository;
import com.iamalexvybornyi.passportmanagementsystem.repository.PersonRepository;
import com.iamalexvybornyi.passportmanagementsystem.validation.CommonFieldValidationUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PassportRepository passportRepository;
    private final PersonConverter personConverter;
    private final PassportConverter passportConverter;
    private final CommonFieldValidationUtil commonFieldValidationUtil;

    public PersonService(
            PersonRepository personRepository,
            PassportRepository passportRepository,
            PersonConverter personConverter,
            PassportConverter passportConverter,
            CommonFieldValidationUtil commonFieldValidationUtil
    ) {
        this.personRepository = personRepository;
        this.passportRepository = passportRepository;
        this.personConverter = personConverter;
        this.passportConverter = passportConverter;
        this.commonFieldValidationUtil = commonFieldValidationUtil;
    }

    public PersonDto addPerson(CreatePersonDto createPersonDto) {
        commonFieldValidationUtil.verifyCreatePersonDtoForBusinessRequirements(createPersonDto);
        Person person = personConverter.createPersonDtoToPerson(createPersonDto);
        personRepository.save(person);
        return personConverter.personToPersonDto(person);
    }

    public List<PersonDto> getPersons(String passportNumber) {
        List<PersonDto> personDtoList = new ArrayList<>();
        if (passportNumber != null && !passportNumber.isEmpty()) {
            PersonDto personDto =
                    personConverter.personToPersonDto(personRepository.findByPassportNumber(passportNumber));
            if (personDto != null) {
                personDtoList.add(personDto);
            }
        } else {
            personRepository.findAll().forEach(person -> personDtoList.add(personConverter.personToPersonDto(person)));
        }
        return personDtoList;
    }

    public PersonDto getPerson(Long id) {
        Person personFromRepo = this.getPersonById(id);
        return personConverter.personToPersonDto(personFromRepo);
    }

    public PersonDto updatePerson(Long id, CreatePersonDto createPersonDto) {
        commonFieldValidationUtil.verifyCreatePersonDtoForBusinessRequirements(createPersonDto);
        Person personFromRepo = this.getPersonById(id);
        personFromRepo.setName(createPersonDto.getName());
        personFromRepo.setBirthCountry(createPersonDto.getBirthCountry());
        personFromRepo.setBirthDate(createPersonDto.getBirthDate());
        personRepository.save(personFromRepo);
        return personConverter.personToPersonDto(personFromRepo);
    }

    public PassportDto addPersonPassport(Long id, CreatePassportDto createPassportDto) {
        Person personFromRepo = this.getPersonById(id);
        List<Passport> personPassports = personFromRepo.getPassports();
        if (personPassports == null) {
            personPassports = new ArrayList<>();
        }

        Passport passport = passportConverter.createPassportDtoToPassport(createPassportDto);
        passportRepository.save(passport);

        personPassports.add(passport);
        personFromRepo.setPassports(personPassports);
        personRepository.save(personFromRepo);
        return passportConverter.passportToPassportDto(passport);
    }

    public List<PassportDto> getPersonPassports(Long id) {
        List<PassportDto> passportDtoList = new ArrayList<>();
        Person personFromRepo = this.getPersonById(id);
        personFromRepo.getPassports()
                .forEach(passport -> passportDtoList.add(passportConverter.passportToPassportDto(passport)));
        return passportDtoList;
    }

    private Person getPersonById(Long id) {
        Person personFromRepo = personRepository.findById(id);
        if (personFromRepo == null) {
            throw new RecordNotFoundException("Person is not found!");
        }
        return personFromRepo;
    }
}
