package demo.web;

import com.headstartech.sermo.DialogEventResult;
import com.headstartech.sermo.SermoDialogExecutor;
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
public class SermoController {

    @Autowired
    private SermoDialogExecutor<States, SubscriberEvent> sermoDialogExecutor;

    @Autowired
    private List<ShortCode> shortCodes;

    @GetMapping("/")
    public String input(Model model) {
        model.addAttribute("shortCodes", shortCodes);
        return "ussdapp";
    }

    @RequestMapping("/")
    public String input(@RequestParam("msisdn") String msisdn, @RequestParam("input") String input , Model model) throws Exception {

        try {
            DialogEventResult dialogEventResult = sermoDialogExecutor.applyEvent(msisdn, new SubscriberEvent(input, msisdn));
            if(dialogEventResult.isDialogComplete()) {
                model.addAttribute("screen", dialogEventResult.getOutput().orElse("(completed)"));
            } else {
                model.addAttribute("screen", dialogEventResult.getOutput().orElse(""));
                model.addAttribute("msisdn", msisdn);
            }
        } catch (Exception e) {
            model.addAttribute("screen","An internal error occured.\nPlease try again later!");
        }

        model.addAttribute("shortCodes", shortCodes);

        return "ussdapp";
    }


}
