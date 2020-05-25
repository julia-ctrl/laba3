
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


public class SkiPassModel {


    String cardId;
    private String type;
    private boolean blocked;
    private double price;
    public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private LocalDateTime now = LocalDateTime.now();
    private LocalDateTime activationDate;
    private LocalDateTime expireDate;
    private boolean vip;
    private boolean discount;
    private int uphills;

    public SkiPassModel(String cardId,String type, boolean blocked, double price, LocalDateTime activationDate, LocalDateTime expireDate, boolean vip, boolean discount, int uphills) {
        this.cardId = cardId;
        this.type = type;
        this.blocked = blocked;
        this.price = price;
        this.activationDate = activationDate;
        this.expireDate = expireDate;
        this.vip = vip;
        this.discount = discount;
        this.uphills = uphills;
    }

    public SkiPassModel(String type, double price, boolean discount, boolean vip, int uphills, int typeOfDateSkiPass, int nDays) {

        this.cardId = UUID.randomUUID().toString();
        this.type = type;
        this.vip = vip;
        this.discount = discount;
        this.uphills = uphills;

        if(uphills > 0) {
            price = price * uphills;
            this.type += " ( Check by uphills )";
        }

        switch (typeOfDateSkiPass) {
            case 1:
                SkiPassMorning();
                break;
            case 2:
                SkiPassDay();
                break;
            case  3:
                SkiPassNight();
                break;
            case 4:
                SkiPassNDays(nDays);
                break;
            case 5:
                fullSeason();
                break;
            default:
            activationDate = LocalDateTime.now();
            expireDate = LocalDateTime.now();
        }

        if(discount) {
            this.price = price - ((20 * price ) / 100);
        }
        else if (vip) {
            this.price = price * 2;
        }
        else {
            this.price = price;
        }
    }


    public String getCardId() {
        return cardId;
    }

    public String getType() {
        return type;
    }

    public boolean isBlocked() {
        return blocked;
    }


    public double getPrice() {
        return price;
    }

    public String getActivationDate() {
        if(this.activationDate == null) {
            activationDate = LocalDateTime.now();
            return dtf.format(activationDate);
        }
        return dtf.format(activationDate);
    }

    public String getExpireDate() {
        if(this.expireDate == null) {
            return "There is no expiration date";
        }
        return dtf.format(expireDate);
    }

    public LocalDateTime getActivationnDate() {
        return this.activationDate;
    }

    public LocalDateTime getExoirenDate() {
        return this.expireDate;
    }


    public int getUphills() {
        return uphills;
    }

    public boolean isDiscount() {
        return discount;
    }

    public boolean isVip() {
        return vip;
    }

    public void SkiPassMorning() {
        activationDate =  LocalDate.now().atTime(9, 0);
        expireDate = LocalDate.now().atTime(14, 0);
        if(now.isAfter(LocalDate.now().atTime(9, 0)))
        {
            System.out.println("Today's Morning skiing is expired. Buying ski pass for tomorrow morning ... ");
            activationDate = activationDate.plusDays(1);
            expireDate = expireDate.plusDays(1);
        }
    }

    public void SkiPassDay() {
        activationDate = LocalDate.now().atTime(14,0);
        expireDate = LocalDate.now().atTime(19, 0);
        if(now.isAfter(activationDate)) {
            activationDate = activationDate.plusDays(1);
            expireDate = expireDate.plusDays(1);
        }
    }

    public void SkiPassNight() {
        activationDate = LocalDate.now().atTime(19, 0);
        expireDate = LocalDate.now().atTime(23,59, 59);
        if(now.isAfter(activationDate)) {
            activationDate = activationDate.plusDays(1);
            expireDate = expireDate.plusDays(1);
        }
    }

    public void SkiPassNDays(int n) {
        activationDate = LocalDateTime.now();
        expireDate = LocalDateTime.now();
        expireDate = expireDate.plusDays(n);
    }

    public void fullSeason() {
        activationDate = LocalDateTime.of(2020, Month.DECEMBER, 01, 0, 0, 0);
        expireDate  = LocalDateTime.of(2021, Month.MAY, 31, 23, 59, 59);
        if(now.isAfter(activationDate)) {
            activationDate = activationDate.plusYears(1);
            expireDate = expireDate.plusYears(1);
        }
    }

    public void setActivationDate(LocalDateTime activationDate) {
        this.activationDate = activationDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public void ride() throws IOException {
        this.uphills--;
        String separator = File.separator;
        String content = Files.readString(Paths.get("info" + separator + this.cardId + ".txt"));
        content = content.replaceAll("Uphills: \n" +
                "[0-9]+", "Uphills: \n" +
                this.uphills);
        Files.write(Paths.get("info" + separator + this.cardId + ".txt"), content.getBytes());
    }


}
