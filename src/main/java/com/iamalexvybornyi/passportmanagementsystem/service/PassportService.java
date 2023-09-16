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
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
        final Person personFromRepo = personRepository.findByPassportNumber(passportFromRepo.getPassportNumber());
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
                DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        passportFromRepo.setDepartmentCode(createPassportDto.getDepartmentCode());
        passportRepository.save(passportFromRepo);
        return passportConverter.passportToPassportDto(passportFromRepo);
    }

    public void deletePassport(@NonNull String id) {
        final Passport passportFromRepo = this.getPassportById(id);
        passportRepository.delete(passportFromRepo);
    }

    public void deactivatePassport(@NonNull String id) {
        final Passport passportFromRepo = this.getPassportById(id);
        passportFromRepo.setStatus(Status.INACTIVE);
        passportRepository.save(passportFromRepo);
    }

    @NonNull
    public List<PassportDto> getPassports(@Nullable LocalDate startDate, @Nullable LocalDate endDate) {
        final Iterable<Passport> foundPassports;
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

        final List<PassportDto> passportDtoList = new ArrayList<>();
        foundPassports.forEach(passport -> passportDtoList.add(passportConverter.passportToPassportDto(passport)));
        return passportDtoList;
    }

    @NonNull
    private Passport getPassportById(@NonNull String id) {
        final Passport passportFromRepo = passportRepository.findById(id);
        if (passportFromRepo == null) {
            throw new RecordNotFoundException("Passport is not found");
        }
        return passportFromRepo;
    }
}
