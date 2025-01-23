package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.*;

class SearchResult {
    private String search;
    private List<String> result;
    private long time;

    public SearchResult(String search, List<String> result, long time) {
        this.search = search;
        this.result = result;
        this.time = time;
    }
}

class Output {
    private long initTime;
    private List<SearchResult> result;

    public Output(long initTime, List<SearchResult> result) {
        this.initTime = initTime;
        this.result = result;
    }
}

public class App 
{
    public static void main(String[] args) {
        String csvFile = null;
        String inputFile = null;
        String outputFile = null;

        int keyColumnIndex = -1; // Индекс колонки для ключа (например, 2 для третьего столбца)
        int valueColumnIndex = 0; // Индекс колонки для значения (например, 0 для первого

        long startTime = System.currentTimeMillis(); // Запоминаем время начала

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--data":
                    if (i + 1 < args.length) {
                        csvFile = args[++i]; // Получаем путь к CSV-файлу
                    }

                    break;
                case "--indexed-column-id":
                    if (i + 1 < args.length) {
                        keyColumnIndex = Integer.parseInt(args[++i]) - 1; // Получаем индекс колонки
                    }

                    break;
                case "--input-file":
                    if (i + 1 < args.length) {
                        inputFile = args[++i]; // Получаем путь к входному файлу
                    }

                    break;
                case "--output-file":
                    if (i + 1 < args.length) {
                        outputFile = args[++i]; // Получаем путь к входному файлу
                    }

                    break;
                default:
                    System.out.println("Неизвестный аргумент: " + args[i]);
                    return;
            }
        }
        if(csvFile==null){
            csvFile = "C:\\Users\\TeaKin\\Documents\\GitHub\\61464198-airports\\files\\airports.csv";
            keyColumnIndex = 1;
            inputFile = "C:\\Users\\TeaKin\\Documents\\GitHub\\61464198-airports\\files\\input1.txt";
            outputFile = "C:\\Users\\TeaKin\\Documents\\GitHub\\61464198-airports\\files\\result1.json";
        }


        Map<String, List<String>> hashMap = new HashMap<>();
        BinaryTree tree = new BinaryTree();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "UTF-8")))  {
            String line;


            while ((line = br.readLine()) != null) {

                String[] values = line.split(",");


                if (values.length > Math.max(keyColumnIndex, valueColumnIndex)) {

                    String key = values[keyColumnIndex].replaceAll("\"","") ;
                    tree.insert(key);
                    String value = values[valueColumnIndex];
                    if(!hashMap.containsKey(key)){
                        hashMap.put(key,new ArrayList<String>());
                    }
                    hashMap.get(key).add(value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        long initTime = endTime - startTime;

        List<SearchResult> searchResults = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"))) {

            String prefix ;

            while ((prefix = br.readLine()) != null) {
                startTime = System.currentTimeMillis();
                List<String> matchedRows = new ArrayList<String>();

                List<String> valuesStartingWithA = tree.getValuesStartingWith(prefix.charAt(0));

                Map<String,Integer> uniqueValue = new HashMap<>();
                for (String value : valuesStartingWithA) {

                    if(!uniqueValue.containsKey(value)){
                        uniqueValue.put(value,1);
                    }else{continue;}
                    if (value.startsWith(prefix)) {
                        //System.out.println("value: "+value+", num: "+hashMap.get(value));
                        matchedRows.addAll(hashMap.get(value));




                    }
                }

                endTime = System.currentTimeMillis(); // Запоминаем время окончания
                long duration = endTime - startTime;
                searchResults.add(new SearchResult(prefix, matchedRows, duration));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Output output = new Output(initTime, searchResults);

        try (FileWriter writer = new FileWriter(outputFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(output, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
