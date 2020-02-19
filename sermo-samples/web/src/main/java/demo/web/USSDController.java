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

/**
 * @author Per Johansson
 */
@Controller
public class USSDController {

    @Autowired
    private USSDApplication<States, SubscriberEvent> ussdApplication;


    @GetMapping("/")
    public String home() {
        return "redirect:/input";
    }

    @GetMapping("/input")
    public String input() {
        return "ussdapp";
    }


    @RequestMapping("/input")
    public String input(@RequestParam("msisdn") String msisdn, @RequestParam("input") String input , Model model) throws Exception {

        EventResult eventResult = ussdApplication.applyEvent(msisdn, new SubscriberEvent(input, msisdn));

        if(eventResult.isApplicationCompleted() || eventResult.isApplicationError()) {
            if(eventResult.isApplicationCompleted()) {
                model.addAttribute("screen", eventResult.getOutput().orElse("(completed)"));
            } else {
                model.addAttribute("screen", eventResult.getOutput().orElse("(internal error)"));
            }
            model.addAttribute("msisdn", "");
        } else {
            model.addAttribute("screen", eventResult.getOutput().orElse("ERROR"));
            model.addAttribute("msisdn",  msisdn);
        }

        return "ussdapp";
    }


}
