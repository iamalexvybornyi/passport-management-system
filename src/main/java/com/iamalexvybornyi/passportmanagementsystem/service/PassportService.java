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
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class PassportService {

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

    @Nullable
    public PassportWithPersonDto getPassport(@NonNull String id) {
        final Passport passportFromRepo = this.getPassportById(id);
        log.debug("Finding a person by passport number {}", passportFromRepo.getPassportNumber());
        final Person personFromRepo = personRepository.findByPassportNumber(passportFromRepo.getPassportNumber());
        log.debug("Found person: {}", personFromRepo);
        final PassportWithPersonDto passportWithPersonDto = passportConverter
                .passportToPassportWithPersonDto(passportFromRepo);
        passportWithPersonDto.setPerson(personConverter.personToPersonDto(personFromRepo));
        return passportWithPersonDto;
    }

    @NonNull
    public PassportDto updatePassport(@NonNull String id, @NonNull CreatePassportDto createPassportDto) {
        commonFieldValidationUtil.verifyCreatePassportDtoForBusinessRequirements(id, createPassportDto);
        final Passport passportFromRepo = this.getPassportById(id);
        passportFromRepo.setPassportType(PassportType.getPassportTypeFromString(createPassportDto.getPassportType()));
        passportFromRepo.setPassportNumber(createPassportDto.getPassportNumber());
        passportFromRepo.setStatus(Status.getPassportStatusFromString(createPassportDto.getStatus()));
        passportFromRepo.setGivenDate(LocalDate.parse(createPassportDto.getGivenDate(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        passportFromRepo.setDepartmentCode(createPassportDto.getDepartmentCode());
        log.debug("Saving passport {} to the repository", passportFromRepo);
        passportRepository.save(passportFromRepo);
        return passportConverter.passportToPassportDto(passportFromRepo);
    }

    public void deletePassport(@NonNull String id) {
        final Passport passportFromRepo = this.getPassportById(id);
        log.debug("Deleting the passport {} from the repository", passportFromRepo);
        passportRepository.delete(passportFromRepo);
    }

    public void deactivatePassport(@NonNull String id) {
        final Passport passportFromRepo = this.getPassportById(id);
        passportFromRepo.setStatus(Status.INACTIVE);
        log.debug("Deactivating the passport: {}", passportFromRepo);
        passportRepository.save(passportFromRepo);
    }

    @NonNull
    public List<PassportDto> getPassports(@Nullable LocalDate startDate, @Nullable LocalDate endDate) {
        final Iterable<Passport> foundPassports;
        log.debug("Will be getting passports using the the start date '{}' and the end date '{}'", startDate, endDate);
        if (startDate != null || endDate != null) {
            if (startDate != null && endDate != null) {
                if (startDate.compareTo(endDate) > 0) {
                    throw new RuntimeException("Start date can't be bigger than the end date!");
                }
            }
            log.debug("Getting passports using the start and end dates");
            foundPassports =
                    passportRepository.findByGivenDateBetween(startDate, endDate);
        } else {
            log.debug("Getting passports without using dates as they are null");
            foundPassports = passportRepository.findAll();
        }

        final List<PassportDto> passportDtoList = new ArrayList<>();
        foundPassports.forEach(passport -> passportDtoList.add(passportConverter.passportToPassportDto(passport)));
        return passportDtoList;
    }

    @NonNull
    private Passport getPassportById(@NonNull String id) {
        log.debug("Getting a passport by id {}", id);
        final Passport passportFromRepo = passportRepository.findById(id);
        log.debug("Obtained passport: {}", passportFromRepo);
        if (passportFromRepo == null) {
            log.warn("Passport with id {} is not found", id);
            throw new RecordNotFoundException("Passport is not found");
        }
        return passportFromRepo;
    }
}
