package MiniProject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class addingJob {

    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("=== Running test on Chrome browser ===");
        runTest(new ChromeDriver());
        System.out.println("=== Chrome browser closed ===");

        System.out.println("=== Running test on Edge browser ===");
        runTest(new EdgeDriver());
        System.out.println("=== Edge browser closed ===");
    }

    public static void runTest(WebDriver driver) throws InterruptedException, IOException {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        //Open the browse
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        driver.manage().window().maximize();
        
        
       try { 
     // Open the browser and enter admin and password
        System.out.println("opened login page");
        driver.findElement(By.name("username")).sendKeys("Admin");
        driver.findElement(By.name("password")).sendKeys("admin123");
        
        
      //Click the “Login” button.
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        System.out.println("Landed to Home Page");

        
      //Verify the current URL and check if it contains the string “dashboard”.
		   String currenturl=driver.getCurrentUrl();
		   if(currenturl.contains("dashboard")) {
		   System.out.println("URL verified: " + currenturl);
		   }
		 //Go to Admin Tab
        driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[1]/aside/nav/div[2]/ul/li[1]/a")).click();
        driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[1]/header/div[2]/nav/ul/li[2]/span/i")).click();
        driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[1]/header/div[2]/nav/ul/li[2]/ul/li[1]/a")).click();
        Thread.sleep(5000);
        driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div/div[1]/div/button")).click();
      //adding "automation tester" in job field using timestamp to avoid duplication
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timeStamp = now.format(formatter);
        
        
       // Fill the appropriate data in the fields “Job Title” and click on “Save”.
        driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div/form/div[1]/div/div[2]/input"))
              .sendKeys("AutomationTester" + timeStamp);
        driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div/form/div[2]/div/div[2]/textarea"))
              .sendKeys("Tests the webApplications");
        driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div/form/div[5]/button[2]")).click();
        System.out.println("Added successfully");

        
        
        // Get the List of All Jobs Available
        List<WebElement> li = driver.findElements(By.xpath("//div[@class='oxd-table']/div[2]/div/div/div[2]/div"));
        System.out.println("size : " + li.size());

        for (WebElement li1 : li) {
            System.out.println(li1.getText());
        }
        //creating excel file to store list of elements 
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("WebElements");
        for (int i = 0; i < li.size(); i++) {
            Row row = sheet.createRow(i);
            Cell cell = row.createCell(0);
            cell.setCellValue(li.get(i).getText());
        }
        String fileName = "webelements_" + timeStamp + ".xlsx";
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            workbook.write(fos);
        }
        workbook.close();
        //logout 
        driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[1]/header/div[1]/div[3]/ul/li/span/i")).click();
        driver.findElement(By.linkText("Logout")).click();
        System.out.println("Logged Out");
        driver.quit();
       }
       catch(Exception e) {
   	    System.out.println(e);
   	} 
   	finally {
   	    // Always close the browser
   	   driver.quit();
   	    }
    }
}
