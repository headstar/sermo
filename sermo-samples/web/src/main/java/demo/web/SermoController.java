package demo.web;

import com.headstartech.sermo.DialogEventResult;
import com.headstartech.sermo.SubscriberEvent;
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

        DialogEventResult dialogEventResult = sermoDialogExecutor.applyEvent(msisdn, new SubscriberEvent(input, msisdn));

        DialogEventResult.DialogState dialogState = dialogEventResult.getDialogState();
        if(DialogEventResult.DialogState.COMPLETE.equals(dialogState) || DialogEventResult.DialogState.ERROR.equals(dialogState)) {
            if(DialogEventResult.DialogState.COMPLETE.equals(dialogState)) {
                model.addAttribute("screen", dialogEventResult.getOutput().orElse("(completed)"));
            } else {
                model.addAttribute("screen", dialogEventResult.getOutput().orElse("(internal error)"));
            }
            model.addAttribute("msisdn", "");
        } else {
            model.addAttribute("screen", dialogEventResult.getOutput().orElse(""));
            model.addAttribute("msisdn",  msisdn);
        }

        model.addAttribute("shortCodes", shortCodes);

        return "ussdapp";
    }


}
