package serviceInstanceTests;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;

public class CreateHost {

	static VirtualMachineConfigSpec vmConfigSpec = new VirtualMachineConfigSpec();
	public static void main (String args[]) throws RemoteException, MalformedURLException, InterruptedException
	{

		String yourhostname = "vpvhost1vm14.ind.hp.com";
	    String url = "https://" + yourhostname + "/sdk/vimService";
	    System.out.println(url);
	    String user = "administrator";
	    String password = "1chovc*help";
	    Datastore targetDatastore = null;

	   
	    String vmName;
	    String dcname;
	    String host;
	    Integer numVMs;
	    long memorySizeMB = 500;
	    int cpuCount = 1;
	    String guestOsId = "sles10Guest";
	    long diskSizeKB = 1000000;
	    
	    	
	    //get VMname, Data center name, Host name
	    vmName = args[0];
	    numVMs = Integer.valueOf(args[1]);
	    dcname = args[2];
	    host = args[3];
	    
	    
	    System.out.println(host);
	    
	    ServiceInstance si = new ServiceInstance(new URL(url), user, password, true);
	    
	    System.out.println("VMWare session established successfully: " + si.getAboutInfo().name);
	    
	    Datacenter datacenter = (Datacenter) getEntity(si,"Datacenter", dcname);

	    HostSystem targetHost = (HostSystem) getEntity(si,"HostSystem", host);
	    ClusterComputeResource cluster = (ClusterComputeResource) targetHost.getParent();
	    
	    targetDatastore = targetHost.getDatastores()[0];
	    Folder vmFolder = datacenter.getVmFolder();
	    
	    ManagedObjectReference [] datastores;      
	    System.out.println("Creating Virtual Machine in datacenter " + dcname + " and host " + host);
	      
	    HostConnectSpec HostConSpec = new HostConnectSpec();
	    HostConSpec.hostName =  host;
	    HostConSpec.force = true;
	    HostConSpec.password = password;
	    HostConSpec.userName = user;
	    HostConSpec.vimAccountName = user;
	    HostConSpec.vimAccountPassword = password;
	   
	    		  


	  //create VMs
	  
	      for (int count = 1;count<=numVMs;count++)
	      		{
	    	  vmConfigSpec.setName(vmName+"_"+count);
	    	  Task task = vmFolder.createVM_Task(vmConfigSpec, cluster.getResourcePool(), targetHost);
	    	  String result = task.waitForTask();
	    	  if (result.equals(task.SUCCESS)) {System.out.println("Created "+ vmName+"_"+count+" successfully");}
	    	  else {System.out.println(vmName + "_"+count+" creation failed");}
	    	  
	  			}
	    	  
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
