/*
 * Copyright (c) 2013 Rising Oak LLC.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import java.io.IOException;

public class Build extends BaseModel {
    int number;
    String url;

    public Build() {
    }

    public Build(Build from) {
        this(from.getNumber(), from.getUrl());
    }

    public Build(int number, String url) {
        this.number = number;
        this.url = url;
    }

    public int getNumber() {
        return number;
    }

    public String getUrl() {
        return url;
    }

	public String getLogText() throws IOException
	{
		return client.get(url + "logText/progressiveText");
	}

    public BuildWithDetails details() throws IOException {
        return client.get(url, BuildWithDetails.class);
    }

	@Override
	public String toString()
	{
		return "#"
				+ number
				+ " (@ "
				+ url.replaceAll("https?://", "").
				replaceAll("/[0-9]+/?$}", "")
				+ ")";
	}

}
