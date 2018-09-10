/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of Liferay, Inc. nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.wedeploy.android.query.aggregation;

import com.wedeploy.android.query.BodyConvertible;
import com.wedeploy.android.query.MapWrapper;
import com.wedeploy.android.query.filter.BucketOrder;
import com.wedeploy.android.query.filter.Range;

import com.wedeploy.android.transport.Request;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.wedeploy.android.util.Validator.isNotNullOrEmpty;

/**
 * Class that represents a search aggregation.
 *
 * @author Victor Oliveira
 */
public class Aggregation extends BodyConvertible {

	Aggregation(String name, String field, String operator) {
		this(name, field, operator, null);
	}

	Aggregation(
		String name, String field, String operator, Object value) {

		this.name = name;
		this.field = field;
		this.operator = operator;
		this.value = value;
	}

	/**
	 * Adds a new aggregation as nested to the current Aggregation instance.
	 * @param aggregation The aggregation to be nested in current aggregation
	 * @return {@link Aggregation}
	 */
	public Aggregation addNestedAggregation(Aggregation... aggregation) {
		if (aggregations == null) {
			aggregations = new LinkedList<>();
		}

		Collections.addAll(aggregations, aggregation);

		return this;
	}

	/**
	 * Creates an {@link Aggregation} instance with the <code>avg</code>
	 * operator.
	 * @param name The aggregation name
	 * @param field The aggregation field
	 * @return {@link Aggregation}
	 */
	public static Aggregation avg(String name, String field) {
		return of(name, field, "avg");
	}

	/**
	 * Creates an {@link Aggregation} instance with the <code>count</code>
	 * operator.
	 * @param name The aggregation name
	 * @param field The aggregation field
	 * @return {@link Aggregation}
	 */
	public static Aggregation count(String name, String field) {
		return of(name, field, "count");
	}

	/**
	 * Creates an {@link Aggregation} instance with the <code>cardinality</code>
	 * operator.
	 * @param name The aggregation name
	 * @param field The aggregation field
	 * @return {@link Aggregation}
	 */
	public static Aggregation cardinality(String name, String field) {
		return of(name, field, "cardinality");
	}

	/**
	 * Creates an {@link DistanceAggregation} instance with the
	 * <code>geoDistance</code> operator.
	 * @param name The aggregation name
	 * @param field The aggregation field
	 * @param location The aggregation location
	 * @param ranges The aggregation ranges
	 * @return {@link DistanceAggregation}
	 */
	public static DistanceAggregation distance(
		String name, String field, Object location, Range... ranges) {

		return new DistanceAggregation(name, field, location, ranges);
	}

	/**
	 * Creates an {@link Aggregation} instance with the
	 * <code>extendedStats</code> operator.
	 * @param name The aggregation name
	 * @param field The aggregation field
	 * @return {@link Aggregation}
	 */
	public static Aggregation extendedStats(String name, String field) {
		return of(name, field, "extendedStats");
	}

	/**
	 * Creates an {@link BucketOrderAggregation} instance with the <code>histogram</code>
	 * operator.
	 * @param name The aggregation name
	 * @param field The aggregation field
	 * @param interval The histogram Interval
	 * @return {@link BucketOrderAggregation}
	 */
	public static BucketOrderAggregation histogram(
		String name, String field, int interval) {

		return new BucketOrderAggregation(name, field, "histogram", interval);
	}

	/**
	 * Creates an {@link BucketOrderAggregation} instance with the <code>histogram</code>
	 * operator.
	 * @param name The aggregation name
	 * @param field The aggregation field
	 * @param interval The histogram Interval
	 * @param bucketOrder The bucket order of this aggregation
	 * @return {@link BucketOrderAggregation}
	 */
	public static BucketOrderAggregation histogram(
		String name, String field, int interval, BucketOrder... bucketOrder) {

		return new BucketOrderAggregation(
			name, field, "histogram", interval, bucketOrder);
	}

