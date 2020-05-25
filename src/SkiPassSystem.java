

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Scanner;

public class SkiPassSystem {
    static Scanner sc = new Scanner(System.in);
    static Path filePath;
    static String separator = File.separator;

    public static void main(String[] args) {



        try {

            boolean exit = false;
            if(!exit) {
                printInstruction();
                int a = sc.nextInt();
                switch(a) {
                    case 1:
                        createCard();
                        break;
                    case 2:
                        System.out.print("Введіть номер карти: ");
                        String uniqueId = sc.next();
                        checkByIdOrBlock(uniqueId, false);
                        break;
                    case 3:
                        System.out.print("Введіть номер карти яку потрібно заблокувати: ");
                        uniqueId = sc.next();
                        checkByIdOrBlock(uniqueId, true);
                        break;
                    case 4:
                        System.out.print("Введіть номер карти: ");
                        uniqueId = sc.next();
                        readEntryData(uniqueId);
                }

            }

        }
        catch(Exception e) {
            e.printStackTrace();
        }
   }


    public static void printInstruction() {
       System.out.println("Реєстр виданих карток натисніть: ");
       System.out.println("1. Аби випустити картку.");
       System.out.println("2. Аби отримати дані за картою .");
       System.out.println("3. Заблокувати картку через порушення. ");
       System.out.println("4. Видати дані про проходи та відмови.");
   }

   public static void createCard() throws IOException {
       System.out.println("Виберіть тип Ski-Pass: ");
       System.out.println("1.За кількістью підйомів.");
       System.out.println("2.За терміном дії.");
       boolean vip = false;
       boolean discount = false;
       int mainChoice = sc.nextInt();
       if(mainChoice == 1) {
           System.out.print("Виберіть кількість підйомів: ");
           int numberOfUphills = sc.nextInt();
           System.out.println("Виберіть тип Ski-Pass: ");
           System.out.println("1.Стандартний");
           System.out.println("2.Пільговий");
           System.out.println("3.ВІП");
           int typeOfSkiPass = sc.nextInt();
           if(typeOfSkiPass == 1) {

           } else if(typeOfSkiPass == 2) {
               discount = true;

           } else if (typeOfSkiPass == 3) {
               vip = true;
           }
           writeDataAboutSkiPass(new SkiPassModel("Ski-Pass за кількістью підйомів", 50, discount, vip, numberOfUphills, 0, 0));
       } else if(mainChoice == 2) {
           System.out.println("Виберіть тип Ski-Pass: ");
           System.out.println("1.Ранковий з 09:00 до 14:00.");
           System.out.println("2.Вечірній з 14:00 до 19:00.");
           System.out.println("3.Нічний з 19:00 до 24:00.");
           System.out.println("4.На повних 'Х' днів.");
           System.out.println("5.Повний сезон.");
           int typeOfSkiPass = sc.nextInt();
           System.out.println("Виберіть тип Ski-Pass: ");
           System.out.println("1.Стандартний");
           System.out.println("2.Пільговий");
           System.out.println("3.ВІП");
           int typeOfSkiPassBasicDiscountOrVIP = sc.nextInt();
           if(typeOfSkiPassBasicDiscountOrVIP == 2) {
               discount = true;
           } else if (typeOfSkiPassBasicDiscountOrVIP == 3) {
               vip = true;
           }
           else {
           }
           switch(typeOfSkiPass) {
               case 1:
               writeDataAboutSkiPass(new SkiPassModel("Ранковий Стандартний", 700, discount, vip, 0, 1, 0));
               break;
               case 2:
                   writeDataAboutSkiPass(new SkiPassModel("Вечірній Стандартний", 700, discount, vip, 0, 2, 0));
                   break;
               case 3:
                   writeDataAboutSkiPass(new SkiPassModel("Нічний Стандартний", 700, discount, vip, 0, 3, 0));
                   break;
               case 4:
                   System.out.print("Введіть кількість повних днів: ");
                    int nDays = sc.nextInt();
                    if(nDays > 0) {
                        writeDataAboutSkiPass(new SkiPassModel("На " + nDays + " повних днів" , 1500*nDays, discount, vip, 0, 4, nDays));
                    } else {
                        System.out.println("ERROR invalid number of days");
                    }
                   break;
               case 5:
                   writeDataAboutSkiPass(new SkiPassModel("Повний сезон", 500000, discount, vip, 0, 5, 0));

           }
       }
   }

   public static void writeDataAboutSkiPass(SkiPassModel skiPassModel) throws IOException {
       filePath = Paths.get("info" + separator + skiPassModel.getCardId() + ".txt");

       Files.write(filePath, (skiPassModel.getCardId() + "\n" +
               "Type: " + "\n"
               + skiPassModel.getType() + "\n" +
               "Price: " + "\n"
               + skiPassModel.getPrice() + "\n" +
               "Blocked: " + "\n"
               + skiPassModel.isBlocked() + "\n" +
               "Uphills: " + "\n"
               + skiPassModel.getUphills() + "\n" +
               "Discount: " + "\n"
               + skiPassModel.isDiscount() + "\n" +
               "VIP: " + "\n"
               + skiPassModel.isVip() + "\n" +
               "Activation Date: " + "\n"
               + skiPassModel.getActivationDate() + "\n" +
               "Expiration Date: " + "\n"
               + skiPassModel.getExpireDate()
       ).getBytes());

       Files.createFile(Paths.get("info" + separator + skiPassModel.getCardId() + "_entrydata" + ".txt"));
   }


    private static void checkByIdOrBlock(String uniqueId, boolean block) {
        filePath = Paths.get("info" + separator + uniqueId + ".txt");
        try {


            if(block) {
                String content = Files.readString(filePath);
                content = content.replaceAll("Blocked: \n" +
                        "false", "Blocked: \n" +
                        "true");
                Files.write(filePath, content.getBytes());
            }

            List<String> contents = Files.readAllLines(filePath);

            for (String content : contents) {
                System.out.println(content);
            }

        } catch (IOException e) {
            System.out.println("No such card was found.");
            System.out.println(e);
        }
    }


    public static void readEntryData(String cardId) {
        filePath = Paths.get("info" + separator + cardId +   "_entrydata" + ".txt");
        try {
            List<String> contents = Files.readAllLines(filePath);

            for(String content : contents) {
                System.out.println(content);
            }
    } catch (IOException e) {
            System.out.println("No such card was found.");
            System.out.println(e);
        }
    }
}
