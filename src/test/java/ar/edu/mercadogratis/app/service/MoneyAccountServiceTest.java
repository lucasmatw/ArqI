package ar.edu.mercadogratis.app.service;

import ar.edu.mercadogratis.app.dao.MoneyAccountRepository;
import ar.edu.mercadogratis.app.model.MoneyAccount;
import ar.edu.mercadogratis.app.model.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class})
public class MoneyAccountServiceTest {

    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public MoneyAccountService moneyAccountService(MoneyAccountRepository moneyAccountRepository) {
            return new MoneyAccountService(moneyAccountRepository);
        }
    }

    @Autowired
    private MoneyAccountService moneyAccountService;

    @MockBean
    private MoneyAccountRepository moneyAccountRepository;


    @Test
    public void testRegisterAccount() {

        User user = mock(User.class);

        MoneyAccount moneyAccountMock = mock(MoneyAccount.class);

        MoneyAccount expected = MoneyAccount.builder()
                .user(user)
                .balance(BigDecimal.ZERO)
                .build();
        when(moneyAccountRepository.save(argThat(new MoneyAccountMatcher(expected)))).thenReturn(moneyAccountMock);

        MoneyAccount moneyAccount = moneyAccountService.registerAccount(user);

        assertThat(moneyAccount).isEqualTo(moneyAccountMock);
    }

    @Test
    public void testCreditAmount() {
        User user = mock(User.class);

        MoneyAccount moneyAccount = mock(MoneyAccount.class);
        when(moneyAccount.getBalance()).thenReturn(BigDecimal.ONE);

        when(moneyAccountRepository.getByUser(user)).thenReturn(Optional.of(moneyAccount));

        moneyAccountService.creditAmount(user, new BigDecimal("100"));

        verify(moneyAccount).addToBalance(new BigDecimal("100"));
    }

    @Test
    public void testDebitAmount() {
        User user = mock(User.class);

        MoneyAccount moneyAccount = mock(MoneyAccount.class);
        when(moneyAccount.getBalance()).thenReturn(new BigDecimal("100"));

        when(moneyAccountRepository.getByUser(user)).thenReturn(Optional.of(moneyAccount));

        moneyAccountService.debitAmount(user, new BigDecimal("50"));

        verify(moneyAccount).subtractFromBalance(new BigDecimal("50"));
    }

    @Data
    @RequiredArgsConstructor
    static class MoneyAccountMatcher implements ArgumentMatcher<MoneyAccount> {

        private final MoneyAccount match;

        @Override
        public boolean matches(MoneyAccount moneyAccount) {
            return match.getUser().equals(moneyAccount.getUser()) &&
                    match.getBalance().equals(moneyAccount.getBalance());
        }
    }
}