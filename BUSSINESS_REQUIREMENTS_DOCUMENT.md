# Hitachi Channel Solutions Indonesia

## Backend API Assessment

As part of our assessment process, we need you to make a backend API server application using Spring Boot.

### Objective:

You are going to implement a backend API using Spring Boot to store customer transactions and generate reports. Since this is for backend API, no user interface is required. The backend should be operable with REST API alone. The API will be used by company staff, as users, to add transactions on behalf of customers. We need you to complete this assessment within 3 days.

---

### User:

1.  API to perform CRUD operations on user data. Users have one or more roles. At minimum, implement 2 roles, **admin** and **user**. Admin can do anything. Users can modify their own profile, create new transactions, and generate reports.
2.  Customer can login and update his/her own profile.
3.  Customer can download his/her transactions report.
4.  Customer can only see his/her transactions in report.
5.  Users can login with either username or email.

---

### Customer:

1.  API to perform CRUD operations on customer data. Staff will register customers manually via this API.
2.  Customer consists of `name`, `birthdate`, `birthplace`, `created_by`, `created_at`, `updated_at`, and `updated_by`. Two different customers can share the same name, birthdate, and birthplace.

---

### Products:

1.  API to perform CRUD operations on product data.
2.  Products consist of `name` and `price` at minimum. Price is tax-excluded.
3.  Each product has tax. Each product may have multiple taxes, or no taxes at all.

---

### Transactions:

1.  API to perform CRUD operations on transaction data.
2.  Transaction consists of `customer`, `net amount paid`, `total amount paid`, `total tax paid`, `transaction time`, `payment status` (paid, not paid, cancelled) and `payment method`.
3.  API to query transactions based on these filters:
    a. Filter by date range
    b. Filter by customer (compare using customer name)
    c. Filter by payment status (can be all, single status, or multiple statuses at once)
    d. Filter by payment method (can be all, single method, or multiple methods at once)
    e. Can choose whether to sort based on transaction time oldest or newest first.
    f. Filter by the staff who creates the transaction.
4.  API to generate transaction reports:
    a. Total amount of money spent by customer between two dates.
    b. Total amount of money spent by customer throughout the transaction history.
    c. Total amount of money spent per tax.
    d. Total amount of money spent per product.

---

### Requirements:

1.  **Spring Boot** 2.7.x or 3.1.x.
2.  Database access using **Hibernate** library.
3.  Database must be in **Postgresql** Or **MySql** Or **MariaDb**.
4.  **Postman** file to test the application.
5.  SQL file to generate the database with test data should be provided.
6.  Object oriented design pattern used in development, ex: **MVC**.

---

### Submission

1.  Make a zip archive out of your project titled: `assessment_souce_code_[your_name].zip`.
2.  Upload to any online cloud storage of your choice, ex: Google Drive (Please make it accessible without restriction).
3.  Mail both links to your HR contact with subject `[ASSESSMENT] Backend - your name`

**Note:** If you have any doubts, please reach out to your HR contact, they’ll ensure that someone from the engineering team gets back to you.
Please do not upload your assignment to GitHub, Bitbucket etc. Only send it to us.

---

Wishing you good luck