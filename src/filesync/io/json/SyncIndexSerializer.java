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
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import filesync.SyncFile;
import filesync.SyncIndex;
import filesync.SyncSchedule;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aaron Lucia
 * @version Aug 15, 2015
 */
public class SyncIndexSerializer implements JsonSerializer<SyncIndex>, JsonDeserializer<SyncIndex> {

    @Override
    public JsonElement serialize(SyncIndex src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("name", src.getName());
        object.addProperty("size", src.getSize());
        object.addProperty("lastModified", src.getLastModified());
        
        JsonArray directoriesArray = new JsonArray();
        for (File path : src.getDirectories()) {
            directoriesArray.add(new JsonPrimitive(path.toString()));
        }
        object.add("directories", directoriesArray);
        
        object.add("schedule", new Gson().toJsonTree(src.getSchedule()));

        JsonArray filesArray = new JsonArray();
        for (SyncFile file : src) {
            filesArray.add(new IndexBuilder().toJsonTree(file));
        }
        object.add("files", filesArray);
        return object;
    }

    @Override
    public SyncIndex deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = (JsonObject) json;
        String name = object.getAsJsonPrimitive("name").getAsString();

        JsonArray directoriesArray = object.getAsJsonArray("directories");
        List<File> directories = new ArrayList<>();
        for (JsonElement path : directoriesArray) {
            directories.add(new File(path.getAsString()));
        }
        
        SyncIndex dir = new SyncIndex(name, directories);
        dir.setSize(object.getAsJsonPrimitive("size").getAsLong());
        dir.setLastModified(object.getAsJsonPrimitive("lastModified").getAsLong());
        dir.setSchedule(new Gson().fromJson(object.get("schedule"), SyncSchedule.class));
        
        JsonArray filesArray = object.getAsJsonArray("files");
        for (JsonElement file : filesArray) {
            dir.add(new IndexBuilder().fromJson(file, SyncFile.class));
        }
        
        return dir;
    }
}
