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
import ru.ibs.entity.Car;
import ru.ibs.service.CarService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/v1/", produces = "application/json")
@RequiredArgsConstructor
@Slf4j
public class CarController {

    private final CarService carService;

    @GetMapping("/cars")
    public List<Car> getAllCars(
        //    @AuthenticationPrincipal Jwt jwt,
            HttpServletRequest request) {
        // Извлекаем роли из JWT-токена
    //    Map<String, Object> realmAccess = jwt.getClaim("realm_access");
    //    Collection<String> roles = (Collection<String>) realmAccess.get("roles");

        // Преобразуем роли в строку
     //   String rolesString = String.join(", ", roles);
     //   log.info(rolesString);

        return carService.findAll(request);
    }

    @GetMapping("/cars2")
    public List<Car> getAllCars2(HttpServletRequest request) {
        // Получаем объект Authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Получаем имя пользователя
        String username = authentication.getName();
        log.info("Username: {}", username);
        // Получаем роли пользователя
        String roles = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.joining(", "));


        log.info(roles);

        return carService.findAll(request);
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
    public String getVersion(HttpServletRequest request) {
        return carService.getVersion(request);
    }
}