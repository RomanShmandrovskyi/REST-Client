import client.WalletClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Response;
import model.Wallet;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.testng.Assert.*;

public class RESTWebClientTest {

    private static WalletClient client = new WalletClient();
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static String url = "http://localhost:8080/rest/service/wallet";

    //1
    @Test(testName = "get all wallets test")
    public void getAllWalletsTest() {
        ArrayList allWallets = gson.fromJson(client.doGet(String.format("%s/all", url)), ArrayList.class);
        assertNotNull(allWallets);
    }

    //2
    @Test(testName = "add new wallet")
    public void addNewWalletTest() {
        String testUrl = String.format("%s/all", url);
        ArrayList allWallets = gson.fromJson(client.doGet(testUrl), ArrayList.class);
        int walletQnt = allWallets.size();
        Response response = gson.fromJson(client.doPost(String.format("%s/add", url)), Response.class);
        int newWalletQnt = gson.fromJson(client.doGet(testUrl), ArrayList.class).size();

        assertEquals(walletQnt + 1, newWalletQnt);
        assertEquals(response.getMessage(), "new wallet added successfully");
    }

    //3
    @Test(testName = "get not exist wallet")
    public void getNotExistWalletTest() {
        String testUrl = String.format("%s/%d", url, Integer.MAX_VALUE);
        Response response = gson.fromJson(client.doGet(testUrl), Response.class);
        Wallet wallet = gson.fromJson(client.doGet(testUrl), Wallet.class);
        assertEquals(response.getMessage(), "there is no one wallet with such id");
        assertEquals(0.0, wallet.getBalance());
    }

    //4
    @Test(testName = "get wallet")
    public void getWalletTest() {
        String testUrl = String.format("%s/%d", url, 2);
        Wallet wallet = gson.fromJson(client.doGet(testUrl), Wallet.class);
        assertNotNull(wallet);
    }

    //5
    @Test(testName = "buy good")
    public void buyGoodTest() {
        String testUrl = String.format("%s/%s/buy?price=%s", url, 2, 25.7);
        double currentBalance = gson.fromJson(client.doGet(String.format("%s/%s", url, 2)), Wallet.class).getBalance();
        Response response = gson.fromJson(client.doPost(testUrl), Response.class);
        double afterPurchaseBalance = gson.fromJson(client.doGet(String.format("%s/%s", url, 2)), Wallet.class).getBalance();

        assertEquals(afterPurchaseBalance + 25.7, currentBalance);
        assertEquals(response.getMessage(), "product was purchased successfully");
    }

    //6
    @Test(testName = "buy expensive good")
    public void buyExpensiveGoodTest() {
        String testUrl = String.format("%s/%s/buy?price=%s", url, 2, 2000.0);
        double currentBalance = gson.fromJson(client.doGet(String.format("%s/%s", url, 2)), Wallet.class).getBalance();
        Response response = gson.fromJson(client.doPost(testUrl), Response.class);
        double afterPurchasingBalance = gson.fromJson(client.doGet(String.format("%s/%s", url, 2)), Wallet.class).getBalance();

        assertEquals(currentBalance, afterPurchasingBalance);
        assertEquals(response.getMessage(), "you have exceeded your credit limit");
    }

    //7
    @Test(testName = "get balance of current wallet")
    public void getBalanceTest() {
        String testUrl = String.format("%s/%s/balance", url, 1);
        double currentBalance = gson.fromJson(client.doGet(testUrl), Wallet.class).getBalance();

        assertEquals(currentBalance, 100.0);
        assertTrue(client.doGet(testUrl).contains("\"balance\":100.0"));
    }

    //8
    @Test(testName = "put money on wallet")
    public void putMoneyOnWalletTest() {
        String testUrl = String.format("%s/%s/putMoney?moneyQnt=%s", url, 2, 25.7);
        double currentBalance = gson.fromJson(client.doGet(String.format("%s/%s", url, 2)), Wallet.class).getBalance();

        Response response = gson.fromJson(client.doPost(testUrl), Response.class);

        double afterRefillingBalance = gson.fromJson(client.doGet(String.format("%s/%s", url, 2)), Wallet.class).getBalance();

        assertEquals(currentBalance, afterRefillingBalance - 25.7);
        assertEquals(response.getMessage(), "balance was refill successfully");
    }

    //9
    @Test(testName = "put to much money on wallet")
    public void putToMuchMoneyOnWalletTest() {
        String testUrl = String.format("%s/%s/putMoney?moneyQnt=%s", url, 2, 2000.0);
        double currentBalance = gson.fromJson(client.doGet(String.format("%s/%s", url, 2)), Wallet.class).getBalance();

        Response response = gson.fromJson(client.doPost(testUrl), Response.class);

        double afterRefillingBalance = gson.fromJson(client.doGet(String.format("%s/%s", url, 2)), Wallet.class).getBalance();

        assertEquals(currentBalance, afterRefillingBalance);
        assertEquals(response.getMessage(), "you have exceeded your maximum limit");
    }

    //10
    @Test(testName = "delete wallet by id")
    public void deleteWalletById() {
        String testUrl = String.format("%s/%s/delete", url, 3);
        int walletCount = gson.fromJson(client.doGet(String.format("%s/all", url)), ArrayList.class).size();

        Response response = gson.fromJson(client.doDelete(testUrl), Response.class);

        int walletCountAfterDeleting = gson.fromJson(client.doGet(String.format("%s/all", url)), ArrayList.class).size();

        assertEquals(walletCount - 1, walletCountAfterDeleting);
        assertEquals(response.getMessage(), "wallet deleted successfully");
    }
}
