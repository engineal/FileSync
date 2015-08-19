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
package filesync.distribution;

import filesync.distribution.github.GHAsset;
import filesync.distribution.github.GHRelease;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

/**
 *
 * @author Aaron Lucia
 * @version Aug 16, 2015
 */
public class Update {

    private static GHRelease getLatestRelease(boolean prerelease) throws IOException {
        List<GHRelease> releases = GHRelease.getReleases();

        GHRelease latest = null;
        Version latestVersion = Version.parseVersion("v0.0.0");
        for (GHRelease release : releases) {
            Version releaseVersion = Version.parseVersion(release.getTagName());
            if (release.isPrerelease() == prerelease
                    && !release.isDraft()
                    && releaseVersion.compareTo(latestVersion) > 0) {
                latest = release;
                latestVersion = releaseVersion;
            }
        }

        return latest;
    }

    private GHRelease latestRelease;
    private final File updateLocation;
    private final boolean prerelease;

    public Update(File updateLocation, boolean prerelease) throws IOException {
        latestRelease = getLatestRelease(prerelease);
        this.updateLocation = updateLocation;
        this.prerelease = prerelease;
    }

    public boolean isUpdate(Version version) throws IOException {
        if (latestRelease != null) {
            return Version.parseVersion(latestRelease.getTagName()).compareTo(version) < 0;
        }
        return false;
    }

    public File update() throws IOException {
        if (latestRelease == null) {
            latestRelease = getLatestRelease(prerelease);
            if (latestRelease == null) {
                latestRelease = getLatestRelease(true);
            }
        }

        if (latestRelease != null) {
            List<GHAsset> assets = latestRelease.getAssets();
            for (GHAsset asset : assets) {
                if (asset.getName().endsWith(".jar")) {
                    File fl = new File(updateLocation, asset.getName());
                    URL website = new URL(asset.getBrowserDownloadUrl());
                    try (ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                            FileOutputStream fos = new FileOutputStream(fl)) {
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    }
                    return fl;
                }
            }
        }
        
        return null;
    }
}
