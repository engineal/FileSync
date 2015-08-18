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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDateTime;
import java.util.List;
import org.kohsuke.github.GHAsset;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;

/**
 *
 * @author Aaron Lucia
 * @version Aug 16, 2015
 */
public class Update {

    private static final String GITHUB_USER = "engineal";
    private static final String GITHUB_REPO = "FileSync";

    private static GHRelease getLatestRelease() throws IOException {
        GHRepository repo = GitHub.connectAnonymously().getUser(GITHUB_USER).getRepository(GITHUB_REPO);
        PagedIterable<GHRelease> releases = repo.listReleases();

        GHRelease latest = releases.asList().get(0);
        Version latestVersion = Version.parseVersion(latest.getTagName());
        for (GHRelease release : releases) {
            Version releaseVersion = Version.parseVersion(release.getTagName());
            if (releaseVersion.compareTo(latestVersion) > 0) {
                latest = release;
                latestVersion = releaseVersion;
            }
        }

        return latest;
    }

    private final GHRelease latestRelease;
    private final File updateLocation;

    public Update(File updateLocation) throws IOException {
        latestRelease = getLatestRelease();
        this.updateLocation = updateLocation;
    }

    public boolean isUpdate(Version version) throws IOException {
        return Version.parseVersion(latestRelease.getTagName()).compareTo(version) > 0;
    }

    public File update() throws IOException {
        File jarLocation = null;

        List<GHAsset> assets = latestRelease.getAssets();
        for (GHAsset asset : assets) {
            File fl = new File(updateLocation, asset.getName());
            if (fl.getName().equals("FileSync.jar")) {
                jarLocation = fl;
            }
            
            URL website = new URL(asset.getBrowserDownloadUrl());
            try (ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                    FileOutputStream fos = new FileOutputStream(fl)) {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }
        }

        return jarLocation;
    }
}
