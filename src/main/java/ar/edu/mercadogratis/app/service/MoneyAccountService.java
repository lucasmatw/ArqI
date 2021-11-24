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

    public BigDecimal getFunds(User user) {
        return moneyAccountRepository.getByUser(user)
                .map(MoneyAccount::getBalance)
                .orElseThrow(() -> new ValidationException("Money account not found for user: " + user.getEmail()));
    }

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

        if(moneyAccount.getBalance().compareTo(amount) < 0) {
            throw new ValidationException("No funds available");
        }

        moneyAccount.subtractFromBalance(amount);
        moneyAccountRepository.save(moneyAccount);

        return moneyAccount.getBalance();
    }

    public BigDecimal creditAmount(User user, BigDecimal amount) {
        MoneyAccount moneyAccount = moneyAccountRepository.getByUser(user)
                .orElseThrow(() -> new ValidationException("Account not found for user"));

        moneyAccount.addToBalance(amount);
        moneyAccountRepository.save(moneyAccount);

        return moneyAccount.getBalance();
    }
}
