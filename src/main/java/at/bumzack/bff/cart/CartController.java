package at.bumzack.bff.cart;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {
    private final Logger LOG = LogManager.getLogger(CartController.class);

    private final CartRepository cartRepository;

    public CartController(final CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }
}
