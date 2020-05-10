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

package com.headstartech.sermo.persist;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.cache.Cache;
import org.springframework.statemachine.StateMachineContext;

/**
 * @author Per Johansson
 */
public class CachePersistTest {

    @Test
    public void canWrite() {
        // given
        Cache cache = Mockito.mock(Cache.class);

        StateMachineContext<String, String> stateMachineContext = getStateMachineContext();
        String contextObj = "foo";

        CachePersist<String, String> cachePersist = new CachePersist<>(cache);


        // when
        cachePersist.write(stateMachineContext, contextObj);

        // then
        Mockito.verify(cache).put(contextObj, stateMachineContext);
        Mockito.verifyNoMoreInteractions(cache);

    }

    @Test
    public void canRead() {
        // given
        Cache cache = Mockito.mock(Cache.class);

        String contextObj = "foo";
        StateMachineContext<String, String> expected = getStateMachineContext();
        Mockito.when(cache.get(contextObj, StateMachineContext.class)).thenReturn(expected);


        CachePersist<String, String> cachePersist = new CachePersist<>(cache);


        // when
        StateMachineContext<String, String> actual = cachePersist.read(contextObj);

        // then
        Assertions.assertSame(expected, actual);
        Mockito.verify(cache).get(contextObj, StateMachineContext.class);
        Mockito.verifyNoMoreInteractions(cache);

    }

    @Test
    public void canDelete() {
        // given
        Cache cache = Mockito.mock(Cache.class);

        String contextObj = "foo";


        CachePersist<String, String> cachePersist = new CachePersist<>(cache);


        // when
        cachePersist.delete(contextObj);

        // then
        Mockito.verify(cache).evictIfPresent(contextObj);
        Mockito.verifyNoMoreInteractions(cache);

    }

    @SuppressWarnings("unchecked")
    private StateMachineContext<String, String> getStateMachineContext() {
      return Mockito.mock(StateMachineContext.class);
    }
}
