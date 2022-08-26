package mt;

import java.util.*;

public class Zuhe {
    public static void main(String[] args) {

        List<Integer> arrayList = Arrays.asList(1,3,5);

        handler(arrayList,arrayList.size());
//        for (int f = 0; f < arrayList.size(); f++) {
//            List<Integer> headerList = new LinkedList(arrayList);
//            Integer header = headerList.get(f);
//            headerList.remove(f);
//
//            for (int i = 0; i < 3; i++) {
//                List<Integer> list = new LinkedList(headerList);
//                int first = list.get(i);
//                list.remove(i);
//                for (int j = 0; j < 2; j++) {
//                    List<Integer> secondList = new LinkedList<>(list);
//                    Integer second = secondList.get(j);
//                    secondList.remove(j);
//                    System.out.println(header+""+first+""+second+""+secondList.get(0));
//                }
//            }
//        }
    }


    static void handler(List<Integer> arrayList,int offset){

        for (int index = 0; index < arrayList.size(); index++) {
            List<Integer> headerList = new LinkedList(arrayList);
            Integer header = headerList.get(index);
            headerList.remove(index);

            System.out.printf(header+"");

            if(offset==2){
                System.out.printf(headerList.get(0)+"");
                System.out.println();
            }else {
                List<Integer> list = new LinkedList(headerList);
                handler(list,arrayList.size()-1);
            }
        }
    }
}
