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

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Aaron Lucia
 */
public class VersionTest {

    public VersionTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void constructorInitializesBaseVersionNumbers() {
        Version version = Version.parseVersion("v1.2.3");
        assertThat(version.getMajor(), is(1));
        assertThat(version.getMinor(), is(2));
        assertThat(version.getPatch(), is(3));
        assertThat(version.getPrerelease(), nullValue());
        assertThat(version.getBuild(), nullValue());
    }

    @Test
    public void constructorParsesFullVersionNumber() {
        Version version = Version.parseVersion("v1.2.3-alpha.1+build.123");
        assertThat(version.getMajor(), is(1));
        assertThat(version.getMinor(), is(2));
        assertThat(version.getPatch(), is(3));
        assertThat(version.getPrerelease(), is("alpha.1"));
        assertThat(version.getBuild(), is("build.123"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsAnExceptionIfVersionIsInvalid() {
        Version.parseVersion("v1.abc.3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void contractFailsIfMajorVersionIsLessThanZero() {
        new Version(-1, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void contractFailsIfMinorVersionIsLessThanZero() {
        new Version(0, -1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void contractFailsIfPatchVersionIsLessThanZero() {
        new Version(0, 0, -1);
    }

    @Test
    public void compareToComparesTwoSemanticVersionObjects() {
        Version version1 = new Version(1, 0, 0);
        Version version2 = new Version(1, 0, 0);
        assertThat(version1, comparesEqualTo(version2));
    }

    @Test
    public void majorVersionIsLessThanOther() {
        Version version1 = new Version(1, 2, 3);
        Version version2 = new Version(2, 0, 0);
        assertThat(version1, lessThan(version2));
    }

    @Test
    public void minorVersionIsGreaterThanOther() {
        Version version1 = new Version(1, 2, 0);
        Version version2 = new Version(1, 1, 0);
        assertThat(version1, greaterThan(version2));
    }

    @Test
    public void patchVersionIsLessThanOther() {
        Version version1 = new Version(1, 1, 3);
        Version version2 = new Version(1, 1, 4);
        assertThat(version1, lessThan(version2));
    }

    @Test
    public void releaseVersionIsGreaterThanPrereleaseVersion() {
        Version version1 = Version.parseVersion("v1.0.0-alpha");
        Version version2 = new Version(1, 0, 0);
        assertThat(version1, lessThan(version2));
        assertThat(version2, greaterThan(version1));
    }

    @Test
    public void versionIsNotEqualToNull() {
        Version version = new Version(1, 0, 0);
        assertThat(version, not(equalTo(null)));
    }

    @Test
    public void versionIsTheSameAsItself() {
        Version version = new Version(1, 0, 0);
        assertThat(version, comparesEqualTo(version));
        assertThat(version, equalTo(version));
    }

    @Test
    public void versionsAreComparedCorrectly() {
        Version version1 = Version.parseVersion("v1.0.0-alpha");
        Version version2 = Version.parseVersion("v1.0.0-alpha.1");
        Version version3 = Version.parseVersion("v1.0.0-beta.2");
        Version version4 = Version.parseVersion("v1.0.0-beta.11");
        Version version5 = Version.parseVersion("v1.0.0-rc.1");
        Version version6 = Version.parseVersion("v1.0.0-rc.1+build.1");
        Version version7 = Version.parseVersion("v1.0.0");
        Version version8 = Version.parseVersion("v1.0.0+0.3.7");
        Version version9 = Version.parseVersion("v1.3.7+build");
        Version version10 = Version.parseVersion("v1.3.7+build.2.b8f12d7");
        Version version11 = Version.parseVersion("v1.3.7+build.11.e0f985a");
        Version version12 = Version.parseVersion("v1.0.0-beta");
        Version version13 = Version.parseVersion("v1.0.0+0.3");
        assertThat(version1, lessThan(version2));
        assertThat(version2, lessThan(version3));
        assertThat(version3, lessThan(version4));
        assertThat(version4, lessThan(version5));
        assertThat(version5, comparesEqualTo(version6));
        assertThat(version6, lessThan(version7));
        assertThat(version7, comparesEqualTo(version8));
        assertThat(version8, lessThan(version9));
        assertThat(version9, comparesEqualTo(version10));
        assertThat(version10, comparesEqualTo(version11));
        assertThat(version4, greaterThan(version12));
        assertThat(version8, comparesEqualTo(version13));
    }

    @Test
    public void versionsAreNotEqual() {
        Version version1 = Version.parseVersion("v1.0.0");
        Version version2 = Version.parseVersion("v1.0.0-alpha+build.1");
        assertThat(version1, not(comparesEqualTo(version2)));
        assertThat(version1, not(equalTo(version2)));
    }

    @Test(expected = NullPointerException.class)
    public void VersionCannotBeComparedToNull() {
        Version version1 = new Version(1, 0, 0);
        Version version2 = null;
        version1.compareTo(version2);
    }
}
