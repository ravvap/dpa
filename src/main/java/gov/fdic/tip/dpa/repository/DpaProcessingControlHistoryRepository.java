package gov.fdic.tip.dpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import gov.fdic.tip.dpa.entity.DpaProcessingControlHistory;

import java.util.List;

public interface DpaProcessingControlHistoryRepository extends JpaRepository<DpaProcessingControlHistory, Long> {
    
    // Mode 1: Search by specific Data Element (sorted desc by period via Pageable/Sort)
    Page<DpaProcessingControlHistory> findByLineItemNumber(Integer lineItemNumber, Pageable pageable);
    
    // Mode 2: Search by Source System (using Description pattern) and Period
    // Matches AC-5: Sorted by Line Item ascending
    List<DpaProcessingControlHistory> findByLineItemDescriptionContainingAndProcessingPeriodOrderByLineItemNumberAsc(
            String sourceSystem, String processingPeriod);
}