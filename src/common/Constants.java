package common;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {
	// Path of resource file 
	public static final Path RESOURCE_FOLDER = Paths.get("resource");
	
	// Path of Android Jar files
	public static final Path ANDROID_JAR = Paths.get("lib/android.jar");
//	public static final Path APK_FABRICA_WORDCARD = RESOURCE_FOLDER.resolve("flappybird.apk");
	public static final Path APK_FABRICA_WORDCARD = RESOURCE_FOLDER.resolve("minecraft.apk");
	
	// Output folder
	public static final Path OUTPUT_FOLDER = Paths.get("sootOutput");
}
