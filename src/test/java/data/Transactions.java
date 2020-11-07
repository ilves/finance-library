package data;

import ee.golive.finance.domain.FlowType;
import ee.golive.finance.domain.IAsset;
import ee.golive.finance.domain.IPrice;
import ee.golive.finance.domain.ITransaction;
import functional.Asset;
import functional.Price;
import functional.Transaction;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Transactions {
    private static Asset EUR = Asset.builder().name("Euro").type(IAsset.AssetType.CURRENCY).build();
    private static Asset USD = Asset.builder().name("Dollar").type(IAsset.AssetType.CURRENCY).build();
    private static Asset STOCK_1 = Asset.builder().name("Stock 1").type(IAsset.AssetType.ASSET).build();
    private static Asset STOCK_2 = Asset.builder().name("Stock 2").type(IAsset.AssetType.ASSET).build();

    public static List<ITransaction> getTransactions() {
        List<ITransaction> transactions = new ArrayList<>();
        transactions.add(getTransaction("2020-01-01", 1000, 1000, EUR, "Transfer"));
        transactions.add(getTransaction("2020-01-02", -50, -50, EUR, "Buying"));
        transactions.add(getTransaction("2020-02-01", -100, -100, EUR, "Exchange"));
        transactions.add(getTransaction("2020-02-01", 110, 110, USD, "Exchange"));
        transactions.add(getTransaction("2020-03-01", -50, -50, USD, "Stock1 buy"));
        transactions.add(getTransaction("2020-03-01", 5, 50, STOCK_1, "Stock1 buy"));
        transactions.add(getTransaction("2020-04-01", -200, -200, EUR, "Stock1 buy"));
        transactions.add(getTransaction("2020-04-01", 50, 200, STOCK_2, "Stock1 buy"));

        return transactions;
    }

    public static Transaction getTransaction(String date, double count, double amount, IAsset asset, String description) {
        return Transaction.builder()
                .dateTime(DateTime.parse(date))
                .count(BigDecimal.valueOf(count))
                .amount(BigDecimal.valueOf(amount))
                .asset(asset)
                .description(description)
                .flowType(FlowType.EXTERNAL)
                .build();
    }

    public static List<IPrice> getPrices() {
        List<IPrice> prices = new ArrayList<>();
        prices.add(Price.builder().asset(USD).currency(EUR).dateTime(DateTime.parse("2020-02-01")).price(new BigDecimal("0.90909")).build());
        prices.add(Price.builder().asset(USD).currency(EUR).dateTime(DateTime.parse("2020-02-28")).price(new BigDecimal("0.8")).build());
        prices.add(Price.builder().asset(STOCK_1).currency(USD).dateTime(DateTime.parse("2020-03-01")).price(new BigDecimal("10.0")).build());
        prices.add(Price.builder().asset(STOCK_1).currency(USD).dateTime(DateTime.parse("2020-03-02")).price(new BigDecimal("20.0")).build());
        prices.add(Price.builder().asset(STOCK_2).currency(EUR).dateTime(DateTime.parse("2020-04-01")).price(new BigDecimal("4.0")).build());

        return prices;
    }
}
