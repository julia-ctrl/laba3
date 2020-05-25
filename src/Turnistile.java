
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Turnistile {
    public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    static Path filePath;
    static String separator = File.separator;
    static SkiPassSystem skiPassSystem;

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Зчитую номер карти: ");
        String cardId = sc.next();
        passList(convertInfoToObject(cardId));

    }

    public static SkiPassModel convertInfoToObject(String skipassId) {

        filePath = Paths.get("info" + separator + skipassId + ".txt");

        List<String> contents = null;

        try {
           contents = Files.readAllLines(filePath);


        }
        catch (IOException e) {
            System.out.println(e);
        }

        String cardId = contents.get(0);
        String type = contents.get(2);
        double price = Double.parseDouble(contents.get(4));
        boolean blocked = Boolean.parseBoolean(contents.get(6));
        int uphills = Integer.parseInt(contents.get(8));
        boolean discount = Boolean.parseBoolean(contents.get(10));
        boolean vip = Boolean.parseBoolean(contents.get(12));
        LocalDateTime activationDate = LocalDateTime.parse(contents.get(14), dtf);
        LocalDateTime expireDate = LocalDateTime.parse(contents.get(16), dtf);

        return new SkiPassModel(cardId,type,blocked, price, activationDate, expireDate, vip, discount, uphills);
    }

    public static void passList(SkiPassModel skiPassModel) throws IOException {
        String contentToAppend;
        if((skiPassModel.cardId == null) || (skiPassModel.isBlocked()) || (((LocalDateTime.now().isBefore(skiPassModel.getActivationnDate())) || (LocalDateTime.now().isAfter(skiPassModel.getExoirenDate()))) && (skiPassModel.getUphills() == 0))) {
            System.out.println("Відмова.");
            contentToAppend = "Denied: " + dtf.format(LocalDateTime.now()) + "\n";

        } else {
            if(skiPassModel.isDiscount()) {
                warn();
            }
            if(skiPassModel.getUphills() > 0) {
                skiPassModel.ride();
            }
            System.out.println("Гарного катання.");
            contentToAppend = "Passed: " + LocalDateTime.now() + "\n";

        }
        Files.write(
                Paths.get("info" + separator + skiPassModel.getCardId() + "_entrydata" + ".txt"),
                contentToAppend.getBytes(),
                StandardOpenOption.APPEND
        );
        }

    private static void warn() {
        System.out.println("Зачекайте будь ласка перевірки документів");
    }
}
