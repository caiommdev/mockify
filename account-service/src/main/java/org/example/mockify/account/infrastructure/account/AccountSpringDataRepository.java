package org.example.mockify.account.infrastructure.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountSpringDataRepository extends JpaRepository<AccountJpaEntity, UUID> {

    Optional<AccountJpaEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
