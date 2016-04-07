package mx.mercatto.designmercastock;

/**
 * Created by Juan Carlos De León on 06/04/2016.
 */

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Parses any class to JSON and populates any class from JSON using Android native JSON, without any third-party libraries.
 *
 * @author Ender Muab'Dib
 *
 */
public class MyJSONParser {
    private static int privacy = 2;

    /**
     * p = 0 --> Only for public fields
     * p = 1 --> Only for public + protected fields
     * p = 2 --> All fields: public + protected + private. Use only under your own responsability.
     */
    public static void setPrivacyLevel(int p) {
        if ((p >= 0) && (p <= 2)) {
            privacy = p;
        }
    }

    /**
     * privacy = 0 --> Only for public fields
     * privacy = 1 --> Only for public + protected fields
     * privacy = 2 --> All fields: public + protected + private. Use only under your own responsability.
     */
    public static int getPrivacyLevel() {
        return privacy;
    }

    /**
     * @return an object of type classname with its PUBLIC fields full with the js info
     */
    public static Object populateObjectFromJSON(Class<?> classname, JSONObject js) {
        Object obj = null;
        System.out.println("Populating " + classname.getSimpleName() + " -with- " + js.toString());
        try {
            obj = classname.newInstance();
        } catch (InstantiationException e1) {
            System.err.println(e1.getMessage());
            return null;
        } catch (IllegalAccessException e1) {
            System.err.println(e1.getMessage());
            return null;
        }

        Field[] fields;

        if (privacy == 0) {
            fields = classname.getFields();
        } else {
            fields = classname.getDeclaredFields();
        }

        for (Field f : fields) {
            // System.out.println("Declared " + f.getName());

            if ((privacy == 2) && (Modifier.isPrivate(f.getModifiers()))) {
                f.setAccessible(true);
            }

            try {
                if (js.has(f.getName())) {
                    String type = f.getType().getSimpleName();
                    if (type.equalsIgnoreCase("boolean")) {
                        f.setBoolean(obj, js.getBoolean(f.getName()));
                    } else if (type.equalsIgnoreCase("int")) {
                        f.setInt(obj, js.getInt(f.getName()));
                    } else if (type.equalsIgnoreCase("double")) {
                        f.setDouble(obj, js.getDouble(f.getName()));
                    } else if (type.equalsIgnoreCase("float")) {
                        f.setFloat(obj, (float) js.getDouble(f.getName()));
                    } else if (type.equalsIgnoreCase("string")) {
                        f.set(obj, js.getString(f.getName()));
                    } else if (f.getType().isArray()) {
                        f.set(obj, Array.newInstance(f.getType().getComponentType(), js.getJSONArray(f.getName()).length()));
                        insertArrayFromJSON(f.get(obj), js.getJSONArray(f.getName()));
                    } else {
                        f.set(obj, populateObjectFromJSON(f.getType(), js.getJSONObject(f.getName())));
                    }
                }
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            } catch (IllegalAccessException e) {
                System.err.println(e.getMessage());
            } catch (JSONException e) {
                System.err.println(e.getMessage());
            }

            if ((privacy == 2) && (Modifier.isPrivate(f.getModifiers()))) {
                f.setAccessible(false);
            }
        }

        return obj;
    }

    /**
     * @param o
     *            This object will be fill up with the JSONArray js data
     */
    public static void insertArrayFromJSON(Object o, JSONArray js) throws IllegalArgumentException, NegativeArraySizeException, IllegalAccessException, JSONException {
        Class<?> c = o.getClass();

        String type = c.getComponentType().getSimpleName();

        for (int i = 0; i < js.length(); i++) {
            if (c.getComponentType().isArray()) {
                Array.set(o, i, Array.newInstance(c.getComponentType().getComponentType(), js.getJSONArray(i).length()));
                insertArrayFromJSON(Array.get(o, i), js.getJSONArray(i));
            } else if (!c.getComponentType().isPrimitive() && (!type.equalsIgnoreCase("string"))) {
                Array.set(o, i, populateObjectFromJSON(c.getComponentType(), js.getJSONObject(i)));
            } else {
                Array.set(o, i, js.get(i));
            }
        }
    }

    /**
     * @param obj Object to convert in JSON format
     * @return JSONOBject with all obj initialited AND PUBLIC fields.
     */
    public static JSONObject parseObjectToJSONClass(Object obj) {
        JSONObject js = new JSONObject();

        Class<?> c = obj.getClass();

        Field[] fields;

        if (privacy == 0) {
            fields = c.getFields();
        } else {
            fields = c.getDeclaredFields();
        }

        for (Field f : fields) {
            if ((privacy == 2) && (Modifier.isPrivate(f.getModifiers()))) {
                f.setAccessible(true);
            }

            try {
                System.out.println(f.getName() + " - " + f.getType().getSimpleName() + " - " + f.get(obj));

                String name = f.getName();
                String type = f.getType().getSimpleName();
                if (f.get(obj) != null) {
                    if (type.equalsIgnoreCase("boolean")) {
                        js.put(name, f.getBoolean(obj));
                    } else if (type.equalsIgnoreCase("int")) {
                        js.put(name, f.getInt(obj));
                    } else if (type.equalsIgnoreCase("double")) {
                        js.put(name, f.getDouble(obj));
                    } else if (type.equalsIgnoreCase("float")) {
                        js.put(name, f.getFloat(obj));
                    } else if (type.equalsIgnoreCase("string")) {
                        js.put(name, (String) f.get(obj));
                    } else if (type.endsWith("]")) {
                        js.put(name, generateJSONArray(f.get(obj)));
                    } else {
                        js.put(name, parseObjectToJSONClass(f.get(obj)));
                    }
                }
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            } catch (IllegalAccessException e) {
                System.err.println(e.getMessage());
            } catch (JSONException e) {
                System.err.println(e.getMessage());
            }

            if ((privacy == 2) && (Modifier.isPrivate(f.getModifiers()))) {
                f.setAccessible(false);
            }
        }

        return js;
    }

    /**
     *
     * @param o  Array object to convert in JSONArray format
     * @return a JSONArray with all the o components.
     */
    public static JSONArray generateJSONArray(Object o) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, JSONException {
        JSONArray ar = new JSONArray();
        for (int i = 0; i < Array.getLength(o); i++) {
            if (Array.get(o, i).getClass().isArray()) {
                ar.put(i, generateJSONArray(Array.get(o, i)));
            } else {
                ar.put(i, Array.get(o, i));
            }
        }
        return ar;
    }
}