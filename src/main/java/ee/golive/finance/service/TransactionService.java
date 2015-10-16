package ee.golive.finance.service;

import ee.golive.finance.domain.Asset;
import ee.golive.finance.domain.Transactional;
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
    public List<Transactional> getTransactionsBefore(DateTime dateTime, List<Transactional> transactions) {
        return transactions
                .stream()
                .sorted((x, y) -> x.getDateTime().compareTo(y.getDateTime()))
                .filter(x -> x.getDateTime().compareTo(dateTime) <= 0)
                .collect(Collectors.toList());
    }

    public BigDecimal getItemCount(List<Transactional> transactions) {
        return transactions
                .stream()
                .map(Transactional::getCount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal getAmountSum(List<Transactional> transactions) {
        return transactions
                .stream()
                .map(Transactional::getAmount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public Map<Asset, List<Transactional>> groupByAsset(List<Transactional> transactions) {
        return transactions
                .stream()
                .collect(Collectors.groupingBy(Transactional::getAsset));
    }
}