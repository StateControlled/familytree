package com.berthouex.familytree.data;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import com.berthouex.familytree.model.Graph;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

public class DatabaseManager {
    /**
     * Creates a Gson object and defines serializer/deserializer for Date objects.
     */
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, type, context) -> new JsonPrimitive(src.toString()))
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, context) -> LocalDate.parse(json.getAsString()))
            .create();

    public static void save(Graph graph, String filepath) throws IOException {
        Path path = Paths.get(filepath);
        Files.createDirectories(path.getParent());

        try (Writer writer = Files.newBufferedWriter(path)) {
            GSON.toJson(graph, writer);
        }

        graph.setFilepath(filepath);
    }

    public static Graph load(String filepath) throws IOException {
        Path path = Paths.get(filepath);

        try (Reader reader = Files.newBufferedReader(path)) {
            Graph graph = GSON.fromJson(reader, Graph.class);
            graph.setFilepath(filepath);
            return graph;
        }
    }

}
