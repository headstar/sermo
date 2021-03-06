/*
 *  Copyright 2020 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package demo.web.dialog;

import com.headstartech.sermo.DialogEvent;

import java.util.StringJoiner;

/**
 * @author Per Johansson
 */
public class SubscriberEvent extends DialogEvent {

    private final String msisdn;

    public SubscriberEvent() {
        msisdn = null;
    }

    public SubscriberEvent(String input, String msisdn) {
        super(input);
        this.msisdn = msisdn;
    }

    public String getMsisdn() {
        return msisdn;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SubscriberEvent.class.getSimpleName() + "[", "]")
                .add("msisdn='" + msisdn + "'")
                .add("input='" + input + "'")
                .toString();
    }
}
