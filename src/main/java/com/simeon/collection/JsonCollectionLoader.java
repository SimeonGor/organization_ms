package com.simeon.collection;


import com.google.gson.*;
import com.simeon.collection.element.OrganizationType;
import com.simeon.exceptions.InvalidCollectionDataException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.Set;

/**
 * Class implements loading a collection from a json file
 * @see MyCollection
 */
public class JsonCollectionLoader implements CollectionLoader {
    private static final Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    /**
     * Load and parse json file
     * @param path path to json file
     * @return instance of the MyCollection
     * @throws IOException errors reading from a file
     * @throws InvalidCollectionDataException invalid collection data in the file
     */
    @Override
    public MyCollection load(String path) throws IOException, InvalidCollectionDataException {
        class LocalDateDeserializer implements JsonDeserializer<LocalDate> {
            @Override
            public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                String data = json.getAsString();
                return LocalDate.parse(data);
            }

        }

        class OrganizationTypeDeserializer implements JsonDeserializer<OrganizationType> {
            @Override
            public OrganizationType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                String data = json.getAsString();
                return OrganizationType.valueOf(data);
            }
        }

        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException(String.format("%s : no such file", path));
        }

        if (!file.canRead()) {
            throw new AccessDeniedException(path, null, "permission to read denied");
        }

        try (BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            StringBuilder jsonString = new StringBuilder();
            String nextLine;
            while ((nextLine = input.readLine()) != null) {
                jsonString.append(nextLine);
            }
            Gson g = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                    .registerTypeAdapter(OrganizationType.class, new OrganizationTypeDeserializer())
                    .create();

            MyCollection collection = g.fromJson(jsonString.toString(), MyCollection.class);
            Set<ConstraintViolation<MyCollection>> validates = validator.validate(collection);
            if (!validates.isEmpty()) {
                for (var e : validates) {
                    System.out.println(e);
                }
                throw new InvalidCollectionDataException(path);
            }
            return collection;
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(String.format("%s : no such file", path));
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    /**
     * Save collection to the json file
     * @param path path to json file
     * @param collection instance of the MyCollection
     */
    @Override
    public void save(String path, MyCollection collection) throws IOException {
        class LocalDateSerializer implements JsonSerializer<LocalDate> {
            @Override
            public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(src.toString());
            }
        }
        class OrganizationTypeSerializer implements JsonSerializer<OrganizationType> {
            @Override
            public JsonElement serialize(OrganizationType organizationType, Type typeOfT, JsonSerializationContext context) {
                return new JsonPrimitive(organizationType.toString());
            }
        }

        File file = new File(path);

        if (file.exists() && !file.canWrite()) {
            throw new AccessDeniedException(path, null, "permission to write denied");
        }

        try (BufferedWriter outputStreamWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                    .registerTypeAdapter(OrganizationType.class, new OrganizationTypeSerializer())
                    .setPrettyPrinting()
                    .create();
            String json = gson.toJson(collection);
            outputStreamWriter.write(json);
            outputStreamWriter.flush();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(String.format("%s : no such file", path));
        } catch (IOException e) {
            throw new IOException(e);
        }
    }
}
