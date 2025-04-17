package at.bumzack.bff;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {

    @GetMapping(value = "/dummy/{id}")
    public String doStuff(@PathVariable final String id) {
        synchronized (this) {
            placeOrderInSAP();
        }
        return "done - " + id + "\n";
    }

    private void placeOrderInSAP() {
        try {
            Thread.sleep(5 * 1000);   //DB request, HTTP reuest an SAP , ...
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
