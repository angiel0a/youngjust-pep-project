package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    public AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public boolean accountExists(String username){
        if(accountDAO.getAccountByUsername(username) != null){
            return true;
        }
        return false;
    }

    public Account addAccount(Account account){
        if(accountDAO.getAccountByUsername(account.getUsername()) != null){
            return null;
        }
        return accountDAO.insertAccount(account);
    }  
    
    public Account getLoggedAccount(Account account){
        return accountDAO.getAccountByUsernameAndPassword(account);
    }
}