	/**
	 * Creates an {@link BucketOrderAggregation} instance with the
	 * <code>date_histogram</code> operator.
	 * Similar to the {@link #histogram(String, String, int)} except it can only
	 * be applied on {@link Interval} dates.
	 * @param name The aggregation name
	 * @param field The aggregation field
	 * @param interval The histogram {@link Interval}
	 * @return {@link BucketOrderAggregation}
	 */
	public static BucketOrderAggregation histogram(
		String name, String field, Interval interval) {

		return new BucketOrderAggregation(name, field, "date_histogram", interval.getRawValue());
	}

	/**
	 * Creates an {@link BucketOrderAggregation} instance with the
	 * <code>date_histogram</code> operator.
	 * Similar to the {@link #histogram(String, String, int)} except it can only
	 * be applied on {@link Interval} dates.
	 * @param name The aggregation name
	 * @param field The aggregation field
	 * @param interval The histogram {@link Interval}
	 * @param bucketOrder The bucket order of this aggregation
	 * @return {@link BucketOrderAggregation}
	 */
	public static BucketOrderAggregation histogram(
		String name, String field, Interval interval, BucketOrder bucketOrder) {

		return new BucketOrderAggregation(
			name, field, "date_histogram", interval.getRawValue(), bucketOrder);
	}

	/**
	 * Creates an {@link BucketOrderAggregation} instance with the
	 * <code>date_histogram</code> operator.
	 * Similar to the {@link #histogram(String, String, int)} except it can
	 * only be applied on {@link TimeUnit} values.
	 * @param name The aggregation name
	 * @param field The aggregation field
	 * @param value The histogram value
	 * @param timeUnit The histogram timeUnit
	 * @return {@link BucketOrderAggregation}
	 */
	public static BucketOrderAggregation histogram(
		String name, String field, int value, TimeUnit timeUnit) {

		return new BucketOrderAggregation(
			name, field, "date_histogram", timeUnit.getRawValue(value));
	}

	/**
	 * Creates an {@link BucketOrderAggregation} instance with the
	 * <code>date_histogram</code> operator.
	 * Similar to the {@link #histogram(String, String, int)} except it can
	 * only be applied on {@link TimeUnit} values.
	 * @param name The aggregation name
	 * @param field The aggregation field
	 * @param value The histogram value
	 * @param timeUnit The histogram timeUnit
	 * @param bucketOrder The bucket order of this aggregation
	 * @return {@link BucketOrderAggregation}
	 */
	public static BucketOrderAggregation histogram(
		String name, String field, int value, TimeUnit timeUnit,
		BucketOrder... bucketOrder) {

		return new BucketOrderAggregation(
			name, field,  "date_histogram", timeUnit.getRawValue(value), bucketOrder);
	}

	/**
	 * Creates an {@link Aggregation} instance with the
	 * <code>max</code> operator.
	 * @param name The aggregation name
	 * @param field The aggregation field
	 * @return {@link Aggregation}
	 */
	public static Aggregation max(String name, String field) {
		return of(name, field, "max");
	}

	/**
	 * Creates an {@link Aggregation} instance with the <code>min</code>
	 * operator.
	 * @param name The aggregation name
	 * @param field The aggregation field
	 * @return {@link Aggregation}
	 */
	public static Aggregation min(String name, String field) {
		return of(name, field, "min");
	}

	/**
	 * Creates an {@link Aggregation} instance with the <code>missing</code>
	 * operator.
	 * @param name The aggregation name
	 * @param field The aggregation field
	 * @return {@link Aggregation}
	 */
	public static Aggregation missing(String name, String field) {
		return of(name, field, "missing");
	}

	/**
	 * Creates an {@link Aggregation} instance with custom operator.
	 * @param name The aggregation name
	 * @param field The aggregation field
	 * @param operator The aggregation operator
	 * @return {@link Aggregation}
	 */
	public static Aggregation of(String name, String field, String operator) {
		return new Aggregation(name, field, operator);
	}

