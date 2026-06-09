package ru.alfabank.ipr.GC;

import java.util.*;
import java.util.concurrent.*;
import java.util.*;

public class LoadSimulator {
    private static final List<byte[]> oldGen = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== GC PROBLEM MAKER ===");
        System.out.println("Создаём 200 объектов в Old Gen...");

        // Создаём базу в Old Gen
        for (int i = 0; i < 200; i++) {
            oldGen.add(new byte[1024 * 200]); // 200 KB = 40 MB всего
        }

        System.out.println("Готово! Начинаем убивать GC...\n");

        for (int cycle = 0; cycle < 500; cycle++) {
            // 1. Создаём тонну временных объектов (давление на Eden)
            for (int i = 0; i < 10000; i++) {
                byte[] trash = new byte[1024]; // 1 KB
                trash[0] = (byte) i;
            }

            // 2. Добавляем новые объекты в Old Gen
            if (cycle % 3 == 0) {
                oldGen.add(new byte[1024 * 200]); // +200 KB каждые 3 цикла
            }

            // 3. Иногда "ломаем" старые объекты (мусор в Old)
            if (cycle % 7 == 0 && oldGen.size() > 50) {
                int index = cycle % oldGen.size();
                oldGen.set(index, null);
            }

            // 4. Выводим статистику
            if (cycle % 20 == 0) {
                int alive = countAlive(oldGen);
                System.out.println("Цикл " + cycle +
                        ": объектов=" + oldGen.size() +
                        ", живых=" + alive +
                        ", память=" + (alive * 200) + "KB");
            }

            Thread.sleep(5);
        }
    }

    private static int countAlive(List<byte[]> list) {
        int count = 0;
        for (byte[] b : list) {
            if (b != null) count++;
        }
        return count;
    }
}