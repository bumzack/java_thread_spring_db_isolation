package at.bumzack.bff.purchase;


import at.bumzack.bff.cart.CartRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PurchaseController {
    private final Logger LOG = LogManager.getLogger(PurchaseController.class);

    private final PurchaseRepository purchaseRepository;
    private final CartRepository cartRepository;

    public PurchaseController(final PurchaseRepository purchaseRepository, CartRepository cartRepository) {
        this.purchaseRepository = purchaseRepository;
        this.cartRepository = cartRepository;
    }

}
