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

package com.headstartech.sermo.statemachine;

import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineException;
import org.springframework.statemachine.config.ObjectStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfig;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.builders.*;
import org.springframework.statemachine.config.common.annotation.AnnotationBuilder;
import org.springframework.statemachine.config.common.annotation.ObjectPostProcessor;
import org.springframework.statemachine.config.model.ConfigurationData;
import org.springframework.statemachine.config.model.DefaultStateMachineModel;
import org.springframework.statemachine.config.model.StatesData;
import org.springframework.statemachine.config.model.TransitionsData;

/**
 * {@code StateMachineBuilder} provides a builder pattern for
 * {@link StateMachine} using a similar concepts found from a
 * normal annotation based configuration.
 *
 * @author Janne Valkealahti
 *
 */
public class StateMachineFactoryBuilder {

    /**
     * Gets a builder for a {@link StateMachine}.
     *
     * @param <S> the type of state
     * @param <E> the type of event
     * @return the builder
     */
    public static <S, E> Builder<S, E> builder() {
        return new StateMachineFactoryBuilder.Builder<>();
    }

    /**
     * {@code Builder} implementation handling logic of building
     * a {@link StateMachine} manually.
     *
     * @param <S> the type of state
     * @param <E> the type of event
     */
    public static class Builder<S, E> {

        private StateMachineConfigBuilder<S, E> builder;
        private BuilderStateMachineConfigurerAdapter<S, E> adapter;

        /**
         * Instantiates a new builder.
         */
        public Builder() {
            adapter = new BuilderStateMachineConfigurerAdapter<>();
            builder = new StateMachineConfigBuilder<>();
        }

        /**
         * Configure model.
         *
         * @return the state machine model configurer
         */
        public StateMachineModelConfigurer<S, E> configureModel() {
            return adapter.modelBuilder;
        }

        /**
         * Configure configuration.
         *
         * @return the state machine configuration configurer
         */
        public StateMachineConfigurationConfigurer<S, E> configureConfiguration() {
            return adapter.configurationBuilder;
        }

        /**
         * Configure states.
         *
         * @return the state machine state configurer
         */
        public StateMachineStateConfigurer<S, E> configureStates() {
            return adapter.stateBuilder;
        }

        /**
         * Configure transitions.
         *
         * @return the state machine transition configurer
         */
        public StateMachineTransitionConfigurer<S, E> configureTransitions() {
            return adapter.transitionBuilder;
        }

        /**
         * Builds a {@link StateMachine}.
         *
         * @return the state machine
         */
        public StateMachineFactory<S, E> build() {
            try {
                builder.apply(adapter);
                StateMachineConfig<S, E> stateMachineConfig = builder.getOrBuild();

                TransitionsData<S, E> stateMachineTransitions = stateMachineConfig.getTransitions();
                StatesData<S, E> stateMachineStates = stateMachineConfig.getStates();
                ConfigurationData<S, E> stateMachineConfigurationConfig = stateMachineConfig.getStateMachineConfigurationConfig();

                ObjectStateMachineFactory<S, E> stateMachineFactory;
                if (stateMachineConfig.getModel() != null && stateMachineConfig.getModel().getFactory() != null) {
                    stateMachineFactory = new ObjectStateMachineFactory<>(
                            new DefaultStateMachineModel<>(stateMachineConfigurationConfig, null, null),
                            stateMachineConfig.getModel().getFactory());
                } else {
                    stateMachineFactory = new ObjectStateMachineFactory<>(new DefaultStateMachineModel<>(
                            stateMachineConfigurationConfig, stateMachineStates, stateMachineTransitions), null);
                }

                stateMachineFactory.setHandleAutostartup(stateMachineConfigurationConfig.isAutoStart());

                if (stateMachineConfigurationConfig.getBeanFactory() != null) {
                    stateMachineFactory.setBeanFactory(stateMachineConfigurationConfig.getBeanFactory());
                }
                if (stateMachineConfigurationConfig.getTaskExecutor() != null) {
                    stateMachineFactory.setTaskExecutor(stateMachineConfigurationConfig.getTaskExecutor());
                } else {
                    stateMachineFactory.setTaskExecutor(new SyncTaskExecutor());
                }
                if (stateMachineConfigurationConfig.getTaskScheduler() != null) {
                    stateMachineFactory.setTaskScheduler(stateMachineConfigurationConfig.getTaskScheduler());
                } else {
                    stateMachineFactory.setTaskScheduler(new ConcurrentTaskScheduler());
                }
                return stateMachineFactory;
            } catch (Exception e) {
                throw new StateMachineException("Error building state machine", e);
            }
        }

    }

