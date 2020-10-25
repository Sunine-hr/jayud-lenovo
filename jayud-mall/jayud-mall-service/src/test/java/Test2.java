/**
 * @author mfc
 * @description:
 * @date 2020/10/25 15:59
 */
public class Test2 {

    public static void main(String[] args) {

        //不同数据类型的值比较是否相等
        Integer a = 0;
        long b = 0;
        if(a.equals(b)){
            System.out.println(true);
        }else{
            System.out.println(false);//为false
        }
        System.out.println("-----------");

        if(Long.valueOf(a).equals(b)){
            System.out.println(true);//为true
        }else{
            System.out.println(false);
        }

    }

}
