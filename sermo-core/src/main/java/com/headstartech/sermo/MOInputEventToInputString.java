package com.headstartech.sermo;

import com.headstartech.sermo.MOInput;

import java.util.function.Function;

/**
 * @author Per Johansson
 */
public class MOInputEventToInputString implements Function<MOInput, String> {

    @Override
    public String apply(MOInput o) {
        return o.getInput();
    }

}
