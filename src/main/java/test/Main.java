package test;

import java.util.Scanner;

import test.exception.InvalidCommand;
import test.exception.InvalidParameterException;
import test.repository.BankAccountRepo;
import test.repository.DebtRepo;
import test.repository.IBankAccountRepo;
import test.repository.IDebtRepo;
import test.repository.IUserRepo;
import test.repository.UserRepo;
import test.service.Deposit;
import test.service.IDeposit;
import test.service.ILogin;
import test.service.ILogout;
import test.service.ITransfer;
import test.service.IWithdraw;
import test.service.Login;
import test.service.Logout;
import test.service.Transfer;
import test.service.Withdraw;

import java.math.BigDecimal;


public class Main {
    private static IUserRepo userRepo;
    private static IBankAccountRepo bankAccountRepo;
    private static IDebtRepo debtRepo;

    private static ILogin loginService;
    private static ILogout logoutService;
    private static IWithdraw withdrawService;
    private static IDeposit depositService;
    private static ITransfer transferService;
    
    public static void main(String[] args) {
       Scanner scan = new Scanner(System.in);

       userRepo = new UserRepo();
       bankAccountRepo = new BankAccountRepo();
       debtRepo = new DebtRepo();

       loginService = new Login(userRepo, bankAccountRepo, debtRepo);
       logoutService = new Logout();
       withdrawService = new Withdraw(bankAccountRepo);
       depositService = new Deposit(bankAccountRepo, debtRepo);
       transferService = new Transfer(bankAccountRepo, debtRepo);
    
       while (true) {
         System.out.print("$ ");
         String input = scan.nextLine();
         if (input.equals("exit")) {
            break;
         }
         try {
            handleInput(input);
         } catch (Exception e) {
            System.out.println(e.getMessage());
         }
       }
       scan.close();
    }

    private static void handleInput(String input) throws Exception {
        String[] inputArr = input.split(" ");
        if (inputArr.length == 0) {
            throw new InvalidCommand();
        }

        if (inputArr[0].equals("login")) {
            if (inputArr.length != 2) {
                throw new InvalidParameterException();
            }
            loginService.login(inputArr[1]);
        } else if (inputArr[0].equals("logout")) {
            if (inputArr.length != 1) {
                throw new InvalidParameterException();
            }
            logoutService.logout();
        } else if (inputArr[0].equals("withdraw")) {
            if (inputArr.length != 2) {
                throw new InvalidParameterException();
            }

            try {
                BigDecimal amount = new BigDecimal(inputArr[1]);
                withdrawService.withdraw(amount);
            } catch (NumberFormatException e) {
                throw new InvalidParameterException();
            }
        } else if (inputArr[0].equals("deposit")) {
            if (inputArr.length != 2) {
                throw new InvalidParameterException();
            }
            try {
                BigDecimal amount = new BigDecimal(inputArr[1]);
                depositService.deposit(amount);
            } catch (NumberFormatException e) {
                throw new InvalidParameterException();
            }
        } else if (inputArr[0].equals("transfer")) {
            if (inputArr.length != 3) {
                throw new InvalidParameterException();
            }
            try {
                BigDecimal amount = new BigDecimal(inputArr[2]);
                transferService.transfer(inputArr[1], amount);
            } catch (NumberFormatException e) {
                throw new InvalidParameterException(); 
            }
        } else {
            throw new InvalidCommand();
        }
    }
}
