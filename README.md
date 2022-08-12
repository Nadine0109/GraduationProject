
## [GraduationProject](https://github.com/Nadine0109/GraduationProject) - Дипломная работа по курсу "Тестировщик ПО"

#### Данный проект реализует автоматизированное тестирование web-приложения для покупки туристического тура.

Для запуска проекта используется установленный на машину пользователя **[Docker](https://www.docker.com/)**.

#### **Инструкция по запуску:**

 1. Склонировать [репозиторий](https://github.com/Nadine0109/GraduationProject).

 2. В терминале ItelliJ Idea CE запустить базы данных и симулятор банковского сервиса командой `docker-compose up -d`
    
 3. Запустить целевое приложение консольной командой:
 
  - для интеграции с PostgreSQL:
 
   `java -Dspring.datasource.url=jdbc:postgresql://localhost:5432/db -jar artifacts/aqa-shop.jar`
  
   - для интеграции с MySQL:
  
   `java -Dspring.datasource.url=jdbc:mysql://localhost:3306/db -jar artifacts/aqa-shop.jar`


 #### **Запуск тестов:**

    - с PostgreSQL: `gradlew clean test allureReport -Durl=jdbc:postgresql://localhost:5432/db`
    - с MySQL: `gradlew clean test allureReport -DdbURL=jdbc:mysql://localhost:3306/db`


 #### **Завершение тестов и получение отчётов:**
  Автоматические отчёты Gradle находятся в каталоге ./build/reports/tests/test

  Открытие отчёта Allure с помощью команды:

       - gradlew allureServe

  Генерация отчёта также возможна с помощью команды:

       - gradlew allureReport (каталог ./build/reports/allure-report)


  Завершить работы контейнеров Docker можно с помощью команды:

       - docker-compose down

### Документация по проекту:
- [План тестирования](https://github.com/Nadine0109/GraduationProject/blob/master/Documentation/Plan.md)
- [Отчёт по итогам тестирования](https://github.com/Nadine0109/GraduationProject/blob/master/Documentation/Report.md)
- [Отчёт по итогам автоматизации](https://github.com/Nadine0109/GraduationProject/blob/master/Documentation/Summary.md)
- [Баг-репорты](https://github.com/Nadine0109/GraduationProject/issues)
