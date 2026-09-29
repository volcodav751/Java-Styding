package org.example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static final Path HISTORY_FILE =
            Paths.get("Історія замовлень.txt");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Створення категорій
        Category electronics = new Category(1, "Електроніка");
        Category smartphones = new Category(2, "Смартфони");
        Category accessories = new Category(3, "Аксесуари");

        // Створення товарів
        Product product1 = new Product(
                1,
                "Ноутбук",
                19999.99,
                "Високопродуктивний ноутбук для роботи та ігор",
                electronics
        );

        Product product2 = new Product(
                2,
                "Смартфон",
                12999.50,
                "Смартфон з великим екраном та високою автономністю",
                smartphones
        );

        Product product3 = new Product(
                3,
                "Навушники",
                2499.00,
                "Бездротові навушники з шумозаглушенням",
                accessories
        );

        // Каталог для пошуку товарів
        List<Product> catalog = Arrays.asList(
                product1,
                product2,
                product3
        );

        Cart cart = new Cart();

        while (true) {
            System.out.println("\nВиберіть опцію:");
            System.out.println("1 - Переглянути список товарів");
            System.out.println("2 - Додати товар до кошика");
            System.out.println("3 - Переглянути кошик");
            System.out.println("4 - Зробити замовлення");
            System.out.println("5 - Видалити товар із кошика");
            System.out.println("6 - Переглянути історію замовлень");
            System.out.println("7 - Пошук товарів за назвою або категорією");
            System.out.println("0 - Вийти");

            if (!scanner.hasNextLine()) {
                scanner.close();
                return;
            }

            int choice = readInt(scanner);

            switch (choice) {
                case 1: {
                    System.out.println(product1);
                    System.out.println(product2);
                    System.out.println(product3);
                    break;
                }

                case 2: {
                    System.out.println(
                            "Введіть ID товару для додавання до кошика:"
                    );

                    int id = readInt(scanner);

                    if (id == 1) {
                        cart.addProduct(product1);
                        System.out.println("Ноутбук додано до кошика.");
                    } else if (id == 2) {
                        cart.addProduct(product2);
                        System.out.println("Смартфон додано до кошика.");
                    } else if (id == 3) {
                        cart.addProduct(product3);
                        System.out.println("Навушники додано до кошика.");
                    } else {
                        System.out.println("Товар з таким ID не знайдено.");
                    }

                    break;
                }

                case 3: {
                    System.out.println(cart);
                    break;
                }

                case 4: {
                    if (cart.getProducts().isEmpty()) {
                        System.out.println(
                                "Кошик порожній. Додайте товари " +
                                        "перед оформленням замовлення."
                        );
                    } else {
                        Order order = new Order(cart);

                        if (saveOrder(order)) {
                            System.out.println("Замовлення оформлено:");
                            System.out.println(order);
                            cart.clear();
                        }
                    }

                    break;
                }

                case 5: {
                    // Самостійна робота: видалення товару
                    if (cart.getProducts().isEmpty()) {
                        System.out.println("Кошик порожній.");
                        break;
                    }

                    System.out.println(cart);
                    System.out.println(
                            "Введіть ID товару для видалення:"
                    );

                    int id = readInt(scanner);
                    Product productToRemove = null;

                    for (Product product : cart.getProducts()) {
                        if (product.getId() == id) {
                            productToRemove = product;
                            break;
                        }
                    }

                    if (productToRemove != null) {
                        cart.removeProduct(productToRemove);
                        System.out.println(
                                "Одну одиницю товару видалено з кошика."
                        );
                    } else {
                        System.out.println(
                                "У кошику немає товару з таким ID."
                        );
                    }

                    break;
                }

                case 6: {
                    // Самостійна робота: історія замовлень
                    showOrderHistory();
                    break;
                }

                case 7: {
                    // Самостійна робота: пошук товарів
                    System.out.println(
                            "Введіть назву товару або категорії:"
                    );

                    if (!scanner.hasNextLine()) {
                        scanner.close();
                        return;
                    }

                    String query = scanner.nextLine()
                            .trim()
                            .toLowerCase(Locale.ROOT);

                    if (query.isEmpty()) {
                        System.out.println(
                                "Пошуковий запит не може бути порожнім."
                        );
                        break;
                    }

                    boolean found = false;

                    for (Product product : catalog) {
                        String name = product.getName()
                                .toLowerCase(Locale.ROOT);

                        String categoryName = product.getCategory()
                                .getName()
                                .toLowerCase(Locale.ROOT);

                        if (name.contains(query)
                                || categoryName.contains(query)) {
                            System.out.println(product);
                            found = true;
                        }
                    }

                    if (!found) {
                        System.out.println("Товари не знайдено.");
                    }

                    break;
                }

                case 0: {
                    System.out.println(
                            "Дякуємо, що використовували наш магазин!"
                    );
                    scanner.close();
                    return;
                }

                default: {
                    System.out.println(
                            "Невідома опція. Спробуйте ще раз."
                    );
                    break;
                }
            }
        }
    }

    // Зчитування числа з перевіркою введення
    private static int readInt(Scanner scanner) {
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Введіть ціле число:");
            }
        }

        return -1;
    }

    // Збереження замовлення у файл
    private static boolean saveOrder(Order order) {
        String text = order.toString()
                + System.lineSeparator()
                + "----------------------------------------"
                + System.lineSeparator();

        try {
            Files.write(
                    HISTORY_FILE,
                    text.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

            return true;
        } catch (IOException e) {
            System.out.println(
                    "Не вдалося зберегти замовлення: " + e.getMessage()
            );
            System.out.println("Товари залишилися в кошику.");

            return false;
        }
    }

    // Перегляд збереженої історії
    private static void showOrderHistory() {
        if (!Files.exists(HISTORY_FILE)) {
            System.out.println("Історія замовлень порожня.");
            return;
        }

        try {
            List<String> lines = Files.readAllLines(
                    HISTORY_FILE,
                    StandardCharsets.UTF_8
            );

            if (lines.isEmpty()) {
                System.out.println("Історія замовлень порожня.");
                return;
            }

            System.out.println("Історія замовлень:");

            for (String line : lines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println(
                    "Не вдалося прочитати історію: " + e.getMessage()
            );
        }
    }
}