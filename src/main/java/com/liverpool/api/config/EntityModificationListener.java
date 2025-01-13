package com.liverpool.api.config;

import org.springframework.context.ApplicationListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.UUID;

@Component
public class EntityModificationListener implements ApplicationListener<BeforeConvertEvent<Object>> {


    @Override
    public void onApplicationEvent(BeforeConvertEvent<Object> event) {
        Object entity = event.getSource();

        if (entity.getClass().getPackageName().startsWith("com.liverpool")) {
            processEntity(entity);
        }
    }

    private boolean isJavaStandardClass(Class<?> clazz) {
        return clazz.getName().startsWith("java.") || clazz.getName().startsWith("javax.");
    }

    private void processEntity(Object entity) {
        if (entity == null || isJavaStandardClass(entity.getClass())) {
            return;
        }

        try {
            Field[] fields = entity.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);

                if ("id".equals(field.getName()) && field.get(entity) == null) {
                    field.set(entity, UUID.randomUUID().toString().toLowerCase());
                }

                if (field.getType() == String.class
                        && !"urlImage".equals(field.getName())
                        && !"id".equals(field.getName())
                        && !"clientId".equals(field.getName())
                ) {
                    String value = (String) field.get(entity);
                    if (value != null) {
                        field.set(entity, value.toUpperCase());
                    }
                }

                if (!field.getType().isPrimitive() && !field.getType().isEnum()) {
                    Object nested = field.get(entity);

                    if (nested instanceof Iterable) {
                        for (Object item : (Iterable<?>) nested) {
                            processEntity(item);
                        }
                    } else if (nested != null) {
                        processEntity(nested);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error while processing entity fields", e);
        }
    }

}
