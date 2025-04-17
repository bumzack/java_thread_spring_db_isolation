package at.bumzack.bff.purchase;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Statement;

@Component
public class PurchaseRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    public PurchaseRepository(final JdbcTemplate jdbcTemplate,
                              final TransactionTemplate transactionTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
        transactionTemplate.setIsolationLevel(TransactionTemplate.ISOLATION_READ_COMMITTED);
    }

    @SuppressWarnings("DataFlowIssue")
    public int createPurchase(final String productCodes, final int cartId, final int cartVersion) {
        final String queryUpdateCart = "UPDATE cart SET  version = ? WHERE id = ? AND version = ? ";

        return transactionTemplate.execute(transaction -> {
            final var keyHolder = new GeneratedKeyHolder();

            final var affectedRows = jdbcTemplate.update(connection -> {
                final var ps = connection.prepareStatement(queryUpdateCart, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, cartVersion + 1);
                ps.setInt(2, cartId);
                ps.setInt(3, cartVersion);

                return ps;
            }, keyHolder);

            if (affectedRows == 0) {
                // transaction rolls back
                throw new RuntimeException("Cart id not found or version not matching.");
            }

            final String queryInsertPurchase = "INSERT INTO purchase (product_codes, cart_id) VALUES (?, ?)";

            final var keyHolderPurchaseId = new GeneratedKeyHolder();

            final var purchaseAffectedRows = jdbcTemplate.update(connection -> {
                final var ps = connection.prepareStatement(queryInsertPurchase, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, productCodes);
                ps.setInt(2, cartId);
                return ps;
            }, keyHolderPurchaseId);

            if (purchaseAffectedRows == 0) {
                // transaction rolls back
                throw new RuntimeException("create Purchase failed.");
            }

            return purchaseAffectedRows;
        });
    }


    @SuppressWarnings("DataFlowIssue")
    public int createPurchase2(final int cartId, final int cartVersion, final String productCodes) {
        final String query = "INSERT INTO purchase (product_codes, cart_id) VALUES (?, ?)";

        return transactionTemplate.execute(transaction -> {
            final var keyHolder = new GeneratedKeyHolder();

            final var affectedRows = jdbcTemplate.update(connection -> {
                final var ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, productCodes);
                ps.setInt(2, cartId);
                return ps;
            }, keyHolder);

            if (affectedRows == 0) {
                // transaction rolls back
                throw new RuntimeException("create Purchase failed.");
            }

            return affectedRows;
        });
    }


    private static RowMapper<Purchase> mapPurchase() {
        return (row, rowNr) -> new Purchase(
                row.getInt("id"),
                row.getString("product_codes")
        );
    }
}
