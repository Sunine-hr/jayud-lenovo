public class Test {

    @org.junit.Test
    public void test1(){

        Object o = null;

        boolean equals = o.equals(null);
        System.out.println(equals);

        String s = o != null ? o.toString() : null;
        System.out.println(s);

    }
}
