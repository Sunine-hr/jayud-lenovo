/**
 * @author mfc
 * @description:
 * @date 2020/10/25 15:59
 */
public class Test2 {

    public static void main(String[] args) {

        Integer a = 0;
        long b = 0;
        if(a.equals(b)){
            System.out.println(true);
        }else{
            System.out.println(false);
        }
        System.out.println("-----------");

        if(Long.valueOf(a).equals(b)){
            System.out.println(true);
        }else{
            System.out.println(false);
        }

    }

}
