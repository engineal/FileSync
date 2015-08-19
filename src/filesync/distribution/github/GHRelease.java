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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Aaron Lucia
 * @version Aug 18, 2015
 */
public class GHRelease {

    public static List<GHRelease> getReleases() throws IOException {
        URL website = new URL("https://api.github.com/repos/engineal/FileSync/releases");
        Type listType = new TypeToken<ArrayList<GHRelease>>() {}.getType();
        try (Reader rbc = new InputStreamReader(website.openStream())) {
            return new Gson().fromJson(rbc, listType);
        }
    }

    private final URL html_url;
    private final String assets_url;
    private final String upload_url;
    private final String tag_name;
    private final String target_commitish;
    private final String name;
    private final String body;
    private final boolean draft;
    private final boolean prerelease;
    private final Date published_at;
    private final String tarball_url;
    private final String zipball_url;
    private final List<GHAsset> assets;

    public GHRelease(URL html_url, String assets_url, String upload_url,
            String tag_name, String target_commitish, String name, String body,
            boolean draft, boolean prerelease, Date published_at, String tarball_url,
            String zipball_url, List<GHAsset> assets) {
        this.html_url = html_url;
        this.assets_url = assets_url;
        this.upload_url = upload_url;
        this.tag_name = tag_name;
        this.target_commitish = target_commitish;
        this.name = name;
        this.body = body;
        this.draft = draft;
        this.prerelease = prerelease;
        this.published_at = published_at;
        this.tarball_url = tarball_url;
        this.zipball_url = zipball_url;
        this.assets = assets;
    }

    public String getAssetsUrl() {
        return assets_url;
    }

    public String getBody() {
        return body;
    }

    public boolean isDraft() {
        return draft;
    }

    public URL getHtmlUrl() {
        return html_url;
    }

    public String getName() {
        return name;
    }

    public boolean isPrerelease() {
        return prerelease;
    }

    public Date getPublished_at() {
        return published_at;
    }

    public String getTagName() {
        return tag_name;
    }

    public String getTargetCommitish() {
        return target_commitish;
    }

    public String getUploadUrl() {
        return upload_url;
    }

    public String getZipballUrl() {
        return zipball_url;
    }

    public String getTarballUrl() {
        return tarball_url;
    }

    public List<GHAsset> getAssets() throws IOException {
        return assets;
    }
}
