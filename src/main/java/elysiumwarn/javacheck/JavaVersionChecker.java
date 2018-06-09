package elysiumwarn.javacheck;

import org.apache.commons.lang3.JavaVersion;
import org.apache.commons.lang3.SystemUtils;
import elysiumwarn.javacheck.report.JavaCheckerReporter;
import elysiumwarn.javacheck.report.OutdatedJavaException;
import elysiumwarn.javacheck.util.ForgeCompatibility;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = "elysiumwarn", name = "Elysium Warn", version = "1.0.0")
public final class JavaVersionChecker{
	public static final String issueReportSite = "https://github.com/iBuyMountainDew/ElysiumWarn/issues";
	
	public static void run(JavaVersion minVersion){
		try{
			unsafeRun(minVersion);
		}catch(OutdatedJavaException me){
			throw me;
		}catch(ShadingException up){
			throw up;
		}catch(Throwable t){
			t.printStackTrace();
			System.out.println("Detected an unexpected error in Java Version Checker, ignoring since trying to run the game is more important.");
			System.out.println("If you crashed and happen to see this, please report the error above to: "+issueReportSite);
		}
	}
	
	private static void unsafeRun(JavaVersion minVersion){
		if (minVersion == null || !SystemUtils.isJavaVersionAtLeast(minVersion)){
			if (minVersion == null)minVersion = JavaVersion.JAVA_1_8; // debugging purposes
			
			JavaCheckerReporter.reportOutdatedJava(minVersion);
			throw new OutdatedJavaException();
		}
		else{
			if (isShaded() && !ForgeCompatibility.tryResetModState()){
				throw new ShadingException();
			}
		}
	}
	
	private static boolean isShaded(){
		return !JavaCheckerReporter.class.getPackage().getName().equals("elysiumwarn.javacheck.report");
	}
	
	public static class ShadingException extends RuntimeException{
		public ShadingException(){
			super("An exception happened when updating the coremod list, the mod you are shading Java Checker in will not run without it. Please, report the issue to "+issueReportSite);
		}
	}
}