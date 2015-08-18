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

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Aaron Lucia
 * @version Aug 17, 2015
 */
public final class Version implements Comparable<Version> {

    private static final Pattern SemanticVersionRegex
            = Pattern.compile("^v(?<major>\\d+)\\.(?<minor>\\d+)\\.(?<patch>\\d+)(-(?<prerelease>[A-Za-z0-9\\-\\.]+))?(\\+(?<build>[A-Za-z0-9\\-\\.]+))?$");
    private static final Pattern AllDigitsRegex = Pattern.compile("^[0-9]+$");

    public static Version parseVersion(String version) {
        Matcher match = SemanticVersionRegex.matcher(version);
        if (match.matches()) {
            int major = Integer.parseInt(match.group("major"));
            int minor = Integer.parseInt(match.group("minor"));
            int patch = Integer.parseInt(match.group("patch"));
            String prerelease = match.group("prerelease");
            String build = match.group("build");

            return new Version(major, minor, patch, prerelease, build);
        }

        throw new IllegalArgumentException(version + " is not a valid version");
    }

    private static int comparePrerelease(String identifier1, String identifier2) {
        boolean hasIdentifier1 = identifier1 != null && !identifier1.isEmpty();
        boolean hasIdentifier2 = identifier2 != null && !identifier2.isEmpty();
        if (hasIdentifier1 && !hasIdentifier2) {
            return -1;
        } else if (!hasIdentifier1 && hasIdentifier2) {
            return 1;
        } else if (hasIdentifier1 && hasIdentifier2) {
            String[] parts1 = identifier1.split("\\.");
            String[] parts2 = identifier2.split("\\.");
            int max = Math.max(parts1.length, parts2.length);
            for (int i = 0; i < max; i++) {
                if (i == parts1.length && i != parts2.length) {
                    return -1;
                }

                if (i != parts1.length && 1 == parts2.length) {
                    return 1;
                }

                String part1 = parts1[i];
                String part2 = parts2[i];
                if (AllDigitsRegex.matcher(part1).matches()
                        && AllDigitsRegex.matcher(part2).matches()) {
                    Integer value1 = Integer.parseInt(part1);
                    Integer value2 = Integer.parseInt(part2);
                    int result = value1.compareTo(value2);
                    if (result != 0) {
                        return result;
                    }
                } else {
                    int result = part1.compareTo(part2);
                    if (result != 0) {
                        return result;
                    }
                }
            }
        }

        return 0;
    }

    private final int major;
    private final int minor;
    private final int patch;
    private final String prerelease;
    private final String build;

    public Version(int major, int minor, int patch) {
        this(major, minor, patch, null);
    }

    public Version(int major, int minor, int patch, String prerelease) {
        this(major, minor, patch, prerelease, null);
    }

    public Version(int major, int minor, int patch, String prerelease, String build) {
        if (major < 0 || minor < 0 || patch < 0) {
            throw new IllegalArgumentException("Version values must be greator than 0!");
        }

        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.prerelease = prerelease;
        this.build = build;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    public String getPrerelease() {
        return prerelease;
    }

    public String getBuild() {
        return build;
    }

    @Override
    public String toString() {
        return "v" + major + "." + minor + "." + patch + "-" + prerelease + "+" + build;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Version) {
            Version o = (Version) obj;
            return major == o.major
                    && minor == o.minor
                    && patch == o.patch
                    && ((prerelease == null && o.prerelease == null)
                    || ((prerelease != null && o.prerelease != null)
                    && prerelease.equals(o.prerelease)));
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + this.major;
        hash = 67 * hash + this.minor;
        hash = 67 * hash + this.patch;
        hash = 67 * hash + Objects.hashCode(this.prerelease);
        return hash;
    }

    @Override
    public int compareTo(Version o) {
        int result = new Integer(major).compareTo(o.major);
        if (result == 0) {
            result = new Integer(minor).compareTo(o.minor);
            if (result == 0) {
                result = new Integer(patch).compareTo(o.patch);
                if (result == 0) {
                    result = comparePrerelease(prerelease, o.prerelease);
                }
            }
        }

        return result;
    }
}
