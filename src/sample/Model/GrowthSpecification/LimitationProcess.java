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




        System.out.println(emailDBA.getHeaderEmailDBA()+"@"+emailDBA.getBodyEmailDBA());
        TableSpace.tableSpaceList.forEach(e->{

            if(GrowthTableContainer.container.get(e.getName()).secondLimit<=e.getUsed()){
                String aux=emailDBA.getHeaderEmailDBA()+"@"+emailDBA.getBodyEmailDBA();
                String[] array={aux};
                String email=em.getEmail()+"@gmail.com";
                StringBuilder sb= new StringBuilder();
                sb.append("Ms/Sr "+emailDBA.getFirstNameDBA()+" "+emailDBA.getSecondNameDBA()+"\n");
                sb.append("The database is on red state\n");
                sb.append("The table "+e.getName()+"\n");
                sb.append("Have the max limit "+e.getLimitSecond());
                sb.append(" but the database already use "+e.getUsed());
                sb.append(" so the database will autoextend.");
                //TODO AUTOEXTEND ocupo el nombre del FILE!

                if(em.send(email,array,"The database is in red state!",sb.toString())){
                    System.out.println("send");
                }else{
                    System.out.println("error sending");
                }

            }else if(GrowthTableContainer.container.get(e.getName()).firstLimit<=e.getUsed()){

                String aux=emailDBA.getHeaderEmailDBA()+"@"+emailDBA.getBodyEmailDBA();
                String[] array={aux};
                String email=em.getEmail()+"@gmail.com";

                StringBuilder sb= new StringBuilder();
                sb.append("Ms/Sr "+emailDBA.getFirstNameDBA()+" "+emailDBA.getSecondNameDBA()+"\n");
                sb.append("The database is on yellow state\n");
                sb.append("The table "+e.getName()+"\n");
                sb.append("Have the max limit "+e.getLimitSecond());
                sb.append(" but the database already use "+e.getUsed());
                sb.append(" so we recommend to autoextend manually.");
                System.out.println(em.getEmail());
                if(em.send(email,array,"The database is in yellow state!",sb.toString())){
                    System.out.println("send");
                }else{
                    System.out.println("error sending");
                }
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
