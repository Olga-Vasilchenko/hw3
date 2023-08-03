package org.example;

import java.util.Iterator;

public class HashMap<K, V> implements Iterable<HashMap<K, V>.Entity> {
    private static final int INIT_BUCKET_COUNT = 16;
    private static final double LOAD_FACTOR = 0.5; // 50%
    private int size;
    private Bucket[] buckets;

    class Entity {
        K key;
        V value;

        @Override
        public String toString() {
            return key.toString() + ": " + value.toString();
        }
    }
    class Bucket<K, V> {
        Node head;
        class Node {
            Node next;
            Entity nodeValue;
        }
        public V add(Entity entity) {
            Node node = new Node();
            node.nodeValue = entity;
            if (head == null) {
                head = node;
                return null;
            }
            Node currentNode = head;
            while (true) {
                if (currentNode.nodeValue.key.equals(entity.key)) {
                    V buf = (V) currentNode.nodeValue.value;
                    currentNode.nodeValue.value = entity.value;
                    return buf;
                }
                if (currentNode.next != null) {
                    currentNode = currentNode.next;
                } else {
                    currentNode.next = node;
                    return null;
                }
            }
        }
    }
    private int calculateBucketIndex(K key) {
        return Math.abs(key.hashCode()) % buckets.length;
    }
    private void recalculate() {
        size = 0;
        Bucket<K, V>[] old = buckets;
        buckets = new Bucket[old.length * 2];
        for (int i = 0; i < old.length; i++) {
            Bucket<K, V> bucket = old[i];
            if (bucket != null) {
                Bucket.Node node = bucket.head;
                while (node != null) {
                    put((K) node.nodeValue.key, (V) node.nodeValue.value);
                    node = node.next;
                }
            }
            old[i] = null;
        }
    }
    public V put(K key, V value) {
        if (buckets.length * LOAD_FACTOR <= size) recalculate();
        int index = calculateBucketIndex(key);
        Bucket bucket = buckets[index];
        if (bucket == null) {
            bucket = new Bucket();
            buckets[index] = bucket;
        }
        Entity entity = new Entity();
        entity.key = key;
        entity.value = value;
        V ret = (V) bucket.add(entity);
        if (ret == null) {
            size++;
        }
        return ret;
    }
    public HashMap() {
        this(INIT_BUCKET_COUNT);
    }
    public HashMap(int initCount) {
        buckets = new Bucket[initCount];
    }

    @Override
    public Iterator<Entity> iterator() {
        return new HashMapIterator();
    }
    public class HashMapIterator implements Iterator<Entity> {
        int bucketIndex;
        Bucket<K, V>.Node currentNode;
        public HashMapIterator() {
            bucketIndex = 0;
            while (bucketIndex < buckets.length && (buckets[bucketIndex] == null || buckets[bucketIndex].head == null)) {
                bucketIndex++;
            }
            if (bucketIndex >= buckets.length) currentNode = null;
            else currentNode = buckets[bucketIndex].head;
        }

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public Entity next() {
            if (!hasNext()) return null;
            Entity output = currentNode.nodeValue;
            currentNode = currentNode.next;
            if (currentNode == null) {
                bucketIndex++;
                while (bucketIndex < buckets.length && (buckets[bucketIndex] == null || buckets[bucketIndex].head == null)) {
                    bucketIndex++;
                }
                if (bucketIndex < buckets.length) currentNode = buckets[bucketIndex].head;
            }
            return output;
        }
    }

    public static void main(String[] args) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Карине", "Геворгян");
        hashMap.put("Клара", "Новикова");
        hashMap.put("Ирина", "Муравьева");
        hashMap.put("Ксения", "Стриж");
        hashMap.put("Иван", "Янковский");
        hashMap.put("Екатерина", "Климова");
        hashMap.put("Петр", "Красилов");
        hashMap.put("Даниил", "Страхов");
        hashMap.put("Никита", "Ефремов");
        hashMap.put("Олег", "Даль");
        hashMap.put("Татьяна", "Доронина");
        hashMap.put("Лада", "Муратова");
        hashMap.put("Наталья", "Кустинская");

        hashMap.forEach(entity -> System.out.println(entity));
    }
}
