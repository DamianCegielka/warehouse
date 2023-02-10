# Warehouse Value Calculator
The Warehouse Value Calculator is desktop application designed to help users calculate their inventory value at the end of the month. The application allows users to import beginning inventory in .xlsx format, purchase invoices in .pdf format, and sale invoices in .xlsx format, then generates a .xlsx file containing the ending inventory value, including a list of items, quantity, and value of goods, which can be imported as the next month's beginning inventory.

The application also imports exchange rates from the National Bank of Poland website and converts foreign currency into PLN.

Inline-style:
![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "Desktop screenshoot")

# Main Class
The main class of the Warehouse Value Calculator is MainFrame in the org.project.view package. This class is responsible for handling the graphical user interface and implementing the core functionalities of the application. The class uses a number of other classes to perform its tasks, including:

+ ShopsWarehouse
+ ExportDataToExcel
+ ExportSalesDataToExcel
+ DownloadExchangeRateFromURL
+ PDFBoxReadFromFile
+ ReadExcelSales

All these classes work together to provide the user with a comprehensive solution to calculate the inventory value.

# Features
Import beginning inventory in .xlsx format
Import purchase invoices in .pdf format
Import sale invoices in .xlsx format
Generates a .xlsx file containing the ending inventory value
Imports exchange rates from the National Bank of Poland website
Converts foreign currency into PLN
Requirements
To use the Warehouse Value Calculator, you need to have the following software installed on your computer:

+ Java 8 or higher
+ A .xlsx file reader, such as Microsoft Excel or LibreOffice
+ A .pdf file reader, such as Adobe Reader or Foxit Reader

# Usage
To use the Warehouse Value Calculator, follow these steps:

Launch the application
+ Click the 'Load Warehouse' button to import the beginning inventory file
+ Click the 'Load Sales' button to import the sale invoices file
+ Click the 'Load Delivers' button to import the purchase invoices file
+ Click the 'Calculate' button to calculate the inventory value
+ Click the 'Current Rate' button to import the exchange rate from the National Bank of Poland website
+ Click the 'Reload Table' button to reload the table with updated data
+ Click the 'Export' button to generate a .xlsx file containing the ending inventory value

# Limitations
The Warehouse Value Calculator only supports .xlsx and .pdf file formats for importing data.
The application only imports exchange rates from the National Bank of Poland website.
The Warehouse Value Calculator is only compatible with Windows operating systems.
