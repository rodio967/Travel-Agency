package ru.travelagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.travelagency.domain.FinancialEntry;
import ru.travelagency.domain.Tourist;
import ru.travelagency.enums.EntryType;
import ru.travelagency.enums.FinancialCategory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FinancialEntryRepository extends JpaRepository<FinancialEntry, Long>, JpaSpecificationExecutor<FinancialEntry> {

    List<FinancialEntry> findAllByGroupId(Long groupId);

    @Query("""
            select coalesce(sum(fe.amount), 0)
            from FinancialEntry fe
            where fe.group.id = :groupId and fe.entryType = :type""")
    BigDecimal sumByGroupAndType(@Param("groupId") Long groupId,
                                 @Param("type") EntryType type);

    @Query("""
            select coalesce(sum(fe.amount), 0)
            from FinancialEntry fe
            where fe.entryDate between :from and :to and fe.entryType = :type""")
    BigDecimal sumByPeriodAndType(@Param("from") LocalDate from,
                                  @Param("to") LocalDate to,
                                  @Param("type") EntryType type);

    @Query("""
            select fe.category, sum(fe.amount)
            from FinancialEntry fe
            where fe.group.id = :groupId and fe.entryType = :type
            group by fe.category""")
    List<Object[]> sumByGroupGroupedByCategory(@Param("groupId") Long groupId,
                                               @Param("type") EntryType type);


    @Query("""
            select coalesce(sum(fe.amount), 0)
            from FinancialEntry fe
            where fe.group.id = :groupId and fe.entryType = :type
            and TYPE(fe.tourist) = :touristType""")
    BigDecimal sumByGroupTypeAndTouristType(@Param("groupId") Long groupId,
                                            @Param("type") EntryType type,
                                            @Param("touristType") Class<? extends Tourist> touristType);

    @Query("""
            select fe.category, sum(fe.amount)
            from FinancialEntry fe
            where fe.group.id = :groupId and fe.entryType = :type
            and TYPE(fe.tourist) = :touristType
            group by fe.category""")
    List<Object[]> sumByGroupCategoryAndTouristType(@Param("groupId") Long groupId,
                                                    @Param("type") EntryType type,
                                                    @Param("touristType") Class<? extends Tourist> touristType);

    @Query("""
            select fe.category, sum(fe.amount)
            from FinancialEntry fe
            where fe.entryDate between :from and :to and fe.entryType = :type
            group by fe.category""")
    List<Object[]> sumByPeriodGroupedByCategory(@Param("from") LocalDate from,
                                                @Param("to") LocalDate to,
                                                @Param("type") EntryType type);

    @Query("""
            select fe.entryType, sum(fe.amount)
            from FinancialEntry fe
            where fe.group.id = :groupId
            group by fe.entryType""")
    List<Object[]> incomeExpenseForGroup(@Param("groupId") Long groupId);


    @Query("""
            select fe.entryType, coalesce(sum(fe.amount), 0)
            from FinancialEntry fe
            group by fe.entryType""")
    List<Object[]> globalIncomeExpense();
}