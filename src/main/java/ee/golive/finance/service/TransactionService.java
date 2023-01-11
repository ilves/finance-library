package ee.golive.finance.service;

import ee.golive.finance.domain.ITransaction;
import ee.golive.finance.domain.TransactionType;
import ee.golive.finance.domain.ValueContext;
import lombok.AllArgsConstructor;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
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
                .sorted(Comparator.comparing(ITransaction::getDateTime))
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

    public AvgPrice averagePrice(List<? extends ITransaction> transactions) {
        BigDecimal avgPrice = BigDecimal.ZERO;
        BigDecimal avgBasePrice = BigDecimal.ZERO;
        BigDecimal count = BigDecimal.ZERO;

        for (ITransaction tx: transactions) {
            if (tx.getType().equals(TransactionType.BUY)) {
                BigDecimal newCount = count.add(tx.getCount());
                avgPrice = avgPrice.multiply(count).add(tx.getCount().multiply(tx.getPrice()))
                        .divide(newCount, RoundingMode.HALF_EVEN);
                avgBasePrice = avgBasePrice.multiply(count).add(tx.getCount().multiply(tx.getBasePrice()))
                        .divide(newCount, RoundingMode.HALF_EVEN);
                count = newCount;
            } else if (tx.getType().equals(TransactionType.SELL)) {
                count = count.subtract((tx.getCount()));
            } else if (tx.getType().equals(TransactionType.SPLIT)) {
                count = count.add(tx.getCount());
            }

            tx.setValueContext(ValueContext.builder()
                    .averagePrice(avgPrice)
                    .averageBasePrice(avgBasePrice)
                    .build());
        }



        return new AvgPrice(avgPrice, avgBasePrice);
    }

    public static Predicate<ITransaction> before(DateTime dateTime) {
        return (t) -> t.getDateTime().compareTo(dateTime) <= 0;
    }

    public class AvgPrice {
        BigDecimal price;
        BigDecimal basePrice;

        AvgPrice (BigDecimal price, BigDecimal basePrice) {
            this.price = price;
            this.basePrice = basePrice;
        }
    }
}
