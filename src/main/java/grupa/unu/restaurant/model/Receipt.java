package grupa.unu.restaurant.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Receipt {
    private int receiptNumber;
    private Order order;
    private Payment payment;
    private LocalDateTime issueDate;

    public Receipt(int receiptNumber, Order order, Payment payment) {
        this.receiptNumber = receiptNumber;
        this.order = order;
        this.payment = payment;
        this.issueDate = LocalDateTime.now();
    }

    public void generateReceipt() {
        System.out.println("=================================");
        System.out.println("           CHITANȚĂ              ");
        System.out.println("=================================");
        System.out.println("Nr. chitanță:      " + receiptNumber);
        System.out.println("Comanda ID:        " + order.getId());
        System.out.printf("Sumă totală:      %8.2f RON%n", payment.amount);
        System.out.println("Metodă plată:      " + (payment instanceof CashPayment ? "Numerar" : "Card"));
        System.out.println("Data emitere:      " +  issueDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        System.out.println("=================================");
    }
}
