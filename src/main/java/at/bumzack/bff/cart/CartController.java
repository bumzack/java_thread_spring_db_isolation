package at.bumzack.bff.cart;

import at.bumzack.bff.otherstuff.SessionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {

    private final CartRepository cartRepository;
    private final SessionService sessionService;

    public CartController(final CartRepository cartRepository, final SessionService sessionService) {
        this.cartRepository = cartRepository;
        this.sessionService = sessionService;
    }

    @PutMapping(value = "/api/carts", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String addToCart(@RequestBody final AddToCart addToCart) {

        final var cart = sessionService.getSessionCart(addToCart.cartId());

        synchronized (cart) {
            final var currentCart = cartRepository.findById(addToCart.cartId());
            final var newProductCodes = currentCart.productCode() + ", " + addToCart.productCode();
            final var affectedRows = cartRepository.addToCart(addToCart.cartId(), newProductCodes);
            return "affected rows: " + affectedRows;
        }
    }

    public String addToCart1(@RequestBody final AddToCart addToCart) {
        final var cart = cartRepository.findById(addToCart.cartId());
        final var newProductCodes = cart.productCode() + ", " + addToCart.productCode();
        final var affectedRows = cartRepository.addToCart(addToCart.cartId(), newProductCodes);
        return "affected rows: " + affectedRows;
    }

    public String addToCart2(@RequestBody final AddToCart addToCart) {
        synchronized (this) {
            final var cart = cartRepository.findById(addToCart.cartId());
            final var newProductCodes = cart.productCode() + ", " + addToCart.productCode();
            final var affectedRows = cartRepository.addToCart(addToCart.cartId(), newProductCodes);
            return "affected rows: " + affectedRows;
        }
    }

    public String addToCart3(@RequestBody final AddToCart addToCart) {
        final var cart = cartRepository.findById(addToCart.cartId());

        synchronized (cart) {
            final var newProductCodes = cart.productCode() + ", " + addToCart.productCode();
            final var affectedRows = cartRepository.addToCart(addToCart.cartId(), newProductCodes);
            return "affected rows: " + affectedRows;
        }
    }
}
