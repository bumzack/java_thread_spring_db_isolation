package at.bumzack.bff.otherstuff;

import at.bumzack.bff.cart.Cart;
import at.bumzack.bff.cart.CartRepository;
import at.bumzack.bff.filter.SessionFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SessionService {
    private final Logger LOG = LogManager.getLogger(SessionService.class);

    private final Map<Integer, Cart> carts = new HashMap<>();

    private final CartRepository cartRepository;

    public SessionService(final CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart getSessionCart(final int cartId) {
        return carts.get(cartId);
    }

    public void addToSession(final int cartId) {
        if (!carts.containsKey(cartId)) {
            LOG.info("1  add cart to session for cart-id {}", cartId);
            synchronized (carts) {
                if (!carts.containsKey(cartId)) {
                    LOG.info("222222   add cart to session for cart-id {}", cartId);
                    final var cart = cartRepository.findById(cartId);
                    carts.put(cartId, cart);
                }
            }
        }
    }
}
