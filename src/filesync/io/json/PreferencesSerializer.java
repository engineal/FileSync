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

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import filesync.FileSync;
import filesync.IndexList;
import filesync.Preferences;
import filesync.SyncIndex;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aaron Lucia
 * @version Aug 15, 2015
 */
public class PreferencesSerializer implements JsonSerializer<Preferences>, JsonDeserializer<Preferences> {

    private static final Logger log = Logger.getLogger(FileSync.class.getName());

    @Override
    public JsonElement serialize(Preferences src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("indexesLocation", src.getIndexesLocation().toString());
        if (!src.getIndexesLocation().exists()) {
            src.getIndexesLocation().mkdirs();
        }

        JsonArray indexesArray = new JsonArray();
        for (SyncIndex index : src.getIndexes()) {
            File indexLocation = new File(src.getIndexesLocation(), index.getName() + ".json");
            try (FileWriter reader = new FileWriter(indexLocation)) {
                reader.write(new IndexBuilder().toJson(index));
            } catch (IOException ex) {
                log.log(Level.SEVERE, ex.getMessage(), ex);
            }
            indexesArray.add(new JsonPrimitive(index.getName()));
        }
        object.add("indexes", indexesArray);

        object.addProperty("prereleases", src.isPrereleases());
        return object;
    }

    @Override
    public Preferences deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = (JsonObject) json;
        File indexesLocation = new File(object.getAsJsonPrimitive("indexesLocation").getAsString());

        IndexList indexes = new IndexList();
        JsonArray indexesArray = object.getAsJsonArray("indexes");
        for (JsonElement index : indexesArray) {
            File indexLocation = new File(indexesLocation, index.getAsString() + ".json");
            try (FileReader reader = new FileReader(indexLocation)) {
                indexes.add(new IndexBuilder().fromJson(reader, SyncIndex.class));
            } catch (IOException ex) {
                log.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }

        boolean prereleases = object.getAsJsonPrimitive("prereleases").getAsBoolean();
        return new Preferences(indexesLocation, indexes, prereleases);
    }
}
