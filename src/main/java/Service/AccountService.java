package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account addAccount(Account account)
    {
        return accountDAO.createAccount(account);
    }

    public Account logInAccount(Account account)
    {
        return accountDAO.logInAccount(account);
    }
}
