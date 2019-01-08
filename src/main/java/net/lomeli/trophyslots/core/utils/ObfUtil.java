package net.lomeli.trophyslots.core.utils;

import net.lomeli.trophyslots.TrophySlots;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObfUtil {
    public static <T, E> T invokeMethod(Class<E> clazz, E instance, String[] names) {
        return invokeMethod(clazz, instance, names, null);
    }

    /**
     * Invokes a method in a class.
     *
     * @param clazz        Class that contains the method.
     * @param instance     Instance of the class. Use null if method is static.
     * @param names        Possible names for the method. It's an array to handle both deobf and srg names.
     * @param paramClasses The classes for each parameter in the method. Used to differentiate between multiple
     *                     methods with the same name but different parameters.
     * @param parameters   The parameter values to input into the method.
     * @return The value the method returns. Will return null if the method could not be found or if the method type is void.
     */
    @SuppressWarnings("unchecked")
    public static <T, E> T invokeMethod(Class<E> clazz, E instance, String[] names, Class[] paramClasses, Object... parameters) {
        T value = null;
        Method method = getMethod(clazz, names, paramClasses);
        if (method != null) {
            boolean flag = method.isAccessible();
            if (!flag) method.setAccessible(true);
            try {
                Object mValue = method.invoke(instance, parameters);
                if (mValue != null)
                    value = (T) mValue;
            } catch (IllegalAccessException ex) {
                TrophySlots.log.exception("Could not access %1$s() in class %2$s", ex, method.getName(), clazz.getName());
            } catch (InvocationTargetException ex) {
                TrophySlots.log.error("Could not invoke %1$s() in class %2$s", method.getName(), clazz.getName());
            }
            if (!flag) method.setAccessible(false);
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    public static <T> T invokeMethod(Method method, Object instance, Object... parameters) {
        T value = null;
        if (method != null) {
            boolean flag = method.isAccessible();
            if (!flag) method.setAccessible(true);
            try {
                value = (T) method.invoke(instance, parameters);
            } catch (IllegalAccessException ex) {
                TrophySlots.log.exception("Could not access %1$s()", ex, method.getName());
            } catch (InvocationTargetException ex) {
                TrophySlots.log.error("Could not invoke %1$s()", method.getName());
            }
            if (!flag) method.setAccessible(false);
        }
        return value;
    }

    /**
     * Gets a method in a class.
     *
     * @param clazz      Class that contains the method.
     * @param names      Possible names for the method. It's an array to handle both deobf and srg names.
     * @param parameters The classes for each parameter in the method. Used to differentiate between multiple
     *                   methods with the same name but different parameters.
     * @return A {@link Method} object for further manipulation or a null.
     */
    @SuppressWarnings("unchecked")
    public static Method getMethod(Class clazz, String[] names, Class... parameters) {
        if (names != null && names.length > 0) {
            for (String name : names) {
                try {
                    Method method = clazz.getDeclaredMethod(name, parameters);
                    if (method != null) return method;
                } catch (NoSuchMethodException ignored) {
                }
            }
            TrophySlots.log.error("Could not find method in class(%s) with the following names: ", clazz.getName());
            for (String name : names)
                TrophySlots.log.error("- %s", name);
        }
        return null;
    }
}
