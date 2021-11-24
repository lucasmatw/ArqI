package ar.edu.mercadogratis.app.controller;


import ar.edu.mercadogratis.app.model.AddFundsRequest;
import ar.edu.mercadogratis.app.model.Product;
import ar.edu.mercadogratis.app.model.User;
import ar.edu.mercadogratis.app.service.MoneyAccountService;
import ar.edu.mercadogratis.app.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.util.Objects;

@RestController
@RequestMapping("/money_account")
@RequiredArgsConstructor
public class MoneyAccountController {

    private final MoneyAccountService moneyAccountService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<BigDecimal> addFunds(@Valid @RequestBody AddFundsRequest addFundsRequest) {
        User user = getUser(addFundsRequest.getUserId());
        BigDecimal newBalance = moneyAccountService.creditAmount(user, addFundsRequest.getAmount());
        return ResponseEntity.ok(newBalance);
    }

    @GetMapping
    public ResponseEntity<BigDecimal> getFunds(@RequestParam Long userId) {
        User user = getUser(userId);
        return ResponseEntity.ok(moneyAccountService.getFunds(user));
    }

    private User getUser(Long userId) {
        User user = userService.getUser(userId);
        if(Objects.isNull(user)) {
            throw new ValidationException("User not found: " + userId);
        }

        return user;
    }
}
