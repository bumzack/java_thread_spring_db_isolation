package at.bumzack.bff.cart;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Statement;

@Component
public class CartRepository {

    private final JdbcTemplate jdbcTemplate;

    public CartRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int addToCart(final int cartId, final String productCodes) {
        final String query = "UPDATE cart SET product_codes = ? WHERE id = ?  ";

        final var keyHolder = new GeneratedKeyHolder();

        return jdbcTemplate.update(connection -> {
            final var ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, productCodes);
            ps.setInt(2, cartId);
            return ps;
        }, keyHolder);
    }

    public Cart findById(final int cartId) {
        final String query = "SELECT * FROM  cart  WHERE id = " + cartId;
        final var res = jdbcTemplate.query(query, mapCart());
        return res.stream().findFirst().orElse(null);
    }

    private static RowMapper<Cart> mapCart() {
        return (row, rowNr) -> new Cart(
                row.getInt("id"),
                row.getString("product_codes")
        );
    }
}
