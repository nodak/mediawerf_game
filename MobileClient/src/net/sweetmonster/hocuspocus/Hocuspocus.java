package net.sweetmonster.hocuspocus;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

import net.sweetmonster.hocuspocus.annotation.HocusPocus;
import net.sweetmonster.hocuspocus.annotation.HocusPocusMethod;


//1. a–adir clases a hocuspocus manager 
//2. analizar variables con anotacion metiendolas en un array 

public class Hocuspocus {


	class QQ { 
		
		public Class cls; 
		public Object obj; 
		public String name; 
		public float value; 
		public float min; 
		public float max;
		public Field attr;
		protected Method method; 
		
		
		public QQ(Class cls, Object obj, String name, float value, float min, float max, Field attr) {
			this.cls = cls; 
			this.obj = obj; 
			this.name = name; 
			this.value = value; 
			this.min = min; 
			this.max = max; 
			this.attr = attr; 
		} 
		
		
		public QQ(Class cls, Object obj, String name, Method method) {
			this.cls = cls; 
			this.obj = obj; 
			this.name = name; 
			this.method = method; 
		} 
		
	}
	
	String hocusPocusAnnotationName; 
	String hocusPocusMethodAnnotationName; 

	private boolean useAutoDiscovery;
	private Vector<QQ> qqs; 
	
	Hocuspocus() {

		hocusPocusAnnotationName = HocusPocus.class
				.getSimpleName();
 
		hocusPocusMethodAnnotationName = HocusPocusMethod.class
				.getSimpleName();

		qqs = new Vector<Hocuspocus.QQ>();
		// PackageUtils.getClasseNamesInPackage(jarName, packageName)
		// System.out.println("qq " +
		// java.lang.Class.class.getClasses().toString());
	}

	public void addObject(Object obj) {

		Class cls = obj.getClass();

		Utils.logD(" -- adding new object with Class " + cls.getName() + " "
				+ cls.getSimpleName());	
		 

		// searching fields with annotations
		Field attr[] = cls.getDeclaredFields(); 
		Utils.logD("Declared annotations " + cls.getDeclaredAnnotations());

		for (int i = 0; i < attr.length; i++) {

			attr[i].setAccessible(true); 
			Field url = attr[i];
			String name = attr[i].getName();
			Class<?> type = attr[i].getType();

			// foreach annotation in this object
			Annotation a[] = attr[i].getAnnotations();
			for (int j = 0; j < a.length; j++) {

				String objectName = a[j].annotationType().getSimpleName();
		
				if (objectName.equals(hocusPocusAnnotationName)) {

					// get value
					float value = (Float) getValue(obj, attr[i]);

					float min = ((HocusPocus) a[j]).min();
					float max = ((HocusPocus) a[j]).max();

					Utils.logD("annotation: " + url + " " + name + " " + type
							+ " " + value + " " + min + " " + max + " ");

					// guardar aqui la referencia al objeto
					QQ qq = new QQ(cls, obj, name, value, min, max, attr[i]); 
					qqs.add(qq);

					// set value
					//setValue(obj, attr[i], 12);

				}

			}

		} 
		
		//------------------ get declared methods 
		Method methods[] = cls.getDeclaredMethods(); 

		for (int i = 0; i < methods.length; i++) {

			Method method = methods[i];
			method.setAccessible(true); 
			String name = methods[i].getName();


			// foreach annotation in this object
			Annotation a[] = method.getAnnotations();
			for (int j = 0; j < a.length; j++) {

				String objectName = a[j].annotationType().getSimpleName();
		
				if (objectName.equals(hocusPocusMethodAnnotationName)) {

					// get value
					//float value = (Float) getValue(obj, attr[i]);

					//float min = ((HocusPocus) a[j]).min();
					//float max = ((HocusPocus) a[j]).max();

					Utils.logD("annotation method: " + method + " " + name);
					
					// guardar aqui la referencia al objeto
					QQ qq = new QQ(cls, obj, name, method); 
					qqs.add(qq);

					// set value
					//setValue(obj, attr[i], 12);

				}

			}

		} 
		

	}

	public Object getValue(Object obj, Field attr) {
		Object value = null;

		// get value
		try {
			value = (Float) attr.get(obj);
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}

		return value;
	}

	public void setValue(Object obj, Field attr, float num) {

		try {
			attr.set(obj, num);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	public Vector<QQ> getData() { 
		return qqs; 
	}

	public void callMethod(Object obj, Method method) {
		try {
			method.invoke(obj, null);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} 
	
	}

}
