package serviceInstanceTests;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;


import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;

public class PowerOnVM {


	public static void main (String args[]) throws RemoteException, MalformedURLException, InterruptedException
	{

		
		String yourhostname = "vpvhost1vm14.ind.hp.com";
	    String url = "https://" + yourhostname + "/sdk/vimService";
	    System.out.println(url);
	    String user = "administrator";
	    String password = "1chovc*help";
	   
	    String vmName;

	    	
	    //get VMname
	    vmName = args[0];
	    
	 
	    ServiceInstance si = new ServiceInstance(new URL(url), user, password, true);
	    
	    System.out.println("VMWare session established successfully: " + si.getAboutInfo().name);
	    
		
	    VirtualMachine vm = (VirtualMachine) getEntity(si,"VirtualMachine", vmName);
	    
	    

	    
	  //=====================================================
	  Task task = vm.powerOnVM_Task(null);
	    		
	    String result = task.waitForTask();
	    
	    System.out.println("result = " + result);
	    
	    if (result.equals(task.SUCCESS)) {
	    	
	    	System.out.println(vmName + " is PoweredOn");
	    }
	    	System.out.println("Done");
	    	
	}
	
	private static ManagedEntity getEntity (ServiceInstance si, String type, String val) {
	ManagedEntity entity = null;
	try {
		entity = new InventoryNavigator(si.getRootFolder()).searchManagedEntity(type, val);
	} catch (RemoteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return entity;		
}
	
	

}
