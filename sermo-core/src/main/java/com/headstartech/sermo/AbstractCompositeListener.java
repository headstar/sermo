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

package com.headstartech.sermo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Per Johansson
 */
public class AbstractCompositeListener<T> {

    private List<T> listeners;

    public AbstractCompositeListener() {
        listeners = new ArrayList<>();
    }

    public void setListeners(List<? extends T> listeners) {
        this.listeners.clear();
        this.listeners.addAll(listeners);
    }

    public void register(T listener) {
        listeners.add(listener);
    }

    public void unregister(T listener) {
        listeners.remove(listener);
    }

    public List<T> getListeners() {
        return listeners;
    }
}
