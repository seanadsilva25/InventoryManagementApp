to complie:
javac -cp "lib/*" -d out src/main/java/com/Stockify/inventory/model/*.java ^
src/main/java/com/Stockify/inventory/db/*.java ^
src/main/java/com/Stockify/inventory/gui/*.java ^
src/main/java/com/Stockify/inventory/backend/*.java ^
src/main/java/com/Stockify/inventory/main/*.java ^
src/main/java/com/Stockify/inventory/test/*.java

to run: java -cp "out;lib/mysql-connector-j-9.4.0.jar" com.Stockify.inventory.main.InventoryApp
