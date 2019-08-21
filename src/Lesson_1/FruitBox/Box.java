package Lesson_1.FruitBox;

import java.util.ArrayList;

public class Box<T extends Fruit> {
    private T frut;
    private ArrayList<T> frutBox = new ArrayList<T>();

    public Box(T frut){
        this.frut = frut;
    }

    public ArrayList getFrut() {
        return frutBox;
    }

    public void addFrut(int quantity){
        for(int i = 0; i < quantity; i++){
            frutBox.add(frut);
        }
    }

    public int getWeightBox(){
        int result = 0;
        for(T o: frutBox){
            result += o.getWeight();
        }

        return result;
    }

    public boolean compare(Box<?> anotherBox){
        boolean result = false;

        if( this.getWeightBox() == anotherBox.getWeightBox()) result = true;

        return result;
    }

    public void sprinkle (Box<T> anotherBox){
        this.frutBox.addAll(anotherBox.frutBox);
        anotherBox.frutBox.clear();
    }

    public static void main(String[] args) {

        Box<Apple> appleBox1 = new Box<>(new Apple());
        Box<Apple> appleBox2 = new Box<>(new Apple());
        Box<Orange> orangeBox = new Box<>(new Orange());

        appleBox1.addFrut(10);
        orangeBox.addFrut(6);
        appleBox2.addFrut(5);

        if(appleBox1.compare(orangeBox)){
            System.out.println("Вес коробок одинаковы.");
        }else {
            System.out.println("Вес коробок разный.");
        }
        appleBox1.sprinkle(appleBox2);

        System.out.println(appleBox1.getWeightBox());
    }

}
