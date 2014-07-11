import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class test{
        private static int executeShellCmd(String shellCmd) throws IOException{
                int success = -1;
                StringBuffer stringBuffer = new StringBuffer();
                BufferedReader bufferedReader = null;
                try {
                        Process pid = null;
                        System.out.println(shellCmd);
                        String[] cmd = new String[2];
                        cmd[0] = "/root/mytool.sh";
                        cmd[1] = "'a b c d e'";
                        pid = Runtime.getRuntime().exec(cmd);
                        if (pid != null) {
                                bufferedReader = new BufferedReader(new InputStreamReader(pid.getInputStream()), 1024);
                                pid.waitFor();
                        } else {
                                String line = null;
                                while (bufferedReader != null &&(line = bufferedReader.readLine()) != null) {
                                        stringBuffer.append(line).append("\r\n");
                                }
                        }
                } catch (Exception e) {
                        System.out.println("Exception:" + e);
                } finally {
                        String line = "";
                        while((line = bufferedReader.readLine()) != null){
                                System.out.println(line);
                                if(line != null && line.length() > 0){
                                        success = 0;
                                        //break;
                                }
                        }
                }

                return success;
        }
        public static void main(String []args){
                System.out.println("aa");
                try{
                        System.out.println(executeShellCmd("/root/mytool.sh 'a b c d'"));
                        //System.out.println(executeShellCmd("/usr/local/whistle/configd/bin/configtool set openfire.jive.xmpp.client.auth.provider.datasync1.mytest4 'abc\\ def\\ hig'"));
                }catch(Exception e){
                        System.out.println("Exception:" + e);
                }
        }
}
