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
package com.couchbase.client.core.event.metrics;

import com.couchbase.client.core.event.CouchbaseEvent;
import com.couchbase.client.core.event.EventType;
import com.couchbase.client.core.utils.Events;

import java.util.Map;

/**
 * This event contains collected system stats, like GC, Thread and Memory usage.
 *
 * @author Michael Nitschinger
 * @since 1.2.0
 */
public class RuntimeMetricsEvent implements CouchbaseEvent {

    private final Map<String, Object> info;

    public RuntimeMetricsEvent(Map<String, Object> info) {
        this.info = info;
    }

    public Map<String, Object> all() {
        return info;
    }

    @Override
    public EventType type() {
        return EventType.METRIC;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RuntimeMetricsEvent");
        sb.append(info);
        return sb.toString();
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> result = Events.identityMap(this);
        result.putAll(all());
        return result;
    }

}
