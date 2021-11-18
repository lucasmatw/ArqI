package ar.edu.mercadogratis.app.controller;


import ar.edu.mercadogratis.app.model.AddFundsRequest;
import ar.edu.mercadogratis.app.model.Product;
import ar.edu.mercadogratis.app.model.User;
import ar.edu.mercadogratis.app.service.MoneyAccountService;
import ar.edu.mercadogratis.app.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("/money_account")
@RequiredArgsConstructor
public class MoneyAccountController {

    private final MoneyAccountService moneyAccountService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<BigDecimal> addFunds(@Valid @RequestBody AddFundsRequest addFundsRequest) {
        User user = userService.getUser(addFundsRequest.getUserId());
        BigDecimal newBalance = moneyAccountService.creditAmount(user, addFundsRequest.getAmount());
        return ResponseEntity.ok(newBalance);
    }
}
