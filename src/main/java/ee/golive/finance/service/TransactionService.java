package ee.golive.finance.service;

import ee.golive.finance.domain.FlowType;
import ee.golive.finance.domain.IsAsset;
import ee.golive.finance.domain.IsTransaction;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Taavi Ilves, Golive, ilves.taavi@gmail.com
 */
public class TransactionService {

    /**
     * Filters out transactions that happened before provided timestamp (inclusive)
     *
     * @param dateTime Date time before the transactions happened
     * @return filtered and sorted list
     */
    public List<IsTransaction> getTransactionsBefore(DateTime dateTime, List<? extends IsTransaction> transactions) {
        return transactions
                .stream()
                .sorted((x, y) -> x.getDateTime().compareTo(y.getDateTime()))
                .filter(x -> x.getDateTime().compareTo(dateTime) <= 0)
                .collect(Collectors.toList());
    }

    public BigDecimal getItemCount(List<? extends IsTransaction> transactions) {
        return transactions
                .stream()
                .map(IsTransaction::getCount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal getAmountSum(List<? extends IsTransaction> transactions) {
        return transactions
                .stream()
                .map(IsTransaction::getAmount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public Map<IsAsset, List<IsTransaction>> groupByAsset(List<? extends IsTransaction> transactions) {
        return transactions
                .stream()
                .filter(x -> x.getFlowType() != FlowType.INTERNAL)
                .collect(Collectors.groupingBy(IsTransaction::getAsset));
    }
}