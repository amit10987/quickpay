package minibank.quickpay.util;

public final class QuickPayEndPoint {

    private QuickPayEndPoint() {

    }

    public static class Account {
        private Account() {
        }

        public static final String GET_ALL_ACCOUNTS = "/accounts";
        public static final String CREATE_ACCOUNT = "/accounts";
        public static final String GET_ACCOUNT_BY_ACCOUNT_NUMBER = "/accounts/:accountNumber";
    }

    public static class MoneyTransaction {
        private MoneyTransaction() {
        }

        public static final String TRANSFER = "/transaction/transfer";
        public static final String DEPOSIT = "/transaction/deposit";
        public static final String WITHDRAW = "/transaction/withdraw";
    }
}
