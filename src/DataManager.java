/**
 * The DataManager class has the knowledge of the existance of files so that if we replaced
 * the files with another data source the the API can be maintained and none of the code that calls
 * these functions would be effected
 * 
 * @author Kelly Kelly
 *
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class DataManager {

	private static final String TesterFileName = "file/testers.csv";
	private static final String DeviceFileName = "file/devices.csv";
	private static final String BugFileName = "file/bugs.csv";
	private static final String TesterDeviceFileName = "file/tester_device.csv";
	
	/**
	 * Fetches the device IDs for the passed in list of device "descriptions"
	 * If null is supplied "All" devices will be assumed and a full list of
	 * device ids will be returned
	 * 
	 * @param devices List if descriptions or null
	 * @return a list of the device ids for the passed in device strings
	 * @throws IOException
	 */
	public ArrayList<String> GetDeviceIDs(ArrayList<String> devices) throws IOException{
		ArrayList<String> list = null;
		BufferedReader in = null; 
		
		try {
			in = new BufferedReader(new FileReader(DeviceFileName));
			
			String header = in.readLine();
			if(header == null)
				throw new IOException("File appears to be empty");
			
			list = new ArrayList<String>();
			
			String str = in.readLine();
			while(str != null) {
				String[] splitList = getSplitList(str);
				
				if(devices == null || devices.contains(splitList[1].toLowerCase()))
					list.add(splitList[0]);
				
				str = in.readLine();
			}
		}finally {
			in.close();
		}
		
		return list;
	}

	/**
	 * Uses the tester_device.csv file to get the tester ids for the give list of device ids
	 * @param deviceList
	 * @return a list of tester ids that use the particular devices
	 * @throws IOException
	 */
	public ArrayList<String> GetTestersForDevices(ArrayList<String> deviceList) throws IOException {
		ArrayList<String> list = null;
		BufferedReader in = null; 
		try {
			in = new BufferedReader(new FileReader(TesterDeviceFileName));
			
			String header = in.readLine();
			if(header == null)
				throw new IOException("File appears to be empty");
			
			list = new ArrayList<String>();
			String str = in.readLine();
			while(str != null) {
				String[] splitList = getSplitList(str);
				
				String deviceID = splitList[1];
				String testerID = splitList[0];
				
				if(deviceList == null || (deviceList.contains(deviceID) && !list.contains(testerID)))
					list.add(testerID);
				
				str = in.readLine();
			}
		}finally {
			in.close();
		}
		
		return list;
	}
	
	/**
	 * Using the tester.csv and a list of devices & countries create a hashtable where the keys are
	 * "testerId" and the object a "Tester".  The testers in the table should be only ones that make the list
	 * of given countries and devices
	 * @param testerList
	 * @param countryList
	 * @return a hashtable of testers
	 * @throws IOException
	 */
	public Hashtable<String, Tester> GetTesterForCountry(ArrayList<String> testerList, ArrayList<String> countryList) throws IOException{
		Hashtable<String, Tester> table = null;
		BufferedReader in = null; 
		try {
			in = new BufferedReader(new FileReader(TesterFileName));
			
			String header = in.readLine();
			if(header == null)
				throw new IOException("File appears to be empty");
			
			table = new Hashtable<String, Tester>();
			String str = in.readLine();
			while(str != null) {
				String[] splitList = getSplitList(str);
				
				String testerID = splitList[0];
				if(testerList.contains(testerID)) {
					String country = splitList[3].toLowerCase();
					if(countryList == null || countryList.contains(country)) {
						table.put(testerID, new Tester(splitList[1], splitList[2]));
					}
				}
				str = in.readLine();
			}
		}finally {
			in.close();
		}
		
		return table;
	}
	
	/**
	 * Give the list of testers we want "experience" scores for go through bugs.csv and find the bugs
	 * for those testers.
	 * @param testerTable
	 * @throws IOException
	 */
	public void GetBugCountForTesters(Hashtable<String, Tester> testerTable) throws IOException{
		
		if(testerTable == null)
			throw new IllegalArgumentException("Invalid argument for GetBugCountForTester null");
		
		BufferedReader in = null; 
		try {
			in = new BufferedReader(new FileReader(BugFileName));
			
			String header = in.readLine();
			if(header == null)
				throw new IOException("File appears to be empty");
	
			String str = in.readLine();
			while(str != null) {
				String[] splitList = getSplitList(str);
				
				String testerID = splitList[2];
				Tester t = testerTable.get(testerID);
				if(t != null) {
					t.addBug(); // increase the bug\experience count for this user
				}
				
				str = in.readLine();
			};
		}finally {
			in.close();
		}
	}

	/**
	 * Convince function to take the line from the file and break into individual works removing the "junk"
	 * @param str
	 * @return array of strings
	 */
	private String[] getSplitList(String str) {
		if(str == null || str.isEmpty())
			return null;
		
		return (str.replace("\"","")).split(",");
	}
}
