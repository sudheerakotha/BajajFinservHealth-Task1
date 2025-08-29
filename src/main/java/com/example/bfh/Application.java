package com.example.bfh;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        RestTemplate rest = new RestTemplate();

        String genUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> genBody = Map.of(
                "name", "Sudheera Kotha",        
                "regNo", "22BCE9013",       
                "email", "sudheerakotha@gmail.com" 
        );

        try {
            HttpEntity<Map<String, String>> genEntity = new HttpEntity<>(genBody, headers);
            ResponseEntity<Map> genResp = rest.postForEntity(genUrl, genEntity, Map.class);

            if (genResp.getStatusCode().is2xxSuccessful() && genResp.getBody() != null) {
                Map<String, Object> resp = genResp.getBody();
                String webhookUrl = (String) resp.get("webhook");
                String accessToken = (String) resp.get("accessToken");

                System.out.println("Webhook URL: " + webhookUrl);
                System.out.println("Access Token: " + accessToken);

                String finalQuery = "SELECT p.AMOUNT AS SALARY, " +
                    "CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, " +
                    "TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, " +
                    "d.DEPARTMENT_NAME " +
                    "FROM PAYMENTS p " +
                    "JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID " +
                    "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
                    "WHERE DAY(p.PAYMENT_TIME) <> 1 " +
                    "ORDER BY p.AMOUNT DESC " +
                    "LIMIT 1;";

                HttpHeaders authHeaders = new HttpHeaders();
                authHeaders.setContentType(MediaType.APPLICATION_JSON);
                authHeaders.set("Authorization", "Bearer " + accessToken);

                Map<String, String> submitBody = Map.of("finalQuery", finalQuery);
                HttpEntity<Map<String, String>> submitEntity = new HttpEntity<>(submitBody, authHeaders);

                ResponseEntity<String> submitResp = rest.postForEntity(webhookUrl, submitEntity, String.class);

                System.out.println("Submission Response: " + submitResp.getBody());
            } else {
                System.out.println("Failed to generate webhook.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
