# Balls Simulator 🎾💨  

## 🌍 English  

### 📝 Project Description  
**Balls Simulator** is an interactive physics-based game where users can add shapes (balls, squares, triangles), observe their movement under gravity, and interact with obstacles. The game also allows changing the shape of new objects, managing obstacles, and triggering hidden functions.  

---

### 📜 Features  
- 🔵 **Add Shapes** - Left-click to add a new shape at the cursor position.  
- 🚧 **Add/Remove Obstacles** - Right-click to add or remove obstacles.  
- 🛠 **Change Shape** - Press `TAB` to switch between:  
  - Circle 🟠  
  - Square 🟥  
  - Triangle 🔺  
- ⏳ **Time Limit** - Objects disappear after **20 seconds**.  
- 🎡 **Resize Objects** - Scroll to adjust the size of new objects.  
- 🏗 **Collisions** - Objects bounce off each other and obstacles.  
- 🏴‍☠️ **"Kokon" Mode** - Typing `kokon` adds **1000** random shapes.  
- 🧹 **Cleanup**:  
  - `C` - Removes all objects.  
  - `V` - Removes all obstacles.  
- ℹ **Info Window** - Press `ⓘ` to display game rules.  

---

### 🎮 Controls  
| Action                          | Key / Gesture                     |  
|----------------------------------|-----------------------------------|  
| Add shape                        | **Left-click**                    |  
| Add/Remove obstacle              | **Right-click**                   |  
| Change shape                     | `TAB`                              |  
| Resize shape                     | **Scroll**                        |  
| Clear all objects                | `C`                                |  
| Clear all obstacles              | `V`                                |  
| "Kokon" Mode (1000 objects)      | Type `kokon`                      |  
| Open game rules                  | Click `ⓘ`                         |  

---

### 🔧 Requirements  
- Java 17+  
- IntelliJ IDEA / Eclipse (optional)  

---

### 🔨 Installation & Run  
1. **Clone the repository**:  
   ```sh
   git clone https://github.com/your-repository.git  
   cd your-repository
   ```
2.Compile the project:
```
javac -d out -sourcepath src src/Frames/GameFrame.java  
```
3.Run the game:
```
java -cp out Frames.GameFrame  
```
📌 Future Improvements
🎨 Ability to choose shape colors.
🕹 A challenge mode (e.g., hitting a target).
🏎 Optimization for large numbers of objects.


📜 License
This project is licensed under the MIT License – you can modify and use it freely. 😊
🎉 Enjoy the game!

