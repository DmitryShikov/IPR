package ru.alfabank.ipr.profiller;

import java.util.ArrayList;
import java.util.Random;

public class LoadSimulatorForProfiler {
    private static final ArrayList<byte[]> cache = new ArrayList<>();
    private static final Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== ДОЛГОИГРАЮЩАЯ ПРОГРАММА ===");
        System.out.println("Будет работать 10 минут, постепенно нагружая память\n");

        for (int minute = 0; minute < 10; minute++) {
            System.out.println("Минута " + (minute + 1) + "...");

            // Каждую минуту добавляем 100 MB
            for (int i = 0; i < 100; i++) {
                byte[] data = new byte[1024 * 1024]; // 1 MB
                data[0] = (byte) i;
                cache.add(data);
            }

            System.out.println("  Кэш: " + cache.size() + " MB");

            // Иногда удаляем старые объекты
            if (minute > 3) {
                int toRemove = random.nextInt(50);
                for (int i = 0; i < toRemove; i++) {
                    if (!cache.isEmpty()) {
                        cache.remove(0);
                    }
                }
                System.out.println("  Удалили " + toRemove + " MB, осталось: " + cache.size() + " MB");
            }

            // Ждём минуту
            Thread.sleep(60000);
        }

        System.out.println("\nГотово! Всего создано объектов: " + cache.size() + " MB");
    }
}