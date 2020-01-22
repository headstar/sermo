package com.headstartech.sermo;

import java.util.Optional;

/**
 * @author Per Johansson
 */
public interface USSDAction {

    void execute(USSDSupport ussdSupport, Optional<Object> event);

}
