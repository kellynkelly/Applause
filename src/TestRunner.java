/**
 * This is the main function for the program users can supply two arguments to the command
 * -countries followed by a space separated list of countries
 * -devices followed by a space separated list of devices, please put quotes around devices 
 * 		that have a space in them
 * -help the help flag will print syntax, all other flags and arguments will be ignored
 * 
 * Both options can also take "ALL" to get either all countries or all devices, not suppling the flag
 * has the same effect as "all"
 * 
 * To run program java -jar applause.jar -countries US JP -devices "iPhone 5" "Galaxy S3"
 * @author Kelly Kelly
 *
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

public class TestRunner {

	static final String CountryFlag = "-countries";
	static final String DeviceFlag = "-devices";
	static final String ALL = "all";
	
	static final String CommandLine  = "-countries <country1> <...> <countryn> -devices <device1> <...> <-deviceN>";
	
	private ArrayList<String> countryStrings = new ArrayList<String>();
	private ArrayList<String> deviceStrings = new ArrayList<String>();
	
	public static void main(String[] args) {
		
		TestRunner runner = new TestRunner();
		
		try {			
			if(runner.parseArgs(args)) {
				runner.printMessage("Help will always be given to those that ask");
				return;
			}
			
			if(runner.countryStrings.contains(ALL) || runner.countryStrings.size() == 0)
				runner.countryStrings = null;
			
			if(runner.deviceStrings.contains(ALL) || runner.deviceStrings.size() == 0)
				runner.deviceStrings = null;
			
		}catch(Exception ex) {
			runner.printError(ex.getMessage());
		}
		
		try {
			DataManager manager = new DataManager();
			
			ArrayList<String> deviceIDs = manager.GetDeviceIDs(runner.deviceStrings);
			ArrayList<String> testerIDs = manager.GetTestersForDevices(deviceIDs);
			
			Hashtable<String, Tester> testerTable = manager.GetTesterForCountry(testerIDs, runner.countryStrings);
			
			manager.GetBugCountForTesters(testerTable);
			
			ArrayList<Tester> testerList = new ArrayList<Tester>(testerTable.values());
			Collections.sort(testerList);
			
			System.out.println("----------RESULTS-------------");
			for(Tester t: testerList)
				System.out.println(t);
			
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

	}
	
	/**
	 * Simple arg parser, anything that starts with a '-' is considered a flag
	 * everything an argument. The expected format is a flag followed by one or more space
	 * separated arguments, but if the user supplies the argument multiple times this function
	 * can handles and will append all arguments to the list.
	 * 
	 * @param args
	 * @return boolean true if -help flag was found
	 */
	private boolean parseArgs(String[] args) {
		ArrayList<String> currentList = null;
		
		for(String s: args) {
			if(s.startsWith("-")) {
				if(s.equalsIgnoreCase("-help")) {
					return true;
				}
				
				else if(s.equalsIgnoreCase(CountryFlag)) {
					currentList = countryStrings;
				}
				else if(s.equalsIgnoreCase(DeviceFlag)) {
					currentList = deviceStrings;
				}
				else {
					throw new IllegalArgumentException("Unknow argument " + s);
				}
			}
			else {
				currentList.add(s.toLowerCase());
			}
		}
		
		return false;
	}
	
	private void printMessage(String message) {
		System.out.println(message);
		System.out.println("Command Line Syntax:");
		System.out.println(CommandLine);
	}
	
	private void printError(String message) {
		System.err.println(message);
		System.err.println("Command Line Syntax:");
		System.err.println(CommandLine);
	}

}
