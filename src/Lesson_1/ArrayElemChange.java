package Lesson_1;

import java.util.ArrayList;
import java.util.List;

public class ArrayElemChange<T> {

    private T arr[];
    private int v1;
    private int v2;

    public ArrayElemChange(){
        this.v2 = v2;
        this.v1 = v1;
        this.arr = arr;
    }

    public T[] change(int v1, int v2, T[] arr){

        T arrX;

        arrX = arr[v1];
        arr[v1] = arr[v2];
        arr[v2] = arrX;

        return arr;
    }

    public static void main(String[] args) {

       ArrayElemChange<ArrayList> arrayListMain = new ArrayElemChange<>();
       ArrayList array[] = new ArrayList[3];
       ArrayList resultArray[] = arrayListMain.change(0, 2, array);

       ArrayElemChange<List> listMain = new ArrayElemChange<>();
       List list[] = new List[3];
       list = listMain.change(1, 2, list);
    }
}
