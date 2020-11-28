package cn.hobom.mobile.datacollector.trace;

import cn.hobom.mobile.datacollector.util.ShellUtils;
import cn.hobom.mobile.datacollector.util.ShellUtils.*;

/**
 * Created by WWT on 2019/8/29.
 */

public class GetLocalTrace {

    private String apkpath;
    private String apkname;
    private String packagename;

    public GetLocalTrace() {
    }

    public GetLocalTrace(String apkpath, String apkname, String packagename) {
        this.apkpath = apkpath;
        this.apkname = apkname;
        this.packagename = packagename;
    }

    public String execShell(){
//        String[] command = {"strace -o "+apkname+".txt -T -ttt -f -e trace=all -p 71","ps |grep fhasjasdjlkf"};
//        String[] command = {"ps"};
//        String[] command = {"monkey -p " + packagename + " 100"};
//        String[] command = {"pm uninstall " + packagename};
//        String[] command = {"ls"};
//        CommandResult commandResult = null;
//        commandResult = ShellUtils.execCommand(command,false);
//        commandResult = ShellUtils.execCommand(stracePIDCommand,true);
        /*
        BufferedReader br = null;
        try {
            Process process1 = Runtime.getRuntime().exec(command);
            br = new BufferedReader(new InputStreamReader(process1.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null){
                sb.append(line+"\n");
            }
           *//* String line;
            StringBuilder sb = new StringBuilder();
            Process process2 = Runtime.getRuntime().exec("su -c ls");
            br = new BufferedReader(new InputStreamReader(process2.getInputStream()));
            while ((line = br.readLine()) != null){
                sb.append(line);
            }*//*

            result = ""+sb;

        } catch (IOException e) {
            e.printStackTrace();
            result = "执行命令失败";
            return result;
        }*/
//        return commandResult.errorMsg;
        /*if (commandResult.successMsg == null&commandResult.errorMsg == null){
            result = "该命令没有返回值";
        } else if (commandResult.successMsg == null) {
            result = commandResult.errorMsg;
        }else {
            result = commandResult.successMsg;
        }*/

        /*String[] PIDInfo = commandResult.successMsg.split("");
        for (int i = 0; i < PIDInfo.length; i++) {
            result = result + " " + PIDInfo[i];
            if (i % 7 == 0){
                result = result + "\n";
            }
        }*/
//        String[] PIDInfo = commandResult.successMsg.split("\n");
//        String PPID = GetPPID(PIDInfo);
        /*String killCommand = "kill -9 " + GetPID(packagename);
        CommandResult commandResult1 = ShellUtils.execCommand(killCommand, true);*/
//        String PPID = GetPPID();
//        String commandStrace = "monkey -p " + packagename + " 100";//"strace –c –f –e trace=all –p " + PPID;
//        commandResult = ShellUtils.execCommand(commandStrace,true);
//        String killCommand = "kill -9 " + GetPID("strace");
//        ShellUtils.execCommand(killCommand,true);
//        String thisPID = GetPID("cn.hobom.mobile.datacollector");
        StringBuilder sb = new StringBuilder();

//        String PPID = GetPPID();
//        String enableCommand = "setenforce 0";
//        String straceCommand = "strace -o " + apkname + ".txt -T -ttt -f -e trace=all -p " + PPID;
        String monkeyCommand = "monkey -p " + packagename + " 1000";

        CommandResult commandResult2 = ShellUtils.execCommand(monkeyCommand, true);

        sb.append(commandResult2.successMsg + "\n");

        /*String stracePID = GetPID("strace");
        sb.append(stracePID + "\n");

        ShellUtils.execCommand("am force-stop strace",true);
        CommandResult commandResult3 = ShellUtils.execCommand("kill -9 " + stracePID, true);
        sb.append(commandResult3.successMsg);

        CommandResult commandResult = ShellUtils.execCommand("ps", false);

        sb.append(commandResult.successMsg);*/

        return sb.toString();
    }

    public static String GetPPID(){
        String command = "ps";
        String PPID = null;
        String IDstr = null;
        CommandResult commandResult = null;

        commandResult = ShellUtils.execCommand(command,false);

        String[] PIDInfo = commandResult.successMsg.split("\n");

        for (int i = 0; i < PIDInfo.length; i++) {
            if (PIDInfo[i].substring(PIDInfo[i].length()-6,PIDInfo[i].length()).equals("zygote")){
                IDstr = PIDInfo[i];
                break;
            }
        }
        int flag = 0;
        for(int i = 0;i<IDstr.length();i++){
            int j = i + 1;
            if((IDstr.charAt(i)==' ')&&(IDstr.charAt(j)!=' ')){
                PPID = String.valueOf(IDstr.charAt(j));
                for(int k = j + 1;k<IDstr.length();k++) {
                    if ((IDstr.charAt(j) != ' ') && (IDstr.charAt(k) == ' ')) {
                        break;
                    } else {
                        PPID = PPID + IDstr.charAt(k);
                    }
                }
                flag++;
            }
            if(flag == 1){
                break;
            }
        }
        return PPID;
    }

    public static String GetPID(String packagename){
        String PID = null;
        String PIDInfo = null;
        String command = "ps |grep " + packagename;
//        String command = "cmd /c adb shell \"ps |grep strace\"";
        CommandResult commandResult = ShellUtils.execCommand(command, false);
        PIDInfo = commandResult.successMsg;
        PID = SearchID(PIDInfo)[0];

        return PID;
    }

    public static String KillPID(String PPID,String thisPID){
        StringBuilder sb = new StringBuilder();

        String [] ID = {null,null};
        String[] PIDInfo = null;
        String commandPID = "ps";
        String killCommand = "kill -9 ";

        PIDInfo = ShellUtils.execCommand(commandPID,false).successMsg.split("\n");
        String line = null;
        for (int i = 0; i < PIDInfo.length; i++) {
            line = PIDInfo[i];
            if(line.length() == 0)
                continue;
            if(line.substring(0,4).equals("USER"))
                continue;
            ID = SearchID(line);
            /*sb.append(ID[0]+"");
            sb.append(" ");*/
            if(ID[1].equals(PPID)){
                if(line.substring(0,6).equals("system"))
                    continue;
                if (ID[0] == thisPID)
                    break;
                CommandResult commandResult = ShellUtils.execCommand(killCommand + ID[0], true);
                sb.append(commandResult.successMsg);
            }
        }

        return sb.toString();
    }

    public static String[] SearchID(String IDstr){
        String [] ID = {null,null};

        int flag = 0;
        for(int i = 0;i<IDstr.length();i++){
            int j = i + 1;

            if((IDstr.charAt(i)==' ')&&(IDstr.charAt(j)!=' ')){
                ID[flag] = String.valueOf(IDstr.charAt(j));
                for(int k = j + 1;k<IDstr.length();k++) {
                    if ((IDstr.charAt(j) != ' ') && (IDstr.charAt(k) == ' ')) {
                        break;
                    } else {
                        ID[flag] = ID[flag] + IDstr.charAt(k);
                    }
                }
                flag++;
            }
            if(flag == 2)
                break;
        }
        return ID;
    }
}
