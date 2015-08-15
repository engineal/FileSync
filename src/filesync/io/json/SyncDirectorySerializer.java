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

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import filesync.SyncDirectory;
import filesync.SyncFile;
import filesync.SyncIndex;
import java.lang.reflect.Type;

/**
 *
 * @author Aaron Lucia
 * @version Aug 15, 2015
 */
public class SyncDirectorySerializer implements JsonSerializer<SyncDirectory>, JsonDeserializer<SyncDirectory> {

    @Override
    public JsonElement serialize(SyncDirectory src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("name", src.getName());
        object.addProperty("size", src.getSize());
        object.addProperty("lastModified", src.getLastModified());
        object.addProperty("added", src.isAdded());

        JsonArray array = new JsonArray();
        for (SyncFile file : src) {
            GsonBuilder gson = new GsonBuilder();
            gson.registerTypeAdapter(SyncIndex.class, new SyncIndexSerializer());
            gson.registerTypeAdapter(SyncDirectory.class, new SyncDirectorySerializer());
            array.add(gson.create().toJsonTree(file));
        }

        object.add("files", array);
        return object;
    }

    @Override
    public SyncDirectory deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = (JsonObject) json;
        String name = object.getAsJsonPrimitive("name").getAsString();
        long size = object.getAsJsonPrimitive("size").getAsLong();
        long lastModified = object.getAsJsonPrimitive("lastModified").getAsLong();
        boolean added = object.getAsJsonPrimitive("added").getAsBoolean();

        SyncDirectory dir = new SyncDirectory(name, size, lastModified, added);
        JsonArray array = object.getAsJsonArray("files");
        for (JsonElement file : array) {
            GsonBuilder gson = new GsonBuilder();
            gson.registerTypeAdapter(SyncIndex.class, new SyncIndexSerializer());
            gson.registerTypeAdapter(SyncDirectory.class, new SyncDirectorySerializer());
            dir.add(gson.create().fromJson(file, SyncFile.class));
        }
        
        return dir;
    }
}
