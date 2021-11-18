package ar.edu.mercadogratis.app.dao;

import ar.edu.mercadogratis.app.model.MoneyAccount;
import ar.edu.mercadogratis.app.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface MoneyAccountRepository extends PagingAndSortingRepository<MoneyAccount, Long> {

    @Query("select u from MoneyAccount u where u.user = ?1")
    Optional<MoneyAccount> getByUser(User user);
}
