package serviceInstanceTests;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;


import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;

public class CreateVM {


	public static void main (String args[]) throws RemoteException, MalformedURLException, InterruptedException
	{

		String yourhostname = "vpvhost1vm14.ind.hp.com";
	    String url = "https://" + yourhostname + "/sdk/vimService";
	    System.out.println(url);
	    String user = "administrator";
	    String password = "1chovc*help";
	    Datastore targetDatastore = null;
	    String host = "vpvscale2.ind.hp.com";
	    String dcname = "LR-0_DataCenter-vpvhost1vm14";
	   
	    String vmName;
	    long memorySizeMB = 500;
	    int cpuCount = 1;
	    String guestOsId = "sles10Guest";
	    long diskSizeKB = 1000000;
	    
	    	
	    //get VMname
	    //vmName = args[0];
	    vmName = "Test_VM201";
	    
	 
	    ServiceInstance si = new ServiceInstance(new URL(url), user, password, true);
	    
	    System.out.println("VMWare session established successfully: " + si.getAboutInfo().name);
	    
		//Datacenter datacenter = (Datacenter) new InventoryNavigator(si.getRootFolder()).searchManagedEntity("Datacenter", dcname);
	    Datacenter datacenter = (Datacenter) getEntity(si,"Datacenter", dcname);
	    ResourcePool respool = (ResourcePool) getEntity(si,"ResourcePool", "rp1");
	    HostSystem targetHost = (HostSystem) getEntity(si,"HostSystem", host);
	
	    
	    targetDatastore = targetHost.getDatastores()[0];
	    Folder vmFolder = datacenter.getVmFolder();
	    
	    ManagedObjectReference [] datastores;      
	    System.out.println("Creating Virtual Machine");
	    
	    VirtualDeviceConfigSpec[] deviceConfigSpec = new VirtualDeviceConfigSpec[2];
	      
	    int diskCtlrKey = 1;      
	    VirtualDeviceConfigSpec scsiCtrlSpec = new VirtualDeviceConfigSpec();
	      scsiCtrlSpec.setOperation(VirtualDeviceConfigSpecOperation.add);
	      VirtualLsiLogicController scsiCtrl = new VirtualLsiLogicController();
	      scsiCtrl.setBusNumber(0);            
	      scsiCtrl.setKey(diskCtlrKey);
	      scsiCtrl.setSharedBus(VirtualSCSISharing.noSharing);      
	      scsiCtrlSpec.setDevice(scsiCtrl);    
	      
	      
	      deviceConfigSpec[0]=scsiCtrlSpec;      
	      
	      VirtualDeviceConfigSpec diskSpec1 = new VirtualDeviceConfigSpec();            
	      diskSpec1.setFileOperation(VirtualDeviceConfigSpecFileOperation.create);
	      diskSpec1.setOperation(VirtualDeviceConfigSpecOperation.add);
	      
	      VirtualDisk disk1 =  new VirtualDisk();
	      VirtualDiskFlatVer2BackingInfo diskfileBacking1 = new VirtualDiskFlatVer2BackingInfo();      
	      diskfileBacking1.setFileName("["+targetDatastore.getName()+"]");
	      diskfileBacking1.setDiskMode("persistent");       
	      diskfileBacking1.setThinProvisioned(true); 
	      
	      disk1.setKey(0);
	      disk1.setControllerKey(diskCtlrKey); 
	      disk1.setUnitNumber(new Integer(0));
	      disk1.setBacking(diskfileBacking1);
	      disk1.setCapacityInKB(diskSizeKB);
	      
	      diskSpec1.setDevice(disk1);
	      deviceConfigSpec[1]=diskSpec1;
	      
	      VirtualMachineConfigSpec vmConfigSpec = new VirtualMachineConfigSpec();
	      VirtualMachineFileInfo vmfi = new VirtualMachineFileInfo();
	      vmfi.setVmPathName("["+targetDatastore.getName()+"]");
	      vmConfigSpec.setFiles(vmfi);
	      vmConfigSpec.setDeviceChange(deviceConfigSpec);      
	      vmConfigSpec.setName(vmName);      
	      vmConfigSpec.setAnnotation("VirtualMachine Annotation");
	      vmConfigSpec.setMemoryMB(new Long((256)));
	      vmConfigSpec.setNumCPUs(cpuCount);
	      vmConfigSpec.setGuestId(guestOsId); 

	      	         
	      
	  //=====================================================
	  Task task = vmFolder.createVM_Task(vmConfigSpec, respool, targetHost);

	    String result = task.waitForTask();
	    
	    System.out.println("result = " + result);
	    
	    if (result.equals(task.SUCCESS)) {
	    	
	    	System.out.println("Created");
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
