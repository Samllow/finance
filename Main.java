
import service.AppData;
import service.AuthService;
import service.StorageService;
import service.TransactionService;
import UI.ConsoleUI;
import UI.AppController;
public class Main {
    public static void main(String[] args) throws Exception {
        AppData appData = new AppData();
        StorageService storageService = new StorageService();
        storageService.load(appData);
        ConsoleUI ui = new ConsoleUI();


        AuthService authService = new AuthService(appData);
        TransactionService transactionService = new TransactionService(appData);

        AppController controller = new AppController(ui, authService, transactionService, appData, storageService);
        controller.start();
    }
}
