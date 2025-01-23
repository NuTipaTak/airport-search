package org.example;

import java.util.ArrayList;
import java.util.List;

public class BinaryTree {
    // Внутренний класс для узла дерева
    private class Node {
        Comparable data;
        Node left, right;

        Node(Comparable item) {
            data = item;
            left = right = null;
        }
    }

    private Node root;

    public BinaryTree() {
        root = null;
    }

    // Метод для вставки нового элемента
    public void insert(Comparable value) {
        // Проверка типа элемента перед вставкой
        if (value instanceof String || value instanceof Integer) {
            root = insertRec(root, value);
        } else {
            throw new IllegalArgumentException("Только строки и числа могут быть вставлены.");
        }
    }

    // Рекурсивный метод для вставки нового элемента
    private Node insertRec(Node root, Comparable value) {
        if (root == null) {
            root = new Node(value);
            return root;
        }

        // Сравнение значений для вставки
        if (isLessThan(value, root.data)) {
            root.left = insertRec(root.left, value);
        } else {
            root.right = insertRec(root.right, value);
        }

        return root;
    }

    // Метод для сравнения значений
    private boolean isLessThan(Comparable a, Comparable b) {
        // Сравниваем только если оба элемента одного типа
        if (a.getClass() == b.getClass()) {
            return a.compareTo(b) < 0;
        }
        // Если разные типы, определяем порядок: строки меньше чисел
        return a instanceof String && b instanceof Integer;
    }

    // Метод для получения отсортированных значений
    public List<Comparable> sortedValues() {
        List<Comparable> values = new ArrayList<>();
        inOrderTraversal(root, values);
        return values;
    }

    // Метод для обхода дерева в симметричном порядке
    private void inOrderTraversal(Node root, List<Comparable> values) {
        if (root != null) {
            inOrderTraversal(root.left, values);
            values.add(root.data);
            inOrderTraversal(root.right, values);
        }
    }
    // Метод для получения значений, начинающихся с определенной буквы
    public List<String> getValuesStartingWith(char letter) {
        List<String> result = new ArrayList<>();
        getValuesStartingWithRec(root, letter, result);
        return result;
    }
    // Рекурсивный метод для поиска значений, начинающихся с определенной буквы
    private void getValuesStartingWithRec(Node root, char letter, List<String> result) {
        if (root != null) {
            getValuesStartingWithRec(root.left, letter, result);

            // Проверяем, является ли узел строкой и начинается ли она с указанной буквы
            if (root.data instanceof String && ((String) root.data).toLowerCase().charAt(0) == Character.toLowerCase(letter)) {
                result.add((String) root.data);
            }

            getValuesStartingWithRec(root.right, letter, result);
        }
    }

//    public static void main(String[] args) {
//        BinaryTree tree = new BinaryTree();
//
//        // Вставка строк
//        tree.insert("banana");
//        tree.insert("apple");
//        tree.insert("orange");
//        tree.insert("appфывle");
//        tree.insert("applввывe");
//
//        // Вставка чисел
//        tree.insert(3);
//        tree.insert(1);
//        tree.insert(2);
//
//        // Получение отсортированных значений
//        List<Comparable> sortedValues = tree.sortedValues();
//        System.out.println("Отсортированные значения: " + sortedValues);
//
//        List<String> valuesStartingWithA = tree.getValuesStartingWith('a');
//        System.out.println("Значения, начинающиеся с 'a': " + valuesStartingWithA);
//    }
}
