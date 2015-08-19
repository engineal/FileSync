/*
 * Copyright (C) 2015 Aaron Lucia
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package filesync.distribution.github;

import java.net.URL;

/**
 *
 * @author Aaron Lucia
 * @version Aug 18, 2015
 */
public class GHAsset {
    
    private final String name;
    private final String label;
    private final String state;
    private final String content_type;
    private final long size;
    private final long download_count;
    private final String browser_download_url;
    private final URL html_url;

    public GHAsset(String name, String label, String state, String content_type,
            long size, long download_count, String browser_download_url, URL html_url) {
        this.name = name;
        this.label = label;
        this.state = state;
        this.content_type = content_type;
        this.size = size;
        this.download_count = download_count;
        this.browser_download_url = browser_download_url;
        this.html_url = html_url;
    }

    public String getContentType() {
        return content_type;
    }

    public long getDownloadCount() {
        return download_count;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public String getState() {
        return state;
    }

    public URL getHtmlUrl() {
        return html_url;
    }

    public String getBrowserDownloadUrl() {
        return browser_download_url;
    }
}
