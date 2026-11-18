public class FeeService {
    public String validateAndPrepareReceipt(String regNo, String amountText) {
        if (regNo == null || regNo.isBlank())
            throw new IllegalArgumentException("Registration number is required.");
        final double amount;
        try { amount = Double.parseDouble(amountText == null ? "" : amountText.trim()); }
        catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Enter a valid numeric amount.", ex);
        }
        if (!Double.isFinite(amount) || amount <= 0)
            throw new IllegalArgumentException("Amount must be greater than zero.");
        return "Validated for " + regNo.trim() + " | Rs. " + String.format("%.2f", amount);
    }
}
