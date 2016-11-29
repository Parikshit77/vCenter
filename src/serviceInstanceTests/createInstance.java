package serviceInstanceTests;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;

public class createInstance {
	
	
	public static void main(String[] args) throws RemoteException, MalformedURLException
	
	{
		String yourhostname = "vpvhost1vm14.ind.hp.com";
	    String url = "https://" + yourhostname + "/sdk/vimService";
	    System.out.println(url);
	    String user = "administrator";
	    String password = "1chovc*help";
	    
	    ServiceInstance si = new ServiceInstance(new URL(url), user, password, true);
	   
	    System.out.println("VMWare session established successfully: " + si.getAboutInfo().name);
	    ManagedEntity hostmanagedEntities = new InventoryNavigator(si.getRootFolder()).searchManagedEntity("ResourcePool", "rp1");
	    
	    ManagedEntity cluster = new InventoryNavigator(si.getRootFolder()).searchManagedEntity("ClusterComputeResource", "LR-0_Cluster-9-vm14");
	    
	  
    	System.out.println(hostmanagedEntities.getName());
    	System.out.println(cluster.getName());

	

	    
	    
	    VirtualMachineConfigSpec vmSpec = new VirtualMachineConfigSpec();
	    
	    
}
	
	
}
