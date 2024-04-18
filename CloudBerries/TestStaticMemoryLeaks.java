import java.util.ArrayList;
import java.util.List;

public class TestStaticMemoryLeaks {
    //TODO: Prøve med og uten bruk av static og test med GC i profiler-tab'en til Intellij 2024 Ultimate:
    static public List<Double> list = new ArrayList<>();

    public void popUlateList(){
        for(int index = 0; index < 100000000; index++){ //100 mill. objekter
            list.add(Math.random());
        }
        System.out.println("Check point 2");
    }

    public static void main(String[] args){
        System.out.println("Check point 1");
        //Lager objekt uten referanse:
        new TestStaticMemoryLeaks().popUlateList();
        System.out.println("Check point 3");
        while(true){}
    }
}
