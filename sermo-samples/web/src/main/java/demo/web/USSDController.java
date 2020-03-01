package demo.web;

import com.headstartech.sermo.EventResult;
import com.headstartech.sermo.SubscriberEvent;
import com.headstartech.sermo.USSDApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Per Johansson
 */
@Controller
public class USSDController {

    @Autowired
    private USSDApplication<States, SubscriberEvent> ussdApplication;

    @Autowired
    private List<ShortCode> shortCodes;

    @GetMapping("/")
    public String input(Model model) {
        model.addAttribute("shortCodes", shortCodes);
        return "ussdapp";
    }

    @RequestMapping("/")
    public String input(@RequestParam("msisdn") String msisdn, @RequestParam("input") String input , Model model) throws Exception {

        EventResult eventResult = ussdApplication.applyEvent(msisdn, new SubscriberEvent(input, msisdn));

        EventResult.ApplicationState applicationState = eventResult.getApplicationState();
        if(EventResult.ApplicationState.COMPLETE.equals(applicationState) || EventResult.ApplicationState.ERROR.equals(applicationState)) {
            if(EventResult.ApplicationState.COMPLETE.equals(applicationState)) {
                model.addAttribute("screen", eventResult.getOutput().orElse("(completed)"));
            } else {
                model.addAttribute("screen", eventResult.getOutput().orElse("(internal error)"));
            }
            model.addAttribute("msisdn", "");
        } else {
            model.addAttribute("screen", eventResult.getOutput().orElse(""));
            model.addAttribute("msisdn",  msisdn);
        }

        model.addAttribute("shortCodes", shortCodes);

        return "ussdapp";
    }


}
