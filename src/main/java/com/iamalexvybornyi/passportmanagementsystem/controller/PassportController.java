package com.iamalexvybornyi.passportmanagementsystem.controller;

import com.iamalexvybornyi.passportmanagementsystem.controller.error.ErrorHandlingController;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.CreatePassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.PassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.PassportWithPersonDto;
import com.iamalexvybornyi.passportmanagementsystem.service.PassportService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/passports")
public class PassportController extends ErrorHandlingController {

    @NonNull
    private final PassportService passportService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PassportWithPersonDto getPassport(@PathVariable String id) {
        return passportService.getPassport(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PassportDto updatePassport(@PathVariable String id, @Valid @RequestBody CreatePassportDto createPassportDto) {
        return passportService.updatePassport(id, createPassportDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePassport(@PathVariable String id) {
        passportService.deletePassport(id);
    }

    @PostMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivatePassport(@PathVariable String id) {
        passportService.deactivatePassport(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PassportDto> getPassports(@Param("startDate") @DateTimeFormat(pattern="dd-MM-yyyy") LocalDate startDate,
                                          @Param("endDate") @DateTimeFormat(pattern="dd-MM-yyyy") LocalDate endDate) {
        return passportService.getPassports(startDate, endDate);
    }
}
