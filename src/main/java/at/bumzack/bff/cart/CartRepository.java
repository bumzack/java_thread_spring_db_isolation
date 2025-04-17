package at.bumzack.bff.cart;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Statement;

@Component
public class CartRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    public CartRepository(final JdbcTemplate jdbcTemplate,
                          final TransactionTemplate transactionTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
        transactionTemplate.setIsolationLevel(TransactionTemplate.ISOLATION_READ_COMMITTED);
    }

    @SuppressWarnings("DataFlowIssue")
    public int addToCart(final int cartId, final int cartVersion, final String productCodes) {
        final String query = "UPDATE cart SET product_codes = ?, version = ?  WHERE id = ? AND version = ? ";

        return transactionTemplate.execute(transaction -> {
            final var keyHolder = new GeneratedKeyHolder();

            final var affectedRows = jdbcTemplate.update(connection -> {
                final var ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, productCodes);
                ps.setInt(2, cartVersion + 1);
                ps.setInt(3, cartId);
                ps.setInt(4, cartVersion);

                return ps;
            }, keyHolder);

            if (affectedRows == 0) {
                // transaction rolls back
                throw new RuntimeException("Cart id not found or version not matching.");
            }

            return affectedRows;
        });
    }

    public Cart findById(final int cartId) {
        final String query = "SELECT * FROM  cart  WHERE id = " + cartId;
        final var res = jdbcTemplate.query(query, mapCart());
        return res.stream().findFirst().orElse(null);
    }

    public Cart findByIdAndVersion(final int cartId, final int cartVersion) {
        final String query = "SELECT * FROM  cart  WHERE id = " + cartId + " AND version = " + cartVersion;
        final var res = jdbcTemplate.query(query, mapCart());
        return res.stream().findFirst().orElse(null);
    }

    private static RowMapper<Cart> mapCart() {
        return (row, rowNr) -> new Cart(
                row.getInt("id"),
                row.getInt("version"),
                row.getString("product_codes")
        );
    }

    @SuppressWarnings("DataFlowIssue")
    public int updateCartVersion(final int cartId, final int cartVersion) {
        final String query = "UPDATE cart SET  version = ?  WHERE id = ? AND version = ? ";

        return transactionTemplate.execute(transaction -> {
            final var keyHolder = new GeneratedKeyHolder();

            final var affectedRows = jdbcTemplate.update(connection -> {
                final var ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, cartVersion + 1);
                ps.setInt(2, cartId);
                ps.setInt(3, cartVersion);

                return ps;
            }, keyHolder);

            if (affectedRows == 0) {
                // transaction rolls back
                throw new RuntimeException("Cart id not found or version not matching.");
            }

            return affectedRows;
        });
    }
}
