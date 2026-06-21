package ru.travelagency.dto.report;

import ru.travelagency.enums.FinancialCategory;

import java.math.BigDecimal;
import java.util.List;


public record FinancialReportDto(
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal balance,
        BigDecimal profitability,
        List<CategoryAmount> incomeByCategory,
        List<CategoryAmount> expenseByCategory
) {
    public record CategoryAmount(
            FinancialCategory category,
            BigDecimal amount
    ) {}
}
