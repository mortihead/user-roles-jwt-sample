package ru.ibs.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.ibs.dto.User;
import ru.ibs.entity.Car;
import ru.ibs.repository.CarRepository;
import ru.ibs.service.CarService;
import ru.ibs.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ibs.dto.User;
import ru.ibs.enums.RoleEnum;

@RestController
@RequestMapping(value = "api/v1/", produces = "application/json")
@RequiredArgsConstructor
@Slf4j
public class CarController {

    private final CarService carService;
    private final CarRepository carRepository;

    @GetMapping("/cars")
    public List<Car> getAllCars(
            @AuthenticationPrincipal Jwt jwt) {
        return carService.findAll(jwt);
    }

    @GetMapping("/cars2")
    public List<Car> getAllCars2() {
        return carService.findAll2();
    }

    @GetMapping("/cars/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Integer id) {
        return carService.findById(id)
                .map(car -> ResponseEntity.ok().body(car))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/cars")
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        Car savedCar = carService.save(car);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCar);
    }

    @DeleteMapping("/cars/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Integer id) {
        carService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/version")
    public String getVersion() {
        return carService.getVersion();
    }
}