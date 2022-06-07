package com.iamalexvybornyi.passportmanagementsystem.service;

import com.iamalexvybornyi.passportmanagementsystem.converter.PassportConverter;
import com.iamalexvybornyi.passportmanagementsystem.converter.PersonConverter;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.CreatePassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.PassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.PassportWithPersonDto;
import com.iamalexvybornyi.passportmanagementsystem.exception.RecordNotFoundException;
import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Passport;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.PassportType;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Status;
import com.iamalexvybornyi.passportmanagementsystem.repository.PassportRepository;
import com.iamalexvybornyi.passportmanagementsystem.repository.PersonRepository;
import com.iamalexvybornyi.passportmanagementsystem.validation.CommonFieldValidationUtil;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PassportService {

    private final PersonRepository personRepository;
    private final PassportRepository passportRepository;
    private final PersonConverter personConverter;
    private final PassportConverter passportConverter;
    private final CommonFieldValidationUtil commonFieldValidationUtil;

    public PassportService(
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

    @Nullable
    public PassportWithPersonDto getPassport(@NonNull Long id) {
        Passport passportFromRepo = this.getPassportById(id);
        Person personFromRepo = personRepository.findByPassportNumber(passportFromRepo.getPassportNumber());
        PassportWithPersonDto passportWithPersonDto = passportConverter
                .passportToPassportWithPersonDto(passportFromRepo);
        passportWithPersonDto.setPerson(personConverter.personToPersonDto(personFromRepo));
        return passportWithPersonDto;
    }

    public PassportDto updatePassport(Long id, CreatePassportDto createPassportDto) {
        commonFieldValidationUtil.verifyCreatePassportDtoForBusinessRequirements(id, createPassportDto);
        Passport passportFromRepo = this.getPassportById(id);
        passportFromRepo.setPassportType(PassportType.getPassportTypeFromString(createPassportDto.getPassportType()));
        passportFromRepo.setPassportNumber(createPassportDto.getPassportNumber());
        passportFromRepo.setStatus(Status.getPassportStatusFromString(createPassportDto.getStatus()));
        passportFromRepo.setGivenDate(LocalDate.parse(createPassportDto.getGivenDate(),
                DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        passportFromRepo.setDepartmentCode(createPassportDto.getDepartmentCode());
        passportRepository.save(passportFromRepo);
        return passportConverter.passportToPassportDto(passportFromRepo);
    }

    public void deletePassport(Long id) {
        Passport passportFromRepo = this.getPassportById(id);
        passportRepository.delete(passportFromRepo);
    }

    public void deactivatePassport(Long id) {
        Passport passportFromRepo = this.getPassportById(id);
        passportFromRepo.setStatus(Status.INACTIVE);
        passportRepository.save(passportFromRepo);
    }

    public List<PassportDto> getPassports(LocalDate startDate, LocalDate endDate) {
        Iterable<Passport> foundPassports;
        if (startDate != null || endDate != null) {
            if (startDate != null && endDate != null) {
                if (startDate.compareTo(endDate) > 0) {
                    throw new RuntimeException("Start date can't be bigger than the end date!");
                }
            }
            foundPassports =
                    passportRepository.findByGivenDateBetween(startDate, endDate);
        } else {
            foundPassports = passportRepository.findAll();
        }

        List<PassportDto> passportDtoList = new ArrayList<>();
        foundPassports.forEach(passport -> passportDtoList.add(passportConverter.passportToPassportDto(passport)));
        return passportDtoList;
    }

    private Passport getPassportById(Long id) {
        Passport passportFromRepo = passportRepository.findById(id);
        if (passportFromRepo == null) {
            throw new RecordNotFoundException("Passport is not found");
        }
        return passportFromRepo;
    }
}
