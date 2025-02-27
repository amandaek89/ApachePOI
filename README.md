# 📌 Excel Hanterare
Ett Java-program för att skapa och läsa en Excel-fil där textfärger hanteras. Programmet kan även filtrera bort celler med grå text.


## 🚀 Funktioner
✅ Skapa en Excel-fil med text i olika färger  
✅ Läsa och lista textfärger från Excel  
✅ Filtrera bort celler med grå text  
✅ Enkel meny i konsolen för att välja funktioner  


## 📂 Projektstruktur

org.example
│── Main.java         // Hanterar konsolmenyn
│── ExcelService.java // Sköter Excel-läsning/skrivning
│── ExcelHelper.java  // Innehåller hjälpfunktioner
│── README.md         // Projektbeskrivning


## 🛠 Installation & Körning

### Krav
- Java 17+  
- Apache POI (för Excel-hantering)  

### Bygga & Köra
1. **Ladda ner Apache POI**  
   Om du använder **Maven**, lägg till detta i `pom.xml`:
   
   <dependencies>
       <dependency>
           <groupId>org.apache.poi</groupId>
           <artifactId>poi-ooxml</artifactId>
           <version>5.2.3</version> <!-- Kolla efter senaste versionen -->
       </dependency>
   </dependencies>
  
   
2. **Kompilera och kör programmet**  
   
   javac -cp .:poi-ooxml-5.2.3.jar org/example/*.java
   java -cp .:poi-ooxml-5.2.3.jar org.example.Main
   

## 📖 Användning
När programmet startas visas en meny:

🔹 **Excel Hanterare** 🔹
1. Skapa en ny Excel-fil
2. Läs textfärger från Excel
3. Läs textfärger och filtrera bort celler med GRÅ
4. Avsluta
Välj ett alternativ (1-4):

- **Välj 1** → Skapar en ny fil `textcolor.xlsx`  
- **Välj 2** → Läser och listar textfärger i filen  
- **Välj 3** → Läser textfärger men hoppar över grå text  
- **Välj 4** → Avslutar programmet

## 🎯 Utökningar & Förbättringar
- 📌 Lägga till fler färgfilter  
- 📌 Möjlighet att ändra textfärg i befintlig Excel  
- 📌 GUI-version istället för konsol  


## 📝 Licens
MIT License – Fritt att använda och modifiera!  


### 🚀 Happy Coding! 🖥️🎨

