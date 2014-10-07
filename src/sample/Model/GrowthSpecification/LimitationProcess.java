package sample.Model.GrowthSpecification;

import sample.Model.FileManagement.EmailDBA;
import sample.Model.FileManagement.EmailManagement;
import sample.Model.entities.TableSpace;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jose on 06/10/2014.
 */
public class LimitationProcess {

    public LimitationProcess() {
        loadEmail();
    }

    public static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(2);

    private void checkLimites(){
        EmailManagement em= new EmailManagement();
        String[] receptor = new String[0];

        System.out.println(emailDBA.getFirstNameDBA()+"@"+emailDBA.getSecondNameDBA());
        TableSpace.tableSpaceList.forEach(e->{

            if(GrowthTableContainer.container.get(e.getName()).secondLimit<=e.getUsed()){
                receptor[0]=emailDBA.getFirstNameDBA()+"@"+emailDBA.getSecondNameDBA();
                StringBuilder sb= new StringBuilder();
                sb.append("The database is on red state\n");
                sb.append("The table "+e.getName()+"\n");
                sb.append("Have the max limit "+e.getLimitSecond());
                sb.append(" but the database already use "+e.getUsed());
                sb.append(" so the database will autoextend.");

                em.send("pablomadrigaless@gmail.com",receptor,"The database is on red state!",sb.toString());

            }else if(GrowthTableContainer.container.get(e.getName()).firstLimit<=e.getUsed()){

                receptor[0]=emailDBA.getFirstNameDBA()+"@"+emailDBA.getSecondNameDBA();

                StringBuilder sb= new StringBuilder();
                sb.append("The database is on yellow state\n");
                sb.append("The table "+e.getName()+"\n");
                sb.append("Have the max limit "+e.getLimitSecond());
                sb.append(" but the database already use "+e.getUsed());
                sb.append(" so we recommend to autoextend manually.");

                em.send("pablomadrigaless@gmail.com",receptor,"The database is on yellow state!",sb.toString());
            }
        });
    }

    private void checkCorte(){

        TableSpace.tableSpaceList.forEach(e->{
            if(GrowthTableContainer.container.get(e.getName()).secondLimit<=e.getUsed()){
                System.out.println(e.getName()+" rojo");
            }else if(GrowthTableContainer.container.get(e.getName()).firstLimit<=e.getUsed()){
                System.out.println(e.getName()+" amarillo");
            }

        });
    }

    public void handleThreads(){
        scheduledThreadPool.scheduleWithFixedDelay (() -> {
            checkLimites();
        },1, 10, TimeUnit.MINUTES);
        scheduledThreadPool.scheduleWithFixedDelay (()->{
            checkCorte();
        },10, 15, TimeUnit.MINUTES);
    }
    public void shutdownThreads(){
        scheduledThreadPool.shutdownNow();
    }

    EmailDBA emailDBA;

    private final String pathEmailDBA="src\\FileMonitor\\emailDBA.ser";

    public boolean loadEmail(){
        EmailDBA lf=null;
        try
        {
            FileInputStream fileIn = new FileInputStream(pathEmailDBA);
            System.out.println(fileIn.available());
            if(fileIn.available()>1){
                ObjectInputStream in = new ObjectInputStream(fileIn);
                lf = (EmailDBA) in.readObject();
                in.close();
                fileIn.close();
                emailDBA=lf;

                return true;
            }else{
                emailDBA= new EmailDBA();
                return false;
            }
        }catch(IOException i)
        {
            i.printStackTrace();
            return false;
        }catch(ClassNotFoundException c)
        {
            System.out.println("class not found");
            c.printStackTrace();
            return false;
        }
    }


}
