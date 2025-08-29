# BajajFinservHealth-Task1
# Bajaj Finserv Health | JAVA Qualifier

This is my solution for the Bajaj Finserv Health Java Qualifier.

## 🚀 Project Overview
- A Spring Boot app that:
  1. On startup, sends a POST request to generate a webhook.
  2. Based on my registration number (**22BCE9013** → odd → Question 1), solves the SQL problem.
  3. Submits the final SQL query to the webhook URL with JWT authorization.

## 📝 My SQL Solution (Question 1)
```sql
SELECT p.AMOUNT AS SALARY,
       CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME,
       TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE,
       d.DEPARTMENT_NAME
FROM PAYMENTS p
JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID
JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
WHERE DAY(p.PAYMENT_TIME) <> 1
ORDER BY p.AMOUNT DESC
LIMIT 1;
