package net.lomeli.trophyslots.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cpw.mods.fml.relauncher.ReflectionHelper;

public class ObfUtil {

    public static boolean isOptifineInstalled() {
        try {
            return Class.forName("optifine.OptiFineForgeTweaker") != null;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean isObf() {
        try {
            Field[] fields = Class.forName("net.minecraft.world.World").getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                if (f.getName().equalsIgnoreCase("loadedEntityList"))
                    return false;
            }
        } catch (Exception e) {
            return true;
        }
        return true;
    }

    public static boolean isFieldAccessible(Class<?> clazz, String... names) {
        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                for (String fieldName : names) {
                    if (field.getName().equalsIgnoreCase(fieldName))
                        return field.isAccessible();
                }
            }
        } catch (Exception e) {
            throw new ReflectionHelper.UnableToFindFieldException(names, e);
        }
        return false;
    }

    public static void setFieldAccessible(Class<?> clazz, String... names) {
        try {
            Field field = getField(clazz, names);
            if (field != null)
                field.setAccessible(true);
        } catch (Exception e) {
        }
    }

    public static <T, E> void setFieldValue(Class<? extends E> clazz, E instance, Object value, String... names) {
        try {
            Field field = getField(clazz, names);
            if (field != null)
                field.set(instance, value);
        } catch (Exception e) {
        }
    }

    public static <T, E> T getFieldValue(Class<? extends E> clazz, E instance, String... names) {
        try {
            return (T) getField(clazz, names).get(instance);
        } catch (Exception e) {
            throw new ReflectionHelper.UnableToAccessFieldException(names, e);
        }
    }

    public static Field getField(Class<?> clazz, String... names) {
        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                for (String fieldName : names) {
                    if (field.getName().equalsIgnoreCase(fieldName)) {
                        field.setAccessible(true);
                        return field;
                    }
                }
            }
        } catch (Exception e) {
            throw new ReflectionHelper.UnableToFindFieldException(names, e);
        }
        return null;
    }

    public static boolean isMethodAccessable(Class<?> clazz, String... names) {
        try {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                for (String methodName : names) {
                    if (method.getName().equalsIgnoreCase(methodName))
                        return method.isAccessible();
                }
            }
        } catch (Exception e) {
            throw new ReflectionHelper.UnableToFindMethodException(names, e);
        }
        return false;
    }

    public static <T, E> Object invokeMethod(Class<? extends E> clazz, E instance, String[] names, Object... args) {
        try {
            Method method = getMethod(clazz, names);
            if (method != null)
                return method.invoke(instance, args);
        } catch (Exception e) {
            throw new ReflectionHelper.UnableToFindMethodException(names, e);
        }
        return null;
    }

    public static Method getMethod(Class<?> clazz, String... names) {
        try {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                for (String methodName : names) {
                    if (method.getName().equalsIgnoreCase(methodName)) {
                        method.setAccessible(true);
                        return method;
                    }
                }
            }
        } catch (Exception e) {
            throw new ReflectionHelper.UnableToFindMethodException(names, e);
        }
        return null;
    }
}
