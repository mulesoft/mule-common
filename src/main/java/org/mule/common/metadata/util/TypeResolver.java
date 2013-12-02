/*
 * CloverETL Engine - Java based ETL application framework.
 * Copyright (c) Javlin, a.s. (info@cloveretl.com).  Use is subject to license terms.
 *
 * www.cloveretl.com
 */
package org.mule.common.metadata.util;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TypeResolver {

    /**
     * Answers all super interfaces that the class implements, including
     * transitively inherited ones.
     * @param type
     * @return
     */
    public static Class<?>[] getSuperInterfaces(Class<?> type) {

        if (!type.isInterface()) {
            throw new IllegalArgumentException("only interfaces allowed");
        }
        List<Class<?>> superInterfaces = new LinkedList<Class<?>>();
        collectSuperInterfaces(type, superInterfaces);
        return superInterfaces.toArray(new Class[superInterfaces.size()]);
    }

    private static void collectSuperInterfaces(Class<?> type, List<Class<?>> superInterfaces) {

        for (Class<?> superInterface : type.getInterfaces()) {
            superInterfaces.add(superInterface);
            collectSuperInterfaces(superInterface, superInterfaces);
        }
    }

    /**
     * Answers immediate parameterized super type
     * that is causing the type to be instance of given declaredType.
     * @param type
     * @param declaredType
     * @return
     */
    public static ParameterizedType getGenericSuperclass(Class<?> type, Class<?> declaredType) {

        List<Type> types = new ArrayList<Type>();
        if (type.getGenericSuperclass() != null) {
            types.add(type.getGenericSuperclass());
        }
        types.addAll(Arrays.asList(type.getGenericInterfaces()));

        for (Type superType : types) {
            if (superType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType)superType;
                Type raw = parameterizedType.getRawType();
                if (raw instanceof Class<?>) {
                    if (declaredType.isAssignableFrom((Class<?>)raw)) {
                        return parameterizedType;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Answers immediate super type that is causing to be the
     * type sub-type of given declaredType.
     * @param type
     * @param declaredType
     * @return
     */
    public static Class<?> getSuperclass(Class<?> type, Class<?> declaredType) {

        List<Class<?>> types = new ArrayList<Class<?>>();
        if (type.getSuperclass() != null) {
            types.add(type.getSuperclass());
        }
        types.addAll(Arrays.asList(type.getInterfaces()));
        
        for (Class<?> superType : types) {
            if (declaredType.isAssignableFrom(superType)) {
                return superType;
            }
        }
        return null;
    }
    
    /**
     * Resolves type variables in given class.
     * @param type
     * @return
     */
    public static Map<TypeVariable<?>, Type> resolveVariables(Type type) {
        
        /*
         * only Class or ParameterizedType
         */
        if (!(type instanceof Class<?> || type instanceof ParameterizedType)) {
            throw new IllegalArgumentException("Invalid type " + type);
        }
        final Map<TypeVariable<?>, Type> map = new HashMap<TypeVariable<?>, Type>();
        Class<?> raw = erase(type);
        TypeVariable<?> formal[] = raw.getTypeParameters();
        Type actaul[] = type instanceof Class<?> ? formal : ((ParameterizedType)type).getActualTypeArguments();
        
        for (int i = 0; i < formal.length; ++i) {
            map.put(formal[i], actaul[i]);
        }

        if (raw.getGenericSuperclass() != null) {
            map.putAll(resolveVariables(raw.getGenericSuperclass()));
        }
        for (Type genericInterface : raw.getGenericInterfaces()) {
            map.putAll(resolveVariables(genericInterface));
        }

        return map;
    }
    
    /**
     * Performs type erasure for given type.
     * @param type
     * @return
     */
    public static Class<?> erase(Type type) {

        if (type instanceof Class<?>) {
            return (Class<?>)type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)type;
            return erase(parameterizedType.getRawType());
        }
        if (type instanceof TypeVariable<?>) {
            TypeVariable<?> typeVar = (TypeVariable<?>)type;
            Type bounds[] = typeVar.getBounds();
            if (bounds.length > 0) {
                return erase(bounds[0]);
            } else {
                return Object.class;
            }
        }
        if (type instanceof WildcardType) {
            WildcardType wildType = (WildcardType)type;
            Type bounds[] = wildType.getUpperBounds();
            if (bounds.length > 1) {
                return erase(bounds[0]);
            } else {
                return Object.class;
            }
        }
        if (type instanceof GenericArrayType) {
            GenericArrayType genericArray = (GenericArrayType)type;
            Class<?> componentClass = erase(genericArray.getGenericComponentType());
            return Array.newInstance(componentClass, 0).getClass();
        }
        throw new IllegalArgumentException("Unknown type " + type);
    }
}
