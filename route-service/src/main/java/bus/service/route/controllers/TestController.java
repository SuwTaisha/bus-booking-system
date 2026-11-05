package bus.service.route.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * This class is just for testing Eureka if it works or not
 * Will be removed in the future
 */

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Service 1, Online";
    }
}
