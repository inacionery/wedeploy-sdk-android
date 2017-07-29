package com.wedeploy.android.query.filter;

import com.wedeploy.android.query.MapWrapper;

import java.util.Arrays;
import java.util.Map;

/**
 * More regex this filter.
 */
public final class SimilarFilter extends Filter {

	protected SimilarFilter(String field, String query) {
		super(field, "similar", MapWrapper.wrap("query", query));
		this.mapValue = (Map)this.value;
	}

	public SimilarFilter maxDf(int value) {
		mapValue.put("maxDf", value);
		return this;
	}

	public SimilarFilter minDf(int value) {
		mapValue.put("minDf", value);
		return this;
	}

	public SimilarFilter minTf(int value) {
		mapValue.put("minTf", value);
		return this;
	}

	public SimilarFilter stopWords(String... words) {
		mapValue.put("stopWords", Arrays.asList(words));
		return this;
	}

	private final Map mapValue;

}