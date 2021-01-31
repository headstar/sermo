package demo.web;

import com.headstartech.sermo.DialogExecutor;
import com.headstartech.sermo.persist.CachePersist;
import com.headstartech.sermo.statemachine.factory.ChoiceOption;
import com.headstartech.sermo.statemachine.factory.DialogStateMachineFactoryBuilder;
import com.headstartech.sermo.statemachine.guards.RegExpTransitionGuard;
import com.headstartech.sermo.states.USSDState;
import com.headstartech.sermo.states.USSDStates;
import com.headstartech.sermo.support.DefaultDialogExecutor;
import com.headstartech.sermo.support.MDCDialogListener;
import demo.web.dialog.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}