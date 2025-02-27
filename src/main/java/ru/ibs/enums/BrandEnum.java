package ru.ibs.enums;


public enum BrandEnum {
    TOYOTA("Toyota"),
    NISSAN("Nissan");

    private final String brand;

    BrandEnum(String brand) {
        this.brand = brand;
    }

    public String getBrandName() {
        return brand;
    }

}
