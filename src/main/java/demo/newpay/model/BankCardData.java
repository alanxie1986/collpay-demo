package demo.newpay.model;

public class BankCardData {
    private String bankAccount;
    private String bankName;
    private String accountName;
    private Boolean amountChanged;
    private String payerName;
    private Double amount;

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Boolean getAmountChanged() {
        return amountChanged;
    }

    public void setAmountChanged(Boolean amountChanged) {
        this.amountChanged = amountChanged;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
