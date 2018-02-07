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

import com.couchbase.client.core.CoreContext;
import com.couchbase.client.core.ResponseEvent;
import com.couchbase.client.core.endpoint.Endpoint;
import com.couchbase.client.core.endpoint.query.QueryEndpoint;
import com.couchbase.client.core.env.CoreEnvironment;
import com.couchbase.client.core.service.strategies.RandomSelectionStrategy;
import com.couchbase.client.core.service.strategies.RoundRobinSelectionStrategy;
import com.couchbase.client.core.service.strategies.SelectionStrategy;
import com.lmax.disruptor.RingBuffer;

/**
 * The {@link QueryService} is composed of and manages {@link com.couchbase.client.core.endpoint.query.QueryEndpoint}s.
 *
 * @author Michael Nitschinger
 * @since 1.0
 */
public class QueryService extends PooledService {

    /**
     * The endpoint selection strategy.
     */
    private static final SelectionStrategy STRATEGY = new RoundRobinSelectionStrategy();

    /**
     * The endpoint factory.
     */
    private static final EndpointFactory FACTORY = new QueryEndpointFactory();

    /**
     * Creates a new {@link QueryService}.
     *
     * @param hostname the hostname of the service.
     * @param bucket the name of the bucket.
     * @param password the password of the bucket.
     * @param port the port of the service.
     * @param ctx the shared context.
     */
    @Deprecated
    public QueryService(final String hostname, final String bucket, final String password, final int port,
                        final CoreContext ctx) {
        this(hostname, bucket, bucket, password, port, ctx);
    }

    /**
     * Creates a new {@link QueryService}.
     *
     * @param hostname the hostname of the service.
     * @param bucket the name of the bucket.
     * @param username the user authorized for bucket access.
     * @param password the password of the user.
     * @param port the port of the service.
     * @param ctx the shared context.
     */
    public QueryService(final String hostname, final String bucket, final String username, final String password, final int port,
        final CoreContext ctx) {
        super(hostname, bucket, username, password, port, ctx, ctx.environment().queryServiceConfig(), FACTORY, STRATEGY);
    }


    @Override
    public ServiceType type() {
        return ServiceType.QUERY;
    }

    /**
     * The factory for {@link com.couchbase.client.core.endpoint.query.QueryEndpoint}s.
     */
    static class QueryEndpointFactory implements EndpointFactory {
        @Override
        public Endpoint create(final String hostname, final String bucket, final String username, final String password, final int port,
            final CoreContext ctx) {
            return new QueryEndpoint(hostname, bucket, username, password, port, ctx.environment(), ctx.responseRingBuffer());
        }
    }
}