	/**
	 * Creates an {@link RangeAggregation} instance with the <code>range</code>
	 * operator.
	 * @param name The aggregation name
	 * @param field The aggregation field
	 * @return {@link RangeAggregation}
	 */
	public static RangeAggregation range(
		String name, String field, Range... ranges) {

		return new RangeAggregation(name, field, ranges);
	}

	/**
	 * Creates an {@link Aggregation} instance with the <code>stats</code>
	 * operator.
	 * @param name The aggregation name
	 * @param field The aggregation field
	 * @return {@link Aggregation}
	 */
	public static Aggregation stats(String name, String field) {
		return of(name, field, "stats");
	}

	/**
	 * Creates an {@link Aggregation} instance with the <code>sum</code>
	 * operator.
	 * @param name The aggregation name
	 * @param field The aggregation field
	 * @return {@link Aggregation}
	 */
	public static Aggregation sum(String name, String field) {
		return of(name, field, "sum");
	}

	/**
	 * Creates an {@link BucketOrderAggregation} instance with the <code>terms</code>
	 * operator.
	 * @param name The aggregation name
	 * @param field The aggregation field
	 * @return {@link BucketOrderAggregation}
	 */
	public static BucketOrderAggregation terms(String name, String field) {
		return new BucketOrderAggregation(name, field, "terms");
	}

	/**
	 * Creates an {@link BucketOrderAggregation} instance with the <code>terms</code>
	 * operator.
	 * @param name The aggregation name
	 * @param field The aggregation field
	 * @param bucketOrder The bucket order of this aggregation
	 * @return {@link BucketOrderAggregation}
	 */
	public static BucketOrderAggregation terms(
		String name, String field, BucketOrder... bucketOrder) {

		return new BucketOrderAggregation(name, field, "terms", bucketOrder);
	}

	/**
	 * Creates an {@link BucketOrderAggregation} instance with the <code>terms</code>
	 * operator.
	 * {@link BucketOrderAggregation} supports {@link BucketOrder} aggregations
	 * @param name The aggregation name
	 * @param field The aggregation field
	 * @param size Represents how many term buckets should be returned
	 * @param bucketOrder The bucket order of this aggregation
	 * @return {@link BucketOrderAggregation}
	 */
	public static BucketOrderAggregation terms(
		String name, String field, int size, BucketOrder... bucketOrder) {

		return new BucketOrderAggregation(
			name, field, "terms", null, size, bucketOrder);
	}

	/**
	 * Gets a map that represents the {@link Request} body for this
	 * {@link Aggregation}.
	 * @return {@link Map}
	 */
	@Override
	public Map body() {
		Map<String, Object> map = new HashMap<>();

		map.put("name", name);
		map.put("operator", operator);

		if (value != null) {
			map.put("value", value);
		}

		if (isNotNullOrEmpty(aggregations)) {
			map.put("aggregation", getAggregationsBody());
		}

		return MapWrapper.wrap(field, map);
	}

	private List<Object> getAggregationsBody() {
		boolean treeRoot = false;
		Set<Aggregation> parsedAggregations = localParsedAggregations.get();

		if (parsedAggregations == null) {
			treeRoot = true;
			parsedAggregations = new HashSet<>();
			localParsedAggregations.set(parsedAggregations);
		}

		List<Object> bodies = new ArrayList<>(aggregations.size());

		try {
			for (Aggregation aggregation : aggregations) {
				if (!parsedAggregations.add(aggregation)) {
					throw new IllegalStateException("Circular reference detected");
				}

				bodies.add(aggregation.body());
			}
		} finally {
			if (treeRoot) {
				localParsedAggregations.remove();
			}
		}
		return bodies;
	}

	protected Object getValue() {
		return value;
	}

	protected String getField() {
		return field;
	}

	protected String getName() {
		return name;
	}

	private List<Aggregation> aggregations;
	private final Object value;
	private final String field;
	private final String name;
	private final String operator;
	private static final ThreadLocal<Set<Aggregation>>
		localParsedAggregations = new ThreadLocal<>();
}
