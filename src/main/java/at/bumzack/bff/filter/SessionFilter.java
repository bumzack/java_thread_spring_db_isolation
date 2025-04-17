package at.bumzack.bff.filter;

import at.bumzack.bff.otherstuff.SessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Objects.nonNull;

@Component
public class SessionFilter extends OncePerRequestFilter {
    private final Logger LOG = LogManager.getLogger(SessionFilter.class);

    private final SessionService sessionService;

    public SessionFilter(final SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        // that's not how "Sessions" in production work, this is just an example for the "double read" pattern in synchronized blocks
        final var cartId = request.getHeader("cart-id");

        if (nonNull(cartId)) {
            LOG.info("filter: add cart to session for cart-id {}", cartId);
            final var id = Integer.valueOf(cartId);
            sessionService.addToSession(id);
        }

        filterChain.doFilter(request, response);
    }
}
