/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.lets.stacktraceminer.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *
 * @author Teresa
 */
public class ReflectionUtil {

    public static Object executeMethod(Object classObject, String methodName, Object param) {
        if (param==null) return null;
        try {
            Method getMethod;
            getMethod = classObject.getClass().getMethod(methodName, param.getClass());
            return getMethod.invoke(classObject, param);
        } catch (Exception ex) {
            
        }
        return null;
    }
    
    public static Object executeMethod(Object classObject, String methodName) {
        try {
            Method getMethod;
            getMethod = classObject.getClass().getMethod(methodName, new Class[]{});
            return getMethod.invoke(classObject, new Object[]{});
        } catch (Exception ex) {
            
        }
        return null;
    }

    public static Object getField(Object classObject, String fieldName) {

        try {
            Field field;
            field = classObject.getClass().getField(fieldName);
            return field.get(classObject);
        } catch (Exception ex) {
            
        }
        return null;
    }
}
