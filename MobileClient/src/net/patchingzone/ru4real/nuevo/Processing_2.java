package net.patchingzone.ru4real.nuevo;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.JavaAdapter;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import processing.core.PApplet;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

public class Processing_2 extends PApplet {

	public class M {
		public M() {

		}

		public void method1() {

		}

		public void method2() {

		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		doit("var Toast = Packages.android.widget.TToast; "
				+ "Toast.makeText(ac, \"qq\", Toast.LENGTH_LONG).show();");

		// This line should instantiate the child class, with
		// method1 + method2 overridden called within the constructor

		// doit(o);

		noLoop();

		doit("var p = Packages.processing.core.PApplet; "
				+ "ac.setup = function setup() { " + ""
				+ "ac.ellipse(22, 22, 12, 12);" + "" + "};");

		loop();

	}

	public void setup() {
		// invoke("setup");
		frameRate(28);
		smooth();

		// processing.core.PApplet
		doit("var p = Packages.processing.core.PApplet; " + //
				"ac.ellipse(82, 82, 125, 125);" + //
				"ac.ellipse(ac.mouseX, ac.mouseY, 125, 125);");

	}

	// public void draw() {
	//
	// }

	public Object qq() {

		String o = "var o = {"
				+ "				  method1: function() {"

				+ "				  },"
				+ "				  method2: function() {"

				+ "				  }"
				+ "				}; "
				+ ""
				+ "var instanceThatIsAMyClass = new JavaAdapter(com.mgmedia.probandofragments.Processing.M, o);";

		JavaAdapter javaAdapter = new JavaAdapter();

		return o;

	}

	public void draw() { // background(255);

		if (frameCount == 1) {
			Looper.prepare();
		} 
		
		background(0); 

		float c = (float) (width * Math.sin(frameCount * 0.01));
		ellipse(mouseX, mouseY, c, c);

		// fill(125); //rect(0, 0, 100, 100); //text("Text: ", 50, 100);

		// ellipse(motionX, motionY, 12, 12);

	}

	void doit(String code) {
		// Create an execution environment.
		Context cx = Context.enter();

		// Turn compilation off.
		cx.setOptimizationLevel(-1);

		try {
			// Initialize a variable scope with bindnings for
			// standard objects (Object, Function, etc.)
			Scriptable scope = cx.initStandardObjects();

			// Set a global variable that holds the activity instance.
			ScriptableObject.putProperty(scope, "ac",
					Context.javaToJS(this, scope));

			// Evaluate the script.
			Object result = cx.evaluateString(scope, code, "doit:", 1, null);

			// Function result2 = cx.compileFunction(scope, code, "doit:", 1,
			// null);
			// result2.call(cx, scope, scope, null);

			// cx.evaluateString(scope, "Toast.makeText('hola')", "qq", 1,
			// null);
		} catch (Exception evException) {
			Log.d("qq",
					"no se puede cargar el script " + evException.toString());
		} finally {
			// Context.exit();
		}

	}

	// public int sketchWidth() { return 500; }
	// public int sketchHeight() { return 500; }
}
