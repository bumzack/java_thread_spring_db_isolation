package at.bumzack.bff.purchase;

import at.bumzack.bff.cart.CartRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Objects.nonNull;

@RestController
public class PurchaseController {
    private final Logger LOG = LogManager.getLogger(PurchaseController.class);

    private final CartRepository cartRepository;
    private final PurchaseRepository purchaseRepository;

    public PurchaseController(final CartRepository cartRepository, final PurchaseRepository purchaseRepository) {
        this.cartRepository = cartRepository;
        this.purchaseRepository = purchaseRepository;
    }

    @PostMapping(value = "/api/purchases", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String addToCart1(@RequestBody final CreatePurchase createPurchase) {
        try {
            final var cart = cartRepository.findByIdAndVersion(createPurchase.cartId(), createPurchase.cartVersion());
            if (nonNull(cart)) {
                LOG.info("XXXX    cart != null, threadId = {}", Thread.currentThread().threadId());
            }
            Thread.sleep(2);
            final var affectRows = cartRepository.updateCartVersion(createPurchase.cartId(), createPurchase.cartVersion());
            if (affectRows == 1) {
                LOG.info("XXXX    affectRows == 1, threadId = {}", Thread.currentThread().threadId());
            }
            Thread.sleep(2);
            final var purchaseId = purchaseRepository.createPurchase2(createPurchase.cartId(), createPurchase.cartId(), cart.productCode());
            LOG.info("XXXX    purchaseId == 1, threadId = {}", Thread.currentThread().threadId());
            LOG.info("Successfully created purchase with Id " + purchaseId);
            return "Successfully created purchase with Id " + purchaseId;
        } catch (final Exception e) {
            LOG.error("cant create purchase for  cartId {} and cartVersion {}. ",
                    createPurchase.cartId(), createPurchase.cartVersion());
            LOG.error(e.getMessage());
            return "cant create purchase";
        }
    }

    public String addToCart(@RequestBody final CreatePurchase createPurchase) {
        final var cart = cartRepository.findByIdAndVersion(createPurchase.cartId(), createPurchase.cartVersion());
        if (nonNull(cart)) {
            try {
                final var purchaseId = purchaseRepository.createPurchase(cart.productCode(), cart.cartId(), cart.cartVersion());
                LOG.info("Successfully created purchase with Id " + purchaseId);
                return "Successfully created purchase with Id " + purchaseId;
            } catch (final Exception e) {
                LOG.error("cant create purchase for  cartId {} and cartVersion {}. ",
                        createPurchase.cartId(), createPurchase.cartVersion());
                LOG.error(e.getMessage());
                return "cant create purchase";
            }
        }
        LOG.error("cart with id {} and version {} not found. ",
                createPurchase.cartId(), createPurchase.cartVersion());
        return "cart with version " + createPurchase.cartVersion() + " not found.";
    }

}
