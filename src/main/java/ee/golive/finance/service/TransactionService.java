package ee.golive.finance.service;

import ee.golive.finance.domain.ITransaction;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;
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
    public List<ITransaction> getTransactionsBefore(DateTime dateTime, List<? extends ITransaction> transactions) {
        return transactions
                .stream()
                .sorted((x, y) -> x.getDateTime().compareTo(y.getDateTime()))
                .filter(before(dateTime))
                .collect(Collectors.toList());
    }

    public BigDecimal sumCount(List<? extends ITransaction> transactions) {
        return transactions
                .stream()
                .map(ITransaction::getCount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal sumAmount(List<? extends ITransaction> transactions) {
        return transactions
                .stream()
                .map(ITransaction::getAmount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public static Predicate<ITransaction> before(DateTime dateTime) {
        return (t) -> t.getDateTime().compareTo(dateTime) <= 0;
    }
}
