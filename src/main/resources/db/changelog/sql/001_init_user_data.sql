-- Создание таблицы cars
CREATE TABLE cars (
    id serial PRIMARY KEY, -- Уникальный идентификатор записи
    brand varchar(50) NOT NULL, -- Марка автомобиля (Toyota, Nissan)
    model varchar(100) NOT NULL, -- Модель автомобиля
    year int NOT NULL, -- Год выпуска
    color varchar(50), -- Цвет автомобиля
    price numeric(10, 2) -- Цена автомобиля
);

-- Заполнение таблицы данными
INSERT INTO cars (brand, model, year, color, price) VALUES
('Toyota', 'Corolla', 2020, 'White', 20000.00),
('Toyota', 'Camry', 2021, 'Black', 25000.00),
('Toyota', 'RAV4', 2022, 'Blue', 30000.00),
('Toyota', 'Prius', 2021, 'Green', 28000.00),
('Toyota', 'Highlander', 2023, 'Silver', 40000.00),
('Nissan', 'Altima', 2020, 'Red', 22000.00),
('Nissan', 'Maxima', 2021, 'Gray', 32000.00),
('Nissan', 'Rogue', 2022, 'White', 28000.00),
('Nissan', 'Murano', 2023, 'Black', 35000.00),
('Nissan', 'Pathfinder', 2022, 'Blue', 38000.00);

-- Комментарии к столбцам таблицы cars
COMMENT ON COLUMN cars.id IS 'Уникальный идентификатор записи (автоинкремент).';
COMMENT ON COLUMN cars.brand IS 'Марка автомобиля (например, Toyota или Nissan).';
COMMENT ON COLUMN cars.model IS 'Модель автомобиля.';
COMMENT ON COLUMN cars.year IS 'Год выпуска автомобиля.';
COMMENT ON COLUMN cars.color IS 'Цвет автомобиля.';
COMMENT ON COLUMN cars.price IS 'Цена автомобиля в числовом формате.';