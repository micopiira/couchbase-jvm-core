/*
 * Copyright (c) 2016 Couchbase, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.couchbase.client.core.service;

import com.couchbase.client.core.endpoint.Endpoint;
import com.couchbase.client.core.state.AbstractStateZipper;
import com.couchbase.client.core.state.LifecycleState;

import java.util.Collection;

/**
 * Calculates a merged state for all registered endpoints.
 *
 * @author Michael Nitschinger
 * @since 1.1.0
 */
public class EndpointStateZipper extends AbstractStateZipper<Endpoint, LifecycleState> {

    private final LifecycleState initialState;

    public EndpointStateZipper(LifecycleState initial) {
        super(initial);
        this.initialState = initial;
    }

    @Override
    protected LifecycleState zipWith(Collection<LifecycleState> states) {
        if (states.isEmpty()) {
            return initialState;
        }
        int connected = 0;
        int connecting = 0;
        int disconnecting = 0;
        for (LifecycleState endpointState : states) {
            switch (endpointState) {
                case CONNECTED:
                    connected++;
                    break;
                case CONNECTING:
                    connecting++;
                    break;
                case DISCONNECTING:
                    disconnecting++;
                    break;
                default:
                    // ignore
            }
        }
        if (states.size() == connected) {
            return LifecycleState.CONNECTED;
        } else if (connected > 0) {
            return LifecycleState.DEGRADED;
        } else if (connecting > 0) {
            return LifecycleState.CONNECTING;
        } else if (disconnecting > 0) {
            return LifecycleState.DISCONNECTING;
        } else {
            return LifecycleState.DISCONNECTED;
        }
    }
}
