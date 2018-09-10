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

/**
 * Available Intervals
 * <li>{@link #YEAR}</li>
 * <li>{@link #QUARTER}</li>
 * <li>{@link #MONTH}</li>
 * <li>{@link #WEEK}</li>
 * <li>{@link #DAY}</li>
 * <li>{@link #HOUR}</li>
 * <li>{@link #MINUTE}</li>
 * <li>{@link #SECOND}</li>
 *
 * @author Victor Oliveira
 */
public enum Interval {

	/**
	 * Represents the interval of one year.
	 */
	YEAR,

	/**
	 * Represents the interval of one quarter.
	 */
	QUARTER,

	/**
	 * Represents the interval of one month.
	 */
	MONTH,

	/**
	 * Represents the interval of one week.
	 */
	WEEK,

	/**
	 * Represents the interval of one day.
	 */
	DAY,

	/**
	 * Represents the interval of one hour.
	 */
	HOUR,

	/**
	 * Represents the interval of one minute.
	 */
	MINUTE,

	/**
	 * Represents the interval of one second.
	 */
	SECOND;

	public String getRawValue() {
		return this.toString().toLowerCase();
	}
}
