package com.ecampos.challenge.infrastructure.persistence;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CalculationHistoryRepository extends JpaRepository<CalculationHistoryEntity, Long> {


    @Query(
            value = "SELECT * FROM calculation_history ORDER BY timestamp DESC",
            countQuery = "SELECT count(*) FROM calculation_history",
            nativeQuery = true
    )
    Page<CalculationHistoryEntity> findAllWithNativePagination(Pageable pageable);


}
