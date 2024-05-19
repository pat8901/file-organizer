package com.patco.hashmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.springframework.boot.context.properties.bind.validation.ValidationBindHandler;

public class HashMap<K, V> {
    public Node<K, V>[] map;
    public double load_factor;
    public int current_size;
    public int capacity;

    public HashMap(int capacity) {
        this.current_size = 0;
        this.capacity = capacity;
        this.load_factor = 0.7;
        this.map = new Node[capacity];
    }

    public class Node<K, V> {
        private Node<K, V> next;
        private K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }

    public int hash(K key) {
        return key.hashCode();
    }

    public double getCurrentLoad() {
        double load = (double) this.current_size / this.capacity;
        return load;
    }

    public Node<K, V>[] reHash(Node<K, V>[] map) {
        int new_capacity = this.capacity * 2;
        Node<K, V>[] new_map = new Node[new_capacity];

        for (int i = 0; i < map.length; i++) {
            if (map[i] != null) {
                Node<K, V> temp = map[i];

                while (temp != null) {
                    int hash = hash(temp.key);
                    int index = Math.abs(hash % new_capacity);
                    Node<K, V> new_temp = new_map[index];

                    if (new_temp == null) {
                        new_map[index] = new Node<>(temp.key, temp.value);
                    } else {
                        while (new_temp.next != null) {
                            new_temp = new_temp.next;
                        }
                        Node<K, V> node = new Node<>(temp.key, temp.value);
                        new_temp.next = node;
                    }
                    // System.out.println("--------------------------");
                    // System.out.println("key: " + temp.key);
                    // System.out.println("hash: " + hash);
                    // System.out.println("index:" + index);
                    // System.out.println("current size: " + current_size);
                    // System.out.println("load: " + getCurrentLoad());
                    temp = temp.next;
                }
            }
        }
        this.capacity = new_capacity;
        return new_map;
    }

    // TODO: Test to make sure how to handle duplicates
    public boolean put(K key, V value) {
        if (getCurrentLoad() > 0.7) {
            // System.out.println("Im too big, rehash!");
            this.map = reHash(map);
        }

        int hash = hash(key);
        int index = Math.abs(hash % this.capacity);
        Node<K, V> temp = map[index];

        // System.out.println("---------------------------");
        // System.out.println("hash: " + hash + " index:" + index);

        if (temp == null) {
            map[index] = new Node<>(key, value);
            this.current_size++;
            // System.out.println(
            // "current size:" + this.current_size + " capacity:" + this.capacity + " load:"
            // + getCurrentLoad());
            return true;
        } else {
            while (temp.next != null) {
                temp = temp.next;
            }
            Node<K, V> node = new Node<>(key, value);
            temp.next = node;
            this.current_size++;
            // System.out.println(
            // "current size:" + this.current_size + " capacity:" + this.capacity + " load:"
            // + getCurrentLoad());
            return true;
        }
    }

    public String get(K key) {
        int hash = hash(key);
        int index = Math.abs(hash % this.capacity);
        Node<K, V> temp = this.map[index];

        while (temp != null) {
            if (!temp.key.equals(key)) {
                temp = temp.next;
            } else {
                return temp.value.toString();
            }
        }
        return "misc";
    }

    // TODO: I need to learn more about generics to implement this as a method.
    // public boolean populateHashMap() throws FileNotFoundException {
    // File file = new File("file-properties.txt");
    // Scanner sc = new Scanner(file);
    // StringBuilder value_property = new StringBuilder();
    // int count = 1;

    // while (sc.hasNextLine()) {
    // String line = sc.nextLine();
    // if (line.startsWith("*")) {
    // value_property.setLength(0);
    // String[] line_tokens = line.split("\\*");
    // value_property.append(line_tokens[1]);
    // System.out.println(value_property + " on line " + count);
    // } else {
    // System.out.println(line);
    // put(value_property.toString(), line);

    // }
    // count++;
    // }

    // sc.close();
    // return false;
    // }

    public void printHashMap() {
        System.out.println("=======================================");
        for (int i = 0; i < this.capacity; i++) {
            Node<K, V> temp = map[i];
            System.out.print(i + ": ");
            if (temp == null) {
                System.out.println("null");
                continue;
            }
            while (temp != null) {
                System.out.print("{" + temp.key + "," + temp.value + "} " + "-> ");
                temp = temp.next;
            }
            System.out.println("null");
        }
    }

    public static void main(String args[]) throws FileNotFoundException {
        HashMap<String, String> my_map = new HashMap<>(5);

        File file = new File("file-properties.txt");
        Scanner sc = new Scanner(file);
        StringBuilder value_property = new StringBuilder();
        // int count = 1;

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("*")) {
                value_property.setLength(0);
                String[] line_tokens = line.split("\\*");
                value_property.append(line_tokens[1]);
                // System.out.println(value_property + " on line " + count);
            } else {
                // System.out.println(line);
                my_map.put(line, value_property.toString());

            }
            // count++;
        }
        sc.close();

        // my_map.printHashMap();

        String key = "flac";
        String result = my_map.get(key);
        System.out.println(key + "'s value is " + result);
    }
}
