package ru.ibs.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.ibs.entity.Car;
import ru.ibs.enums.BrandEnum;
import ru.ibs.repository.CarRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarService {

    private final CarRepository carRepository;

    @Value("${project.version}")
    private String version;

    public List<Car> findAll(HttpServletRequest request) {
//        if (userService.isToyotaManager(user)) {
//            return carRepository.findByBrand(BrandEnum.TOYOTA.getBrandName());
//        } else if (userService.isNissanManager(user)) {
//            return carRepository.findByBrand(BrandEnum.NISSAN.getBrandName());
//        } else if (userService.isAdmin(user)) {
//            return carRepository.findAll();
//        } else return Collections.emptyList();

        return carRepository.findAll();

    }

    public Optional<Car> findById(Integer id) {
        return carRepository.findById(id);
    }

    public Car save(Car car) {
        return carRepository.save(car);
    }

    public void deleteById(Integer id) {
        carRepository.deleteById(id);
    }

    public String getVersion(HttpServletRequest request) {
        return "Версия приложения: " + version;
    }
}