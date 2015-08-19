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
package filesync.io.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import filesync.SyncDirectory;
import filesync.SyncFile;
import filesync.SyncIndex;
import java.io.Reader;

/**
 *
 * @author Aaron Lucia
 * @version Aug 18, 2015
 */
public class IndexBuilder {

    private final Gson gson;

    public IndexBuilder() {
        GsonBuilder gsonBuild = new GsonBuilder();
        gsonBuild.registerTypeAdapter(SyncIndex.class, new SyncIndexSerializer());
        gsonBuild.registerTypeAdapter(SyncDirectory.class, new SyncDirectorySerializer());
        gsonBuild.registerTypeAdapter(SyncFile.class, new SyncFileSerializer());
        gson = gsonBuild.create();
    }

    public <T> T fromJson(JsonElement json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public <T> T fromJson(Reader json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public String toJson(Object src) {
        return gson.toJson(src);
    }

    public JsonElement toJsonTree(Object src) {
        return gson.toJsonTree(src);
    }
}
