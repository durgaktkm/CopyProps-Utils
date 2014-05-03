import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * Created by Ramprasad on 3/13/14.
 */
public class CopyProperties {
    public static void main(String[] args) throws Exception {
        T1 t1 = new T1();
        t1.setSw("abc");
        t1.s2 = "xyz";
        t1.i = 10;
        t1.today = new Date();
        Abc abc = new Abc();
        abc.a="abc -a ";
        abc.b = "abc -b";
        t1.abc = abc;

        T2 t2 = new T2();
        copyFields(t1, t2);
        System.out.println(t2.s1);
    }

    public static <A,B> void copyFields(A source, B target) throws Exception {
        Class<?> clazz = source.getClass();
        Class<?> targetClazz = target.getClass();
        String annotationFiled;
        MyAnnotation myAnnotation;
        Field[] sourceFields = clazz.getDeclaredFields();
        for(Field field:sourceFields){
            field.setAccessible(true);
        }
        Field[] targetFields = targetClazz.getDeclaredFields();
        for(Field field:targetFields){
            field.setAccessible(true);
        }
        for (Field field : sourceFields) {
            for (Field targetField : targetFields) {
                if (field.isAnnotationPresent(MyAnnotation.class)) {
                    Annotation[] annotation = field.getDeclaredAnnotations();
                    for (Annotation ann : annotation) {
                        ann.annotationType().equals(MyAnnotation.class);
                        myAnnotation = (MyAnnotation) ann;
                        annotationFiled = myAnnotation.name();
                        if (annotationFiled.equalsIgnoreCase(targetField.getName())) {
                            targetField.set(target, field.get(source));
                        }
                    }
                } else if (targetField.getName().equalsIgnoreCase(field.getName())) {
                    Object value = field.get(source);
                    targetField.set(target, value);
                }
            }
        }

    }
}
