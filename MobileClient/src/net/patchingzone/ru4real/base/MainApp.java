package net.patchingzone.ru4real.base;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

/*
@ReportsCrashes(
	formKey = "", // will not be used
	mailTo = "victormdb@gmail.com",
	mode = ReportingInteractionMode.SILENT)
*/ 

public class MainApp extends Application {

	
	public MainApp() { 
		
		
	} 
	
	@Override
	public void onCreate() {
		super.onCreate();
	
      //  ACRA.init(this);

	}
}