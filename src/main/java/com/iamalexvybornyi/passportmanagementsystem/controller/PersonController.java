package com.iamalexvybornyi.passportmanagementsystem.controller;

import com.iamalexvybornyi.passportmanagementsystem.controller.error.ErrorHandlingController;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.CreatePassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.PassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.person.CreatePersonDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.person.PersonDto;
import com.iamalexvybornyi.passportmanagementsystem.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController extends ErrorHandlingController {

    private final PersonService personService;

    @Autowired
    public PersonController(
            PersonService personService
    ) {
        this.personService = personService;
    }

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
    public PersonDto getPerson(@PathVariable("id") Long id) {
        return personService.getPerson(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonDto updatePerson(@PathVariable("id") Long id, @Valid @RequestBody CreatePersonDto createPersonDto) {
        return personService.updatePerson(id, createPersonDto);
    }

    @PostMapping("/{id}/passports")
    @ResponseStatus(HttpStatus.OK)
    public PassportDto addPersonPassport(@PathVariable("id") Long id,
                                         @Valid @RequestBody CreatePassportDto createPassportDto) {
        return personService.addPersonPassport(id, createPassportDto);
    }

    @GetMapping("/{id}/passports")
    @ResponseStatus(HttpStatus.OK)
    public List<PassportDto> getPersonPassports(@PathVariable("id") Long id, @Param("activeOnly") boolean activeOnly) {
        return personService.getPersonPassports(id, activeOnly);
    }
}
