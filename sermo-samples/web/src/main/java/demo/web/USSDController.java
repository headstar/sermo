package demo.web;

import com.headstartech.sermo.SubscriberEvent;
import com.headstartech.sermo.USSDApplication;
import org.springframework.stereotype.Controller;

/**
 * @author Per Johansson
 */
@Controller
public class USSDController {

    private final  USSDApplication<States, SubscriberEvent> ussdApplication;

    public USSDController(USSDApplication<States, SubscriberEvent> ussdApplication) {
        this.ussdApplication = ussdApplication;
    }


}
