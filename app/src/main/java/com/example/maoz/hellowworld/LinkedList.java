package com.example.maoz.hellowworld;


/**
 * Created by ZEPTIMUS on 10/31/2014.
 */
public class LinkedList {
    private Node head;
    private int listCount;

    public  LinkedList(){ //เรียกเวลาสร้างออบเจ็กต์ใหม่
        head = new Node(null);
        listCount = 0;
    }

    public void add(Object data){
        Node temp = new Node(data); // เพิ่มข้อมูล
        Node current = head;
        while (current.getNext()!=null){
            current = current.getNext();
        }
        current.setNext(temp);
        listCount++;
    }
    public void addAt(Object data,int index){
        Node temp = new Node(data);
        Node current = head;
        for (int i = 1; i < index && current.getNext() != null ;i++){
            current = current.getNext();
        }
        temp.setNext(current.getNext());
        current.setNext(temp);
        listCount++;
    }
    public Object getAt(int index){
        if (index <= 0) return null;
        Node current = head.getNext();
        for (int i = 1; i < index; i++){
            if (current.getNext() == null) return null;
            current = current.getNext();
        }
        return current.getData();
    }
    public boolean removeAt(int index){
        if (index < 1 || index > size()) return false;
        Node current = head;
        for (int i = 1; i < index; i++){
            if (current.getNext()==null) return false;
            current = current.getNext();
        }
        current.setNext(current.getNext().getNext());
        listCount++;
        return true;
    }
    public int size(){
        return listCount;
    }
    public String toString(){
        Node current = head.getNext();
        String output = "";
        while (current!=null){
            output += "["+current.getData().toString()+"]";
            current = current.getNext();
        }
        return output;
    }
}
