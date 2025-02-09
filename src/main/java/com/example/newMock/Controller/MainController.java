package com.example.newMock.Controller;

import com.example.newMock.Model.RequestDTO;
import com.example.newMock.Model.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@RestController
public class MainController {

    private final Logger log = LoggerFactory.getLogger(MainController.class);
    private final ObjectMapper mapper = new ObjectMapper();

    @PostMapping(
            value = "/info/postBalances",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    public static void setBalanceAndLimit(ResponseDTO response, String currency, String Limit) {

        BigDecimal maxLimit = new BigDecimal(Limit);
        BigDecimal balance = BigDecimal.valueOf(
                new Random().nextDouble(Double.parseDouble(Limit)))
                .setScale(2, RoundingMode.HALF_UP);
        response.setBalance(balance);
        response.setMaxLimit(maxLimit);
        response.setCurrency(currency);
    }

    public Object postBalances(@RequestBody RequestDTO requestDTO) {

        try {
            String rqUID = requestDTO.getRqUID();
            String clientId = requestDTO.getClientId();
            String account = requestDTO.getAccount();

            char firstDigit = clientId.charAt(0);

            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setRqUID(rqUID);
            responseDTO.setClientId(clientId);
            responseDTO.setAccount(account);

            if (firstDigit == '8') {
                setBalanceAndLimit(responseDTO, "US", "2000.00");
            } else if (firstDigit == '9') {
                setBalanceAndLimit(responseDTO, "EU", "1000.00");
            } else {
                setBalanceAndLimit(responseDTO, "RU", "10000.00");
            }

            log.info("Request: {}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDTO));
            log.info("Response: {}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDTO));
            return responseDTO;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}