    private static class BuilderStateMachineConfigurerAdapter<S extends Object, E extends Object>
            implements StateMachineConfigurer<S, E> {

        private StateMachineModelBuilder<S, E> modelBuilder;
        private StateMachineTransitionBuilder<S, E> transitionBuilder;
        private StateMachineStateBuilder<S, E> stateBuilder;
        private StateMachineConfigurationBuilder<S, E> configurationBuilder;

        BuilderStateMachineConfigurerAdapter() {
            try {
                getStateMachineModelBuilder();
                getStateMachineTransitionBuilder();
                getStateMachineStateBuilder();
                getStateMachineConfigurationBuilder();
            } catch (Exception e) {
                throw new StateMachineException("Error instantiating builder adapter", e);
            }
        }

        @Override
        public void init(StateMachineConfigBuilder<S, E> config) throws Exception {
            config.setSharedObject(StateMachineModelBuilder.class, getStateMachineModelBuilder());
            config.setSharedObject(StateMachineTransitionBuilder.class, getStateMachineTransitionBuilder());
            config.setSharedObject(StateMachineStateBuilder.class, getStateMachineStateBuilder());
            config.setSharedObject(StateMachineConfigurationBuilder.class, getStateMachineConfigurationBuilder());
        }

        @Override
        public void configure(StateMachineConfigBuilder<S, E> builder) {
        }

        @Override
        public boolean isAssignable(AnnotationBuilder<StateMachineConfig<S, E>> builder) {
            return false;
        }

        @Override
        public void configure(StateMachineModelConfigurer<S, E> model) {
        }

        @Override
        public void configure(StateMachineConfigurationConfigurer<S, E> config) {
        }

        @Override
        public void configure(StateMachineStateConfigurer<S, E> states) {
        }

        @Override
        public void configure(StateMachineTransitionConfigurer<S, E> transitions) {
        }

        protected final StateMachineModelBuilder<S, E> getStateMachineModelBuilder() throws Exception {
            if (modelBuilder != null) {
                return modelBuilder;
            }
            modelBuilder = new StateMachineModelBuilder<>(ObjectPostProcessor.QUIESCENT_POSTPROCESSOR, true);
            configure(modelBuilder);
            return modelBuilder;
        }

        protected final StateMachineTransitionBuilder<S, E> getStateMachineTransitionBuilder() {
            if (transitionBuilder != null) {
                return transitionBuilder;
            }
            transitionBuilder = new StateMachineTransitionBuilder<>(ObjectPostProcessor.QUIESCENT_POSTPROCESSOR, true);
            return transitionBuilder;
        }

        protected final StateMachineStateBuilder<S, E> getStateMachineStateBuilder() {
            if (stateBuilder != null) {
                return stateBuilder;
            }
            stateBuilder = new StateMachineStateBuilder<>(ObjectPostProcessor.QUIESCENT_POSTPROCESSOR, true);
            return stateBuilder;
        }

        protected final StateMachineConfigurationBuilder<S, E> getStateMachineConfigurationBuilder() {
            if (configurationBuilder != null) {
                return configurationBuilder;
            }
            configurationBuilder = new StateMachineConfigurationBuilder<>(ObjectPostProcessor.QUIESCENT_POSTPROCESSOR, true);
            return configurationBuilder;
        }
    }
}

