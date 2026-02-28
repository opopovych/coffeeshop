# ☕ CoffeeShop Enterprise — High-Load E-commerce Engine

Професійна E-commerce платформа для продажу кави, побудована на базі Spring Boot. Проєкт демонструє глибокі знання архітектури Java-додатків, інтеграцію з зовнішніми сервісами та готовність до високих навантажень.

## 🚀 "Killer Features" проекту

* **📦 Інтеграція з Новою Поштою:** Повноцінний вибір міст та відділень через офіційне API (`ShippingController`).
* **🤖 Telegram Automation:** Миттєві сповіщення адміністратора про нові замовлення через Telegram Bot.
* **📊 High-Load Tested:** Додаток витримує **10,000 запитів за хвилину** із середнім часом відповіді ~400ms.
* **🔍 SEO Optimized:** Автоматична генерація `sitemap.xml` та налаштований `robots.txt` для індексації пошуковими системами.
* **🏬 Warehouse Management:** Спеціальний модуль для складських працівників (Pick-list) для швидкого збору замовлень.
* **📁 Smart Excel Sync:** Модуль синхронізації прайсів через Excel з автоматичним аналізом відсутніх товарів.

## 📈 Результати навантажувального тестування (Loader.io)

Я провів серію стрес-тестів, щоб перевірити стабільність системи при різному трафіку:

| Навантаження (RPM) | Avg Response Time | Success Rate | Примітка |
| :--- | :--- | :--- | :--- |
| 1,000 | 414 ms | 100% | Стабільна робота |
| 5,000 | **299 ms** | 100% | Пік ефективності (JIT warmed up) |
| 10,000 | 580 ms | 100% | Максимальний стрес-тест |

## 🛠 Технологічний стек
* **Core:** Java 17/21, Spring Boot 3.x, Spring MVC.
* **Security:** Spring Security (управління сесіями, зміна паролів, ролі).
* **Data:** Spring Data JPA, Hibernate, PostgreSQL.
* **Integrations:** Nova Poshta API, Telegram Bot API, Apache POI (Excel).
* **Architecture:** SOLID, DRY, Service Layer pattern, Global Controller Advice.
* **Frontend:** Thymeleaf, Bootstrap 5, AJAX/REST API.

## 🔌 Огляд ключових API та Контролерів
* `CheckoutController` — Обробка замовлень та інтеграція з Telegram.
* `CoffeeCatalogController` — Складна фільтрація товарів, пагінація та AJAX-запити.
* `AdminSyncController` — Завантаження та обробка масивів даних через Excel.
* `SeoController` — Динамічна генерація XML для пошукових роботів.
* `ShippingController` — REST API для роботи з гео-даними логістичних компаній.

## 🏗 Як розгорнути проект
1. **Клонування:** `git clone ...`
2. **База даних:** Створіть БД PostgreSQL та вкажіть параметри у `application.properties`.
3. **API Keys:** Додайте токен Нової Пошти та Telegram Bot у конфігурацію.
4. **Запуск:** `./mvnw spring-boot:run`