package net.lomeli.trophyslots;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraftforge.fml.common.FMLModContainer;
import net.minecraftforge.fml.common.ILanguageAdapter;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.Side;

public class KotlinAdapter implements ILanguageAdapter {

    private Logger log = LogManager.getLogger("ILanguageAdapter/Kotlin");

    @Override
    public Object getNewInstance(FMLModContainer container, Class<?> objectClass, ClassLoader classLoader, Method factoryMarkedAnnotation) throws Exception {
        log.debug("FML has asked for {} to be constructed...", objectClass.getSimpleName());
        try {
            // Try looking for an object type
            Field f = objectClass.getField("INSTANCE$");
            Object obj = f.get(null);
            if (obj == null) throw new NullPointerException();
            log.debug("Found an object INSTANCE$ reference in {}, using that. ({})", objectClass.getSimpleName(), obj);
            return obj;
        } catch (Exception ex) {
            // Try looking for a class type
            log.debug("Failed to get object reference, trying class construction.");
            try {
                Object obj = objectClass.newInstance();
                if (obj == null) throw new NullPointerException();
                log.debug("Constructed an object from a class type ({}), using that. ({})", objectClass, obj);
                return obj;
            } catch (Exception e) {
                throw new KotlinAdapterException(e);
            }
        }
    }

    @Override
    public boolean supportsStatics() {
        return false;
    }

    @Override
    public void setProxy(Field target, Class<?> proxyTarget, Object proxy) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        log.debug("Setting proxy: {}.{} -> {}", target.getDeclaringClass().getSimpleName(), target.getName(), proxy);
        for (Field x : proxyTarget.getFields()) {
            if (x.getName().equals("INSTANCE$")) {
                try {
                    log.debug("Setting proxy on INSTANCE$; singleton target.");
                    Object obj = proxyTarget.getField("INSTANCE$").get(null);
                    target.set(obj, proxy);
                } catch (Exception ex) {
                    throw new KotlinAdapterException(ex);
                }
            } else
                target.set(proxyTarget, proxy);
        }
    }

    @Override
    public void setInternalProxies(ModContainer mod, Side side, ClassLoader loader) {

    }

    private class KotlinAdapterException extends RuntimeException {
        public KotlinAdapterException(Exception ex) {
            super("Kotlin adapter error - do not report to Forge!", ex);
        }
    }
}
