package at.bumzack.bff.cart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Objects.nonNull;

@RestController
public class CartController {
    private final Logger LOG = LogManager.getLogger(CartController.class);

    private final CartRepository cartRepository;

    public CartController(final CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @PutMapping(value = "/api/carts", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String addToCart(@RequestBody final AddToCart addToCart) {
        final var currentCart = cartRepository.findByIdAndVersion(addToCart.cartId(), addToCart.cartVersion());
        if (nonNull(currentCart)) {
            try {
                // 13:52:00.343   4 requests
                final var newProductCodes = currentCart.productCode() + ", " + addToCart.productCode();
                final var affectedRows = cartRepository.addToCart(addToCart.cartId(), addToCart.cartVersion(), newProductCodes);
                LOG.info("Successfully added " + addToCart.productCode() + " to the cart");
                return "Successfully added " + addToCart.productCode() + " to the cart";
            } catch (final Exception e) {
                LOG.error("cart update did not affect any rows -  version {}. ", addToCart.cartVersion());
                return "cart update did not affect any rows ";
            }
        }
        LOG.error("cart with version {} not found. ", addToCart.cartVersion());
        return "cart with version " + addToCart.cartVersion() + " not found.";
    }
}
