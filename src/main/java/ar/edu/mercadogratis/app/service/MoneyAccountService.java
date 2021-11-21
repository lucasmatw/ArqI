package ar.edu.mercadogratis.app.service;

import ar.edu.mercadogratis.app.dao.MoneyAccountRepository;
import ar.edu.mercadogratis.app.model.MoneyAccount;
import ar.edu.mercadogratis.app.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class MoneyAccountService {

    private final MoneyAccountRepository moneyAccountRepository;

    public MoneyAccount registerAccount(User user) {
        MoneyAccount account = MoneyAccount.builder()
                .balance(BigDecimal.ZERO)
                .user(user)
                .build();

        return moneyAccountRepository.save(account);
    }

    public BigDecimal debitAmount(User user, BigDecimal amount) {
        MoneyAccount moneyAccount = moneyAccountRepository.getByUser(user)
                .orElseThrow(() -> new ValidationException("Account not found for user"));

        moneyAccount.subtractFromBalance(amount);
        moneyAccountRepository.save(moneyAccount);

        return moneyAccount.getBalance();
    }

    public BigDecimal creditAmount(User user, BigDecimal amount) {
        MoneyAccount moneyAccount = moneyAccountRepository.getByUser(user)
                .orElseThrow(() -> new ValidationException("Account not found for user: " + user.getEmail()));

        moneyAccount.addToBalance(amount);
        moneyAccountRepository.save(moneyAccount);

        return moneyAccount.getBalance();
    }
}
