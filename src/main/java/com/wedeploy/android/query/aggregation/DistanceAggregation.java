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

import com.wedeploy.android.query.filter.Range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that represents a Distance {@link Aggregation}.
 * You can filter for results based on a distance away from a specific location
 */
public class DistanceAggregation extends Aggregation {

	DistanceAggregation(
		String name, String field, Object location, Range... ranges) {

		super(name, field, "geoDistance", new HashMap());

		Map map = (Map) getValue();
		this.ranges = new ArrayList();
		this.ranges.addAll(Arrays.asList(ranges));

		map.put("location", location);
		map.put("ranges", this.ranges);
	}

	/**
	 * Adds a range to current aggregation.
	 * @param from The initial value of {@link Range}
	 * @param to The final value of {@link Range}
	 * @return {@link DistanceAggregation}
	 */
	public DistanceAggregation range(Object from, Object to) {
		return range(Range.range(from, to));
	}

	/**
	 * Adds a range to current aggregation.
	 * @param range Instance of {@link Range}
	 * @return {@link DistanceAggregation}
	 */
	public DistanceAggregation range(Range range) {
		this.ranges.add(range);
		return this;
	}

	/**
	 * Sets current aggregation's unit.
	 * @param unit The aggregation unit to be set
	 * <p>
	 * Supported distance units:</br>
	 * mi: miles</br>
	 * yd: yards</br>
	 * ft: feet</br>
	 * in: inch</br>
	 * km: kilometers</br>
	 * m: meters</br>
	 * cm: centimeters</br>
	 * mm: millimeters</br>
	 * nmi: nautical miles</br>
	 *
	 * @see <a href="https://wedeploy.com/docs/data/filtering-data/#distance">
	 *     https://wedeploy.com/docs/data/filtering-data/#distance</a>
	 * @return {@link DistanceAggregation}
	 */
	public DistanceAggregation unit(String unit) {
		((Map) getValue()).put("unit", unit);
		return this;
	}

	private final List<Object> ranges;

}
