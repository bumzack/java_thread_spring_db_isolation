package at.bumzack.bff.cart;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Statement;
import java.util.Optional;

@Service
public class CartRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    public CartRepository(final JdbcTemplate jdbcTemplate,
                          final TransactionTemplate transactionTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
    }

    public Cart findById(int cartId) {
        return transactionTemplate.execute(transaction -> {
            final var selectQuery = "SELECT * FROM cart WHERE id =" + cartId;       // highly unsafe ..., altough int ..
            final var res = jdbcTemplate.query(selectQuery, cartFromDbRows());
            return res.stream().findFirst().orElse(null);
        });
    }

    @SuppressWarnings("DataFlowIssue")
    public int create() {
        // plain SQL  +  placeholder ? for prepared statements

        final var query = "INSERT INTO cart (product_codes) VALUES (?)";

        return transactionTemplate.execute(transaction -> {
            final var keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                final var ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, "empty");
                return ps;
            }, keyHolder);

            return Optional.ofNullable(keyHolder.getKey())
                    .map(Number::intValue)
                    .orElse(-1);
        });
    }

    @SuppressWarnings("DataFlowIssue")
    public int addToCart(final int cartId, final String productCode) {
        // plain SQL  +  placeholder ? for prepared statements
        final var updateQuery = "UPDATE cart SET x product_codes =  ?  WHERE id = ?";

        return transactionTemplate.execute(transaction -> {
            final var keyHolder = new GeneratedKeyHolder();

            final int affectedRows = jdbcTemplate.update(connection -> {
                final var ps = connection.prepareStatement(updateQuery, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, productCode);
                ps.setInt(2, cartId);
                return ps;
            }, keyHolder);

            if (affectedRows == 0) {
                // transaction rolls back
                throw new RuntimeException("CartId not found.");
            }
            return affectedRows;
        });
    }

    private static RowMapper<Cart> cartFromDbRows() {
        return (row, rowNo) ->
                new Cart(
                        row.getInt("id"),
                        row.getString("product_codes")
                );
    }
}


