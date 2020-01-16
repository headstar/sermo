package com.headstartech.sermo;

import java.util.Map;
import java.util.Optional;

/**
 * @author Per Johansson
 */
public interface USSDSupport {

    Map<Object, Object> getVariables();

    Optional<Object> getItemKey();

}
