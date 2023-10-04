package com.iamalexvybornyi.passportmanagementsystem.controller;

import com.iamalexvybornyi.passportmanagementsystem.controller.error.ErrorHandlingController;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.CreatePassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.PassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.person.CreatePersonDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.person.PersonDto;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Status;
import com.iamalexvybornyi.passportmanagementsystem.service.PersonService;
import com.iamalexvybornyi.passportmanagementsystem.validation.ValueOfEnum;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/persons")
public class PersonController extends ErrorHandlingController {

    @NonNull
    private final PersonService personService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public PersonDto addPerson(@Valid @RequestBody CreatePersonDto createPersonDto) {
        return personService.addPerson(createPersonDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PersonDto> getPersons(@Param("passportNumber") String passportNumber) {
        return personService.getPersons(passportNumber);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonDto getPerson(@PathVariable("id") String id) {
        return personService.getPerson(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonDto updatePerson(@PathVariable("id") String id, @Valid @RequestBody CreatePersonDto createPersonDto) {
        return personService.updatePerson(id, createPersonDto);
    }

    @PostMapping("/{id}/passports")
    @ResponseStatus(HttpStatus.OK)
    public PassportDto addPersonPassport(@PathVariable("id") String id,
                                         @Valid @RequestBody CreatePassportDto createPassportDto) {
        return personService.addPersonPassport(id, createPassportDto);
    }

    @GetMapping("/{id}/passports")
    @ResponseStatus(HttpStatus.OK)
    public List<PassportDto> getPersonPassports(@PathVariable("id") String id,
                                                @ValueOfEnum(
                                                        enumClass = Status.class,
                                                        message = "{passport.management.system.constraints.passport.status.message}")
                                                @Param("status") Status status) {
        return personService.getPersonPassports(id, status);
    }
}
