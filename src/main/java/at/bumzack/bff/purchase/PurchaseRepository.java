package at.bumzack.bff.purchase;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Statement;
import java.util.Optional;

@Service
public class PurchaseRepository {
    // private final Logger LOG = LogManager.getLogger(PurchaseRepository.class);

    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    public PurchaseRepository(final JdbcTemplate jdbcTemplate,
                              final TransactionTemplate transactionTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
    }

    @SuppressWarnings({"DataFlowIssue"})
    public int create(final String productCodes) {
        final String query = "INSERT INTO purchase (product_codes) VALUES (?)";

        // transaction rolls back
        return transactionTemplate.execute(transaction -> {
            final var keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                final var ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, productCodes);
                return ps;
            }, keyHolder);

            return Optional.ofNullable(keyHolder.getKey())
                    .map(Number::intValue)
                    .orElse(-1);
        });
    }
}
