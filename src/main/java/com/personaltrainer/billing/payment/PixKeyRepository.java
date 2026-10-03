package com.personaltrainer.billing.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PixKeyRepository extends JpaRepository <PixKey, Long> {

    @Query (value = "SELECT * FROM pix_keys WHERE active = true ORDER BY random() LIMIT 1",
            nativeQuery = true)
    Optional<PixKey> findRandomActive();
}
