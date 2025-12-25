# SmartFridge 🧊📱

SmartFridge is an Android mobile application designed to help users manage food products, track expiration dates, and plan meals more effectively. The main goal of the app is to reduce food waste by providing a clear overview of fridge contents and timely reminders about products that are close to expiring.

---

## 🚀 Features

### 🗓️ Expiration Date Management
- Add products with name, quantity, and expiration date
- Products stored locally using **Room** database
- Visual status indicators:
  - 🟢 **Green** – fresh
  - 🟡 **Yellow** – expiring soon
  - 🔴 **Red** – expired

### 🔔 Expiration Notifications
- Automatic reminders for products nearing their expiration date
- Implemented using **WorkManager** or **AlarmManager**
- Example notification:
  > *"Yellow cheese expires tomorrow!"*

### 🍽️ Recipe Suggestions
- Suggests recipes based on available ingredients in the fridge
- Uses an external API **Spoonacular**
- Displays:
  - Recipe name
  - Ingredients list
  - Cooking instructions
- Helps users utilize existing products and avoid waste

### 🛒 Shopping List
- Create shopping lists manually
- Automatically generate shopping lists from selected recipes
- Makes grocery planning fast and convenient

### 📷 Barcode Scanning
- Quickly add products by scanning barcodes
- Powered by **ML Kit Barcode Scanning**
- Automatically fills in product name
- Integrated with **OpenFoodFacts API** for product data

---

## 🛠️ Technologies Used

- **Language:** Kotlin  
- **Architecture:** MVVM (Model–View–ViewModel)  
- **Database:** Room (local SQL database)  
- **UI:** Material Design, RecyclerView, CardView  
- **Notifications:** WorkManager / AlarmManager  
- **Barcode Scanning:** ML Kit Barcode Scanning  
- **APIs:**
  - Spoonacular (recipes)
  - OpenFoodFacts (product information)

---

## ✅ Benefits for Users

- Reduce food waste through smart reminders and expiration tracking
- Easy meal planning with recipe suggestions
- Quick creation of shopping lists based on real needs
- Fast product input using barcode scanning
- Clean, intuitive, and user-friendly interface
