package pactera.tf.chat;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class Test implements quicksort {

    private String Str;
    public static void main(String[] args) throws Exception{
        String aimStr="{1:F21EIBCCNB0AXXX2695132810}{4:{177:2009161049}{451:0}}{1:F01EIBCCNB0AXXX2695132810}{2:O2020523181113BKCHUS33XXX05101903961811131824N}{3:{108:S015582ICP111318}{121:aa8f0e15-0fff-47ce-aa73-1aec469662f8}}{4:\n" +
                ":20:TEST2020916001\n" +
                ":32A:200915HKD3500000000,\n" +
                ":52A:COMMHKHHXXX\n" +
                ":58D:/10000000319\n" +
                "WANG WEIADD. 2392 2949 0086 0966\n" +
                " 0968\n" +
                ":72:/INS/DHBLBDDHTOD\n" +
                "/INS/DHBLBDDH\n" +
                "/INS/SCBLUS30XXX\n" +
                "/BNF/FEES DEDUCTED  0, USD  100.00\n" +
                "//FEE DEDUCTED\n" +
                "-}{5:{MAC:00000000}{CHK:A0EF2815B3F2}{TNG:}}{S:{SAC:}{COP:P}}";
        Object target = new Test();
        Method method = Test.class.getDeclaredMethod("setStr",String.class);
        Object po = method.invoke(target, new Object[]{aimStr});
        System.out.println(target.toString());
    }

    @Override
    public String toString() {
        return "Test{" +
                "Str='" + Str + '\'' +
                '}';
    }

    public String getStr() {
        return Str;
    }

    public void setStr(String str) {
        Str = str;
    }
}
