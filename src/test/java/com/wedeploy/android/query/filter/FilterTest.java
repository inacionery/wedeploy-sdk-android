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

package com.wedeploy.android.query.filter;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterTest {

	@Test
	public void testFilter_combiningDifferentFilters() throws Exception {
		Filter filter = Filter.field("a", 1)
			.and("a", 1)
			.and("a", ">", 1)
			.or("a", 1)
			.or("a", ">", 1);

		JSONAssert.assertEquals(
			"{\"or\":[" +
				"{\"and\":[" +
				"{\"a\":{\"operator\":\"=\",\"value\":1}}," +
				"{\"a\":{\"operator\":\"=\",\"value\":1}}," +
				"{\"a\":{\"operator\":\">\",\"value\":1}}]}," +
				"{\"a\":{\"operator\":\"=\",\"value\":1}}," +
				"{\"a\":{\"operator\":\">\",\"value\":1}}]}",
			filter.bodyAsJson(), true);
	}

	@Test
	public void testFilter_upgradeToCompositeFilter() throws Exception {
		Filter filter = Filter.field("a", 1).or("a", 1);

		JSONAssert.assertEquals(
			"{\"or\":[" +
				"{\"a\":{\"operator\":\"=\",\"value\":1}}," +
				"{\"a\":{\"operator\":\"=\",\"value\":1}}]}",
			filter.bodyAsJson(), true);

		filter = Filter.field("a", 1).and("a", 1);

		JSONAssert.assertEquals(
			"{\"and\":[" +
				"{\"a\":{\"operator\":\"=\",\"value\":1}}," +
				"{\"a\":{\"operator\":\"=\",\"value\":1}}]}",
			filter.bodyAsJson(), true);
	}

	@Test
	public void testFilter_withAnd() throws Exception {
		List<String> bodies = new ArrayList();
		bodies.add(Filter.field("field", 1).and("field", 1).bodyAsJson());
		bodies.add(Filter.field("field", 1).and("field", "=", 1).bodyAsJson());
		bodies.add(
			Filter.field(
				"field", 1).and(Filter.field("field", 1)).bodyAsJson());
		bodies.add(Filter.field("field", 1).and("field", 1).bodyAsJson());

		for (String body : bodies) {
			JSONAssert.assertEquals(getCompositeFilter("and", 2), body, true);
		}

		String body = Filter.field("field", 1)
			.and("field", 1)
			.and("field", 1)
			.and("field", "=", 1)
			.and(Filter.field("field", 1))
			.bodyAsJson();

		JSONAssert.assertEquals(getCompositeFilter("and", 5), body, true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFilter_withCompositeFilterAsString() {
		Filter.field("field", "and", 1);
	}

	@Test
	public void testFilter_withFuzzyFilter() throws Exception {
		JSONAssert.assertEquals(
			"{\"*\":{\"operator\":\"fuzzy\",\"value\":{\"query\":\"str\"}}}",
			Filter.fuzzy("str").bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"*\":{\"operator\":\"fuzzy\"," +
				"\"value\":{\"query\":\"str\",\"fuzziness\":1}}}",
			Filter.fuzzy("str", 1).bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"fuzzy\",\"value\":{\"query\":\"str\"}}}",
			Filter.fuzzy("f", "str").bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"fuzzy\"," +
				"\"value\":{\"query\":\"str\",\"fuzziness\":1}}}",
			Filter.fuzzy("f", "str", 1).bodyAsJson(), true);
	}

	@Test
	public void testFilter_withGeo() throws Exception {
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"gp\",\"value\":[\"0,0\",\"0,0\"]}}",
			Filter.boundingBox("f", "0,0", "0,0").bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"gp\",\"value\":[\"0,0\",\"0,0\"]}}",
			Filter.boundingBox("f", Geo.boundingBox("0,0", "0,0")).bodyAsJson(),
			true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"gp\",\"value\":[\"0,0\",[0,1],[0,1]]}}",
			Filter.polygon(
				"f", "0,0", Arrays.asList(0d, 1d),
				Geo.point(1, 0)).bodyAsJson(),
			true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"gd\"," +
				"\"value\":{\"location\":\"0,0\",\"max\":\"10m\"}}}",
			Filter.distance("f", "0,0", "10m").bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"gd\"," +
				"\"value\":{\"location\":\"0,0\",\"max\":\"10m\"}}}",
			Filter.distance("f", Geo.circle("0,0", "10m")).bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"gd\",\"value\":" +
				"{\"location\":\"0,0\",\"min\":\"1m\",\"max\":\"10m\"}}}",
			Filter.distance(
				"f", "0,0", Range.range("1m", "10m")).bodyAsJson(), true);

		String body = Filter.shape("f", "0,0")
			.shape(Arrays.asList(0, 0))
			.shape(new int[]{0, 0})
			.shape(Geo.point(0, 0))
			.shape(new int[0])
			.shape(Arrays.asList())
			.shape(Geo.boundingBox("0,0", "0,0"))
			.shape(Geo.line("0,0", "0,0"))
			.shape(Geo.circle("0,0", "1m"))
			.shape(Geo.polygon("0,0", "0,0").hole("0,0", "0,0"))
			.bodyAsJson();

		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"gs\",\"value\":{" +
				"\"type\":\"geometrycollection\",\"geometries\":[" +
				"\"0,0\",[0,0],[0,0],[0,0],[],[]," +
				"{\"type\":\"envelope\",\"coordinates\":[\"0,0\",\"0,0\"]}," +
				"{\"type\":\"linestring\",\"coordinates\":[\"0,0\",\"0,0\"]}," +
				"{\"type\":\"circle\"," +
				"\"coordinates\":\"0,0\",\"radius\":\"1m\"}," +
				"{\"type\":\"polygon\"," +
				"\"coordinates\":[[\"0,0\",\"0,0\"],[\"0,0\",\"0,0\"]]}," +
				"]}}}",
			body, true);
	}

	@Test
	public void testFilter_withMatchFilter() throws Exception {
		JSONAssert.assertEquals(
			"{\"*\":{\"operator\":\"match\",\"value\":\"str\"}}",
			Filter.match("str").bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"match\",\"value\":\"str\"}}",
			Filter.match("f", "str").bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"*\":{\"operator\":\"phrase\",\"value\":\"str\"}}",
			Filter.phrase("str").bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"phrase\",\"value\":\"str\"}}",
			Filter.phrase("f", "str").bodyAsJson(), true);
	}

	@Test
	public void testFilter_withMultiMatchFilter() {
		JSONAssert.assertEquals(
			"{\"field1,field2,field3\":"
				+ "{\"operator\":\"multi_match\",\"value\":\"str\"}}",
			Filter.multiMatch(
				new String[] { "field1", "field2", "field3" }, "str").bodyAsJson(),
			true);
	}

	@Test
	public void testFilter_withPrefixFilter() throws Exception {
		JSONAssert.assertEquals(
			"{\"*\":{\"operator\":\"prefix\",\"value\":\"str\"}}",
			Filter.prefix("str").bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"prefix\",\"value\":\"str\"}}",
			Filter.prefix("f", "str").bodyAsJson(), true);
	}

	@Test
	public void testFilter_withSimilarFilter() throws Exception {
		JSONAssert.assertEquals(
			"{\"*\":{\"operator\":\"similar\",\"value\":{\"query\":\"str\"}}}",
			Filter.similar("str").bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"similar\",\"value\":{\"query\":\"str\"}}}",
			Filter.similar("f", "str").bodyAsJson(), true);

		String body = Filter.similar("str")
			.stopWords("w1", "w2")
			.minTf(1)
			.minDf(2)
			.maxDf(3)
			.bodyAsJson();

		JSONAssert.assertEquals(
			"{\"*\":{\"operator\":\"similar\",\"value\":{" +
				"\"query\":\"str\"," +
				"\"stopWords\":[\"w1\",\"w2\"]," +
				"\"minTf\":1," +
				"\"minDf\":2," +
				"\"maxDf\":3}}}",
			body, true);

		body = Filter.similar("f", "str")
			.stopWords("w1")
			.minTf(1)
			.minDf(2)
			.maxDf(3)
			.bodyAsJson();

		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"similar\",\"value\":{" +
				"\"query\":\"str\"," +
				"\"stopWords\":[\"w1\"]," +
				"\"minTf\":1," +
				"\"minDf\":2," +
				"\"maxDf\":3}}}",
			body, true);
	}

	@Test
	public void testFilter_withSimpleFilters() throws Exception {
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"=\",\"value\":1}}",
			Filter.equal("f", 1).bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"!=\",\"value\":1}}",
			Filter.notEqual("f", 1).bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\">\",\"value\":1}}",
			Filter.gt("f", 1).bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\">=\",\"value\":1}}",
			Filter.gte("f", 1).bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"<\",\"value\":1}}",
			Filter.lt("f", 1).bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"<=\",\"value\":1}}",
			Filter.lte("f", 1).bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"any\",\"value\":[1,2]}}",
			Filter.any("f", 1, 2).bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"any\",\"value\":[1,2]}}",
			Filter.any("f", Arrays.asList(1, 2)).bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"none\",\"value\":[1,2]}}",
			Filter.none("f", 1, 2).bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"none\",\"value\":[1,2]}}",
			Filter.none("f", Arrays.asList(1, 2)).bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"~\",\"value\":\"str\"}}",
			Filter.regex("f", "str").bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"exists\"}}",
			Filter.exists("f").bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"missing\"}}",
			Filter.missing("f").bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"range\",\"value\":{\"from\":1,\"to\":2, \"includeLower\": true, \"includeUpper\": true}}}",
			Filter.range("f", 1, 2).bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"range\",\"value\":{\"to\":1, \"includeLower\": true, \"includeUpper\": true}}}}",
			Filter.range("f", Range.to(1)).bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"f\":{\"operator\":\"range\",\"value\":{\"from\":0,\"to\":1, \"includeLower\": false, \"includeUpper\": true}}}}",
			Filter.range("f", Range.range(0, false, true, 1)).bodyAsJson(), true);
	}

	private String getCompositeFilter(String operator, int count) {
		StringBuilder builder = new StringBuilder();

		builder.append("{\"" + operator + "\":[");

		for (int i = 0; i < count; i++) {
			builder.append("{\"field\":{\"operator\":\"=\",\"value\":1}},");
		}

		builder.setCharAt(builder.length() - 1, ']');
		builder.append("}");

		return builder.toString();
	}

}
