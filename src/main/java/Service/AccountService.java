package Service;

import DAO.AccountDAO;
import Model.Account;
import Util.BCrypt;

public class AccountService {

    AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    // assign accoutnDAO
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    // create and return new user
    public Account addAccount(Account account){
        if (account.getUsername().equals("") || account.getPassword().length() < 4) {
            return null;
        }
        Account exists = accountDAO.getAccountByUsername(account.getUsername());
        if (exists != null) {
            return null;
        }
        return accountDAO.insertAccount(account);
    }

    public Account verifyCredentials(Account credentials) {
        if (credentials.getUsername().equals("")) {
            return null;
        }

        Account account = accountDAO.getAccountByUsername(credentials.getUsername());
        if (account == null) {
            return null;
        }
        boolean matches = BCrypt.checkpw(credentials.getPassword(), account.getPassword());
        if (matches) {
            account.setPassword(credentials.getPassword());
            return account;
        }
        return null;
    }
}